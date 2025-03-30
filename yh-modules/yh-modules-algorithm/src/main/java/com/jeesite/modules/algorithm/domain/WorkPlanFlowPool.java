package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.entity.AlgBeltProcess;
import com.jeesite.modules.algorithm.entity.AlgBeltProcessOcc;
import com.jeesite.modules.algorithm.entity.AlgShipMachineAlloc;
import com.jeesite.modules.algorithm.entity.AlgShipSiloArrange;
import com.jeesite.modules.algorithm.workplan.WorkPlanContext;

import java.util.List;

/**
 * @author 郝勇
 * @description 筒仓池
 * @date 2022/3/28
 */
public class WorkPlanFlowPool {


    private final WorkPlanContext workPlanContext;

    private List<WorkPlanBeltProcessDO> algBeltProcessDoList;





    public WorkPlanFlowPool(WorkPlanContext workPlanContext,List<WorkPlanBeltProcessDO>  algBeltProcessDoList) {
        this.algBeltProcessDoList = algBeltProcessDoList;
        this.workPlanContext = workPlanContext;
    }

    public List<WorkPlanBeltProcessDO> getAvailableBeltProcess(WorkPlanShipDO workPlanShipDO) {
        List<AlgShipMachineAlloc> algShipMachineAllocList = workPlanShipDO.getAlgShipMachineAllocList();

        List<String> machineIdList = algShipMachineAllocList.stream().map(t -> t.getMachineId()).toList();
        //  统计卸船机数量
        long xiechuanjiCount = workPlanContext.getMachinePool().getWorkPlanShoreMachines().stream().filter(t -> {
            return machineIdList.contains(t.getAlgShoreMachine().getId()) && "卸船机".equals(t.getAlgShoreMachine().getMachineName());
        }).count();
        List<AlgShipSiloArrange> shipSiloArrangeList = workPlanShipDO.getShipSiloArrangeList();
        List<String> siloNoList = shipSiloArrangeList.stream().map(t -> t.getSiloNo()).toList();

        List<WorkPlanBeltProcessDO> list = algBeltProcessDoList.stream().filter(algBeltProcessDo -> {

            return machineIdList.contains(algBeltProcessDo.getAlgBeltProcess().getHeadLinkShore())
                    && siloNoList.contains(algBeltProcessDo.getAlgBeltProcess().getTailLinkSilo())
                    && algBeltProcessDo.isFree(workPlanShipDO.getAlgShipPlan().getPlanStartTime());

        }).toList();

        if (list.size()<xiechuanjiCount){
            throw new RuntimeException("没有合适的流程:"+workPlanShipDO.getShipForecast().getShipNameCn());
        }
        // 即将被占用的皮带
        List<WorkPlanBeltProcessDO> workPlanBeltProcessDOS = list.subList(0, (int) xiechuanjiCount);
        List<AlgBeltProcessOcc> algBeltProcessOccs = workPlanBeltProcessDOS.stream().map(t -> {
            AlgBeltProcessOcc algBeltProcessOcc = new AlgBeltProcessOcc();
            algBeltProcessOcc.setOccupiedProcessNo(t.getAlgBeltProcess().getProcessNo());
            algBeltProcessOcc.setVoyageNo(workPlanShipDO.getShipForecast().getVoyageNo());
            algBeltProcessOcc.setStartTime(workPlanShipDO.getAlgShipPlan().getPlanStartTime());
            algBeltProcessOcc.setEndTime(workPlanShipDO.getAlgShipPlan().getPlanFinishTime());
            t.putSegmentOrderByPlanBerthTime(algBeltProcessOcc);
            return algBeltProcessOcc;
        }).toList();

        workPlanShipDO.setBeltProcessOccList(algBeltProcessOccs);


        return list;
    }




}
