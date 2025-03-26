package com.jeesite.modules.algorithm.planning;

import com.jeesite.modules.algorithm.enums.BerthDirectionEnum;
import com.jeesite.modules.algorithm.planning.ruler.BerthRestInWeek;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthCargoOwner;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthCargoType;
import com.jeesite.modules.algorithm.planning.ruler.RulerBerthShipInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 所有规则集合
 */


@Data
public class RulerCollection {

    /**
     * 到锚地后至少多久能到泊位
     * 预到时间至少多少小时之后才能靠泊
     */
    private BigDecimal startWorkReadyHour;

    /**
     * 船舶靠离时间间隔 同泊位
     */
    private BigDecimal shipWorkingGap;

    /**
     * 离泊准备时间（从完工时间开始计算） 船舶作业完之后水尺手续，边检手续，引水手续等需要平均3个小时
     */
    private BigDecimal leaveReadyHour;

    /**
     * 船长余量
     * 1、双船靠泊同一泊位，两条船之间靠泊间距要大于最长船长的10%
     */
    private BigDecimal shipLengthMargin;

    /**
     * 趁潮余量
     * 1、乘潮水高度计算公式：船舶吃水* 1.1 -航道水深（港池实际水深）= 最低的乘潮靠泊米数
     */
    private BigDecimal tideHeightMargin;


    /**
     * 泊位 一周休息哪几天 不排泊
     */
    private List<BerthRestInWeek> berthRestInWeekList = new ArrayList<>();
    /**
     * 船舶信息匹配泊位规则
     * 用来校验的 最终校验船可以去哪个泊位
     */
    private List<RulerBerthShipInfo> rulerBerthShipInfoList;
    /**
     * 货主信息匹配泊位规则
     * 用来筛选的 范围较小 第二步匹配缩小范围 如果匹配不到直接使用第一步匹配的结果
     */
    private List<RulerBerthCargoOwner> rulerBerthCargoOwnerList;
    /**
     * 货种信息匹配泊位规则
     * 用来筛选的 范围较大 第一步匹配
     */
    private List<RulerBerthCargoType> rulerBerthCargoTypeList;


    /**
     * 海关检疫时间 时间点 时分秒
     */
    private List<LocalTime> quarantineTimePointArr;

    /**
     * 检疫时间 分钟
     * 三小时
     */
    private int quarantineMinutes;

    /**
     * 被占用超过几条缆柱就看做一条船在这个泊位上
     */
    private int bollardCountAsShip;



    /**
     * 泊位组
     */
    private List<List<String>> berthGroupList ;

    /**
     * 货种列表
     * 载重到实时船舶数据中取
     */
    private List<String> cargoTypesCarryWeightInRealTime;


    /**
     * 着岸(左右舷停靠)
     */
    private Map<String,Integer> berthDirectionMap;

    /**
     * 港池水深
     */
    private BigDecimal waterDepth ;

    {

        startWorkReadyHour = new BigDecimal("3");
//        shipWorkingGap = new BigDecimal("3");
//        leaveReadyHour = new BigDecimal("3");
//        shipLengthMargin = new BigDecimal("1.1");
//        tideHeightMargin = new BigDecimal("1.1");
//        quarantineMinutes = 180;
//        BerthRestInWeek berthRestInWeek = new BerthRestInWeek();
//        berthRestInWeek.setBerthNo("802");
//        berthRestInWeek.setDaysList(List.of(DayOfWeek.MONDAY));
//        berthRestInWeekList.add(berthRestInWeek);
        bollardCountAsShip = 4;

        rulerBerthShipInfoList = List.of(
                new RulerBerthShipInfo(1,1,true,"802","150","9.6","5.5"),
                new RulerBerthShipInfo(2,1,false,"803","230","11.2","8"),
                new RulerBerthShipInfo(4,1,true,"805","350","13.9","10"),
//                new RulerBerthShipInfo(3,1,false,"806","299","15.19","15"),
                new RulerBerthShipInfo(3,1,true,"509","300","14.5","15")
        );

        rulerBerthCargoOwnerList = List.of(
                new RulerBerthCargoOwner(1,200,true,"805","中纺粮油"),
                new RulerBerthCargoOwner(1,200,true,"805","嘉吉"),
                new RulerBerthCargoOwner(1,200,true,"805","邦基三维"),
                new RulerBerthCargoOwner(1,200,true,"805","中储粮"),
                new RulerBerthCargoOwner(1,100,false,"806","中纺粮油"),
                new RulerBerthCargoOwner(1,100,false,"806","嘉吉"),
                new RulerBerthCargoOwner(1,100,false,"806","邦基三维"),
                new RulerBerthCargoOwner(1,100,false,"806","中储粮"),
                new RulerBerthCargoOwner(1,100,true,"509","日照象明"),
                new RulerBerthCargoOwner(1,100,true,"509","山东嘉冠"),
                new RulerBerthCargoOwner(1,100,true,"509","凌云海")
        );

        rulerBerthCargoTypeList = List.of(
                new RulerBerthCargoType(1,300,true,"802","粮食",3),
//                new RulerBerthCargoType(1,200,true,"802","木材"),
                new RulerBerthCargoType(1,300,true,"802","农.林.牧.渔业产品",2),
                new RulerBerthCargoType(1,300,true,"802","饲料",1),
                new RulerBerthCargoType(1,400,false,"803","粮食",3),
                new RulerBerthCargoType(1,300,false,"803","木材",2),
                new RulerBerthCargoType(1,200,false,"803","农.林.牧.渔业产品",2),
                new RulerBerthCargoType(1,200,false,"803","饲料",1),


                new RulerBerthCargoType(1,700,true,"805","粮食",2),
                new RulerBerthCargoType(1,500,true,"806","粮食",2),
                new RulerBerthCargoType(1,600,true,"509","粮食",3),
                new RulerBerthCargoType(1,100,true,"509","木材",2)
        );

        // 整体时间主要依据海关检疫时间，海关一般早上9点、11点、4（16）点左右到码头进行检查，检查完后即可开始作业（检疫默认1个小时）
//        quarantineTimePointArr = List.of(LocalTime.of(9,0),LocalTime.of(11,0),LocalTime.of(16,0));


        // 连续泊位
        berthGroupList = List.of(List.of("802","803"),List.of("805","806"),List.of("509"));

        berthDirectionMap = Map.of("803", BerthDirectionEnum.LEFT.getCode(),
                "806", BerthDirectionEnum.LEFT.getCode(),
                "802", BerthDirectionEnum.RIGHT.getCode(),
//                "西4", BerthDirectionEnum.RIGHT.getCode(),
                "805", BerthDirectionEnum.RIGHT.getCode(),
                "509", BerthDirectionEnum.RIGHT.getCode());


        cargoTypesCarryWeightInRealTime = List.of("木材");

        waterDepth = new BigDecimal("9");
    }

//    {
//        rulerBerthShipInfoList = List.of(
//                new RulerBerthShipInfo(1,1,true,"2#","150","9.6","5.5"),
//                new RulerBerthShipInfo(2,1,false,"3#","230","11.2","8"),
//                new RulerBerthShipInfo(4,1,true,"5#","350","13.9","10"),
//                new RulerBerthShipInfo(3,1,false,"6#","299","15.19","15"),
//                new RulerBerthShipInfo(3,1,true,"18#","300","14.5","15")
//        );
//
//        rulerBerthCargoOwnerList = List.of(
//                new RulerBerthCargoOwner(1,200,true,"5#","中纺粮油"),
//                new RulerBerthCargoOwner(1,200,true,"5#","嘉吉"),
//                new RulerBerthCargoOwner(1,200,true,"5#","邦基三维"),
//                new RulerBerthCargoOwner(1,200,true,"5#","中储粮"),
//                new RulerBerthCargoOwner(1,100,false,"6#","中纺粮油"),
//                new RulerBerthCargoOwner(1,100,false,"6#","嘉吉"),
//                new RulerBerthCargoOwner(1,100,false,"6#","邦基三维"),
//                new RulerBerthCargoOwner(1,100,false,"6#","中储粮"),
//                new RulerBerthCargoOwner(1,100,true,"18#","日照象明"),
//                new RulerBerthCargoOwner(1,100,true,"18#","山东嘉冠"),
//                new RulerBerthCargoOwner(1,100,true,"18#","凌云海")
//        );
//
//        rulerBerthCargoTypeList = List.of(
//                new RulerBerthCargoType(1,300,true,"2#","粮食"),
//                new RulerBerthCargoType(1,200,true,"2#","木材"),
//                new RulerBerthCargoType(1,300,true,"2#","农.林.牧.渔业产品"),
//                new RulerBerthCargoType(1,300,true,"2#","饲料"),
//                new RulerBerthCargoType(1,400,false,"3#","粮食"),
//                new RulerBerthCargoType(1,300,false,"3#","木材"),
//                new RulerBerthCargoType(1,200,false,"3#","农.林.牧.渔业产品"),
//                new RulerBerthCargoType(1,200,false,"3#","饲料"),
//
//
//                new RulerBerthCargoType(1,500,true,"5#","粮食"),
//                new RulerBerthCargoType(1,700,true,"6#","粮食"),
//                new RulerBerthCargoType(1,600,true,"18#","粮食")
//        );
//
//        // 整体时间主要依据海关检疫时间，海关一般早上9点、11点、4（16）点左右到码头进行检查，检查完后即可开始作业（检疫默认1个小时）
//        quarantineTimePointArr = List.of(LocalTime.of(9,0),LocalTime.of(11,0),LocalTime.of(16,0));
//
//
//        // 连续泊位
//        berthGroupList = List.of(List.of("2#","3#"),List.of("5#","6#"),List.of("18#"));
//
//        berthDirectionMap = Map.of("3#", BerthDirectionEnum.LEFT.getCode(),
//                "6#", BerthDirectionEnum.LEFT.getCode(),
//                "2#", BerthDirectionEnum.RIGHT.getCode(),
//                "西4", BerthDirectionEnum.RIGHT.getCode(),
//                "5#", BerthDirectionEnum.RIGHT.getCode(),
//                "18#", BerthDirectionEnum.RIGHT.getCode());
//
//
//        cargoTypesCarryWeightInRealTime = List.of("木材");
//    }


}
