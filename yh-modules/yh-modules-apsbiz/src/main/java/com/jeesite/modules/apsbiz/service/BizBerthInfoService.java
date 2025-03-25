package com.jeesite.modules.apsbiz.service;


import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizBerthInfo;

/**
 * 泊位表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:16
 */
public interface BizBerthInfoService extends YhService<BizBerthInfo> {

    /**
     * 通过泊位号，查询泊位
     * @param berthNo
     * @return
     */
    BizBerthInfo infoByBerthNo(String berthNo);
}
