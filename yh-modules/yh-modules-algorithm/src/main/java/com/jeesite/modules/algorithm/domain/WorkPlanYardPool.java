package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.entity.*;
import com.jeesite.modules.algorithm.utils.MathUtil;
import com.jeesite.modules.algorithm.workplan.WorkPlanContext;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 郝勇
 * @description 库场池
 * @date 2022/3/28
 */
public class WorkPlanYardPool {
    private final WorkPlanContext workPlanContext;

    List<AlgYard> yardList;
    List<AlgYardBerthRel> yardBerthRelList;
    Map<String,List<AlgYard>> yardListMap;
    Predicate<AlgYard> yardPredicate = t-> MathUtil.isMoreThan(t.getAvailableCapacity(),0);
    /**
     * 容量大的优先
     * todo: 优先级应该按照刚好能满足的容量的优先 或者载货量大的倒序 载货量小的正序
     */
    Comparator<AlgYard> comparator =  Comparator.comparing(AlgYard::getAvailableCapacity).reversed();

    public WorkPlanYardPool(WorkPlanContext workPlanContext, List<AlgYard> yardList, List<AlgYardBerthRel> yardBerthRelList) {
        this.workPlanContext = workPlanContext;
        this.yardList = yardList;
        this.yardBerthRelList = yardBerthRelList;
        Map<String, AlgYard> algYardMap = this.yardList.stream().collect(Collectors.toMap(t -> t.getYardNo(), t -> t));
        this.yardListMap = this.yardBerthRelList.stream().collect(Collectors.groupingBy(AlgYardBerthRel::getBerthNo, Collectors.mapping(t->algYardMap.get(t.getYardNo()), Collectors.toList())));
    }



    public List<AlgYard> getBerthYardList(String berthNo) {
        return this.yardListMap.get(berthNo).stream().filter(yardPredicate).sorted(comparator).toList();
    }


    public List<AlgYard> getYardListByYardNo(String... yardNos) {
        return this.yardList.stream().filter(yard-> Arrays.stream(yardNos).anyMatch(yardNo-> StringUtils.equals(yardNo,yard.getYardNo())))
                .filter(yardPredicate)
                .sorted(comparator)
                .toList();
    }



    public List<AlgYard> mappingShipSiloList(WorkPlanShipDO workPlanShipDO, String... yardNos) {

        List<AlgYard> algYardList;
        if (yardNos == null || yardNos.length == 0) {
            algYardList = getBerthYardList(workPlanShipDO.getAlgShipPlan().getBerthNo());
        } else {
            algYardList = getYardListByYardNo(yardNos);
        }
        BigDecimal initial = workPlanShipDO.getThroughputWeight().add(BigDecimal.ZERO);

        List<AlgYard> result = new ArrayList<>();
        for (AlgYard algYard : algYardList) {
            // 如果一个库场容量大于等于当前要装载的货量 直接返回
            if (MathUtil.isMoreThan(algYard.getAvailableCapacity(), initial)){
                initial = BigDecimal.ZERO;
                result.add(algYard);
                break;
            }

            initial = initial.subtract(algYard.getAvailableCapacity());
            result.add(algYard);
        }

        if (initial.compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("库场容量不够");
        }

        initial = workPlanShipDO.getThroughputWeight().add(BigDecimal.ZERO);


        List<AlgShipYardArrange> shipYardArrangeList = new ArrayList<>();
        for (AlgYard t : result) {
            AlgShipYardArrange algShipYardArrange = new AlgShipYardArrange();
            algShipYardArrange.setYardNo(t.getYardNo());
            algShipYardArrange.setVoyageNo(workPlanShipDO.getShipForecast().getVoyageNo());

            if (MathUtil.isMoreEqualThan(initial,t.getAvailableCapacity())) {
                BigDecimal availableCapacity = t.getAvailableCapacity().add(BigDecimal.ZERO);
                initial = initial.subtract(t.getAvailableCapacity());
                t.setAvailableCapacity(BigDecimal.ZERO);
                t.setOccupiedCapacity(t.getOccupiedCapacity().add(availableCapacity));

            } else {
                t.setAvailableCapacity(t.getAvailableCapacity().subtract(initial));
                t.setOccupiedCapacity(t.getOccupiedCapacity().add(initial));
            }

            shipYardArrangeList.add(algShipYardArrange);
        }

        workPlanShipDO.setShipYardArrangeList(shipYardArrangeList);
        return result;
    }
}
