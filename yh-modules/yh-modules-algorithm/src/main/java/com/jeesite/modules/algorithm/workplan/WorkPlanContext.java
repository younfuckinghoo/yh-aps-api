package com.jeesite.modules.algorithm.workplan;

import com.jeesite.modules.algorithm.domain.WorkPlanMachinePool;
import com.jeesite.modules.algorithm.domain.WorkPlanShipDO;
import com.jeesite.modules.algorithm.entity.AlgCargoOwner;
import com.jeesite.modules.algorithm.entity.AlgCargoType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WorkPlanContext {


    private WorkPlanRulerCollection workPlanRulerCollection;
    private Map<String, AlgCargoType> cargoTypeMap;
    private Map<String, AlgCargoOwner> cargoOwnerMap;
    private WorkPlanMachinePool machinePool;
    private List<WorkPlanShipDO> workPlanShipDOS;

    {
        workPlanRulerCollection = new WorkPlanRulerCollection();
    }



}
