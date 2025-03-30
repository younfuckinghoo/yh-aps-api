package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.base.MutableLocalDateTime;
import com.jeesite.modules.algorithm.entity.AlgBeltProcess;
import com.jeesite.modules.algorithm.entity.AlgBeltProcessOcc;
import com.jeesite.modules.algorithm.entity.AlgShoreMachine;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 岸机
 */

@Data
public class WorkPlanBeltProcessDO implements Cloneable {


    private AlgBeltProcess algBeltProcess;

    private List<AlgBeltProcessOcc> beltProcessOccSegments;

    public WorkPlanBeltProcessDO(@NotNull AlgBeltProcess algBeltProcess, List<AlgBeltProcessOcc> beltProcessOccSegments) {
        this.algBeltProcess = algBeltProcess;
        this.beltProcessOccSegments = beltProcessOccSegments == null ? new ArrayList<>() : beltProcessOccSegments.stream().sorted(Comparator.comparing(t->t.getEndTime())).toList();
    }


    /**
     * 在某个时间点机械是否可用
     * @param readyTime
     * @return
     */
    public boolean isFree(LocalDateTime readyTime){
        return CollectionUtils.isEmpty(this.beltProcessOccSegments) || !this.beltProcessOccSegments.stream().anyMatch(t->
                !t.getStartTime().isAfter(readyTime)
                        && !t.getEndTime().isBefore(readyTime));
    }



    /**
     * 根据开工时间排序 把皮带占用放到占用时间段中
     * @param beltProcessOcc
     * @return
     */
    public int putSegmentOrderByPlanBerthTime(AlgBeltProcessOcc beltProcessOcc) {
        // 找到要把这条船的segment 放到第几个
        int i = 0;
        Iterator<AlgBeltProcessOcc> iterator = this.beltProcessOccSegments.iterator();
        while (iterator.hasNext()){
            AlgBeltProcessOcc allocDO = iterator.next();
            //找到第一个比这条船大的靠泊时间A 说明这条船在A之前靠 插在A前面
            if (allocDO.getStartTime().isAfter(beltProcessOcc.getStartTime())) {
                break;
            }
            i++;
        }

        this.beltProcessOccSegments.add(i, beltProcessOcc);
        return i;
    }






}
