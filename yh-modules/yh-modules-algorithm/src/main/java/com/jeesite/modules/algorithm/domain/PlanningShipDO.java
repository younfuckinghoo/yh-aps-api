package com.jeesite.modules.algorithm.domain;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.modules.algorithm.base.CommonConstant;
import com.jeesite.modules.algorithm.base.MutableLocalDateTime;
import com.jeesite.modules.algorithm.entity.*;

import com.jeesite.modules.algorithm.enums.LoadUnloadEnum;
import com.jeesite.modules.algorithm.enums.TradeTypeEnum;
import com.jeesite.modules.algorithm.planning.PlanningContext;
import com.jeesite.modules.algorithm.utils.MathUtil;
import com.jeesite.modules.sys.utils.UserUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class PlanningShipDO implements Cloneable{

    /**
     * 船舶要求的潮汐高度
     */
    private BigDecimal requiredTideHeight;

    public PlanningShipDO(PlanningContext planningContext) {
        this.planningContext = planningContext;
    }

    // 船舶预报信息
    private AlgShipInfo shipInfo;
    // 船舶预报信息
    private AlgShipForecast shipForecast;
    // 实时船舶动态信息
    private AlgShipRealTime algShipRealTime;
    // 船舶长期计划
    private AlgShipPlan algShipPlan;
    // 船舶长期计划临时计划
    private AlgShipPlanTemp algShipPlanTemp;

    private PlanningContext planningContext;

    private LoadUnloadEnum loadUnloadEnum;
    private AlgCargoType cargoType;

    private BigDecimal carryWeight;
    /**
     * 货主简称对应数据
     */
    private AlgCargoOwner cargoOwner;
    /**
     * 最快靠泊时间
     */
    private LocalDateTime fastestBerthTime;
    /**
     * 整船作业时间
     */
    private BigDecimal shipWorkingDurationHour;

    /**
     * 是否已排
     */
    private boolean isPlaned = false;
    /**
     * 计划靠泊时间
     */
    private MutableLocalDateTime planBerthTime;

    /**
     * 计划开工时间
     */
    private MutableLocalDateTime planStartTime;

    /**
     * 计划完工时间
     */
    private MutableLocalDateTime planFinishTime;

    /**
     * 计划离泊时间
     */
    private MutableLocalDateTime planLeaveTime;


    /**
     * 泊位信息 停靠的泊位
     */
    private PlanningBerthDO occupiedBerth;
    /**
     * 占用的缆柱集合
     */
    private List<PlanningBollardDO> occupiedBollardList;

    /**
     * 靠泊抢潮作业优先级
     */
    private int requireTideLevelBerth;
    /**
     * 离泊抢潮作业优先级
     */
    private int requireTideLevelLeave;

    /**
     * 尝试找泊位的次数
     */
    private int tryBerthCount;

    /**
     * 岸边机械池
     */
    private WorkPlanMachinePool workPlanMachinePool;

    /**
     * 潮水备注
     */
    private String tideRemark;

    private ObjectMapper objectMapper = new ObjectMapper();


    List<WorkPlanShoreMachineDO> machineDOList;

    {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.objectMapper.registerModule(javaTimeModule);
    }


    public void setWorkPlanMachinePool(WorkPlanMachinePool workPlanMachinePool) {
        this.workPlanMachinePool = workPlanMachinePool;
    }



    public boolean lengthMoreThan(String length) {
       return MathUtil.isMoreEqualThan(this.shipInfo.getShipLength(),length);
    }


    public void setShipForecast(AlgShipForecast shipForecast) {
        this.shipForecast = shipForecast;
        this.fastestBerthTime = shipForecast.getExpectArriveTime().plusMinutes(this.planningContext.getRulerCollection().getStartWorkReadyHour().multiply(BigDecimal.valueOf(CommonConstant.MinutePerHour)).longValue());
        this.loadUnloadEnum = LoadUnloadEnum.getInstanceByCode(shipForecast.getLoadUnload());
        this.cargoOwner  = this.planningContext.getCargoOwnerMap().get(shipForecast.getCargoOwner());
        this.cargoType = this.planningContext.getCargoTypeMap().get(shipForecast.getCargoSubTypeCode());
//        this.calculateRequireTide();
        this.settingCarryWeight();
    }

    public void calculateRequireTide() {
        Assert.notNull(this.occupiedBerth,"要计算趁潮要求首先需要确定泊位！");
        BigDecimal waterDepth = this.occupiedBerth.getBerthInfo().getBerthDepth();
        final BigDecimal shipRequireDepth = this.getArriveDraft().multiply(this.planningContext.getRulerCollection().getTideHeightMargin());
        BigDecimal subtract = shipRequireDepth.subtract(waterDepth);
        this.requiredTideHeight = MathUtil.isLessEqualThan(subtract,BigDecimal.ZERO)?BigDecimal.ZERO:subtract;
        this.setTideRemark(String.format("需要乘%1$s米以上潮水靠泊",requiredTideHeight.toString()));
    }

    private void settingCarryWeight() {

        this.carryWeight = this.shipForecast.getCarryWeight();
        if (this.carryWeight==null){
            switch (this.loadUnloadEnum) {
                case LOAD: this.carryWeight = this.shipForecast.getLoadPlanWeight();break;
                case UNLOAD: this.carryWeight = this.shipForecast.getUnloadPlanWeight();break;
            }
        }
        if (carryWeight==null)
            throw new RuntimeException(String.format("船号：%1$s--找不到作业量", shipForecast.getShipCode()));

    }


    public void setAlgShipRealTime(AlgShipRealTime algShipRealTime) {
        this.algShipRealTime = algShipRealTime;

        // 木片的实际重量取实时数据
        if (this.planningContext.getRulerCollection().getCargoTypesCarryWeightInRealTime().contains(this.cargoType.getTypeName())){
            this.carryWeight = this.algShipRealTime.getCarryWeight();
            if (this.carryWeight==null){
                switch (this.loadUnloadEnum) {
                    case LOAD: this.carryWeight = this.algShipRealTime.getLoadPlanWeight();break;
                    case UNLOAD: this.carryWeight = this.algShipRealTime.getUnloadPlanWeight();break;
                }
            }
        }
    }

    /**
     * 开始计划
     * 设置计划靠泊时间
     * @param planningTime
     * @param bollardDOListCandidate
     * @return
     */
    public PlanningShipDO startPlanning(LocalDateTime planningTime, List<PlanningBollardDO> bollardDOListCandidate) {

        this.planBerthTime = new MutableLocalDateTime(planningTime);
        this.occupiedBollardList = bollardDOListCandidate;
        return this;

    }



    /**
     * 计划靠泊时间
     * 如果靠泊时间与实际靠泊的泊位关联，那么这个方法就需要在确定泊位后再调用，
     * 因此在排泊时，排泊过滤候选船舶的条件就不能使用靠泊时间进行过滤，而要使用预计抵锚时间
     * @return
     */
    private PlanningShipDO planningAnchorTime() {
        LocalDateTime expectArriveTime = this.getShipForecast().getExpectArriveTime();
        this.planBerthTime =  new MutableLocalDateTime(expectArriveTime.plusHours(3));
        return this;
    }

    /**
     * 计算开始时间
     * @return
     */
    public PlanningShipDO planningStartTime(){
        Assert.notNull(this.planBerthTime,"计算开工时间需要先确定靠泊时间！");
        // 固定检疫时间
        List<LocalTime> quarantineTimePointArr = this.getPlanningContext().getRulerCollection().getQuarantineTimePointArr();
        int quarantineMinutes = this.getPlanningContext().getRulerCollection().getQuarantineMinutes();
//        List<LocalTime> startTimeList = quarantineTimePointArr.stream().map(t -> t.plusMinutes(quarantineMinutes)).collect(Collectors.toList());
        LocalDateTime berthTime = this.planBerthTime.getData();
        for (int i = 0; i< 48 ; i++) {
            LocalDateTime localDateTime = berthTime.plusMinutes(i*CommonConstant.MinutePerHalfHour);
            LocalTime localTime = localDateTime.toLocalTime();
            if (quarantineTimePointArr.contains(localTime)){
                this.planStartTime = new MutableLocalDateTime(localDateTime.plusMinutes(quarantineMinutes));
                return this;
            }
        }

        return this;
    }


    public PlanningShipDO planningWorkTime() {
        Assert.notNull(this.planStartTime,"计算完工时间需要先确定开工时间！");
        Assert.notNull(this.occupiedBerth,"计算完工时间需要先确定泊位！");
        List<AlgBerthCargoEffi> berthCargoEffiList = this.occupiedBerth.getBerthCargoEffiList();
        Optional<AlgBerthCargoEffi> cargoOwnerOptional = berthCargoEffiList.stream().filter(t -> {
            return this.cargoType.getTypeName().equals(t.getCargoTypeName())
                    && this.getShipForecast().getLoadUnload() == t.getLoadUnload()
                    && this.getCargoOwner() != null
                    && this.getCargoOwner().getOwnerShortName() != null
                    && this.getCargoOwner().getOwnerShortName().equals(t.getCargoOwner());
        }).findFirst();
        BigDecimal efficiency = BigDecimal.ZERO;
        if (cargoOwnerOptional.isPresent()) {
            efficiency = cargoOwnerOptional.get().getEfficiency();
        }else{
            cargoOwnerOptional = berthCargoEffiList.stream().filter(t -> {
                return this.cargoType.getTypeName().equals(t.getCargoTypeName())
                        && this.getShipForecast().getLoadUnload() == t.getLoadUnload();
            }).findFirst();
            if (cargoOwnerOptional.isPresent()) {
                efficiency = cargoOwnerOptional.get().getEfficiency();
            }else {
                throw new RuntimeException(String.format("船号：%1$s,装卸类型：%2$s,货物类型：%3$s,泊位：%4$s,货主：%5$s--找不到作业效率",
                        shipForecast.getShipCode(),
                        this.loadUnloadEnum.getName(),
                        shipForecast.getCargoTypeName(),
                        occupiedBerth.getBerthInfo().getBerthNo(),
                        shipForecast.getCargoOwner()
                        )
                );
            }
        }

        // todo: 上面是单机效率 需要查找岸机数量
        efficiency = efficiency.multiply(BigDecimal.valueOf(3));

        shipWorkingDurationHour = this.carryWeight.divide(efficiency, 2, RoundingMode.CEILING);

        this.planFinishTime = this.planStartTime.plusMinutes(shipWorkingDurationHour.multiply(BigDecimal.valueOf(CommonConstant.MinutePerHour)).longValue());

        return this;
    }

    /**
     * 计算离港时间
     * @return
     */
    public PlanningShipDO planningLeaveTime(){
        // 船舶作业完之后水尺手续，边检手续，引水手续等需要平均3个小时
        Assert.notNull(this.planFinishTime,"计算离港时间需要先确定完工时间！");
        this.planLeaveTime = this.planFinishTime.plusMinutes(this.planningContext.getRulerCollection().getLeaveReadyHour().multiply(BigDecimal.valueOf(CommonConstant.MinutePerHour)).longValue());
        return this;
    }



    /**
     * 内贸
     *
     * @return
     */
    public boolean isInTrade() {
        return TradeTypeEnum.IN.getCode() == this.shipForecast.getTradeType();
    }

    /**
     * 内贸
     *
     * @return
     */
    public boolean isOutTrade() {
        return TradeTypeEnum.OUT.getCode() == this.shipForecast.getTradeType();
    }

    /**
     * 装船
     *
     * @return
     */
    public boolean isLoad() {
        return LoadUnloadEnum.LOAD.getCode() == this.shipForecast.getLoadUnload();
    }

    /**
     * 卸船
     *
     * @return
     */
    public boolean isUnLoad() {
        return LoadUnloadEnum.UNLOAD.getCode() == this.shipForecast.getLoadUnload();
    }

    /**
     * 排不上就清除排泊数据
     */
    public void clearPlanData() {
        this.algShipPlanTemp = null;
        this.planBerthTime = null;
        this.planStartTime = null;
        this.planFinishTime = null;
        this.planLeaveTime = null;
        this.occupiedBerth = null;
        this.isPlaned = false;
        if (this.occupiedBollardList!=null) {
            this.occupiedBollardList.stream().forEach(t->t.removeSegment(this));
        }
        this.occupiedBollardList = null;
        this.requiredTideHeight = null;
        this.tideRemark = null;

    }



    public BigDecimal getArriveDraft(){
       Assert.notNull( this.shipForecast,"船舶信息不能为空");
        return this.shipForecast.getDraftIn();

    }

    public BigDecimal getLeaveDraft(){
       Assert.notNull( this.shipForecast,"船舶信息不能为空");
       return this.shipForecast.getDraftOut();

    }




    /**
     * 看看这些缆柱能不能满足要求
     * @param availableBollardDOList
     * @param planningTime
     * @param planningBerthDO
     * @return
     */
    public List<PlanningBollardDO> tryValidFreeBollardList(List<PlanningBollardDO> availableBollardDOList, LocalDateTime planningTime, PlanningBerthDO planningBerthDO) {
        MutableLocalDateTime checkTime = new MutableLocalDateTime(planningTime);
        BigDecimal shipLength = this.getShipInfo().getShipLength();
        // 船舶长度余量
        if (hasShipInSameTimeSameBerth(planningBerthDO,planningTime)){
            shipLength = shipLength.multiply(this.getPlanningContext().getRulerCollection().getShipLengthMargin());
        }
        BigDecimal shipRequireLength = shipLength.setScale(0, RoundingMode.FLOOR);
        // 已推理缆桩数
        int occupiedAmount;
        List<PlanningBollardDO> candidateBollardList = new ArrayList<>();
        // 当前所有占用的缆柱长度
        BigDecimal totalLength;
        // 是否已安排
        boolean planned = false;
        seekStart: for (int i = 0; i < availableBollardDOList.size();) {
            planned = false;
            PlanningBollardDO planningBollardDO = availableBollardDOList.get(i);
            // 找到第一个空闲的缆柱
            boolean free = planningBollardDO.isFree(checkTime);
            if (free){
                // 每次开始匹配是否满足长度时都要初始化还原占用数据
                totalLength = BigDecimal.ZERO;
                candidateBollardList.clear();
                occupiedAmount = 0;

                // 如果是倒排，52，51，50 如果上一次占用到了50 ,下一次还要从50开始
                int start = i;
                if (!planningBerthDO.isBollardSortAsc()) {
                    if (i > 0) {
                        start--;
                    }
                }
                // 累加缆柱
                accumulationBollard :
                for (int j = start; j < availableBollardDOList.size(); j++) {
                    PlanningBollardDO freeBollard = availableBollardDOList.get(j);
                    // 不管是被占用还是未被占用，都先加上
                    candidateBollardList.add(freeBollard);
                    // 累加缆柱与前一个缆柱的长度
                    totalLength = totalLength.add(freeBollard.getBollardInfo().getLastBollardDistance());
                    if (MathUtil.isMoreEqualThan(totalLength,shipRequireLength)) {
                        // 如果不是从第一个榄桩开始排的，那么可以将船的艏缆系到前一个被占用的缆上
                        if (planningBerthDO.isBollardSortAsc()) {
                            if (i > 0) {
                                PlanningBollardDO firstBollard = availableBollardDOList.get(i - 1);
                                candidateBollardList.add(0, firstBollard);
                            }
                        } else {
                            if (j < availableBollardDOList.size() - 1) {
                                PlanningBollardDO lastBollard = availableBollardDOList.get(j + 1);
                                candidateBollardList.add(lastBollard);
                            }
                        }


                        planned = true;

                    }

                    if (planned) {
                        return candidateBollardList;
                    }

                    // 如果是空闲的
                    if (freeBollard.isFree(checkTime) || candidateBollardList.size() == 1) {
                        occupiedAmount++;
                        // 如果是空闲的则优先向后找本泊位的空闲缆柱
                        continue accumulationBollard;
                    }else {
                        // 如果没满足长度要求 并且这一个是被占用的 则跳过中间这些已经累加的缆柱
                        break accumulationBollard;
                    }

                }

                // 前面安排的缆柱长度加起来不够 直接跳过
                i += occupiedAmount;

            }else {
                i++;
            }
        }

        return Collections.EMPTY_LIST;

    }

    private boolean hasShipInSameTimeSameBerth(PlanningBerthDO planningBerthDO, LocalDateTime planningTime) {
        List<PlanningBollardDO> bollardDTOList = planningBerthDO.getBollardDOList();
        // 几个缆桩在同一时间被占用
        Long bollardCountAtSameTime = bollardDTOList.stream().filter(bollardDO -> {
            List<PlanningShipDO> occupiedTimeSegments = bollardDO.getOccupiedTimeSegments();
            // 任何一个时间段包含了这个时间点 那么就认为是有相同的船在这里
            return occupiedTimeSegments.stream().anyMatch(shipDo -> shipDo.getPlanBerthTime().isBeforeEqual(planningTime) && shipDo.getPlanLeaveTime().isAfterEqual(planningTime));
        }).collect(Collectors.counting());

        return bollardCountAtSameTime>this.planningContext.getRulerCollection().getBollardCountAsShip();


    }

    /**
     * 检查时间是否符合缆柱空闲 并且满足每个缆柱的安全距离 如果可以就放进去
     * @return
     */
    public boolean checkShipLocate() {
        final ArrayList<PlanningBollardDO> planningBollardDOS = new ArrayList<>(this.occupiedBollardList);
        // 掐头
        if (!planningBollardDOS.get(0).isFree(this.planBerthTime)) {
            planningBollardDOS.get(0).putSegmentOrderByPlanBerthTime(this);
            planningBollardDOS.remove(0);
        }
        // 去尾
        if (!planningBollardDOS.get(planningBollardDOS.size()-1).isFree(this.planBerthTime)){
            planningBollardDOS.get(planningBollardDOS.size()-1).putSegmentOrderByPlanBerthTime(this);
            planningBollardDOS.remove(planningBollardDOS.size()-1);
        }
        return planningBollardDOS.stream().allMatch(t->t.checkShipAndPut(this));
    }

    /**
     * 生成计划
     */
    public void generatePlan() {
        this.algShipPlanTemp = new AlgShipPlanTemp();
        this.algShipPlanTemp.setShipForcastId(this.shipForecast.getId());
        this.algShipPlanTemp.setBerthNo(this.occupiedBerth.getBerthInfo().getBerthNo());
        this.algShipPlanTemp.setBerthDirection(this.planningContext.getRulerCollection().getBerthDirectionMap().get(algShipPlanTemp.getBerthNo()));
        this.algShipPlanTemp.setPlanStatus(AlgorithmEnum.STATE2.getStatus());
        this.algShipPlanTemp.setHeadBollardId(this.occupiedBollardList.get(0).getBollardInfo().getId().toString());
        this.algShipPlanTemp.setTailBollardId(this.occupiedBollardList.get(this.occupiedBollardList.size()-1).getBollardInfo().getId().toString());
        this.algShipPlanTemp.setPlanBerthTime(this.planBerthTime.getData());
        this.algShipPlanTemp.setPlanStartTime(this.planStartTime.getData());
        this.algShipPlanTemp.setPlanFinishTime(this.planFinishTime.getData());
        this.algShipPlanTemp.setPlanLeaveTime(this.planLeaveTime.getData());
        this.algShipPlanTemp.setNeedLead(this.shipForecast.getTradeType()==TradeTypeEnum.OUT.getCode()?CommonConstant.TRUE_INT:CommonConstant.FALSE_INT);
        this.algShipPlanTemp.setNeedTractor(this.shipForecast.getTradeType()==TradeTypeEnum.OUT.getCode()?CommonConstant.TRUE_INT:CommonConstant.FALSE_INT);
        this.algShipPlanTemp.setCreator(UserUtils.getUser().getUserName());
        this.algShipPlanTemp.setCreateTime(LocalDateTime.now());
        this.algShipPlanTemp.setBerthRemark(this.generateBerthRemark());
        this.isPlaned = true;
    }

    private String generateBerthRemark() {
        StringBuffer sb = new StringBuffer();
        if (this.shipForecast.getTradeType()==TradeTypeEnum.OUT.getCode()){
            sb.append("外贸默认需要引水、拖轮；");
        }
        if (StringUtils.isNotBlank(this.tideRemark)) sb.append(this.tideRemark);
        return sb.toString();

    }

    /**
     * 设置并回填计划数据
     * @param algShipPlan
     */
    public void setAlgShipPlanAndBackFillPlan(@NotNull AlgShipPlan algShipPlan) {

        this.setAlgShipPlan(algShipPlan);
        // 1. 初始化锁定船计划的数据 靠泊时间 开工时间 完工时间 离泊时间 泊位 缆柱
        this.setPlanBerthTime(new MutableLocalDateTime(algShipPlan.getPlanBerthTime()));
        this.setPlanStartTime(new MutableLocalDateTime(algShipPlan.getPlanStartTime()));
        this.setPlanFinishTime(new MutableLocalDateTime(algShipPlan.getPlanFinishTime()));
        this.setPlanLeaveTime(new MutableLocalDateTime(algShipPlan.getPlanLeaveTime()));
        this.setOccupiedBerth(this.planningContext.getPlanningBerthPoolDO().getBerthByBerthNo(algShipPlan.getBerthNo()));
        String headBollardId = algShipPlan.getHeadBollardId();
        String tailBollardId = algShipPlan.getTailBollardId();
        List<PlanningBollardDO> bollardDOList =  this.planningContext.getPlanningBerthPoolDO().findBollardList(headBollardId,tailBollardId);
        this.setOccupiedBollardList(bollardDOList);
        // 2.设置被占用缆柱的segment
        boolean checkShipLocate = this.checkShipLocate();
        if (!checkShipLocate){
            throw new RuntimeException("船舶计划冲突,船号："+this.shipForecast.getShipCode());
        }
        this.setPlaned(true);
    }

    /**
     * 计划数据回填
     * @param algShipPlanTemp
     */
    public void setAlgShipPlanTempAndBackFillPlan(@NotNull AlgShipPlanTemp algShipPlanTemp) {
        this.setAlgShipPlanTemp(algShipPlanTemp);
        // 1. 初始化锁定船计划的数据 靠泊时间 开工时间 完工时间 离泊时间 泊位 缆柱
        this.setPlanBerthTime(new MutableLocalDateTime(algShipPlanTemp.getPlanBerthTime()));
        this.setPlanStartTime(new MutableLocalDateTime(algShipPlanTemp.getPlanStartTime()));
        this.setPlanFinishTime(new MutableLocalDateTime(algShipPlanTemp.getPlanFinishTime()));
        this.setPlanLeaveTime(new MutableLocalDateTime(algShipPlanTemp.getPlanLeaveTime()));
        this.setOccupiedBerth(this.planningContext.getPlanningBerthPoolDO().getBerthByBerthNo(algShipPlanTemp.getBerthNo()));
        String headBollardId = algShipPlanTemp.getHeadBollardId();
        String tailBollardId = algShipPlanTemp.getTailBollardId();
        List<PlanningBollardDO> bollardDOList =  this.planningContext.getPlanningBerthPoolDO().findBollardList(headBollardId,tailBollardId);
        this.setOccupiedBollardList(bollardDOList);
        // 2.设置被占用缆柱的segment
        boolean checkShipLocate = this.checkShipLocate();
        if (!checkShipLocate){
            throw new RuntimeException("船舶计划冲突,船号："+this.shipForecast.getShipCode());
        }
        this.setPlaned(true);
    }


    public void setOccupiedBerth(PlanningBerthDO occupiedBerth) {
        this.occupiedBerth = occupiedBerth;
    }
}
