
package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeesite.common.base.R;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.utils.MybatisPlusOracleUtils;
import com.jeesite.common.utils.MybatisPlusOracleUtils;
import com.jeesite.modules.apsbiz.entity.BizDailyWorkPlan;
import com.jeesite.modules.apsbiz.entity.BizDailyWorkPlanTemp;
import com.jeesite.modules.apsbiz.entity.BizShipRealTime;
import com.jeesite.modules.apsbiz.mapper.BizDailyWorkPlanTempMapper;
import com.jeesite.modules.apsbiz.service.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (预)作业计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Service
public class BizDailyWorkPlanTempServiceImpl extends YhServiceImpl<BizDailyWorkPlanTempMapper, BizDailyWorkPlanTemp> implements BizDailyWorkPlanTempService {

	@Resource
	private BizDailyWorkPlanService bizDailyWorkPlanService;

	@Resource @Lazy
	private BizShipRealTimeService bizShipRealTimeService;

	@Resource
	private DockService dockService;


	@Value("${default.page}")
	private Integer defPage;

	@Value("${default.size}")
	private Integer defSize;

	@Override
	public BizDailyWorkPlanTemp infoByRealTimeId(String id) {
		BizDailyWorkPlanTemp algDailyWorkPlanTemp = new BizDailyWorkPlanTemp();
		algDailyWorkPlanTemp.setShipRealTimeId(id);
		QueryWrapper<BizDailyWorkPlanTemp> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algDailyWorkPlanTemp,null);
		return getOne(queryWrapper);
	}


	@Override
	public List<BizDailyWorkPlanTemp> queryList(BizDailyWorkPlanTemp algDailyWorkPlanTemp) {
		QueryWrapper<BizDailyWorkPlanTemp> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algDailyWorkPlanTemp, "a");

		// 船名模糊查询
		queryWrapper.like(StrUtil.isNotEmpty(algDailyWorkPlanTemp.getShipNameCnLike()), "B.SHIP_NAME_CN", algDailyWorkPlanTemp.getShipNameCnLike());
		// 当前状态
		queryWrapper.eq(null != algDailyWorkPlanTemp.getStatus(), "B.STATUS", algDailyWorkPlanTemp.getStatus());
		// 算法状态
		queryWrapper.eq(null != algDailyWorkPlanTemp.getAlgorithmState(), "B.ALGORITHM_STATE", algDailyWorkPlanTemp.getAlgorithmState());
		// 算法状态
		if(StrUtil.isNotEmpty(algDailyWorkPlanTemp.getAlgorithmStateIn())){
			queryWrapper.and(wrapper -> wrapper
					.in("B.ALGORITHM_STATE", StrUtil.split(algDailyWorkPlanTemp.getAlgorithmStateIn(), ","))
					.or()
					.in("A.STAY_ALGORITHM_STATE", StrUtil.split(algDailyWorkPlanTemp.getAlgorithmStateIn(), ","))
			);
		}
		// 泊位
		queryWrapper.eq(null != algDailyWorkPlanTemp.getBerthNo(), "B.REAL_TIME_BERTH", algDailyWorkPlanTemp.getBerthNo());

		// 靠泊时间（范围开始）
		queryWrapper.apply(null != algDailyWorkPlanTemp.getBerthTimeStart(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algDailyWorkPlanTemp.getBerthTimeStart());
		// 靠泊时间（范围结束）
		queryWrapper.apply(null != algDailyWorkPlanTemp.getBerthTimeEnd(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algDailyWorkPlanTemp.getBerthTimeEnd());

		return baseMapper.queryList(queryWrapper);
	}

	@Override
	public IPage<BizDailyWorkPlanTemp> queryPage(BizDailyWorkPlanTemp algDailyWorkPlanTemp, Integer page, Integer size) {
		if (page == null) {
			page = defPage;
		}
		if (size == null) {
			size = defSize;
		}
		IPage<BizDailyWorkPlanTemp> p = new Page<>(page, size);
		QueryWrapper<BizDailyWorkPlanTemp> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algDailyWorkPlanTemp, "a");

		// 船名模糊查询
		queryWrapper.like(StrUtil.isNotEmpty(algDailyWorkPlanTemp.getShipNameCnLike()), "B.SHIP_NAME_CN", algDailyWorkPlanTemp.getShipNameCnLike());
		// 当前状态
		queryWrapper.eq(null != algDailyWorkPlanTemp.getStatus(), "B.STATUS", algDailyWorkPlanTemp.getStatus());
		// 算法状态
		queryWrapper.eq(null != algDailyWorkPlanTemp.getAlgorithmState(), "B.ALGORITHM_STATE", algDailyWorkPlanTemp.getAlgorithmState());
		// 算法状态
		if(StrUtil.isNotEmpty(algDailyWorkPlanTemp.getAlgorithmStateIn())){
			queryWrapper.and(wrapper -> wrapper
					.in("B.ALGORITHM_STATE", StrUtil.split(algDailyWorkPlanTemp.getAlgorithmStateIn(), ","))
					.or()
					.in("A.STAY_ALGORITHM_STATE", StrUtil.split(algDailyWorkPlanTemp.getAlgorithmStateIn(), ","))
			);
		}
		// 泊位
		queryWrapper.eq(null != algDailyWorkPlanTemp.getBerthNo(), "B.REAL_TIME_BERTH", algDailyWorkPlanTemp.getBerthNo());

		// 靠泊时间（范围开始）
		queryWrapper.apply(null != algDailyWorkPlanTemp.getBerthTimeStart(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algDailyWorkPlanTemp.getBerthTimeStart());
		// 靠泊时间（范围结束）
		queryWrapper.apply(null != algDailyWorkPlanTemp.getBerthTimeEnd(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algDailyWorkPlanTemp.getBerthTimeEnd());




		Long total = baseMapper.pageCount(queryWrapper);
		List<BizDailyWorkPlanTemp> list = new ArrayList<>();
		if(total > 0){
			list = baseMapper.page(queryWrapper, (page - 1) * size, page * size);
		}

		IPage<BizDailyWorkPlanTemp> iPage = new Page<>();
		iPage.setSize(size);
		iPage.setCurrent(page);
		iPage.setPages((total / size) + (total % size > 0 ? 1 : 0));
		iPage.setTotal(total);
		iPage.setRecords(list);

		return iPage;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R deleteById(String id) {
		if(StrUtil.isEmpty(id)){
			return R.failed_empty("id");
		}
		BizDailyWorkPlanTemp algDailyWorkPlanTemp = infoById(id);
		if(null == algDailyWorkPlanTemp){
			return R.failed_null("id");
		}
		BizShipRealTime algShipRealTime = bizShipRealTimeService.infoById(algDailyWorkPlanTemp.getShipRealTimeId());
		if(null == algShipRealTime){
			return R.failed_null("id");
		}

		if(!AlgorithmEnum.STATE12.getStatus().equals(algShipRealTime.getAlgorithmState())){
			return R.failed_biz("当前状态不可删除");
		}
		// 1. 删除预排泊记录
		Boolean success = removeById(id);

		// 2. 修改船舶预报
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

	@Override
	public Boolean updateOrCleanById(BizDailyWorkPlanTemp algDailyWorkPlanTemp) {
		return update(MybatisPlusOracleUtils.getUpdateWrapper(algDailyWorkPlanTemp));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R submit(String ids) {
        // 1. 判断ID是否为空
        if(StrUtil.isEmpty(ids)){
            return R.failed_empty("ids");
        }
        // 2. 判断ID是存在
        BizDailyWorkPlanTemp form1 = new BizDailyWorkPlanTemp();
        form1.setIdIn(ids);
        List<BizDailyWorkPlanTemp> algDailyWorkPlanTempList = queryList(form1);
        if(algDailyWorkPlanTempList.size() != ids.split(",").length){
            return R.failed_null("id");
        }
        // 3. 判断ID是否可提交计划确认
		List<BizDailyWorkPlan> algDailyWorkPlanList = new ArrayList<>();

		// 为了保证 入库顺序与temp一致。对查出的内容反转
		algDailyWorkPlanTempList = CollUtil.reverse(algDailyWorkPlanTempList);
        for(BizDailyWorkPlanTemp algDailyWorkPlanTemp : algDailyWorkPlanTempList){
            // 3.1 检查是否可提交计划确认
			if(!AlgorithmEnum.STATE13.getStatus().equals(algDailyWorkPlanTemp.getAlgorithmState())
				&& !AlgorithmEnum.STATE12.getStatus().equals(algDailyWorkPlanTemp.getAlgorithmState())
			){
                return R.failed_biz("当前状态不可提交计划确认");
            }
			// 3.3
			algDailyWorkPlanList.add(dockService.getDailyWorkPlan(algDailyWorkPlanTemp));
        }

        // 6. 修改状态
        boolean success = updateBatchById(algDailyWorkPlanTempList);

		// 7. 生成排泊计划
		success = success && bizDailyWorkPlanService.saveBatch(algDailyWorkPlanList);

		// 8. 批量修改状态
		success = success && bizShipRealTimeService.updateStatus(
				AlgorithmEnum.STATE14.getStatus(),
				CollUtil.join(algDailyWorkPlanTempList.stream().map(BizDailyWorkPlanTemp::getShipRealTimeId).collect(Collectors.toList()), ",")
		);

		if(!success){
			//手动强制回滚事务
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return R.failed();
		}

        return R.ok();
	}
}
