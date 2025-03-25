package com.jeesite.modules.apsbiz.service;


import com.jeesite.common.base.R;
import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizShipPlan;
import com.jeesite.modules.apsbiz.entity.BizShipPlanTemp;

/**
 * 靠离泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
public interface BizShipPlanService extends YhService<BizShipPlan> {

	/**
	 * 删除靠离泊计划
	 * @param ids
	 * @return
	 */
	R deleteByIds(String ids);

	/**
	 * 新增靠泊计划
	 * @param algShipPlan
	 * @return
	 */
	R addPlan(BizShipPlan algShipPlan, boolean submit);

	/**
	 * 提交审核
	 * @param ids
	 * @return
	 */
	R submit(String ids);

	/**
	 * 通过预报船舶ID查询
	 * @param id
	 * @return
	 */
	BizShipPlan getByForecastId(String id);

	/**
	 * 修改船舶计划
	 * @param bizShipPlan
	 * @return
	 */
	boolean updateShipPlan(BizShipPlan bizShipPlan);


}
