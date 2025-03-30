package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.base.MutableLocalDateTime;
import com.jeesite.modules.algorithm.entity.AlgShoreMachine;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 岸机
 */

@Data
public class WorkPlanShoreMachineDO implements Cloneable {


    private AlgShoreMachine algShoreMachine;
    private List<WorkPlanShipMachineAllocDO> algShipMachineAllocSegments;

    public WorkPlanShoreMachineDO(@NotNull AlgShoreMachine algShoreMachine, List<WorkPlanShipMachineAllocDO> algShipMachineAllocSegments) {
        this.algShoreMachine = algShoreMachine;
        this.algShipMachineAllocSegments = algShipMachineAllocSegments == null ? new ArrayList<>() : algShipMachineAllocSegments.stream().sorted(Comparator.comparing(t->t.getEndTime())).toList();
    }


    /**
     * 在某个时间点机械是否可用
     * @param readyTime
     * @return
     */
    public boolean isFree(MutableLocalDateTime readyTime){
        return CollectionUtils.isEmpty(this.algShipMachineAllocSegments) || !this.algShipMachineAllocSegments.stream().anyMatch(t->
                t.getStartTime().isBeforeEqual(readyTime)
                        && t.getEndTime().isAfterEqual(readyTime));
    }



    /**
     * 根据开工时间排序 把机械占用放到占用时间段中
     * @param workPlanShipMachineAllocDO
     * @return
     */
    public int putSegmentOrderByPlanBerthTime(WorkPlanShipMachineAllocDO workPlanShipMachineAllocDO) {
        // 找到要把这条船的segment 放到第几个
        int i = 0;
        Iterator<WorkPlanShipMachineAllocDO> iterator = this.algShipMachineAllocSegments.iterator();
        while (iterator.hasNext()){
            WorkPlanShipMachineAllocDO allocDO = iterator.next();
            //找到第一个比这条船大的靠泊时间A 说明这条船在A之前靠 插在A前面
            if (allocDO.getStartTime().isAfter(workPlanShipMachineAllocDO.getStartTime())) {
                break;
            }
            i++;
        }

        this.algShipMachineAllocSegments.add(i, workPlanShipMachineAllocDO);
        return i;
    }






}
