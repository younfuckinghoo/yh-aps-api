package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.planning.PlanningContext;
import com.jeesite.modules.algorithm.planning.ruler.BaseBerthRuler;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthCargoOwner;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthCargoType;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthShipInfo;
import com.jeesite.modules.algorithm.utils.MathUtil;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 泊位资源池
 */
public class PlanningBerthPoolDO {


    private final PlanningContext planningContext;

    // 泊位集合
    private final List<PlanningBerthDO> berthDOList;
    private final List<PlanningBollardDO> planningBollardDOList;
    private final Map<String, PlanningBerthDO> berthNoMap;

    public PlanningBerthPoolDO(PlanningContext planningContext, List<PlanningBerthDO> berthDOList, List<PlanningBollardDO> planningBollardDOList) {
        this.planningContext = planningContext;
        this.berthDOList = berthDOList;
        this.berthNoMap = berthDOList.stream().collect(Collectors.toMap(t -> t.getBerthInfo().getBerthNo(), t -> t));
        this.planningBollardDOList = planningBollardDOList;

    }

    /**
     * 根据船的信息匹配泊位
     *
     * @param shipDO
     * @param planningTime
     * @return
     */
    public List<PlanningBerthDO> mappingBerthByShip(PlanningShipDO shipDO, LocalDateTime planningTime) {

        List<RulerBerthShipInfo> rulerBerthShipInfoList = this.planningContext.getRulerCollection().getRulerBerthShipInfoList();
        List<RulerBerthShipInfo> berthShipInfoList = rulerBerthShipInfoList.stream()
                .filter(t -> this.berthNoMap.containsKey(t.getBerthNo()))
                .filter(t -> MathUtil.isLessThan(shipDO.getShipForecast().getDraftIn(), t.getMaxShipDraft())).collect(Collectors.toList());
        List<String> shipInfoBerthNos = berthShipInfoList.stream().map(BaseBerthRuler::getBerthNo).collect(Collectors.toList());
        // 校验当天是否不排泊
        DayOfWeek dayOfWeek = planningTime.getDayOfWeek();
        Map<String, List<DayOfWeek>> berthNoDaysMap = this.planningContext.getRulerCollection().getBerthRestInWeekList().stream().collect(Collectors.toMap(t -> t.getBerthNo(), v -> v.getDaysList()));
        List<String> workableShipInfoBerthNos = shipInfoBerthNos.stream().filter(t -> {
            if (!berthNoDaysMap.containsKey(t)) return true;
            List<DayOfWeek> dayOfWeeks = berthNoDaysMap.get(t);
            if (CollectionUtils.isEmpty(dayOfWeeks)) return true;
            return !dayOfWeeks.contains(dayOfWeek);
        }).collect(Collectors.toList());

        String cargoOwner = shipDO.getCargoOwner() != null ? shipDO.getCargoOwner().getOwnerShortName() : null;
        List<RulerBerthCargoOwner> rulerBerthCargoOwnerList = this.planningContext.getRulerCollection().getRulerBerthCargoOwnerList();
        // 匹配货主关联的泊位
        List<RulerBerthCargoOwner> berthCargoOwners = rulerBerthCargoOwnerList.stream().filter(t -> StringUtils.equals(t.getOwnerShortName(), cargoOwner)).collect(Collectors.toList());
        String cargoTypeName = shipDO.getCargoType().getTypeName();
        List<PlanningBerthDO> berthDOS = null;
        //todo: 货主优先 和 货种匹配 应该是 或 的关系 不应该是 排斥关系

        // 如果货主不为空以货主为准
        if (!CollectionUtils.isEmpty(berthCargoOwners)) {
            berthCargoOwners = berthCargoOwners.stream().sorted(Comparator.comparing(BaseBerthRuler::getPrivilege)).collect(Collectors.toList());
            berthDOS = berthCargoOwners.stream().map(t -> {
                PlanningBerthDO planningBerthDO = this.berthNoMap.get(t.getBerthNo());
                if (planningBerthDO == null) {
                    return null;
                }
                planningBerthDO.setBollardSortAsc(t.getBollardSort());
                return planningBerthDO;
            }).filter(Objects::nonNull).collect(Collectors.toList());

        } else {
            // 如果以货主筛选不出来则以货物类型筛选
            List<RulerBerthCargoType> rulerBerthCargoTypeList = this.planningContext.getRulerCollection().getRulerBerthCargoTypeList();
            // 使用货物类型筛选泊位 并按照优先级排序
            List<RulerBerthCargoType> berthCargoTypes = rulerBerthCargoTypeList.stream().filter(t -> t.getTypeName().equals(cargoTypeName) && (t.getLoadUnload() & shipDO.getShipForecast().getLoadUnload()) > 0).sorted((t1, t2) -> t2.getPrivilege() - t1.getPrivilege()).collect(Collectors.toList());


            berthDOS = berthCargoTypes.stream().map(t -> {
                PlanningBerthDO planningBerthDO = this.berthNoMap.get(t.getBerthNo());
                if (planningBerthDO == null) {
                    return null;
                }
                planningBerthDO.setBollardSortAsc(t.getBollardSort());
                return planningBerthDO;
            }).filter(Objects::nonNull).collect(Collectors.toList());


        }

        if (CollectionUtils.isEmpty(berthDOS)) throw new RuntimeException("找不到候选泊位");


        List<PlanningBerthDO> planningBerthDOList = berthDOS.stream().filter(t -> workableShipInfoBerthNos.contains(t.getBerthInfo().getBerthNo())).collect(Collectors.toList());

        // 特殊规划 木片卸船 超过吃水才去西18 否只能去西3
        if ("木材".equals(cargoTypeName)) {
            return planningBerthDOList.subList(0, 1);
        }
        return planningBerthDOList;
    }


    public List<PlanningBerthDO> getBerthByBerthNo(List<String> berthNoList) {
        List<PlanningBerthDO> result = this.berthDOList.stream().filter(t -> berthNoList.stream().anyMatch(berthNo -> StringUtils.equals(berthNo, t.getBerthInfo().getBerthNo()))).collect(Collectors.toList());
        return result;
    }

    public PlanningBerthDO getBerthByBerthNo(@NotNull String berthNo) {
        Optional<PlanningBerthDO> berthDTO = this.berthDOList.stream().filter(t -> berthNo.equals(t.getBerthInfo().getBerthNo())).findFirst();
        return berthDTO.orElse(null);
    }

    /**
     * 根据泊位获取榄桩列表
     *
     * @param planningBerthDO
     * @return
     */
    public List<PlanningBollardDO> acquireAvailableBollard(PlanningBerthDO planningBerthDO) {
        List<PlanningBollardDO> bollardDTOList = planningBerthDO.getBollardDOList();
        List<PlanningBollardDO> result = new ArrayList<>();
        result.addAll(bollardDTOList);
        List<List<String>> berthGroupList = this.planningContext.getRulerCollection().getBerthGroupList();

        // 去同一泊位组饥借5个缆柱 前后延申
        String berthNo = planningBerthDO.getBerthInfo().getBerthNo();
        Optional<List<String>> berthGroupOptional = berthGroupList.stream().filter(t -> t.contains(berthNo)).findFirst();
        if (berthGroupOptional.isPresent()) {
            List<String> berthNos = berthGroupOptional.get();
            int i = berthNos.indexOf(berthNo);
            int frontBerthIdx = i - 1;
            int nextBerthIdx = i + 1;
            if (frontBerthIdx >= 0) {
                PlanningBerthDO berthByBerthNo = getBerthByBerthNo(berthNos.get(frontBerthIdx));
                if (berthByBerthNo != null) {
                    List<PlanningBollardDO> frontBerthBollardList = berthByBerthNo.getBollardDOList();
                    frontBerthBollardList = frontBerthBollardList.stream().sorted(Comparator.comparing(t -> t.getBollardInfo().getBollardNo())).collect(Collectors.toList());
                    List<PlanningBollardDO> addBollards = frontBerthBollardList.subList(frontBerthBollardList.size() / 3 * 2, frontBerthBollardList.size());
                    result.addAll(addBollards);
                }
            }
            if (nextBerthIdx < berthNos.size()) {
                PlanningBerthDO berthByBerthNo = getBerthByBerthNo(berthNos.get(nextBerthIdx));
                if (berthByBerthNo != null) {
                    List<PlanningBollardDO> nextBerthBollardList = berthByBerthNo.getBollardDOList();
                    nextBerthBollardList = nextBerthBollardList.stream().sorted(Comparator.comparing(t -> t.getBollardInfo().getBollardNo())).collect(Collectors.toList());
                    List<PlanningBollardDO> addBollards = nextBerthBollardList.subList(0, nextBerthBollardList.size() / 3);
                    result.addAll(addBollards);

                }
            }
        }

        Comparator<PlanningBollardDO> comparing = PlanningBollardDO.comparator;
        // 如果是反向
        if (!planningBerthDO.isBollardSortAsc()) {
            comparing = comparing.reversed();
        }
        return result.stream().sorted(comparing).collect(Collectors.toList());

    }

    /**
     * 根据首桩尾桩找到缆桩集合
     *
     * @param headBollardId
     * @param tailBollardId
     * @return
     */
    public List<PlanningBollardDO> findBollardList(String headBollardId, String tailBollardId) {

        PlanningBollardDO headBollard = planningBollardDOList.stream().filter(t -> t.getBollardInfo().getId().equals(headBollardId)).findFirst().get();
        PlanningBollardDO tailBollard = planningBollardDOList.stream().filter(t -> t.getBollardInfo().getId().equals(tailBollardId)).findFirst().get();

        List<List<String>> berthGroupList = this.planningContext.getRulerCollection().getBerthGroupList();
        List<String> berthNoList = berthGroupList.stream().filter(t -> t.contains(headBollard.getBollardInfo().getBerthNo())).findFirst().get();

        List<PlanningBerthDO> berthDOList = this.getBerthByBerthNo(berthNoList);
        // 缆桩排序 先排泊位编号 在排缆桩号
        List<PlanningBollardDO> candidateBollardList = berthDOList.stream().map(t -> t.getBollardDOList()).flatMap(t -> t.stream()).sorted(PlanningBollardDO.comparator).collect(Collectors.toList());

//        int start = headBollard.getBollardInfo().getBollardNo(), end = tailBollard.getBollardInfo().getBollardNo();
//        if (start > end) {
//            int temp = start;
//            start = end;
//            end = temp;
//        }
        List<PlanningBollardDO> result = new ArrayList<>();
        for (int i = 0; i < candidateBollardList.size(); i++) {
            PlanningBollardDO planningBollardDO = candidateBollardList.get(i);
            if (result.isEmpty()) {
                if (headBollardId.equals(planningBollardDO.getBollardInfo().getId()) || tailBollardId.equals(planningBollardDO.getBollardInfo().getId())) {
                    result.add(planningBollardDO);
                }
            } else {
                result.add(planningBollardDO);
                if (headBollardId.equals(planningBollardDO.getBollardInfo().getId()) || tailBollardId.equals(planningBollardDO.getBollardInfo().getId())) {
                    break;
                }
            }
        }

        return result;
    }
}
