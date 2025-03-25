package com.jeesite.modules.algorithm.domain;


import com.jeesite.modules.algorithm.base.CommonConstant;
import com.jeesite.modules.algorithm.base.MutableLocalDateTime;
import com.jeesite.modules.algorithm.entity.AlgBollardInfo;
import com.jeesite.modules.algorithm.planning.PlanningContext;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


@Data
public class PlanningBollardDO implements Cloneable{



    private final PlanningContext planningContext;


    // 榄柱信息
    private final AlgBollardInfo bollardInfo;

	// 占用时间段 保证是按照时间顺序排列的 并且满足安全靠泊时间
    private final List<PlanningShipDO>  occupiedTimeSegments = new ArrayList<>();

    public PlanningBollardDO(@NotNull PlanningContext planningContext, @NotNull AlgBollardInfo bollardInfo, @NotNull PlanningShipDO planningShipDO) {
        this.planningContext = planningContext;
        this.bollardInfo = bollardInfo;
        this.occupiedTimeSegments.add(planningShipDO);
    }
    public PlanningBollardDO(@NotNull PlanningContext planningContext, @NotNull AlgBollardInfo bollardInfo) {
        this.planningContext = planningContext;
        this.bollardInfo = bollardInfo;

    }



    /**
     * 在某个时间点是否可用
     * 这个时间只跟靠泊、离泊时间相关
     * @param readyTime
     * @return
     */
    public boolean isFree(MutableLocalDateTime readyTime){
      return CollectionUtils.isEmpty(this.occupiedTimeSegments) || !this.occupiedTimeSegments.stream().anyMatch(t->
              t.getPlanBerthTime().isBeforeEqual(readyTime)
                      && t.getPlanLeaveTime().isAfterEqual(readyTime));
    }

    /**
     * 是否可以被计划--已被占用的时间不冲突
     * 如果可以就放进去
     * @param planningShipDO
     * @return
     */
    public boolean checkShipAndPut(PlanningShipDO planningShipDO){
        // 任何有重复的都不行
        boolean anyRepeatMatch = this.occupiedTimeSegments.stream().anyMatch(t -> {
            return (t.getPlanBerthTime().isBefore(planningShipDO.getPlanBerthTime())
                    && t.getPlanLeaveTime().isAfter(planningShipDO.getPlanBerthTime())
            )
                    || (planningShipDO.getPlanBerthTime().isBefore(t.getPlanBerthTime())
                    && planningShipDO.getPlanLeaveTime().isAfter(t.getPlanBerthTime())
            );
        });

        // 如果有重复的说明不能安排
        if (anyRepeatMatch)return false;

        int i = putSegmentOrderByPlanBerthTime(planningShipDO);


        return this.checkSafetyWorking(i);

    }

    /**
     * 根据PlanBerthTime排序，将segment放入合适位置
     * @param planningShipDO
     * @return
     */
    public int putSegmentOrderByPlanBerthTime(PlanningShipDO planningShipDO) {
        // 找到要把这条船的segment 放到第几个
        int i = 0;
        Iterator<PlanningShipDO> iterator = this.occupiedTimeSegments.iterator();
        while (iterator.hasNext()){
            PlanningShipDO shipDO = iterator.next();
            //找到第一个比这条船大的靠泊时间A 说明这条船在A之前靠 插在A前面
            if (shipDO.getPlanBerthTime().isAfter(planningShipDO.getPlanBerthTime())) {
                break;
            }
            i++;
        }

        this.occupiedTimeSegments.add(i, planningShipDO);
        return i;
    }

    /**
     * 检查安全作业时间（同泊位的）
     * @param segmentIndex
     * @return
     */
    private boolean checkSafetyWorking(int segmentIndex) {

        BigDecimal shipWorkingGap = this.planningContext.getRulerCollection().getShipWorkingGap();

        BigDecimal minutesGap = shipWorkingGap.multiply(BigDecimal.valueOf(CommonConstant.MinutePerHour));
        PlanningShipDO current = this.occupiedTimeSegments.get(segmentIndex);
        boolean flag = true;
        int preIdx = segmentIndex - 1;
        if (preIdx>=0){
            PlanningShipDO preShip = this.occupiedTimeSegments.get(preIdx);
            flag &= preShip.getPlanLeaveTime().plusMinutes(minutesGap.longValue()).isBeforeEqual(current.getPlanBerthTime());
        }
        int nextIdx = segmentIndex + 1;
        if (nextIdx<this.occupiedTimeSegments.size()){
            PlanningShipDO nextShip = this.occupiedTimeSegments.get(nextIdx);
            flag &= current.getPlanLeaveTime().plusMinutes(minutesGap.longValue()).isBeforeEqual(nextShip.getPlanBerthTime());
        }

        // 如果不满足 需要将这条船删除
        if (!flag){
            this.occupiedTimeSegments.remove(segmentIndex);
        }

        return flag;



    }


    public void removeSegment(PlanningShipDO planningShipDO) {
        for (int i = 0; i < this.occupiedTimeSegments.size(); i++) {
            PlanningShipDO seg = this.occupiedTimeSegments.get(i);
            if (seg.getShipForecast().getId().equals(planningShipDO.getShipForecast().getId())){
                this.occupiedTimeSegments.remove(i);
            }
        }

    }


    public static Comparator<PlanningBollardDO> comparator = (b1, b2) -> {
        int i = b1.getBollardInfo().getBerthNo().compareTo(b2.getBollardInfo().getBerthNo());
        if (i == 0) {
            return b1.getBollardInfo().getBollardNo() - b2.getBollardInfo().getBollardNo();
        }
        return i;
    };
}
