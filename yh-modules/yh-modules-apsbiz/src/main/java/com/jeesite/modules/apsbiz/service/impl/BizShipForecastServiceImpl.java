
package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jeesite.common.base.R;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.algorithm.service.IPlanningService;
import com.jeesite.modules.apsbiz.entity.BizShipForecast;
import com.jeesite.modules.apsbiz.entity.BizShipPlan;
import com.jeesite.modules.apsbiz.entity.BizShipPlanTemp;
import com.jeesite.modules.apsbiz.mapper.BizShipForecastMapper;
import com.jeesite.modules.apsbiz.service.BizShipForecastService;
import com.jeesite.modules.apsbiz.service.BizShipPlanService;
import com.jeesite.modules.apsbiz.service.BizShipPlanTempService;
import com.jeesite.modules.apsbiz.service.DockService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 船舶预报
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Service
public class BizShipForecastServiceImpl extends YhServiceImpl<BizShipForecastMapper, BizShipForecast> implements BizShipForecastService {

	@Resource
	private BizShipPlanTempService bizShipPlanTempService;
	@Resource
	private BizShipPlanService bizShipPlanService;
	@Resource
	private IPlanningService iPlanningService;
	@Resource
	private DockService dockService;

	@Override
	public BizShipForecast infoByVoyageNo(String voyageNo) {
		BizShipForecast form = new BizShipForecast();
		form.setVoyageNo(voyageNo);
		QueryWrapper<BizShipForecast> queryWrapper = MybatisPlusUtils.getQueryWrapper(form, null);
		return getOne(queryWrapper);
	}

	@Override
	@Transactional
	public R scheduling(String ids) {
		// 1. 判断ID是否为空
		if(StrUtil.isEmpty(ids)){
			return R.failed_empty("ids");
		}
		// 2. 判断ID是存在
		BizShipForecast form1 = new BizShipForecast();
		form1.setIdIn(ids);
		List<BizShipForecast> algShipForecastList = queryList(form1);
		if(StrUtil.split(ids,",").size() != algShipForecastList.size()){
			return R.failed_null("ids");
		}

		// 3. 判断ID是否可进行预排
		for(BizShipForecast bizShipForecast : algShipForecastList){
			if(!AlgorithmEnum.STATE0.getStatus().equals(bizShipForecast.getAlgorithmState())){
				return R.failed_biz("["+bizShipForecast.getShipNameCn()+"]当前状态不可进行预排");
			}
			// 4. 进行预排
			bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE1.getStatus());
		}

		if(updateBatchById(algShipForecastList)){
			return R.ok();
		}
		return R.failed();
	}

	@Override
	@Transactional
	public R canelScheduling(String id) {
		// 1. 判断ID是否为空
		if(StrUtil.isEmpty(id)){
			return R.failed_empty("id");
		}
		// 2. 判断ID是存在
		BizShipForecast bizShipForecast =infoById(id);

		// 3. 判断是否可以取消预排
		if(!AlgorithmEnum.STATE1.getStatus().equals(bizShipForecast.getAlgorithmState())
				&& !AlgorithmEnum.STATE2.getStatus().equals(bizShipForecast.getAlgorithmState())
				&& !AlgorithmEnum.STATE4.getStatus().equals(bizShipForecast.getAlgorithmState())
		){
			return R.failed_biz("["+bizShipForecast.getShipNameCn()+"]当前状态不可取消预排");
		}
		Boolean success = true;

		// 删除预计划
		if(AlgorithmEnum.STATE2.getStatus().equals(bizShipForecast.getAlgorithmState())){
			BizShipPlanTemp bizShipPlanTemp = bizShipPlanTempService.infoByForeacastId(bizShipForecast.getId());
			if(null != bizShipPlanTemp){
				success = success && bizShipPlanTempService.removeById(bizShipPlanTemp.getId());
			}
		}

		// 删除计划
		if(AlgorithmEnum.STATE4.getStatus().equals(bizShipForecast.getAlgorithmState())){
			BizShipPlan bizShipPlan = bizShipPlanService.getByForecastId(bizShipForecast.getId());
			if(null != bizShipPlan){
				success = success && bizShipPlanService.removeById(bizShipPlan.getId());
			}
		}

		// 4. 重置预排状态
		bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE0.getStatus());

		// 5. 修改状态
		success = success && updateOrCleanById(bizShipForecast);

		if(!success){
			//手动强制回滚事务
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return R.failed();
		}

        return R.ok();
	}

	@Transactional
	@Override
	public R apsScheduling() {
        // 1. 判断当前预排数据是否满足智能排泊
        // 默认只查询 APS排泊预排(1), APS排泊未提交(2)  的数据
        BizShipForecast form1 = new BizShipForecast();
        form1.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE1.getStatus(), AlgorithmEnum.STATE2.getStatus()));
        List<BizShipForecast> list = queryList(form1);
		if(CollUtil.isEmpty(list)){
			return R.failed_biz("无预排船舶");
		}
		// 2. 调整预报船舶的算法状态 为（已排泊）
		list.forEach(bizShipForecast -> {
			bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE2.getStatus());
		});
		// 4. 删除预排泊中的数据
		BizShipPlanTemp form2 = new BizShipPlanTemp();
		form2.setShipForcastIdIn(CollUtil.join(list.stream().map(BizShipForecast::getId).collect(Collectors.toList()), ","));
		List<BizShipPlanTemp> oldAlgShipPlanTempList = bizShipPlanTempService.queryList(form2);

		boolean success = true;
		// 5. 执行 删除预排泊中的数据
		if(CollUtil.isNotEmpty(oldAlgShipPlanTempList)){
			success = bizShipPlanTempService.removeBatchByIds(oldAlgShipPlanTempList);
		}

		// 7. 修改预报船舶状态
		success = success && updateBatchById(list);

		// 8. 进行智能排泊（调用算法）
		R r = iPlanningService.scheduleShipPlan();
		if(!R.checkOk(r)){
			return r;
		}

		if(!success){
			//手动强制回滚事务
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return R.failed();
		}

        return R.ok();
	}

//	@Transactional
//	@Override
//	public R apsScheduling() {
//		// 1. 判断当前预排数据是否满足智能排泊
//		// 默认只查询 APS排泊预排(1), APS排泊未提交(2)  的数据
//		BizShipForecast form1 = new BizShipForecast();
//		form1.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE1.getStatus(), AlgorithmEnum.STATE2.getStatus()));
//		List<BizShipForecast> list = queryList(form1);
//
//		if(CollUtil.isEmpty(list)){
//			return R.ok();
//		}
//
//
//		// 为了保证 入库顺序与forecast一致。对查出的内容反转
//		list = CollUtil.reverse(list);
//		// 2. 调整预报船舶的算法状态 为（已排泊）
//		list.forEach(bizShipForecast -> {
//			bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE2.getStatus());
//		});
//
//		// 3. 生成 （预）靠离泊计划
//		List<BizShipPlanTemp> algShipPlanTempList = new ArrayList<>(dockService.getPlanTemp(list));
//
//		// 4. 删除预排泊中的数据
//		BizShipPlanTemp form2 = new BizShipPlanTemp();
//		form2.setShipForcastIdIn(CollUtil.join(list.stream().map(BizShipForecast::getId).collect(Collectors.toList()), ","));
//		List<BizShipPlanTemp> oldAlgShipPlanTempList = bizShipPlanTempService.queryList(form2);
//
//		boolean success = true;
//		// 5. 执行 删除预排泊中的数据
//		if(CollUtil.isNotEmpty(oldAlgShipPlanTempList)){
//			success = bizShipPlanTempService.removeBatchByIds(oldAlgShipPlanTempList);
//		}
//
//		// 6. 新增预排泊数据
//		success = success && bizShipPlanTempService.saveBatch(algShipPlanTempList);
//
//		// 7. 修改预报船舶状态
//		success = success && updateBatchById(list);
//
//		// 8. 进行智能排泊（调用算法）
//		R r = dockService.apsShipScheduling();
//		if(!R.checkOk(r)){
//			return r;
//		}
//
//		if(!success){
//			//手动强制回滚事务
//			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			return R.failed();
//		}
//
//		return R.ok();
//	}



	@Override
	public boolean updateStatus(Integer status, String ids) {
		if(StrUtil.isEmpty(ids)){
			return true;
		}
		UpdateWrapper<BizShipForecast> updateWrapper = new UpdateWrapper<>();
		updateWrapper.in("ID", StrUtil.split(ids, ","));
		updateWrapper.set("ALGORITHM_STATE", status);
		return update(updateWrapper);
	}
}
