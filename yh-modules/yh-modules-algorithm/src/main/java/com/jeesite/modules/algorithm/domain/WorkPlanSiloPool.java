package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.entity.AlgShipSiloArrange;
import com.jeesite.modules.algorithm.entity.AlgSiloBase;
import com.jeesite.modules.algorithm.entity.AlgSiloBerthRel;
import com.jeesite.modules.algorithm.utils.MathUtil;
import com.jeesite.modules.algorithm.workplan.WorkPlanContext;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 郝勇
 * @description 筒仓池
 * @date 2022/3/28
 */
public class WorkPlanSiloPool {


    private final WorkPlanContext workPlanContext;

    List<AlgSiloBase> siloBaseList;

    List<AlgSiloBerthRel> siloBerthRelList;

    Map<String, List<AlgSiloBase>> berthSiloListMap;
    Predicate<AlgSiloBase> siloBasePredicate = t -> {
        // 可获得容量大于等于总容量 说明未被占用
        return MathUtil.isMoreEqualThan(t.getAvailableCapacity(), t.getTotalCapacity());
    };


    public WorkPlanSiloPool(WorkPlanContext workPlanContext, List<AlgSiloBase> siloBaseList, List<AlgSiloBerthRel> siloBerthRelList) {
        this.workPlanContext = workPlanContext;
        this.siloBaseList = siloBaseList;
        this.siloBerthRelList = siloBerthRelList;
        Map<String, AlgSiloBase> algSiloBaseMap = siloBaseList.stream().collect(Collectors.toMap(t -> t.getSiloNo(), t -> t));
        this.berthSiloListMap = siloBerthRelList.stream().collect(
                Collectors.groupingBy(AlgSiloBerthRel::getBerthNo, Collectors.mapping(t -> algSiloBaseMap.get(t.getSiloNo()), Collectors.toList()))
        );
    }

    public List<AlgSiloBase> getBerthSiloList(String berthNo) {
        // 获取该泊位的筒仓列表 过滤掉已经被占用
        return this.berthSiloListMap.get(berthNo).stream().filter(siloBasePredicate)
                .toList();
    }

    public List<AlgSiloBase> getSiloBaseListBySiloNo(String... siloNos) {
        return this.siloBaseList.stream().filter(t -> {
            return Arrays.stream(siloNos).anyMatch(siloNo -> StringUtils.equals(siloNo, t.getSiloNo()));
        }).filter(siloBasePredicate).toList();
    }


    public List<AlgSiloBase> mappingShipSiloList(WorkPlanShipDO workPlanShipDO, String... siloNos) {

        List<AlgSiloBase> berthSiloList;
        if (siloNos == null || siloNos.length == 0) {
            berthSiloList = getBerthSiloList(workPlanShipDO.getAlgShipPlan().getBerthNo());
        } else {
            berthSiloList = getSiloBaseListBySiloNo(siloNos);
        }
        BigDecimal initial = workPlanShipDO.getThroughputWeight().add(BigDecimal.ZERO);

        List<AlgSiloBase> result = new ArrayList<>();
        for (AlgSiloBase algSiloBase : berthSiloList) {
            if (MathUtil.isMoreEqualThan(algSiloBase.getTotalCapacity(), initial)) {
                initial = BigDecimal.ZERO;
                result.add(algSiloBase);
                break;
            }

            initial = initial.subtract(algSiloBase.getTotalCapacity());
            result.add(algSiloBase);
        }

        if (initial.compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("筒仓容量不够");
        }

        initial = workPlanShipDO.getThroughputWeight().add(BigDecimal.ZERO);


        List<AlgShipSiloArrange> shipSiloArrangeList = new ArrayList<>();
        for (AlgSiloBase t : result) {
            AlgShipSiloArrange algShipSiloArrange = new AlgShipSiloArrange();
            algShipSiloArrange.setSiloNo(t.getSiloNo());
            algShipSiloArrange.setVoyageNo(workPlanShipDO.getShipForecast().getVoyageNo());

            if (initial.compareTo(t.getTotalCapacity()) > 0) {
                initial = initial.subtract(t.getTotalCapacity());
                t.setAvailableCapacity(BigDecimal.ZERO);
                t.setOccupiedCapacity(t.getTotalCapacity());
                t.setRealTimeCapacity(BigDecimal.ZERO);
            } else {
                t.setAvailableCapacity(t.getTotalCapacity().subtract(initial));
                t.setOccupiedCapacity(initial);
                t.setRealTimeCapacity(BigDecimal.ZERO);
            }

            shipSiloArrangeList.add(algShipSiloArrange);
        }

        workPlanShipDO.setShipSiloArrangeList(shipSiloArrangeList);
        return result;
    }

}
