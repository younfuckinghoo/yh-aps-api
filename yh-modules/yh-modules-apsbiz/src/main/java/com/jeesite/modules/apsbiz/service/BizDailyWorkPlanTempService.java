package com.jeesite.modules.apsbiz.service;


import com.jeesite.common.base.R;
import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizDailyWorkPlanTemp;

/**
 * （预）作业计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
public interface BizDailyWorkPlanTempService extends YhService<BizDailyWorkPlanTemp> {


	/**
	 * 通过在港ID查询
	 * @param id
	 * @return
	 */
	BizDailyWorkPlanTemp infoByRealTimeId(String id);

	/**
	 * 通过ID删除
	 * @param id
	 * @return
	 */
	R deleteById(String id);

	/**
	 * 提交审核
	 * @param ids
	 * @return
	 */
	R submit(String ids);
}
