package com.jeesite.modules.algorithm.domain;


import com.jeesite.modules.algorithm.base.MutableLocalDateTime;
import com.jeesite.modules.algorithm.entity.AlgBerthMachineRel;
import com.jeesite.modules.algorithm.entity.AlgShipMachineAlloc;
import com.jeesite.modules.algorithm.entity.AlgShoreMachine;
import com.jeesite.modules.algorithm.enums.LoadUnloadEnum;
import com.jeesite.modules.algorithm.enums.MachineClassEnum;
import com.jeesite.modules.algorithm.enums.MachineTypeEnum;
import com.jeesite.modules.algorithm.workplan.WorkPlanContext;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机械资源池
 */

@Data
public class WorkPlanMachinePool implements Cloneable {
    WorkPlanContext workPlanContext;

    /**
     * 岸机列表
     */
    private final List<WorkPlanShoreMachineDO> workPlanShoreMachines;


    private final List<AlgBerthMachineRel> algBerthMachineRels;

    private final Map<String, List<WorkPlanShoreMachineDO>> berthMachineListMap;

    public WorkPlanMachinePool(WorkPlanContext workPlanContext, List<WorkPlanShoreMachineDO> workPlanShoreMachines, List<AlgBerthMachineRel> algBerthMachineRels) {
        this.workPlanContext = workPlanContext;
        this.workPlanShoreMachines = workPlanShoreMachines;
        this.algBerthMachineRels = algBerthMachineRels;
        Map<String, WorkPlanShoreMachineDO> machineDoMap = workPlanShoreMachines.stream().collect(Collectors.toMap(t -> t.getAlgShoreMachine().getMachineNo(), t -> t));
        berthMachineListMap = algBerthMachineRels.stream().collect(Collectors.groupingBy(t -> t.getBerthNo(), Collectors.collectingAndThen(Collectors.toList(), p -> p.stream().map(x -> machineDoMap.get(x.getMachineNo())).collect(Collectors.toList()))));

    }

    /**
     * 获取某个泊位的可用机械
     *
     * @param berthNo
     * @return
     */

    public List<WorkPlanShoreMachineDO> getBerthMachine(String berthNo) {
        return this.berthMachineListMap.get(berthNo);
    }

    /**
     * 分配机械
     * @param workPlanShipDO
     * @return
     */
    public List<WorkPlanShipMachineAllocDO>  assignMachineForShip(WorkPlanShipDO workPlanShipDO) {
        String berthNo = workPlanShipDO.getAlgShipPlan().getBerthNo();
        List<WorkPlanShoreMachineDO> berthMachineList = getBerthMachine(berthNo);
        LocalDateTime planStartTime = workPlanShipDO.getAlgShipPlan().getPlanStartTime();
        // 过滤掉被占用的机械
        List<WorkPlanShoreMachineDO> avaliableMachineList = berthMachineList.stream().filter(t -> t.isFree(new MutableLocalDateTime(planStartTime))).toList();

        // 卸船默认可获取的机械全上
        if (workPlanShipDO.getLoadUnloadEnum() == LoadUnloadEnum.LOAD){
            avaliableMachineList = mappingLoadMachine(avaliableMachineList,workPlanShipDO);
        }else {
            if ("木材".equals(workPlanShipDO.getCargoType().getTypeName())){
                // 木片不能用卸船机
                avaliableMachineList = avaliableMachineList.stream().filter(t -> MachineTypeEnum.MEN.getCode() == t.getAlgShoreMachine().getMachineCode()).toList();
            }
        }



        List<WorkPlanShipMachineAllocDO> result = new ArrayList<>();
        for (WorkPlanShoreMachineDO workPlanShoreMachineDO : avaliableMachineList) {
            AlgShoreMachine algShoreMachine = workPlanShoreMachineDO.getAlgShoreMachine();
            WorkPlanShipMachineAllocDO workPlanShipMachineAllocDO = new WorkPlanShipMachineAllocDO();
            AlgShipMachineAlloc algShipMachineAlloc = new AlgShipMachineAlloc();
            algShipMachineAlloc.setMachineCount((short) 1);
            algShipMachineAlloc.setMachineId(algShoreMachine.getId());
            algShipMachineAlloc.setMachineName(algShoreMachine.getMachineName());
            algShipMachineAlloc.setMachineClassCode(MachineClassEnum.SHORE.getCode());
            algShipMachineAlloc.setVoyageNo(workPlanShipDO.getShipForecast().getVoyageNo());
            workPlanShipMachineAllocDO.setAlgShipMachineAlloc(algShipMachineAlloc);
            workPlanShipMachineAllocDO.setStartTime(workPlanShipDO.getAlgShipPlan().getPlanStartTime());
            workPlanShipMachineAllocDO.setEndTime(workPlanShipDO.getAlgShipPlan().getPlanFinishTime());
            result.add(workPlanShipMachineAllocDO);
            workPlanShoreMachineDO.putSegmentOrderByPlanBerthTime(workPlanShipMachineAllocDO);
        }


        // 流动机械
        // 挖掘机配置（仅卸船需要用到）：

        if (workPlanShipDO.getLoadUnloadEnum() == LoadUnloadEnum.UNLOAD){
            Integer machineNum = workPlanShipDO.getShipInfo().getCabinNum();
            //大豆：7个舱一般配4台挖掘机，舱比较少的话一舱一台
            if ("粮食".equals(workPlanShipDO.getCargoType().getTypeName())){
                if (machineNum>4)machineNum=4;
            }
            // //木片：一舱一台
            if ("木材".equals(workPlanShipDO.getCargoType().getTypeName())){
                machineNum = machineNum*1;
            }

        }
        return result;

    }


    private List<WorkPlanShoreMachineDO> mappingLoadMachine(List<WorkPlanShoreMachineDO> avaliableMachineList, WorkPlanShipDO workPlanShipDO) {
        String berthNo = workPlanShipDO.getAlgShipPlan().getBerthNo();
//        西2：默认2台门机（1#，2#）
        if ("802".equals(berthNo)){
            return avaliableMachineList.stream().filter(t -> MachineTypeEnum.MEN.getCode() == t.getAlgShoreMachine().getMachineCode()).limit(2).toList();
        }
//        西3：根据舱数分配，超过3个舱分配三台门机，2个舱分配2台门机
        if ("803".equals(berthNo)){
            Integer cabinNum = workPlanShipDO.getShipInfo().getCabinNum();
            Integer limit = 3;
            if (cabinNum<3){
                limit = 2;
            }
            return avaliableMachineList.stream().filter(t -> MachineTypeEnum.MEN.getCode() == t.getAlgShoreMachine().getMachineCode()).limit(limit).toList();
        }
//        西6：待定
//        西18：根据舱数分配，几个舱分配几台门机
        if ("803".equals(berthNo)){
            List<WorkPlanShoreMachineDO> machineDOS = avaliableMachineList.stream().filter(t -> MachineTypeEnum.MEN.getCode() == t.getAlgShoreMachine().getMachineCode()).toList();
            Integer cabinNum = workPlanShipDO.getShipInfo().getCabinNum();
            int limit = cabinNum>machineDOS.size()?machineDOS.size():cabinNum;
            return machineDOS.subList(0, limit);
        }

        throw new RuntimeException("此泊位暂不支持装船，不能分配机械！");

    }


    @Override
    public WorkPlanMachinePool clone() {
        try {
            return (WorkPlanMachinePool) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
