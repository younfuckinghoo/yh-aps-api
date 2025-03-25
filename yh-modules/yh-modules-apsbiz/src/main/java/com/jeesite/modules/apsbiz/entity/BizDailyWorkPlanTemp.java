package com.jeesite.modules.apsbiz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jeesite.common.annotation.JhyjField;
import com.jeesite.common.base.BaseEntity;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * （预）作业计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Data
@TableName("ALG_DAILY_WORK_PLAN_TEMP")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "（预）作业计划")
public class BizDailyWorkPlanTemp extends BaseEntity<BizShipPlanTemp> {


    //导出 开始
    @ExcelFields({@ExcelField(
            title = "泊位",
            attrName = "berth",
            align = ExcelField.Align.CENTER,
            sort = 10
    ), @ExcelField(
            title = "靠泊要求",
            attrName = "berthDirection",
            align = ExcelField.Align.CENTER,
            dictType = "APS_BERTH_DIRECTION",
            sort = 20
    ), @ExcelField(
            title = "桩位",
            attrName = "bollardNo",
            align = ExcelField.Align.CENTER,
            sort = 30
    ), @ExcelField(
            title = "船名",
            attrName = "shipNameCn",
            align = ExcelField.Align.CENTER,
            sort = 40
    ), @ExcelField(
            title = "舱数",
            attrName = "cabinNum",
            align = ExcelField.Align.CENTER,
            sort = 50
    ), @ExcelField(
            title = "载货吨",
            attrName = "carryWeight",
            align = ExcelField.Align.CENTER,
            sort = 60
    ), @ExcelField(
            title = "船长",
            attrName = "shipLength",
            align = ExcelField.Align.CENTER,
            sort = 70
    ), @ExcelField(
            title = "吃水",
            attrName = "draft",
            align = ExcelField.Align.CENTER,
            sort = 80
    ), @ExcelField(
            title = "货名",
            attrName = "cargoTypeName",
            align = ExcelField.Align.CENTER,
            sort = 90
    ), @ExcelField(
            title = "货主",
            attrName = "cargoOwner",
            align = ExcelField.Align.CENTER,
            sort = 100
    ), @ExcelField(
            title = "船代",
            attrName = "agentCompany",
            align = ExcelField.Align.CENTER,
            sort = 110
    ), @ExcelField(
            title = "计划靠泊",
            attrName = "planBerthTime",
            align = ExcelField.Align.CENTER,
            sort = 120
    ), @ExcelField(
            title = "实际靠泊",
            attrName = "berthTime",
            align = ExcelField.Align.CENTER,
            sort = 130
    ), @ExcelField(
            title = "计划完船",
            attrName = "planFinishTime",
            align = ExcelField.Align.CENTER,
            sort = 140
    ), @ExcelField(
            title = "吞吐量",
            attrName = "handleWeight",
            align = ExcelField.Align.CENTER,
            sort = 150
    ), @ExcelField(
            title = "剩余吨数",
            attrName = "remainWeight",
            align = ExcelField.Align.CENTER,
            sort = 160
    ), @ExcelField(
            title = "作业内容",
            attrName = "workContent",
            align = ExcelField.Align.CENTER,
            sort = 170
    ), @ExcelField(
            title = "作业量-夜班",
            attrName = "nightWorkAmount",
            align = ExcelField.Align.CENTER,
            sort = 180
    ), @ExcelField(
            title = "机械配备-夜班",
            attrName = "nightMachine",
            align = ExcelField.Align.CENTER,
            sort = 190
    ), @ExcelField(
            title = "人力-夜班",
            attrName = "nightPerson",
            align = ExcelField.Align.CENTER,
            sort = 200
    ), @ExcelField(
            title = "作业量-夜班",
            attrName = "dayWorkAmount",
            align = ExcelField.Align.CENTER,
            sort = 210
    ), @ExcelField(
            title = "机械配备-夜班",
            attrName = "dayMachine",
            align = ExcelField.Align.CENTER,
            sort = 220
    ), @ExcelField(
            title = "人力-夜班",
            attrName = "dayPerson",
            align = ExcelField.Align.CENTER,
            sort = 230
    ), @ExcelField(
            title = "作业要求",
            attrName = "workRequire",
            align = ExcelField.Align.CENTER,
            sort = 240
    )
    })
    //导出 结束


    private static final long serialVersionUID = 1L;

    /**
     * ID（申报记录唯一标识）
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="ID（申报记录唯一标识）")
    private String id;

    /**
     * 在港船舶ID
     */
    @JhyjField(eq = true)
    @Schema(description ="在港船舶ID")
    private String shipRealTimeId;

    /**
     * 计划日期
     */
    @JhyjField(dateEq = true)
    @Schema(description ="计划日期")
    private Date planDate;

    /**
     * 计划完工时间
     */
    @JhyjField(eq = true)
    @Schema(description ="计划完工时间")
    private Date planFinishTime;

    /**
     * 吞吐量
     */
    @JhyjField(eq = true)
    @Schema(description ="吞吐量")
    private String handleWeight;

    /**
     * 吞吐量
     */
    @JhyjField(eq = true)
    @Schema(description ="吞吐量")
    private String remainWeight;

    /**
     * 工作内容
     */
    @JhyjField(eq = true)
    @Schema(description ="工作内容")
    private String workContent;

    /**
     * 夜班-作业量
     */
    @JhyjField(eq = true)
    @Schema(description ="夜班作业量")
    private String nightWorkAmount;

    /**
     * 夜班-机械配置
     */
    @JhyjField(eq = true)
    @Schema(description ="夜班-机械配置")
    private String nightMachine;

    /**
     * 夜班-人力
     */
    @JhyjField(eq = true)
    @Schema(description ="夜班-人力")
    private String nightPerson;

    /**
     * 白班-作业量
     */
    @JhyjField(eq = true)
    @Schema(description ="白班作业量")
    private String dayWorkAmount;

    /**
     * 白班-机械配置
     */
    @JhyjField(eq = true)
    @Schema(description ="白班-机械配置")
    private String dayMachine;

    /**
     * 作业要求
     */
    @JhyjField(eq = true)
    @Schema(title ="作业要求")
    private String workRequire;

    /**
     * 白班-人力
     */
    @JhyjField(eq = true)
    @Schema(description ="白班-人力")
    private String dayPerson;

    /**
     * 创建人
     */
    @JhyjField(eq = true)
    @Schema(description ="创建人")
    private String creator;

    /**
     * 创建时间
     */
    @JhyjField(eq = true)
    @Schema(description ="创建时间")
    private Date createDate;

    /**
     * 修改时间
     */
    @JhyjField(eq = true)
    @Schema(description ="修改时间")
    private Date reviseDate;

    /**
     * 算法状态记录
     */
    @JhyjField(eq = true)
    @Schema(title ="算法状态记录", description = "字典（APS_ALGORITHM_STATE）")
    private Integer stayAlgorithmState;


////仅供查询字段//////////////////////////////////////////////////

    /**
     * 船名（模糊查询）
     */
    @TableField(exist = false)
    @Schema(description ="船名（模糊查询）")
    private String shipNameCnLike;

    /**
     * 靠泊时间（范围开始）
     */
    @TableField(exist = false)
    @Schema(description ="靠泊时间（范围开始）")
    private Date berthTimeStart;

    /**
     * 靠泊时间（范围结束）
     */
    @TableField(exist = false)
    @Schema(description ="靠泊时间（范围结束）")
    private Date berthTimeEnd;

    /**
     * 完船时间（范围开始）
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "PLAN_FINISH_TIME")
    @TableField(exist = false)
    @Schema(description ="完船时间（范围开始）")
    private Date planFinishimeStart;

    /**
     * 完船时间（范围结束）
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "PLAN_FINISH_TIME")
    @TableField(exist = false)
    @Schema(description ="完船时间（范围结束）")
    private Date planFinishimeEnd;


    /**
     * 算法状态
     */
    @JhyjField(eq = false)
    @TableField(exist = false)
    @Schema(title ="算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private Integer algorithmState;

    /**
     * 算法状态
     */
//    @JhyjField(in = true, tableField = false, fieldName = "ALGORITHM_STATE")
    @TableField(exist = false)
    @Schema(title ="算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private String algorithmStateIn;


    /**
     * ID(IN查询)
     */
    @JhyjField(in = true, tableField = false, fieldName = "ID")
    @TableField(exist = false)
    @Schema(description ="ID(IN查询)")
    private String idIn;


    /**
     * 在港ID(IN查询)
     */
    @JhyjField(in = true, tableField = false, fieldName = "SHIP_REAL_TIME_ID")
    @TableField(exist = false)
    @Schema(description ="在港ID(IN查询)")
    private String shipRealTimeIdIn;


////仅供展示字段//////////////////////////////////////////////////

// WORK_REAL_TIME
    /**
     * 中文船名
     */
    @TableField(exist = false)
    @Schema(description ="中文船名")
    private String shipNameCn;

    /**
     * 靠泊要求
     */
    @TableField(exist = false)
    @Schema(description ="靠泊要求")
    private Integer berthDirection;

    /**
     * 航次号
     */
    @TableField(exist = false)
    @Schema(description ="航次号")
    private String voyageNo;

    /**
     * 首榄桩
     */
    @TableField(exist = false)
    @Schema(description ="首榄桩")
    private String headBollardNo;

    /**
     * 尾榄桩
     */
    @TableField(exist = false)
    @Schema(description ="尾榄桩")
    private String tailBollardNo;

    /**
     * 吃水
     */
    @TableField(exist = false)
    @Schema(description ="吃水")
    private String draft;

    /**
     * 靠泊时间
     */
    @TableField(exist = false)
    @Schema(description ="靠泊时间")
    private Date berthTime;

//    FORECAST

    /**
     * 货主
     */
    @TableField(exist = false)
    @Schema(description ="货主")
    private String cargoOwner;

    /**
     * 作业公司
     */
    @TableField(exist = false)
    @Schema(description ="作业公司")
    private String workingCompany;

    /**
     * 代理公司
     */
    @TableField(exist = false)
    @Schema(description ="代理公司")
    private String agentCompany;

    /**
     * 创建公司
     */
    @TableField(exist = false)
    @Schema(description ="创建公司")
    private String createCompany;


    /**
     * 配载(吨)
     */
    @TableField(exist = false)
    @Schema(description ="配载(吨)")
    private Integer carryWeight;

    /**
     * 货物小类代码
     */
    @TableField(exist = false)
    @Schema(description ="货物小类代码")
    private String cargoSubTypeCode;

    /**
     * 装卸货种
     */
    @TableField(exist = false)
    @Schema(description ="装卸货种")
    private String cargoTypeName;



    // SHIP_INFO
    @TableField(exist = false)
    @Schema(description ="船长")
    private String shipLength;

    @TableField(exist = false)
    @Schema(description ="舱数")
    private Integer cabinNum;



    // SHIP_PLAN
    @TableField(exist = false)
    @Schema(description ="计划靠泊时间")
    private Date planBerthTime;


    /**
     * 泊位
     */
    @TableField(exist = false)
    @Schema(description ="泊位")
    private String berth;


    /**
     * 缆柱
     */
    @TableField(exist = false)
    @Schema(description ="缆柱")
    private String bollardNo;



////查询 + 展示字段//////////////////////////////////////////////////

    /**
     * 泊位
     */
    @TableField(exist = false)
    @Schema(description ="泊位")
    private String berthNo;

    /**
     * 船舶代码
     */
    @TableField(exist = false)
    @Schema(description ="船舶代码")
    private String shipCode;

    /**
     * 当前状态
     */
    @TableField(exist = false)
    @Schema(description ="当前状态")
    private Integer status;

}
