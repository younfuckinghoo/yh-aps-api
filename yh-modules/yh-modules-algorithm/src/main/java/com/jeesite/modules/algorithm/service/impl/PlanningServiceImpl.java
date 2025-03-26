package com.jeesite.modules.algorithm.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeesite.common.base.R;
import com.jeesite.common.base.REnum;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.modules.algorithm.base.CommonConstant;
import com.jeesite.modules.algorithm.base.MutableLocalDateTime;
import com.jeesite.modules.algorithm.domain.*;
import com.jeesite.modules.algorithm.entity.*;
import com.jeesite.modules.algorithm.enums.MachineClassEnum;
import com.jeesite.modules.algorithm.feign.client.DmsClientService;
import com.jeesite.modules.algorithm.planning.PlanningContext;
import com.jeesite.modules.algorithm.planning.TideRulerHandler;
import com.jeesite.modules.algorithm.runner.DMSLoginApplicationRunner;
import com.jeesite.modules.algorithm.service.*;
import com.jeesite.modules.algorithm.workplan.WorkPlanContext;
import jakarta.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PlanningServiceImpl implements IPlanningService {
    private static final Logger log = LoggerFactory.getLogger(PlanningServiceImpl.class);
    @Resource
    private AlgShipInfoService algShipInfoService;
    @Resource
    private AlgShipForecastService algShipForecastService;
    @Resource
    private AlgShipRealTimeService algShipRealTimeService;
    @Resource
    private AlgBerthInfoService algBerthInfoService;
    @Resource
    private AlgBollardInfoService algBollardInfoService;
    @Resource
    private AlgBerthCargoEffiService algBerthCargoEffiService;
    @Resource
    private AlgCargoOwnerService algCargoOwnerService;
    @Resource
    private AlgShipPlanTempService algShipPlanTempService;
    @Resource
    private AlgShipPlanService algShipPlanService;
    @Resource
    private AlgCargoTypeService algCargoTypeService;
    @Resource
    private AlgTideService algTideService;
    @Resource
    private DmsClientService dmsClientService;
    @Resource
    private DMSLoginApplicationRunner dmsLoginApplicationRunner;
    @Resource
    private ObjectProvider<IPlanningService> thisService;
    @Resource
    private  IAlgShipMachineAllocService algShipMachineAllocService;
    @Resource
    private  AlgShoreMachineService algShoreMachineService;
    @Resource
    private  IAlgBerthMachineRelService algBerthMachineRelService;



    private final ThreadLocal<Integer> DMSTryTokenTimes = ThreadLocal.withInitial(() -> 0);

    @Value("${dms.auth.max-try-times:5}")
    private Integer DMSMaxTryTimes;

    @Override
    public R scheduleShipPlan() {

        PlanningContext planningContext = null;

        try {
            R<JSONObject> r  = dmsClientService.getRulerConfig();
            if (r.getCode() == REnum.SUCCESS.getValue()) {
                planningContext = new PlanningContext(r.getData());
            } else {
               throw new AuthenticationException("调用DMS接口失败");
            }
        } catch (Exception e) {
            log.error("DMS获取规则配置失败",e);
            if (this.DMSTryTokenTimes.get() < this.DMSMaxTryTimes) {
                dmsLoginApplicationRunner.refreshDmsRequestToken();
                this.DMSTryTokenTimes.set(this.DMSTryTokenTimes.get() + 1);
                return thisService.getIfAvailable().scheduleShipPlan();

            }else{
                throw e;
            }

        }


        // 货主简称
        setCargoOwnerData(planningContext);

        // 货种
        setCargoTypeData(planningContext);

        // 组装船舶数据
        List<PlanningShipDO> shipDOList = this.assembleShipData(planningContext);
        if (shipDOList.isEmpty()) {
            return R.failed_biz("无预排船舶");
        }
        PlanningBerthPoolDO planningBerthPoolDO = this.assembleBerthPool(planningContext);

        //初始化所已锁定的船舶
        initLockedShipPlan(planningContext);

        int maxDays = 10;
        int halfHourAddition = 0;

        LocalDateTime planStartTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0));

        // 潮汐
        setTideData(planningContext, planStartTime);


        do {
            LocalDateTime planningTime = planStartTime.plusMinutes(halfHourAddition * CommonConstant.MinutePerHalfHour);
            List<PlanningShipDO> notPlannedShipList = planningContext.getShipNotPlanned(planningTime);
            shipLoop:
            for (PlanningShipDO planningShipDO : notPlannedShipList) {


                // 匹配泊位
                List<PlanningBerthDO> availableBerthList = planningBerthPoolDO.mappingBerthByShip(planningShipDO,planningTime);
                // 每个泊位都尝试一下 如果可以就排完然后排下一条船
                berthLoop:
                for (PlanningBerthDO planningBerthDO : availableBerthList) {
                    planningShipDO.setOccupiedBerth(planningBerthDO);
                    planningShipDO.calculateRequireTide();
                    boolean validTideFlag = planningContext.validTide(planningShipDO, planningTime);
                    if (validTideFlag) {
                        // 返列表已排序
                        List<PlanningBollardDO> availableBollardDOList = planningBerthPoolDO.acquireAvailableBollard(planningBerthDO);
                        List<PlanningBollardDO> bollardDOListCandidate = planningShipDO.tryValidFreeBollardList(availableBollardDOList, planningTime, planningBerthDO);
                        if (!bollardDOListCandidate.isEmpty()) {
                            planningShipDO.startPlanning(planningTime, bollardDOListCandidate).planningStartTime().planningWorkTime().planningLeaveTime();

                            boolean locationRight = planningShipDO.checkShipLocate();
                            if (locationRight) {
                                planningShipDO.generatePlan();
                                break berthLoop;
                            } else {
                                planningShipDO.clearPlanData();
                            }
                        }

                    }else {
                        planningShipDO.clearPlanData();
                    }



                }
            }
            halfHourAddition++;
            // 只有当所有船都排好了 或者 超出最大计划时长时跳出循环
        } while (shipDOList.stream().anyMatch(t -> !t.isPlaned()) && halfHourAddition / 2 / 24 <= maxDays);

        List<AlgShipPlanTemp> planTempList = shipDOList.stream().filter(t -> t.isPlaned() && t.getAlgShipPlanTemp() != null && t.getAlgShipPlanTemp().getId() == null).map(t -> t.getAlgShipPlanTemp()).collect(Collectors.toList());
        algShipPlanTempService.saveBatch(planTempList);

        return R.ok();
    }



    private void setTideData(PlanningContext planningContext, LocalDateTime planStartTime) {
        LocalDate localDate = planStartTime.toLocalDate().plusDays(-1);
        final List<AlgTide> algTideList = algTideService.list(Wrappers.lambdaQuery(AlgTide.class).gt(AlgTide::getTideDate, localDate));
        final List<PlanningTideDO> planningTideDOS = algTideList.stream().map(t -> new PlanningTideDO(t)).collect(Collectors.toList());
        final TideRulerHandler tideRulerHandler = new TideRulerHandler(planningTideDOS);
        planningContext.setTideRulerHandler(tideRulerHandler);

    }

    /**
     * 初始化已锁定的船舶
     *
     * @param planningContext
     */
    private void initLockedShipPlan(PlanningContext planningContext) {
        List<PlanningShipDO> shipDOList = planningContext.getShipDOList();
        List<PlanningBollardDO> bollardDOList = planningContext.getBollardDOList();

        // 1. 查询出被锁定船
        List<AlgShipForecast> shipForecastList = algShipForecastService.list(Wrappers.lambdaQuery(AlgShipForecast.class).in(AlgShipForecast::getAlgorithmState,
                AlgorithmEnum.STATE3.getStatus(),
                AlgorithmEnum.STATE4.getStatus(),
                AlgorithmEnum.STATE5.getStatus(),
                AlgorithmEnum.STATE6.getStatus(),
                AlgorithmEnum.STATE7.getStatus()
        ));

        if (shipForecastList.isEmpty()) return;
        List<String> shipForecastIdList = shipForecastList.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<AlgShipPlanTemp> shipPlanTempList = algShipPlanTempService.list(Wrappers.lambdaQuery(AlgShipPlanTemp.class).in(AlgShipPlanTemp::getShipForcastId, shipForecastIdList));
        Map<String, AlgShipPlanTemp> shipPlanTempMap = shipPlanTempList.stream().collect(Collectors.toMap(t -> t.getShipForcastId(), t -> t));
        List<AlgShipPlan> shipPlanList = algShipPlanService.list(Wrappers.lambdaQuery(AlgShipPlan.class).in(AlgShipPlan::getShipForcastId, shipForecastIdList));
        Map<String, AlgShipPlan> shipPlanMap = shipPlanList.stream().collect(Collectors.toMap(t -> t.getShipForcastId(), t -> t));

        // 2. 被锁定的船组装为shipDO
        List<PlanningShipDO> planningShipDOS = shipForecastList.stream().map(t -> {
            PlanningShipDO planningShipDO = new PlanningShipDO(planningContext);
            planningShipDO.setShipForecast(t);
            AlgShipPlanTemp algShipPlanTemp = shipPlanTempMap.get(t.getId());
            if (algShipPlanTemp == null) {
                AlgShipPlan algShipPlan = shipPlanMap.get(t.getId());
                if (algShipPlan != null) {
                    // 回填计划数据
                    planningShipDO.setAlgShipPlanAndBackFillPlan(algShipPlan);
                }
            } else {
                // 回填计划数据
                planningShipDO.setAlgShipPlanTempAndBackFillPlan(algShipPlanTemp);
            }
            return planningShipDO;
        }).collect(Collectors.toList());

        shipDOList.addAll(planningShipDOS);

    }

    /**
     * 组装泊位池数据
     *
     * @param planningContext
     * @return
     */
    private PlanningBerthPoolDO assembleBerthPool(PlanningContext planningContext) {
        // 作业效率
        List<AlgBerthCargoEffi> cargoEffiList = algBerthCargoEffiService.list();
        Map<String, List<AlgBerthCargoEffi>> berthNoEfficiencyGroupList = cargoEffiList.stream().collect(Collectors.groupingBy(t -> t.getBerthNo(), Collectors.toList()));

        // 泊位榄桩数据
        List<AlgBerthInfo> berthInfoList = algBerthInfoService.list(Wrappers.lambdaQuery(AlgBerthInfo.class).eq(AlgBerthInfo::getAvailable, CommonConstant.TRUE_INT));
        List<AlgBollardInfo> bollardInfoList = algBollardInfoService.list(Wrappers.lambdaQuery(AlgBollardInfo.class).eq(AlgBollardInfo::getAvailable, CommonConstant.TRUE_INT));
        List<PlanningBollardDO> planningBollardDOList = bollardInfoList.stream().map(t -> new PlanningBollardDO(planningContext, t)).collect(Collectors.toList());
        Map<String, List<PlanningBollardDO>> berthNoBollardGroupMap = planningBollardDOList.stream().collect(Collectors.groupingBy(t -> t.getBollardInfo().getBerthNo(), Collectors.toList()));
        planningContext.setBollardDOList(planningBollardDOList);

        // 组装泊位缆柱对象
        List<PlanningBerthDO> berthDOList = berthInfoList.stream().map(t -> {
            List<PlanningBollardDO> bollardDOS = berthNoBollardGroupMap.get(t.getBerthNo());
            List<AlgBerthCargoEffi> cargoEffis = berthNoEfficiencyGroupList.get(t.getBerthNo());
            PlanningBerthDO planningBerthDO = new PlanningBerthDO(t, bollardDOS);
            planningBerthDO.setPlanningContext(planningContext);
            planningBerthDO.setBerthCargoEffiList(cargoEffis);
            return planningBerthDO;
        }).collect(Collectors.toList());
        planningContext.setBerthDOList(berthDOList);
        PlanningBerthPoolDO planningBerthPoolDO = new PlanningBerthPoolDO(planningContext, berthDOList, planningBollardDOList);
        planningContext.setPlanningBerthPoolDO(planningBerthPoolDO);
        return planningBerthPoolDO;

    }

    private void setCargoTypeData(PlanningContext planningContext) {
        List<AlgCargoType> cargoList = algCargoTypeService.list();
        Map<String, AlgCargoType> cargoTypeMap = cargoList.stream().collect(Collectors.toMap(t -> t.getCargoCode(), t -> t, (k1, k2) -> k1));
        planningContext.setCargoTypeMap(cargoTypeMap);
    }

    /**
     * 货主简称数据
     *
     * @param planningContext
     */
    private void setCargoOwnerData(PlanningContext planningContext) {
        // 查询计划所需要的数据
        // 货主简称
        Map<String, AlgCargoOwner> cargoOwnerMap = algCargoOwnerService.list().stream().collect(Collectors.toMap(t -> t.getCargoOwner(), t -> t));
        planningContext.setCargoOwnerMap(cargoOwnerMap);
    }


    /**
     * 组装船舶数据
     *
     * @param planningContext
     * @return
     */
    private List<PlanningShipDO> assembleShipData(PlanningContext planningContext) {
        // 1.船舶数据
        List<AlgShipForecast> shipForecastList = algShipForecastService.list(Wrappers.lambdaQuery(AlgShipForecast.class).in(AlgShipForecast::getAlgorithmState, AlgorithmEnum.STATE1.getStatus(), AlgorithmEnum.STATE2.getStatus()));
        if (shipForecastList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        // 从船期集合中获取对应船舶code集合
        List<String> voyageNoList = shipForecastList.stream().map(AlgShipForecast::getVoyageNo).collect(Collectors.toList());

        // 查询船期关联的所有船舶
        List<AlgShipInfo> shipInfoList = algShipInfoService.list(Wrappers.lambdaQuery(AlgShipInfo.class).in(AlgShipInfo::getVoyageNo, voyageNoList));

        // 船舶List转Map (key:shiCode ; value:船舶对象)
        Map<String, AlgShipInfo> shipInfoMap = shipInfoList.stream().collect(Collectors.toMap(AlgShipInfo::getVoyageNo, t -> t));

        // 通过船舶CODE查询所有在港船舶
        List<AlgShipRealTime> shipRealTimeList = algShipRealTimeService.list(Wrappers.lambdaQuery(AlgShipRealTime.class).in(AlgShipRealTime::getVoyageNo, voyageNoList));

        // 在港船舶List转Map (key:id ; value:在港船舶对象)
        Map<String, AlgShipRealTime> shipRealTimeMap = shipRealTimeList.stream().collect(Collectors.toMap(AlgShipRealTime::getVoyageNo, t -> t));

        // 组装计划对象
        List<PlanningShipDO> shipDOList = shipForecastList.stream().map(t -> {
            PlanningShipDO planningShipDO = new PlanningShipDO(planningContext);
            planningShipDO.setShipForecast(t);
            AlgShipInfo algShipInfo = shipInfoMap.get(t.getVoyageNo());
            if (algShipInfo != null) planningShipDO.setShipInfo(algShipInfo);
            AlgShipRealTime algShipRealTime = shipRealTimeMap.get(t.getVoyageNo());
            if (algShipRealTime != null) planningShipDO.setAlgShipRealTime(algShipRealTime);
            return planningShipDO;
        }).sorted(Comparator.comparing(t -> t.getShipForecast().getExpectArriveTime())).collect(Collectors.toList());
        planningContext.setShipDOList(shipDOList);

        return shipDOList;
    }

    @Override
    public R generateWorkPlan() {
        LocalDateTime now = LocalDateTime.now();

        WorkPlanContext workPlanContext = new WorkPlanContext();
        // 货种
        this.setCargoTypeData(workPlanContext);
        // 货主
        this.setCargoOwnerData(workPlanContext);
        // 船舶数据
        List<WorkPlanShipDO> shipDoList = assembleShipWorkPlanData(workPlanContext);

        WorkPlanMachinePool machinePool = this.createMachinePool(workPlanContext,now);




        return null;
    }

    private WorkPlanMachinePool createMachinePool(WorkPlanContext workPlanContext, LocalDateTime now) {

        List<AlgShipMachineAlloc> algShipMachineAllocs = this.algShipMachineAllocService.listAfterWorkingFinishTime(now.minusDays(2));
        List<String> voyageNoList = algShipMachineAllocs.stream().map(t -> t.getVoyageNo()).distinct().toList();
        List<AlgShipPlan> shipPlanList = algShipPlanService.list(Wrappers.lambdaQuery(AlgShipPlan.class).in(AlgShipPlan::getShipForcastId, voyageNoList));
        Map<String, AlgShipPlan> algShipPlanMap = shipPlanList.stream().collect(Collectors.toMap(t -> t.getShipForcastId(), t -> t));
        // 机械占用
        List<WorkPlanShipMachineAllocDO> machineAllocDOS = algShipMachineAllocs.stream()
                // 只统计岸机
                .filter(t-> MachineClassEnum.SHORE.getCode() == t.getMachineClassCode())
                .map(t -> {
            WorkPlanShipMachineAllocDO workPlanShipMachineAllocDO = new WorkPlanShipMachineAllocDO();
            workPlanShipMachineAllocDO.setAlgShipMachineAlloc(t);
            AlgShipPlan algShipPlan = algShipPlanMap.get(t.getVoyageNo());
            if (algShipPlan != null) {
                workPlanShipMachineAllocDO.setStartTime(new MutableLocalDateTime(algShipPlan.getPlanStartTime()));
                workPlanShipMachineAllocDO.setEndTime(new MutableLocalDateTime(algShipPlan.getPlanFinishTime()));

            }
            return workPlanShipMachineAllocDO;
        }).toList();
        Map<String, List<WorkPlanShipMachineAllocDO>> machineAllocListMap = machineAllocDOS.stream().collect(Collectors.groupingBy(t -> t.getAlgShipMachineAlloc().getMachineId(), Collectors.toList()));

        // 岸机列表
        List<AlgShoreMachine> algShoreMachines = this.algShoreMachineService.list(Wrappers.lambdaQuery(AlgShoreMachine.class).eq(AlgShoreMachine::getStatus, CommonConstant.TRUE_INT));
        // 组装机械和机械占用
        List<WorkPlanShoreMachineDO> machineDOS = algShoreMachines.stream().map(t -> {
            return new WorkPlanShoreMachineDO(t, machineAllocListMap.get(t.getId()));
        }).toList();

        // 泊位机械关系
        List<AlgBerthMachineRel> berthMachineRelList = this.algBerthMachineRelService.list();

        WorkPlanMachinePool machinePool = new WorkPlanMachinePool(workPlanContext, machineDOS, berthMachineRelList);

        return machinePool;
    }

    private List<WorkPlanShipDO> assembleShipWorkPlanData(WorkPlanContext workPlanContext) {
        List<AlgShipRealTime> algShipRealTimeList = algShipRealTimeService.list(Wrappers.lambdaQuery(AlgShipRealTime.class).in(AlgShipRealTime::getAlgorithmState, AlgorithmEnum.STATE11.getStatus(), AlgorithmEnum.STATE12.getStatus()));
        if (algShipRealTimeList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        // 从船期集合中获取对应船舶code集合
        List<String> voyageNoList = algShipRealTimeList.stream().map(AlgShipRealTime::getVoyageNo).collect(Collectors.toList());

        List<AlgShipForecast> shipForecastList = algShipForecastService.list(Wrappers.lambdaQuery(AlgShipForecast.class).in(AlgShipForecast::getVoyageNo, voyageNoList));
        Map<String, AlgShipForecast> shipForecastMap = shipForecastList.stream().collect(Collectors.toMap(AlgShipForecast::getVoyageNo, t -> t));
        // 查询船期关联的所有船舶
        List<AlgShipInfo> shipInfoList = algShipInfoService.list(Wrappers.lambdaQuery(AlgShipInfo.class).in(AlgShipInfo::getVoyageNo, voyageNoList));

        // 船舶List转Map (key:shiCode ; value:船舶对象)
        Map<String, AlgShipInfo> shipInfoMap = shipInfoList.stream().collect(Collectors.toMap(AlgShipInfo::getVoyageNo, t -> t));

        List<AlgShipPlan> shipPlanList = algShipPlanService.list(Wrappers.lambdaQuery(AlgShipPlan.class).in(AlgShipPlan::getShipForcastId, voyageNoList));

        Map<String, AlgShipPlan> algShipPlanMap = shipPlanList.stream().collect(Collectors.toMap(AlgShipPlan::getShipForcastId, t -> t));

        return algShipRealTimeList.stream().map(t -> {
            WorkPlanShipDO workPlanShipDO = new WorkPlanShipDO(workPlanContext);
            workPlanShipDO.setAlgShipRealTime(t);
            AlgShipForecast algShipForecast = shipForecastMap.get(t.getVoyageNo());
            if (algShipForecast != null) workPlanShipDO.setShipForecast(algShipForecast);
            AlgShipInfo algShipInfo = shipInfoMap.get(t.getVoyageNo());
            if (algShipInfo != null) workPlanShipDO.setShipInfo(algShipInfo);
            AlgShipPlan algShipPlan = algShipPlanMap.get(t.getVoyageNo());
            if (algShipPlan != null) workPlanShipDO.setAlgShipPlan(algShipPlan);

            return workPlanShipDO;
        }).collect(Collectors.toList());


    }

    /**
     * 货主简称数据
     *
     * @param workPlanContext
     */
    private void setCargoOwnerData(WorkPlanContext workPlanContext) {
        // 查询计划所需要的数据
        // 货主简称
        Map<String, AlgCargoOwner> cargoOwnerMap = algCargoOwnerService.list().stream().collect(Collectors.toMap(t -> t.getCargoOwner(), t -> t));
        workPlanContext.setCargoOwnerMap(cargoOwnerMap);
    }


    private void setCargoTypeData(WorkPlanContext workPlanContext) {
        List<AlgCargoType> cargoList = algCargoTypeService.list();
        Map<String, AlgCargoType> cargoTypeMap = cargoList.stream().collect(Collectors.toMap(t -> t.getCargoCode(), t -> t, (k1, k2) -> k1));
        workPlanContext.setCargoTypeMap(cargoTypeMap);
    }
}
