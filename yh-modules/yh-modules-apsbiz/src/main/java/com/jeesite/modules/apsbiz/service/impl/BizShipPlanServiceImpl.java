
package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeesite.common.base.R;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.apsbiz.entity.*;
import com.jeesite.modules.apsbiz.mapper.BizShipPlanMapper;
import com.jeesite.modules.apsbiz.service.*;
import com.jeesite.modules.sys.utils.UserUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 靠离泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Service
public class BizShipPlanServiceImpl extends YhServiceImpl<BizShipPlanMapper, BizShipPlan> implements BizShipPlanService {


	@Resource
	@Lazy
	private BizShipForecastService bizShipForecastService;

	@Resource
	private BizBerthInfoService bizBerthInfoService;

	@Resource
	private BizBollardInfoService bizBollardInfoService;

	@Resource
	private DockService dockService;

	@Value("${default.page}")
	private Integer defPage;

	@Value("${default.size}")
	private Integer defSize;

	@Override
	public List<BizShipPlan> queryList(BizShipPlan bizShipPlan) {
		QueryWrapper<BizShipPlan> queryWrapper = MybatisPlusUtils.getQueryWrapper(bizShipPlan, "a");

		// 船名模糊查询
		queryWrapper.like(StrUtil.isNotEmpty(bizShipPlan.getShipNameCnLike()), "B.SHIP_NAME_CN", bizShipPlan.getShipNameCnLike());
		// 当前状态
		queryWrapper.eq(null != bizShipPlan.getStatus(), "B.STATUS", bizShipPlan.getStatus());
		// 算法状态
		queryWrapper.eq(null != bizShipPlan.getAlgorithmState(), "B.ALGORITHM_STATE", bizShipPlan.getAlgorithmState());
		// 散算法状态
		queryWrapper.in(StrUtil.isNotEmpty(bizShipPlan.getAlgorithmStateIn()), "B.ALGORITHM_STATE", StrUtil.split(bizShipPlan.getAlgorithmStateIn(), ","));

		return baseMapper.queryList(queryWrapper);
	}

	@Override
	public IPage<BizShipPlan> queryPage(BizShipPlan bizShipPlan, Integer page, Integer size) {
		if (page == null) {
			page = defPage;
		}
		if (size == null) {
			size = defSize;
		}
		IPage<BizShipPlan> p = new Page<>(page, size);
		QueryWrapper<BizShipPlan> queryWrapper = MybatisPlusUtils.getQueryWrapper(bizShipPlan, "a");
		// 船名模糊查询
		queryWrapper.like(StrUtil.isNotEmpty(bizShipPlan.getShipNameCnLike()), "B.SHIP_NAME_CN", bizShipPlan.getShipNameCnLike());
		// 当前状态
		queryWrapper.eq(null != bizShipPlan.getStatus(), "B.STATUS", bizShipPlan.getStatus());
		// 算法状态
		queryWrapper.eq(null != bizShipPlan.getAlgorithmState(), "B.ALGORITHM_STATE", bizShipPlan.getAlgorithmState());
		// 算法状态
		queryWrapper.in(StrUtil.isNotEmpty(bizShipPlan.getAlgorithmStateIn()), "B.ALGORITHM_STATE", StrUtil.split(bizShipPlan.getAlgorithmStateIn(), ","));

		Long total = baseMapper.pageCount(queryWrapper);
		List<BizShipPlan> list = new ArrayList<>();
		if(total > 0){
			list = baseMapper.page(queryWrapper, (page - 1) * size, page * size);
		}
		IPage<BizShipPlan> iPage = new Page<>();
		iPage.setSize(size);
		iPage.setCurrent(page);
		iPage.setPages((total / size) + (total % size > 0 ? 1 : 0));
		iPage.setTotal(total);
		iPage.setRecords(list);

		return iPage;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R deleteByIds(String ids) {
		if (StrUtil.isEmpty(ids)) {
			return R.failed_empty("id");
		}
		List<String> idList = Arrays.asList(ids.split(","));
		List<BizShipForecast> shipList = new ArrayList<>();
		AtomicInteger result = new AtomicInteger(0);
		idList.forEach(id -> {
			BizShipPlan bizShipPlan = infoById(id);
			if(null == bizShipPlan){
				result.set(1);
			}
			BizShipForecast bizShipForecast = bizShipForecastService.infoById(bizShipPlan.getShipForcastId());
			if(null == bizShipForecast){
				result.set(1);
			}
			if (bizShipForecast != null) {
				bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE0.getStatus());
			}
			shipList.add(bizShipForecast);
		});
		if (result.get() == 1) {
			return R.failed_null("id");
		}
//		if(!AlgorithmEnum.STATE4.getStatus().equals(bizShipForecast.getAlgorithmState())){
//			return R.failed_biz("当前状态不可删除");
//		}

		// 2. 删除靠离泊计划
		boolean success = removeBatchByIds(idList);

		// 3. 修改船舶预报状态
		if(!shipList.isEmpty()){
			success = success && bizShipForecastService.updateBatchById(shipList);
		}

		if(!success){
			//手动强制回滚事务
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return R.failed();
		}
		return R.ok();

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R addPlan(BizShipPlan bizShipPlan, boolean submit) {

		// 非空判断
		if(StrUtil.isEmpty(bizShipPlan.getShipForcastId())){
			return R.failed_empty("shipForcastId");
		}
		if(StrUtil.isEmpty(bizShipPlan.getBerthNo())){
			return R.failed_empty("berthNo");
		}
		if(StrUtil.isEmpty(bizShipPlan.getHeadBollardId())){
			return R.failed_empty("headBollardId");
		}
		if(StrUtil.isEmpty(bizShipPlan.getTailBollardId())){
			return R.failed_empty("tailBollardId");
		}
		if(null == bizShipPlan.getPlanBerthTime()){
			return R.failed_empty("planBerthTime");
		}
		if(null == bizShipPlan.getPlanStartTime()){
			return R.failed_empty("planStartTime");
		}
		if(null == bizShipPlan.getPlanFinishTime()){
			return R.failed_empty("planFinishTime");
		}
		if(null == bizShipPlan.getPlanLeaveTime()){
			return R.failed_empty("planLeaveTime");
		}

		// 判断空对象
		// 判断船舶预报
		BizShipForecast bizShipForecast = bizShipForecastService.infoById(bizShipPlan.getShipForcastId());
		if(null == bizShipForecast){
			return R.failed_null("shipForcastId");
		}
		if(!AlgorithmEnum.STATE0.getStatus().equals(bizShipForecast.getAlgorithmState())){
			return R.failed_biz("当前状态不可新增靠离泊计划");
		}
		// 判断 泊位
		BizBerthInfo bizBerthInfo = bizBerthInfoService.infoByBerthNo(bizShipPlan.getBerthNo());
		if(bizBerthInfo == null){
			return R.failed_null("brethNo");
		}
		// 判断 缆住
		BizBollardInfo headAlgBollardInfo = bizBollardInfoService.infoById(bizShipPlan.getHeadBollardId());
		BizBollardInfo tailAlgBollardInfo = bizBollardInfoService.infoById(bizShipPlan.getTailBollardId());
		if(headAlgBollardInfo == null){
			return R.failed_null("headBollardId");
		}
		if(tailAlgBollardInfo == null){
			return R.failed_null("tailBollardId");
		}

		// 修改 靠离泊计划状态
		if(submit){
			// 调用生产系统-提交审核
			R r = dockService.submitAudit();
			if(!R.checkOk(r)){
				return r;
			}
			// 状态设置为 待审核
			bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE5.getStatus());
			bizShipPlan.setPlanStatus(AlgorithmEnum.STATE5.getStatus());
		}else{
			// 状态设置为 待提交
			bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE4.getStatus());
			bizShipPlan.setPlanStatus(AlgorithmEnum.STATE4.getStatus());
		}

		bizShipPlan.setCreator(UserUtils.getUser().getUserName());
		bizShipPlan.setCreateTime(new Date());

		// 新增 靠离泊计划
//		bizShipPlan.setId(sysIdService.getId("ALG_SHIP_PLAN"));
		boolean success = save(bizShipPlan);
		success = success && bizShipForecastService.updateOrCleanById(bizShipForecast);

		if(!success){
			//手动强制回滚事务
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return R.failed();
		}

		return R.ok();
	}

	@Override
	public R submit(String ids) {
		// 1. 判断ID是否为空
		if(StrUtil.isEmpty(ids)){
			return R.failed_empty("ids");
		}
		// 2. 判断ID是存在
		BizShipPlan form1 = new BizShipPlan();
		form1.setIdIn(ids);
		List<BizShipPlan> bizShipPlanList = queryList(form1);
		if(bizShipPlanList.size() != ids.split(",").length){
			return R.failed_null("id");
		}
		// 3. 判断ID是否可提交审核
		for(BizShipPlan bizShipPlan : bizShipPlanList){
			// 3.1 检查是否可提交审核
			if(!AlgorithmEnum.STATE4.getStatus().equals(bizShipPlan.getAlgorithmState())
					&& !AlgorithmEnum.STATE7.getStatus().equals(bizShipPlan.getAlgorithmState())
			){
				return R.failed_biz("当前状态不可提交审核");
			}
		}

		// 5. 调用生产系统-提交审核
		R r = dockService.submitAudit();
		if(!R.checkOk(r)){
			return r;
		}

		// 6. 修改状态
		boolean success = updateBatchById(bizShipPlanList);

		success = success && bizShipForecastService.updateStatus(
				AlgorithmEnum.STATE5.getStatus(),
				CollUtil.join(bizShipPlanList.stream().map(BizShipPlan::getShipForcastId).collect(Collectors.toList()), ",")
		);

		if(!success){
			//手动强制回滚事务
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return R.failed();
		}
		return R.ok();
	}

	@Override
	public BizShipPlan getByForecastId(String forecastId) {
		BizShipPlan form = new BizShipPlan();
		form.setShipForcastId(forecastId);
		QueryWrapper<BizShipPlan> queryWrapper = MybatisPlusUtils.getQueryWrapper(form, null);

		return getOne(queryWrapper);
	}

	@Transactional
	@Override
	public boolean updateShipPlan(BizShipPlan bizShipPlan) {
//		Boolean success = true;
//		BizShipForecast bizShipForecast =  bizShipForecastService.infoById(bizShipPlan.getShipForcastId());
//		if(null != bizShipForecast){
//			bizShipForecast.setNeedLead(bizShipPlan.getNeedLead());
//			bizShipForecast.setNeedTractor(bizShipPlan.getNeedTractor());
//			success = success && bizShipForecastService.updateOrCleanById(bizShipForecast);
//		}
//		success = success && updateOrCleanById(bizShipPlan);
//
//		if(!success){
//			//手动强制回滚事务
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//		}
//
//		return success;
		return updateOrCleanById(bizShipPlan);
	}

}
