package com.jeesite.modules.algorithm.service;

import com.jeesite.common.base.R;

public interface IPlanningService {

    /**
     * 长期计划排产
     * @return
     */
   R scheduleShipPlan();

    /**
     * 昼夜计划
     * @return
     */
   R generateWorkPlan();

}
