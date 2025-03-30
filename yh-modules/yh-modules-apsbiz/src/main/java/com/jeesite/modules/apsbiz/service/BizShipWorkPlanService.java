package com.jeesite.modules.apsbiz.service;

import com.jeesite.common.base.R;
import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizShipWorkPlan;
import com.jeesite.modules.apsbiz.entity.BizShipWorkPlanTemp;

public interface BizShipWorkPlanService extends YhService<BizShipWorkPlan> {
    /**
     * 删除作业计划
     * @param id
     * @return
     */
    R deleteById(String id);
    /**
     * 更新基础信息后，更新其他表的信息
     * @param bizShipWorkPlan
     * @return
     */
    void updateOtherInfo(BizShipWorkPlan bizShipWorkPlan, BizShipWorkPlan base);
}
