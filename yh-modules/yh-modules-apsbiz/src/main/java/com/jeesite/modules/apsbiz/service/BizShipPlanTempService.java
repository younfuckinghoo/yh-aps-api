package com.jeesite.modules.apsbiz.service;


import com.jeesite.common.base.R;
import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizShipPlanTemp;

import java.io.Serializable;

/**
 * （预）靠离泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
public interface BizShipPlanTempService extends YhService<BizShipPlanTemp> {

	/**
	 * 通过ID删除
	 * @param ids
	 * @return
	 */
	R deleteByIds(String ids);

	/**
	 * 提交审核
	 * @param ids
	 * @return
	 */
	R submit(String ids);


	/**
	 * 通过预报船舶ID查询
	 * @param forecastId
	 * @return
	 */
	BizShipPlanTemp infoByForeacastId(String forecastId);

	/**
	 * 修改预船舶计划
	 * @param bizShipPlanTemp
	 * @return
	 */
    boolean updateShipPlan(BizShipPlanTemp bizShipPlanTemp);
}
