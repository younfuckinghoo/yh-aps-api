package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.base.MutableLocalDateTime;
import com.jeesite.modules.algorithm.entity.AlgShipMachineAlloc;
import com.jeesite.modules.algorithm.entity.AlgShipPlan;
import com.jeesite.modules.algorithm.entity.AlgShipRealTime;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkPlanShipMachineAllocDO {
    /**
     * 机械分配
     */
    private AlgShipMachineAlloc algShipMachineAlloc;
    /**
     * 开工时间
     * 取值 实际开工时间{@link AlgShipRealTime.workingStartTime} | 计划开工时间{@link AlgShipPlan.planStartTime}
     */
    private MutableLocalDateTime startTime;
    /**
     * 完工时间
     * 取值  实际完工时间{@link AlgShipRealTime.workingFinishTime} | 计划完工时间{@link AlgShipPlan.planFinishTime}
     */
    private MutableLocalDateTime endTime;

}
