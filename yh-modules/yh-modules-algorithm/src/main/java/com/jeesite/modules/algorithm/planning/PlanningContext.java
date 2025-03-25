package com.jeesite.modules.algorithm.planning;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeesite.modules.algorithm.base.CommonConstant;
import com.jeesite.modules.algorithm.domain.*;
import com.jeesite.modules.algorithm.entity.AlgCargoOwner;
import com.jeesite.modules.algorithm.entity.AlgCargoType;
import com.jeesite.modules.algorithm.entity.AlgTide;
import com.jeesite.modules.algorithm.planning.ruler.BerthRestInWeek;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthCargoOwner;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthCargoType;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthShipInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class PlanningContext {




    private JSONObject allRulerJson;


    private RulerCollection rulerCollection;


    private List<PlanningShipDO> shipDOList;
    private List<PlanningBerthDO> berthDOList;
    private List<PlanningBollardDO> bollardDOList;
    private PlanningBerthPoolDO planningBerthPoolDO ;
    private  Map<String, AlgCargoType> cargoTypeMap;
    private Map<String, AlgCargoOwner> cargoOwnerMap;
    private TideRulerHandler tideRulerHandler;


    public PlanningContext() {
        this.rulerCollection = new RulerCollection();
    }

    public PlanningContext(JSONObject allRulerJson) {
        this.rulerCollection = new RulerCollection();
        this.allRulerJson = allRulerJson;
        this.analyzeRulerJson();
    }

    /**
     * 解析规则json
     */
    private void analyzeRulerJson() {

        // 1、乘潮水高度计算公式：船舶吃水* 1.1 -航道水深（港池实际水深）= 最低的乘潮靠泊米数
        if (this.allRulerJson.containsKey("tideHeightMargin")) {
            this.rulerCollection.setTideHeightMargin(this.allRulerJson.getBigDecimal("tideHeightMargin"));
        }

        // 3、双船靠泊同一泊位，两条船之间靠泊间距要大于最长船长的10%
        if (this.allRulerJson.containsKey("shipLengthMargin")) {
            this.rulerCollection.setShipLengthMargin(this.allRulerJson.getBigDecimal("shipLengthMargin").divide(BigDecimal.valueOf(100)).setScale(2,RoundingMode.HALF_UP).add(BigDecimal.ONE));
        }

        // 2、开工前手续-海关来港检疫时间：9点、11点、16点  ------可以多选
        if (this.allRulerJson.containsKey("quarantineTimePointArr")) {
            String quarantineTimeStr = this.allRulerJson.getString("quarantineTimePointArr");
            if (StringUtils.isNotBlank(quarantineTimeStr)) {
                List<LocalTime> quarantineTimeList = Arrays.stream(quarantineTimeStr.split(","))
                        .map(LocalTime::parse)
                        .collect(Collectors.toList());
                this.rulerCollection.setQuarantineTimePointArr(quarantineTimeList);
            }
        }

        if (this.allRulerJson.containsKey("startWorkReadyHour")) {
            this.rulerCollection.setStartWorkReadyHour(this.allRulerJson.getBigDecimal("startWorkReadyHour"));
        }
        // 5、同泊位的船舶离泊后至少要 3 个小时下一艘船才能靠泊
        if (this.allRulerJson.containsKey("shipWorkingGap")) {
            this.rulerCollection.setShipWorkingGap(this.allRulerJson.getBigDecimal("shipWorkingGap"));
        }

        // 4、船舶作业完之后水尺手续，边检手续，引水手续等需要平均 3 个小时
        if (this.allRulerJson.containsKey("leaveReadyHour")) {
            this.rulerCollection.setLeaveReadyHour(this.allRulerJson.getBigDecimal("leaveReadyHour"));
        }

        // 6、从靠泊至开工中间的手续（边检、海关卫检、动植物检疫，量水尺），手续开始时间主要依据海关时间，整套手续时间大概 3 个小时
        if (this.allRulerJson.containsKey("quarantineMinutes")) {
            this.rulerCollection.setQuarantineMinutes(this.allRulerJson.getBigDecimal("quarantineMinutes").multiply(BigDecimal.valueOf(CommonConstant.MinutePerHour)).intValue());
        }

        if (this.allRulerJson.containsKey("bollardCountAsShip")) {
            this.rulerCollection.setBollardCountAsShip(this.allRulerJson.getInteger("bollardCountAsShip"));
        }

        if (this.allRulerJson.containsKey("berthGroupList")) {
            JSONArray berthGroupArray = this.allRulerJson.getJSONArray("berthGroupList");
            List<List<String>> berthGroupList = berthGroupArray.stream()
                    .map(group -> ((JSONArray) group).toJavaList(String.class))
                    .collect(Collectors.toList());
            this.rulerCollection.setBerthGroupList(berthGroupList);
        }

        if (this.allRulerJson.containsKey("cargoTypesCarryWeightInRealTime")) {
            JSONArray cargoTypesArray = this.allRulerJson.getJSONArray("cargoTypesCarryWeightInRealTime");
            List<String> cargoTypesList = cargoTypesArray.toJavaList(String.class);
            this.rulerCollection.setCargoTypesCarryWeightInRealTime(cargoTypesList);
        }

        if (this.allRulerJson.containsKey("berthDirectionMap")) {
            JSONObject berthDirectionJson = this.allRulerJson.getJSONObject("berthDirectionMap");
            Map<String, Integer> berthDirectionMap = berthDirectionJson.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> Integer.parseInt(entry.getValue().toString())));
            this.rulerCollection.setBerthDirectionMap(berthDirectionMap);
        }

        //
        if (this.allRulerJson.containsKey("waterDepth")) {
            this.rulerCollection.setWaterDepth(this.allRulerJson.getBigDecimal("waterDepth"));
        }

        // 处理 rulerBerthShipInfoList
        if (this.allRulerJson.containsKey("rulerBerthShipInfoList")) {
            JSONArray shipInfoArray = this.allRulerJson.getJSONArray("rulerBerthShipInfoList");
            List<RulerBerthShipInfo> shipInfoList = shipInfoArray.stream()
                    .map(info -> JSON.parseObject(JSONObject.toJSONString(info), RulerBerthShipInfo.class))
                    .collect(Collectors.toList());
            this.rulerCollection.setRulerBerthShipInfoList(shipInfoList);
        }

        // 处理 rulerBerthCargoOwnerList
        if (this.allRulerJson.containsKey("rulerBerthCargoOwnerList")) {
            JSONArray cargoOwnerArray = this.allRulerJson.getJSONArray("rulerBerthCargoOwnerList");
            List<RulerBerthCargoOwner> cargoOwnerList = cargoOwnerArray.stream()
                    .map(owner -> JSON.parseObject(owner.toString(), RulerBerthCargoOwner.class))
                    .collect(Collectors.toList());
            this.rulerCollection.setRulerBerthCargoOwnerList(cargoOwnerList);
        }

        // 处理 rulerBerthCargoTypeList
        if (this.allRulerJson.containsKey("rulerBerthCargoTypeList")) {
            JSONArray cargoTypeArray = this.allRulerJson.getJSONArray("rulerBerthCargoTypeList");
            List<RulerBerthCargoType> cargoTypeList = cargoTypeArray.stream()
                    .map(type -> JSON.parseObject(type.toString(), RulerBerthCargoType.class))
                    .collect(Collectors.toList());
            this.rulerCollection.setRulerBerthCargoTypeList(cargoTypeList);
        }

        // 处理 rulerBerthCargoTypeList
        if (this.allRulerJson.containsKey("berthRestInWeekList")) {
            JSONArray cargoTypeArray = this.allRulerJson.getJSONArray("berthRestInWeekList");
            List<BerthRestInWeek> berthRestInWeekList = cargoTypeArray.stream()
                    .map(obj -> {
                        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>)obj ;
                        String daysStr = map.get("daysList");
                        if (StringUtils.isBlank(daysStr)) return null;
                        BerthRestInWeek berthRestInWeek = new BerthRestInWeek();
                        berthRestInWeek.setBerthNo( map.get("berthNo"));
                        berthRestInWeek.setDaysList(Arrays.stream(daysStr.split(",")).map(day-> DayOfWeek.of(Integer.parseInt(day))).collect(Collectors.toList()));
                        return berthRestInWeek;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            this.rulerCollection.setBerthRestInWeekList(berthRestInWeekList);
        }



    }




    public List<PlanningShipDO> getShipNotPlanned(LocalDateTime planningTime) {
        return this.shipDOList.stream().filter(t -> !t.isPlaned() && !t.getFastestBerthTime().isAfter(planningTime)).collect(Collectors.toList());
    }



    public boolean validTide(PlanningShipDO planningShipDO,LocalDateTime berthTime){
        return this.tideRulerHandler.validTide(planningShipDO,berthTime);
    }

}
