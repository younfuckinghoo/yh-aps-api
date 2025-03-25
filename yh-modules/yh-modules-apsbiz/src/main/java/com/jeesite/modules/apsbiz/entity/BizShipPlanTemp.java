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

import java.math.BigDecimal;
import java.util.Date;

/**
 * （预）靠离泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Data
@TableName("ALG_SHIP_PLAN_TEMP")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "（预）靠离泊计划")
public class BizShipPlanTemp extends BaseEntity<BizShipPlanTemp> {

    //导出 开始
    @ExcelFields({@ExcelField(
            title = "船名",
            attrName = "shipNameCn",
            align = ExcelField.Align.CENTER,
            sort = 10
    ), @ExcelField(
            title = "作业公司",
            attrName = "workingCompany",
            align = ExcelField.Align.CENTER,
            sort = 20
    ), @ExcelField(
            title = "当前状态",
            attrName = "status",
            align = ExcelField.Align.CENTER,
            dictType = "APS_FORECAST_STATUS",
            sort = 30
    ), @ExcelField(
            title = "泊位",
            attrName = "berth",
            align = ExcelField.Align.CENTER,
            sort = 40
    ), @ExcelField(
            title = "缆绳桩号",
            attrName = "bollardNo",
            align = ExcelField.Align.CENTER,
            sort = 41
    ), @ExcelField(
            title = "靠泊时间",
            attrName = "planBerthTime",
            align = ExcelField.Align.CENTER,
            sort = 50
    ), @ExcelField(
            title = "离泊时间",
            attrName = "planLeaveTime",
            align = ExcelField.Align.CENTER,
            sort = 60
    ), @ExcelField(
            title = "计划开工时间",
            attrName = "planFinishTime",
            align = ExcelField.Align.CENTER,
            sort = 61
    ), @ExcelField(
            title = "计划完工时间",
            attrName = "planLeaveTime",
            align = ExcelField.Align.CENTER,
            sort = 62
    ), @ExcelField(
            title = "贸别",
            attrName = "tradeType",
            align = ExcelField.Align.CENTER,
            dictType = "APS_TRADE_TYPE",
            sort = 70
    ), @ExcelField(
            title = "船代",
            attrName = "agentCompany",
            align = ExcelField.Align.CENTER,
            sort = 80
    ), @ExcelField(
            title = "货主",
            attrName = "cargoOwner",
            align = ExcelField.Align.CENTER,
            sort = 90
    ), @ExcelField(
            title = "船长",
            attrName = "shipLength",
            align = ExcelField.Align.CENTER,
            sort = 100
    ), @ExcelField(
            title = "船宽",
            attrName = "shipWidth",
            align = ExcelField.Align.CENTER,
            sort = 110
    ), @ExcelField(
            title = "进口吃水",
            attrName = "draftIn",
            align = ExcelField.Align.CENTER,
            sort = 120
    ), @ExcelField(
            title = "出口吃水",
            attrName = "draftOut",
            align = ExcelField.Align.CENTER,
            sort = 130
    ), @ExcelField(
            title = "载重吨",
            attrName = "carryWeight",
            align = ExcelField.Align.CENTER,
            sort = 140
    ), @ExcelField(
            title = "货物",
            attrName = "cargoTypeName",
            align = ExcelField.Align.CENTER,
            sort = 150
    ), @ExcelField(
            title = "装卸",
            attrName = "loadUnload",
            align = ExcelField.Align.CENTER,
            dictType = "APS_LOAD_UNLOAD",
            sort = 160
    ), @ExcelField(
            title = "装卸计划",
            attrName = "loadUnloadWeight",
            align = ExcelField.Align.CENTER,
            sort = 170
    ), @ExcelField(
            title = "引水",
            attrName = "needLead",
            align = ExcelField.Align.CENTER,
            dictType = "Y_OR_N",
            sort = 180
    ), @ExcelField(
            title = "拖轮",
            attrName = "needTractor",
            align = ExcelField.Align.CENTER,
            dictType = "Y_OR_N",
            sort = 190
    ), @ExcelField(
            title = "着岸",
            attrName = "berthDirection",
            align = ExcelField.Align.CENTER,
            dictType = "APS_BERTH_DIRECTION",
            sort = 200
    ), @ExcelField(
            title = "出发港",
            attrName = "portCodeFrom",
            align = ExcelField.Align.CENTER,
            sort = 220
    ), @ExcelField(
            title = "到达港",
            attrName = "portCodeTo",
            align = ExcelField.Align.CENTER,
            sort = 230
    ), @ExcelField(
            title = "离泊备注",
            attrName = "leaveRemark",
            align = ExcelField.Align.CENTER,
            sort = 240
    ), @ExcelField(
            title = "靠泊备注",
            attrName = "berthRemark",
            align = ExcelField.Align.CENTER,
            sort = 250
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
     * 申报唯一ID
     */
    @JhyjField(eq = true)
    @Schema(description ="申报唯一ID")
    private String shipForcastId;

    /**
     * 泊位
     */
    @JhyjField(eq = true)
    @Schema(description ="泊位")
    private String berthNo;

    /**
     * 计划靠泊时间
     */
    @JhyjField(eq = true)
    @Schema(description ="计划靠泊时间")
    private Date planBerthTime;

    /**
     * 计划开工时间
     */
    @JhyjField(eq = true)
    @Schema(description ="计划开工时间")
    private Date planStartTime;

    /**
     * 计划完工时间
     */
    @JhyjField(eq = true)
    @Schema(description ="计划完工时间")
    private Date planFinishTime;

    /**
     * 计划离泊时间
     */
    @JhyjField(eq = true)
    @Schema(description ="计划离泊时间")
    private Date planLeaveTime;

    /**
     * 首榄桩
     */
    @JhyjField(eq = true)
    @Schema(description ="首榄桩")
    private String headBollardId;

    /**
     * 尾榄桩
     */
    @JhyjField(eq = true)
    @Schema(description ="尾榄桩")
    private String tailBollardId;

    /**
     * 计划状态(1未提交2已提交3已通过4已拒绝)
     */
    @JhyjField(eq = true)
    @Schema(description ="计划状态(1未提交2已提交3已通过4已拒绝)")
    private Integer planStatus;

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
    private Date createTime;

    /**
     * 修改人
     */
    @JhyjField(eq = true)
    @Schema(description ="修改人")
    private String modifier;
    /**
     * 修改时间
     */
    @JhyjField(eq = true)
    @Schema(description ="修改时间")
    private Date modifyTime;

    /**
     * 着岸(左右舷停靠) 1左 2右
     */
    @JhyjField(eq = true)
    @Schema(description ="着岸(左右舷停靠) 1左 2右")
    private String berthDirection;

    /**
     * 靠泊备注
     */
    @JhyjField(eq = true)
    @Schema(description ="靠泊备注")
    private String berthRemark;

    /**
     * 离泊备注
     */
    @JhyjField(eq = true)
    @Schema(description ="离泊备注")
    private String leaveRemark;

    /**
     * 算法状态
     */
//    @JhyjField(eq = true)
    @TableField(exist = false)
    @Schema(title ="算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private Integer algorithmState;

    /**
     * 是否引水(1是,0否)
     */
    @JhyjField(eq = true)
    @Schema(description ="是否引水(1是,0否)")
    private Integer needLead;

    /**
     * 是否拖轮(1是,0否)
     */
    @JhyjField(eq = true)
    @Schema(description ="是否拖轮(1是,0否)")
    private Integer needTractor;


////仅供查询字段//////////////////////////////////////////////////

    /**
     * 计划靠泊时间（范围开始）
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "PLAN_BERTH_TIME")
    @TableField(exist = false)
    @Schema(description ="预到时间（范围开始）")
    private Date planBerthTimeStart;

    /**
     * 计划靠泊时间（范围结束）
     */
    @JhyjField(datetimeLe = true, tableField = false, fieldName = "PLAN_BERTH_TIME")
    @TableField(exist = false)
    @Schema(description ="预到时间（范围结束）")
    private Date planBerthTimeEnd;

    /**
     * 计划离泊时间（范围开始）
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "PLAN_LEAVE_TIME")
    @TableField(exist = false)
    @Schema(description ="预到时间（范围开始）")
    private Date planLeaveTimeStart;

    /**
     * 计划离泊时间（范围结束）
     */
    @JhyjField(datetimeLe = true, tableField = false, fieldName = "PLAN_LEAVE_TIME")
    @TableField(exist = false)
    @Schema(description ="预到时间（范围结束）")
    private Date planLeaveTimeEnd;

    /**
     * 船名（模糊查询）
     */
    @TableField(exist = false)
    @Schema(description ="船名（模糊查询）")
    private String shipNameCnLike;


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
     * 预报ID(IN查询)
     */
    @JhyjField(in = true, tableField = false, fieldName = "SHIP_FORCAST_ID")
    @TableField(exist = false)
    @Schema(description ="预报ID(IN查询)")
    private String shipForcastIdIn;


////仅供展示字段//////////////////////////////////////////////////

    /**
     * 来港航次
     */
    @TableField(exist = false)
    @Schema(description ="来港航次")
    private String voyageNo;

    /**
     * 船舶代码
     */
    @TableField(exist = false)
    @Schema(description ="船舶代码")
    private String shipCode;

    /**
     * 中文船名
     */
    @TableField(exist = false)
    @Schema(description ="中文船名")
    private String shipNameCn;

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
     * 进口吃水
     */
    @TableField(exist = false)
    @Schema(description ="进口吃水")
    private Integer draftIn;

    /**
     * 出口吃水
     */
    @TableField(exist = false)
    @Schema(description ="出口吃水")
    private Integer draftOut;

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

    /**
     * 贸别(1内贸,0外贸)
     */
    @TableField(exist = false)
    @Schema(description ="贸别(1内贸,0外贸)")
    private Integer tradeType;

    /**
     * 装卸类别(1装,2卸,3装卸)
     */
    @TableField(exist = false)
    @Schema(description ="装卸类别(1装,2卸,3装卸)")
    private Integer loadUnload;

    /**
     * 装计划(吨)
     */
    @TableField(exist = false)
    @Schema(description ="装计划(吨)	")
    private BigDecimal loadPlanWeight;

    /**
     * 卸计划(吨)
     */
    @TableField(exist = false)
    @Schema(description ="卸计划(吨)")
    private BigDecimal unloadPlanWeight;

    /**
     * 配载(吨)
     */
    @TableField(exist = false)
    @Schema(description ="配载(吨)")
    private Integer carryWeight;

    /**
     * 预到时间
     */
    @TableField(exist = false)
    @Schema(description ="预到时间")
    private Date expectArriveTime;

    /**
     * 预离时间
     */
    @TableField(exist = false)
    @Schema(description ="预离时间")
    private Date expectLeaveTime;

    /**
     * 出发港代码
     */
    @TableField(exist = false)
    @Schema(description ="出发港代码")
    private String portCodeFrom;

    /**
     * 到达港代码
     */
    @TableField(exist = false)
    @Schema(description ="到达港代码")
    private String portCodeTo;

    /**
     * 是否减载(1是,0否)
     */
    @TableField(exist = false)
    @Schema(description ="是否减载(1是,0否)")
    private String isDeload;

    /**
     * 备车时间(小时)
     */
    @TableField(exist = false)
    @Schema(description ="备车时间(小时)")
    private Integer carRaedyTime;

    /**
     * 重点船舶标记(1是,0否)
     */
    @TableField(exist = false)
    @Schema(description ="重点船舶标记(1是,0否)")
    private Integer isImportant;

    /**
     * 特殊船舶标记（非生产性船舶）(1是,0否)
     */
    @TableField(exist = false)
    @Schema(description ="特殊船舶标记（非生产性船舶）(1是,0否)")
    private Integer isSpecial;


    /**
     * 货主
     */
    @TableField(exist = false)
    @Schema(description ="货主")
    private String cargoOwner;

    /**
     * 备注
     */
    @TableField(exist = false)
    @Schema(description ="备注")
    private String remark;

    /**
     * 创建公司
     */
    @TableField(exist = false)
    @Schema(description ="创建公司")
    private String createCompany;


// SHIP_INFO
    /**
     * 船长
     */
    @TableField(exist = false)
    @Schema(description ="船长")
    private String shipLength;


    /**
     * 船宽
     */
    @TableField(exist = false)
    @Schema(description ="船宽")
    private String shipWidth;


    /**
     * 装卸计划
     */
    @TableField(exist = false)
    @Schema(description ="装卸计划")
    private String loadUnloadWeight;


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
     * 当前状态
     */
    @TableField(exist = false)
    @Schema(description ="当前状态")
    private Integer status;

    /**
     * 计划开工时间
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "PLAN_START_TIME")
    @TableField(exist = false)
    @Schema(description ="计划开工时间（>= date）")
    private Date planStartTimeStart;

    /**
     * 计划开工时间
     */
    @JhyjField(datetimeLe = true, tableField = false, fieldName = "PLAN_START_TIME")
    @TableField(exist = false)
    @Schema(description ="计划开工时间（<= date）")
    private Date planStartTimeEnd;

    /**
     * 计划完工时间
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "PLAN_FINISH_TIME")
    @TableField(exist = false)
    @Schema(description ="计划完工时间（>= date）")
    private Date planFinishTimeStart;

    /**
     * 计划完工时间
     */
    @JhyjField(datetimeLe = true, tableField = false, fieldName = "PLAN_FINISH_TIME")
    @TableField(exist = false)
    @Schema(description ="计划完工时间（<= date）")
    private Date planFinishTimeEnd;

}
