package com.jeesite.modules.algorithm.workplan.ruler;


import com.jeesite.modules.algorithm.enums.CargoWhereaboutsRequirementEnum;
import com.jeesite.modules.algorithm.enums.LoadUnloadEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data


public class CargoEfficiencyRuler {

    private String berthNo;

    private String cargoType;

    private String cargoOwnerShortName;

    private LoadUnloadEnum loadUnload;

    /**
     * 一班干多少吨
     */
    private BigDecimal efficiencyPerHour;

    private CargoWhereaboutsRequirementEnum cargoWhereaboutsRequirement;


    public CargoEfficiencyRuler(String berthNo, String cargoType, LoadUnloadEnum loadUnload, BigDecimal efficiencyPerHour) {
        this.berthNo = berthNo;
        this.cargoType = cargoType;
        this.loadUnload = loadUnload;
        this.efficiencyPerHour = efficiencyPerHour;
    }

    public CargoEfficiencyRuler(String berthNo, String cargoType, LoadUnloadEnum loadUnload, CargoWhereaboutsRequirementEnum cargoWhereaboutsRequirement, BigDecimal efficiencyPerHour) {
        this.berthNo = berthNo;
        this.cargoType = cargoType;
        this.loadUnload = loadUnload;
        this.efficiencyPerHour = efficiencyPerHour;
        this.cargoWhereaboutsRequirement = cargoWhereaboutsRequirement;
    }

    public CargoEfficiencyRuler(String berthNo, String cargoType, String cargoOwnerShortName, LoadUnloadEnum loadUnload, BigDecimal efficiencyPerHour) {
        this.berthNo = berthNo;
        this.cargoType = cargoType;
        this.cargoOwnerShortName = cargoOwnerShortName;
        this.loadUnload = loadUnload;
        this.efficiencyPerHour = efficiencyPerHour;
    }
}
