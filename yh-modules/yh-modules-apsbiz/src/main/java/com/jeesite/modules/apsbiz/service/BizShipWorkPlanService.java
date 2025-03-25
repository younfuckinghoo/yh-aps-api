package com.jeesite.modules.apsbiz.service;

import com.jeesite.common.base.R;
import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizShipWorkPlan;

public interface BizShipWorkPlanService extends YhService<BizShipWorkPlan> {
    /**
     * 删除作业计划
     * @param id
     * @return
     */
    R deleteById(String id);
}
