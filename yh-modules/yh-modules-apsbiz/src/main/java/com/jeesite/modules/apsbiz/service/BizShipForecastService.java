package com.jeesite.modules.apsbiz.service;


import com.jeesite.common.base.R;
import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizShipForecast;

/**
 * 船舶预报
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
public interface BizShipForecastService extends YhService<BizShipForecast> {



	/**
	 * 通过来港航次查询船舶预报
	 * @param voyageNo
	 * @return
	 */
	BizShipForecast infoByVoyageNo(String voyageNo);

	/**
	 * 预排
	 * @param ids
	 * @return
	 */
	R scheduling(String ids);

	/**
	 * 取消预排
	 * @param id
	 * @return
	 */
	R canelScheduling(String id);

	/**
	 * 智能排泊
	 * @return
	 */
    R apsScheduling();

	/**
	 * 修改状态
	 * @param status
	 * @param join
	 * @return
	 */
    boolean updateStatus(Integer status, String join);
}
