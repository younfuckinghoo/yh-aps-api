package com.jeesite.modules.apsbiz.service;


import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizShipInfo;

/**
 * 缆柱表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:17
 */
public interface BizShipInfoService extends YhService<BizShipInfo> {
	BizShipInfo infoByCode(String shipCode);
	BizShipInfo infoByName(String shipName);
	BizShipInfo infoByVoyageNo(String voyageNo);
}
