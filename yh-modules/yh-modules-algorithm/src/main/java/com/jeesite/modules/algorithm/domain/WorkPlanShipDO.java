package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.entity.*;
import com.jeesite.modules.algorithm.enums.LoadUnloadEnum;
import com.jeesite.modules.algorithm.workplan.WorkPlanContext;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class WorkPlanShipDO {

    public WorkPlanShipDO(WorkPlanContext workPlanContext) {
        this.workPlanContext = workPlanContext;
    }

    // 船舶预报信息
    private AlgShipInfo shipInfo;
    // 船舶预报信息
    private AlgShipForecast shipForecast;
    // 实时船舶动态信息
    private AlgShipRealTime algShipRealTime;
    // 船舶长期计划
    private AlgShipPlan algShipPlan;


    private final WorkPlanContext workPlanContext;

    private LoadUnloadEnum loadUnloadEnum;

    private AlgCargoType cargoType;

    /**
     * 吞吐吨
     */
    private BigDecimal throughputWeight;
    /**
     * 货主简称对应数据
     */
    private AlgCargoOwner cargoOwner;

    /**
     * 是否需要更新计划
     */
    private boolean updatePlan = false;

    /**
     * 以前计划
     */
    private AlgShipWorkPlan previousWorkPlan;

    /**
     * 库场安排
     */
    List<AlgShipYardArrange> shipYardArrangeList;
    /**
     * 筒仓安排
     */
    List<AlgShipSiloArrange> shipSiloArrangeList;

    /**
     * 机械占用
     */
    List<AlgShipMachineAlloc> algShipMachineAllocList;


    public void setShipForecast(AlgShipForecast shipForecast) {
        this.shipForecast = shipForecast;
        this.loadUnloadEnum = LoadUnloadEnum.getInstanceByCode(shipForecast.getLoadUnload());
        this.cargoOwner = this.workPlanContext.getCargoOwnerMap().get(shipForecast.getCargoOwner());
        this.cargoType = this.workPlanContext.getCargoTypeMap().get(shipForecast.getCargoSubTypeCode());
        this.settingThroughputWeight();
    }

    private void settingThroughputWeight() {

        if (this.throughputWeight == null) {
            switch (this.loadUnloadEnum) {
                case LOAD:
                    this.throughputWeight = this.shipForecast.getLoadPlanWeight();
                    break;
                case UNLOAD:
                    this.throughputWeight = this.shipForecast.getUnloadPlanWeight();
                    break;
            }
        }
        if (throughputWeight == null)
            throw new RuntimeException(String.format("船号：%1$s--找不到吞吐吨", shipForecast.getShipCode()));

    }


    public void setShipInfo(AlgShipInfo shipInfo) {
        this.shipInfo = shipInfo;
    }

    public void setAlgShipRealTime(AlgShipRealTime algShipRealTime) {
        this.algShipRealTime = algShipRealTime;
    }

    public void setAlgShipPlan(AlgShipPlan algShipPlan) {
        this.algShipPlan = algShipPlan;
    }

    public void setUpdatePlan(boolean updatePlan) {
        this.updatePlan = updatePlan;
    }

    public void setPreviousWorkPlan(AlgShipWorkPlan previousWorkPlan) {
        this.previousWorkPlan = previousWorkPlan;
    }

    public void setShipYardArrangeList(List<AlgShipYardArrange> shipYardArrangeList) {
        this.shipYardArrangeList = shipYardArrangeList;
    }

    public List<AlgShipYardArrange> getShipYardArrangeList() {
        return shipYardArrangeList;
    }

    public void setShipSiloArrangeList(List<AlgShipSiloArrange> shipSiloArrangeList) {
        this.shipSiloArrangeList = shipSiloArrangeList;
    }


    public void setAlgShipMachineAllocList(List<AlgShipMachineAlloc> algShipMachineAllocList) {
        this.algShipMachineAllocList = algShipMachineAllocList;
    }


    public boolean isPlanned(){
        return this.previousWorkPlan!=null;
    }


}
