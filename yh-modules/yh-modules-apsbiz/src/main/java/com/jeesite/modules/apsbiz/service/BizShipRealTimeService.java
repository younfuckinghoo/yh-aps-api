package com.jeesite.modules.apsbiz.service;


import com.jeesite.common.base.R;
import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizShipRealTime;

import java.util.Date;

/**
 * 在泊船舶动态数据
 *
 * @author zhudi
 * @date 2024-12-08 21:19:20
 */
public interface BizShipRealTimeService extends YhService<BizShipRealTime> {

    /**
     * 通过航次查询
     * @param voyageNo
     * @return
     */
    BizShipRealTime infoByVoyageNo(String voyageNo);

    /**
     * 智能预排
     * @return
     */
    R apsScheduling(Integer mode);

    /**
     * 批量预排
     * @param ids
     * @return
     */
    R scheduling(String ids);

    /**
     * 修改状态
     * @param status
     * @param join
     * @return
     */
    boolean updateStatus(Integer status, String join);
    /**
     * 修改状态
     * @param status
     * @param join
     * @return
     */
    boolean updateStatusByVoyage(Integer status, String join);
    /**
     * 修改预排表里面的状态
     * @return
     */
    boolean updateTempStatus(Integer status, String voyageNo);
    /**
     * 取消预排
     * @param id
     * @return
     */
    R canelScheduling(String id);
}
