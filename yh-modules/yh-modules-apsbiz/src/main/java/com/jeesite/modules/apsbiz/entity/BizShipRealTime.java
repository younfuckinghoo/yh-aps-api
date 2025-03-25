package com.jeesite.modules.apsbiz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jeesite.common.annotation.JhyjField;
import com.jeesite.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 在泊船舶动态数据
 *
 * @author zhudi
 * @date 2024-12-08 21:19:20
 */
@Data
@TableName("ALG_SHIP_REAL_TIME")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "在泊船舶动态数据")
public class BizShipRealTime extends BaseEntity<BizShipRealTime> {

    private static final long serialVersionUID = 1L;

    /**
     * ID（申报记录唯一标识）
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="ID（申报记录唯一标识）")
    private String id;

    /**
     * 来港航次
     */
    @JhyjField(eq = true)
    @Schema(description ="来港航次")
    private String voyageNo;

    /**
     * 船舶代码
     */
    @JhyjField(eq = true)
    @Schema(description ="船舶代码")
    private String shipCode;

    /**
     * 中文船名
     */
    @JhyjField(eq = true)
    @Schema(description ="中文船名")
    private String shipNameCn;

    /**
     * 预靠泊位动态
     */
    @JhyjField(eq = true)
    @Schema(description ="预靠泊位动态")
    private String realTimeBerth;

    /**
     * 着岸(1左舷,2右舷,0任意)
     */
    @JhyjField(eq = true)
    @Schema(description ="着岸(1左舷,2右舷,0任意)")
    private Integer berthDirection;

    /**
     * 前岸桩号(实时)
     */
    @JhyjField(eq = true)
    @Schema(description ="前岸桩号(实时)")
    private String bollardHead;

    /**
     * 后岸桩号(实时)
     */
    @JhyjField(eq = true)
    @Schema(description ="后岸桩号(实时)")
    private String bollardTail;

    /**
     * 动态吃水(米)
     */
    @JhyjField(eq = true)
    @Schema(description ="动态吃水(米)")
    private Integer realTimeDraft;

    /**
     * 装卸类别(1装,2卸,3装卸)
     */
    @JhyjField(eq = true)
    @Schema(description ="装卸类别(1装,2卸,3装卸)")
    private Integer loadUnload;

    /**
     * 装计划(吨)	
     */
    @JhyjField(eq = true)
    @Schema(description ="装计划(吨)	")
    private Integer loadPlanWeight;

    /**
     * 卸计划(吨)
     */
    @JhyjField(eq = true)
    @Schema(description ="卸计划(吨)")
    private Integer unloadPlanWeight;

    /**
     * 配载(吨)
     */
    @JhyjField(eq = true)
    @Schema(description ="配载(吨)")
    private Integer carryWeight;

    /**
     * 预到时间
     */
    @JhyjField(eq = true)
    @Schema(description ="预到时间")
    private Date expectArriveTime;

    /**
     * 预离时间
     */
    @JhyjField(eq = true)
    @Schema(description ="预离时间")
    private Date expectLeaveTime;

    /**
     * 到港时间(锚地下锚时间)
     */
    @JhyjField(eq = true)
    @Schema(description ="到港时间(锚地下锚时间)")
    private Date arriveTime;

    /**
     * 靠泊时间
     */
    @JhyjField(eq = true)
    @Schema(description ="靠泊时间")
    private Date berthTime;

    /**
     * 开工时间
     */
    @JhyjField(eq = true)
    @Schema(description ="开工时间")
    private Date workingStartTime;

    /**
     * 完工时间
     */
    @JhyjField(eq = true)
    @Schema(description ="完工时间")
    private Date workingFinishTime;

    /**
     * 白班进度(吨)
     */
    @JhyjField(eq = true)
    @Schema(description ="白班进度(吨)")
    private Integer dayClassProgress;

    /**
     * 夜班进度(吨)
     */
    @JhyjField(eq = true)
    @Schema(description ="夜班进度(吨)")
    private Integer nightClassProgress;

    /**
     * 装进度(吨)
     */
    @JhyjField(eq = true)
    @Schema(description ="装进度(吨)")
    private Integer loadProgress;

    /**
     * 装进度(吨)
     */
    @JhyjField(eq = true)
    @Schema(description ="装进度(吨)")
    private Integer unloadProgress;

    /**
     * 实际进度（门机称重）
     */
    @JhyjField(eq = true)
    @Schema(description ="实际进度（门机称重）")
    private Integer realProgress;

    /**
     * 当前状态(1到港下锚;2作业;3停工)
     */
    @JhyjField(eq = true)
    @Schema(description ="当前状态")
    private Integer status;

    /**
     * 备注
     */
    @JhyjField(eq = true)
    @Schema(description ="备注")
    private String remark;

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
     * 创建公司
     */
    @JhyjField(eq = true)
    @Schema(description ="创建公司")
    private String createCompany;

    /**
     * 创建部门名称
     */
    @JhyjField(eq = true)
    @Schema(description ="创建部门名称")
    private String createDept;

    /**
     * 算法状态
     */
    @JhyjField(eq = true)
    @Schema(title ="算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private Integer algorithmState;

    /**
     * 是否完成
     */
    @JhyjField(eq = true)
    @Schema(title ="是否完成")
    private Integer isFinish;


////仅供查询字段//////////////////////////////////////////////////

    /**
     * 靠泊时间（范围开始）
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "BERTH_TIME")
    @TableField(exist = false)
    @Schema(description ="靠泊时间（范围开始）")
    private Date berthTimeStart;

    /**
     * 靠泊时间（范围结束）
     */
    @JhyjField(datetimeLe = true, tableField = false, fieldName = "BERTH_TIME")
    @TableField(exist = false)
    @Schema(description ="靠泊时间（范围结束）")
    private Date berthTimeEnd;

    /**
     * 开工时间（范围开始）
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "WORKING_START_TIME")
    @TableField(exist = false)
    @Schema(description ="开工时间（范围开始）")
    private Date workingStartTimeStart;

    /**
     * 开工时间（范围结束）
     */
    @JhyjField(datetimeLe = true, tableField = false, fieldName = "WORKING_START_TIME")
    @TableField(exist = false)
    @Schema(description ="开工时间（范围结束）")
    private Date workingStartTimeEnd;


    /**
     * 船名（模糊查询）
     */
    @JhyjField(like = true, tableField = false, fieldName = "SHIP_NAME_CN")
    @TableField(exist = false)
    @Schema(description ="船名（模糊查询）")
    private String shipNameCnLike;


    /**
     * 算法状态
     */
    @JhyjField(in = true, tableField = false, fieldName = "ALGORITHM_STATE")
    @TableField(exist = false)
    @Schema(title ="算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private String algorithmStateIn;


    /**
     * ID集合
     */
    @JhyjField(in = true, tableField = false, fieldName = "ID")
    @TableField(exist = false)
    @Schema(description ="ID（集合）")
    private String idIn;

    /**
     * （船期）算法状态
     */
    @TableField(exist = false)
    @Schema(title ="（船期）算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private Integer forecastAlgorithmState;


////仅供展示字段//////////////////////////////////////////////////


    /**
     * 作业公司
     */
    @Schema(description ="作业公司")
    @TableField(exist = false)
    private String workingCompany;

    /**
     * 代理公司
     */
    @Schema(description ="代理公司")
    @TableField(exist = false)
    private String agentCompany;


    /**
     * 装卸货种
     */
    @Schema(description ="装卸货种")
    @TableField(exist = false)
    private String cargoTypeName;


    /**
     * 货主
     */
    @Schema(description ="货主")
    @TableField(exist = false)
    private String cargoOwner;


    /**
     * 贸别
     */
    @Schema(description ="贸别")
    @TableField(exist = false)
    private Integer tradeType;


}
