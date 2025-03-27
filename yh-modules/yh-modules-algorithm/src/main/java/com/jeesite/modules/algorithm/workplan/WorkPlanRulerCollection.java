package com.jeesite.modules.algorithm.workplan;

import com.jeesite.modules.algorithm.enums.CargoWhereaboutsRequirementEnum;
import com.jeesite.modules.algorithm.enums.LoadUnloadEnum;
import com.jeesite.modules.algorithm.workplan.ruler.CargoEfficiencyRuler;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data

public class WorkPlanRulerCollection {

    /**
     * 单班最大工作时长
     */
    private BigDecimal maxWorkTimeInShift;


    private List<CargoEfficiencyRuler> cargoEfficiencyRulerList;

    {
        maxWorkTimeInShift = new BigDecimal("10");



        cargoEfficiencyRulerList = List.of(
                // 西2
                new CargoEfficiencyRuler("802", "粮食", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(400)),
                new CargoEfficiencyRuler("802", "木材", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(200)),
                new CargoEfficiencyRuler("802", "粮食", LoadUnloadEnum.LOAD, BigDecimal.valueOf(600)),
                // 西3
                new CargoEfficiencyRuler("803", "木材", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(750)),
                new CargoEfficiencyRuler("803", "粮食", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(900)),
                new CargoEfficiencyRuler("803", "粮食", LoadUnloadEnum.LOAD, BigDecimal.valueOf(800)),
                new CargoEfficiencyRuler("803", "农.林.牧.渔业产品", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(500)),
                // 西5
                new CargoEfficiencyRuler("805", "粮食", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(1000)),
                new CargoEfficiencyRuler("805", "粮食", "中纺粮油", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(1300)),
                new CargoEfficiencyRuler("805", "粮食", "嘉吉", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(1500)),
                new CargoEfficiencyRuler("805", "粮食", "邦基三维", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(850)),
                new CargoEfficiencyRuler("805", "粮食", "中储粮", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(2000)),
                // 西6
                new CargoEfficiencyRuler("806", "粮食", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(1000)),
                new CargoEfficiencyRuler("806", "粮食", "中纺粮油", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(1300)),
                new CargoEfficiencyRuler("806", "粮食", "嘉吉", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(1500)),
                new CargoEfficiencyRuler("806", "粮食", "邦基三维", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(850)),
                new CargoEfficiencyRuler("806", "粮食", "中储粮", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(2000)),
                // 西18
                new CargoEfficiencyRuler("509", "木材", LoadUnloadEnum.UNLOAD, BigDecimal.valueOf(400)),
                new CargoEfficiencyRuler("509", "粮食", LoadUnloadEnum.LOAD, BigDecimal.valueOf(2000)),
                new CargoEfficiencyRuler("509", "粮食", LoadUnloadEnum.UNLOAD, CargoWhereaboutsRequirementEnum.PICK, BigDecimal.valueOf(1500)),
                new CargoEfficiencyRuler("509", "粮食", LoadUnloadEnum.UNLOAD, CargoWhereaboutsRequirementEnum.STORE, BigDecimal.valueOf(1300))

        );
    }


}
