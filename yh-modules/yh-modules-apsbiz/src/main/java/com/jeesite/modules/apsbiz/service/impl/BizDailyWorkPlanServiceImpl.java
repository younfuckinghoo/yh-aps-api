
package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeesite.common.base.R;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.utils.MybatisPlusOracleUtils;
import com.jeesite.modules.apsbiz.entity.BizDailyWorkPlan;
import com.jeesite.modules.apsbiz.entity.BizShipRealTime;
import com.jeesite.modules.apsbiz.mapper.BizDailyWorkPlanMapper;
import com.jeesite.modules.apsbiz.service.BizDailyWorkPlanService;
import com.jeesite.modules.apsbiz.service.BizShipRealTimeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 船舶靠泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Service
public class BizDailyWorkPlanServiceImpl extends YhServiceImpl<BizDailyWorkPlanMapper, BizDailyWorkPlan> implements BizDailyWorkPlanService {

	@Resource @Lazy
	private BizShipRealTimeService bizShipRealTimeService;


	@Value("${default.page}")
	private Integer defPage;

	@Value("${default.size}")
	private Integer defSize;

	@Override
	public List<BizDailyWorkPlan> queryList(BizDailyWorkPlan algDailyWorkPlan) {
		QueryWrapper<BizDailyWorkPlan> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algDailyWorkPlan, "a");

		// 船名模糊查询
		queryWrapper.like(StrUtil.isNotEmpty(algDailyWorkPlan.getShipNameCnLike()), "B.SHIP_NAME_CN", algDailyWorkPlan.getShipNameCnLike());
		// 当前状态
		queryWrapper.eq(null != algDailyWorkPlan.getStatus(), "B.STATUS", algDailyWorkPlan.getStatus());
		// 泊位
		queryWrapper.eq(null != algDailyWorkPlan.getBerthNo(), "B.REAL_TIME_BERTH", algDailyWorkPlan.getBerthNo());
		// 算法状态
		queryWrapper.eq(null != algDailyWorkPlan.getAlgorithmState(), "B.ALGORITHM_STATE", algDailyWorkPlan.getAlgorithmState());
		// 算法状态
		queryWrapper.in(StrUtil.isNotEmpty(algDailyWorkPlan.getAlgorithmStateIn()), "B.ALGORITHM_STATE", StrUtil.split(algDailyWorkPlan.getAlgorithmStateIn(), ","));

		// 靠泊时间（范围开始）
		queryWrapper.apply(null != algDailyWorkPlan.getBerthTimeStart(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algDailyWorkPlan.getBerthTimeStart());
		// 靠泊时间（范围结束）
		queryWrapper.apply(null != algDailyWorkPlan.getBerthTimeEnd(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algDailyWorkPlan.getBerthTimeEnd());

		return baseMapper.queryList(queryWrapper);
	}


	@Override
	public IPage<BizDailyWorkPlan> queryPage(BizDailyWorkPlan algDailyWorkPlan, Integer page, Integer size) {
		if (page == null) {
			page = defPage;
		}
		if (size == null) {
			size = defSize;
		}
		IPage<BizDailyWorkPlan> p = new Page<>(page, size);
		QueryWrapper<BizDailyWorkPlan> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algDailyWorkPlan, "a");

		// 船名模糊查询
		queryWrapper.like(StrUtil.isNotEmpty(algDailyWorkPlan.getShipNameCnLike()), "B.SHIP_NAME_CN", algDailyWorkPlan.getShipNameCnLike());
		// 当前状态
		queryWrapper.eq(null != algDailyWorkPlan.getStatus(), "B.STATUS", algDailyWorkPlan.getStatus());
		// 算法状态
		queryWrapper.eq(null != algDailyWorkPlan.getAlgorithmState(), "B.ALGORITHM_STATE", algDailyWorkPlan.getAlgorithmState());
		// 算法状态
		queryWrapper.in(StrUtil.isNotEmpty(algDailyWorkPlan.getAlgorithmStateIn()), "B.ALGORITHM_STATE", StrUtil.split(algDailyWorkPlan.getAlgorithmStateIn(), ","));
		// 泊位
		queryWrapper.eq(null != algDailyWorkPlan.getBerthNo(), "B.REAL_TIME_BERTH", algDailyWorkPlan.getBerthNo());


		// 靠泊时间（范围开始）
		queryWrapper.apply(null != algDailyWorkPlan.getBerthTimeStart(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algDailyWorkPlan.getBerthTimeStart());
		// 靠泊时间（范围结束）
		queryWrapper.apply(null != algDailyWorkPlan.getBerthTimeEnd(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algDailyWorkPlan.getBerthTimeEnd());


		Long total = baseMapper.pageCount(queryWrapper);
		List<BizDailyWorkPlan> list = new ArrayList<>();
		if(total > 0){
			list = baseMapper.page(queryWrapper, (page - 1) * size, page * size);
		}
		IPage<BizDailyWorkPlan> iPage = new Page<>();
		iPage.setSize(size);
		iPage.setCurrent(page);
		iPage.setPages((total / size) + (total % size > 0 ? 1 : 0));
		iPage.setTotal(total);
		iPage.setRecords(list);

		return iPage;
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public R deleteById(String id) {
		if(StrUtil.isEmpty(id)){
			return R.failed_empty("id");
		}
		BizDailyWorkPlan algDailyWorkPlan = infoById(id);
		if(null == algDailyWorkPlan){
			return R.failed_null("id");
		}
		BizShipRealTime algShipRealTime = bizShipRealTimeService.infoById(algDailyWorkPlan.getShipRealTimeId());
		if(null == algShipRealTime){
			return R.failed_null("id");
		}
		if(!AlgorithmEnum.STATE14.getStatus().equals(algShipRealTime.getAlgorithmState())){
			return R.failed_biz("["+algDailyWorkPlan.getShipNameCn()+"]当前状态不可删除");
		}

		// 2. 删除 作业计划
		Boolean success = removeById(id);

		// 3. 修改 （预）作业计划 状态
		if(null != algShipRealTime){
			algShipRealTime.setAlgorithmState(AlgorithmEnum.STATE10.getStatus());
			success = success && bizShipRealTimeService.updateOrCleanById(algShipRealTime);
		}

		if(!success){
			//手动强制回滚事务
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return R.failed();
		}
		return R.ok();

	}
}
