package com.jeesite.modules.apsbiz.service;


import com.jeesite.common.base.R;
import com.jeesite.modules.apsbiz.entity.*;

import java.util.Date;
import java.util.List;

/**
 * 三方接口
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
public interface DockService {

	/**
	 * 船期管理-向生产系统提交审核
	 * @return
	 */
	R submitAudit();

	/**
	 * 船期管理-向生产系统获取审核状态
	 * @return
	 */
	R pullStatus();

	/**
	 * 算法-靠离泊计划排产
	 * @return
	 */
	R apsShipScheduling();

	/**
	 * 算法-作业计划排产
	 * @return
	 */
	R apsWorkScheduling(Integer mode);

	/**
	 * 添加船舶预约
	 */
    void addForcast();

	/**
	 * 获取 （预）靠离泊计划
	 * @param algShipForecastList
	 * @return
	 */
	List<BizShipPlanTemp> getPlanTemp(List<BizShipForecast> algShipForecastList);

	/**
	 * 获取 靠离泊计划
	 * @param algShipPlanTemp
	 * @return
	 */
	BizShipPlan getAlgShipPlan(BizShipPlanTemp algShipPlanTemp);

	/**
	 * 添加在港船舶
	 */
	void addRealTime();

	/**
	 * 获取 （预）作业计划
	 * @param algShipRealTime
	 * @return
	 */
	BizDailyWorkPlanTemp getWorkPlanTemp(BizShipRealTime algShipRealTime);

	/**
	 * 获取 作业计划
	 * @param algDailyWorkPlanTemp
	 * @return
	 */
	BizDailyWorkPlan getDailyWorkPlan(BizDailyWorkPlanTemp algDailyWorkPlanTemp);

	/**
	 * 刷新在港船舶
	 */
	void refreshRealTime();


	/**
	 * 获取在港船舶 日期
	 */
	Date getPlanDate();
}
