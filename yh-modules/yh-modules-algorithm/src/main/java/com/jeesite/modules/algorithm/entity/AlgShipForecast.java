package com.jeesite.modules.algorithm.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.modules.algorithm.enums.CargoWhereaboutsRequirementEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 预报船舶表
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_SHIP_FORECAST")
public class AlgShipForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID（申报记录唯一标识）
     */
    @TableId("ID")
    private String id;

    /**
     * 来港航次
     */
    @TableField("VOYAGE_NO")
    private String voyageNo;

    /**
     * 船舶代码
     */
    @TableField("SHIP_CODE")
    private String shipCode;

    /**
     * 中文船名
     */
    @TableField("SHIP_NAME_CN")
    private String shipNameCn;

    /**
     * 作业公司
     */
    @TableField("WORKING_COMPANY")
    private String workingCompany;

    /**
     * 代理公司
     */
    @TableField("AGENT_COMPANY")
    private String agentCompany;

    /**
     * 进口吃水
     */
    @TableField("DRAFT_IN")
    private BigDecimal draftIn;

    /**
     * 出口吃水
     */
    @TableField("DRAFT_OUT")
    private BigDecimal draftOut;

    /**
     * 货物小类代码
     */
    @TableField("CARGO_SUB_TYPE_CODE")
    private String cargoSubTypeCode;

    /**
     * 装卸货种
     */
    @TableField("CARGO_TYPE_NAME")
    private String cargoTypeName;

    /**
     * 贸别(1内贸,0外贸)
     */
    @TableField("TRADE_TYPE")
    private Integer tradeType;

    /**
     * 装卸类别(1装,2卸,3装卸)
     */
    @TableField("LOAD_UNLOAD")
    private Integer loadUnload;

    /**
     * 装计划(吨)	
     */
    @TableField("LOAD_PLAN_WEIGHT")
    private BigDecimal loadPlanWeight;

    /**
     * 卸计划(吨)
     */
    @TableField("UNLOAD_PLAN_WEIGHT")
    private BigDecimal unloadPlanWeight;

    /**
     * 配载(吨)
     */
    @TableField("CARRY_WEIGHT")
    private BigDecimal carryWeight;

    /**
     * 预到时间
     */
    @TableField("EXPECT_ARRIVE_TIME")
    private LocalDateTime expectArriveTime;

    /**
     * 预离时间
     */
    @TableField("EXPECT_LEAVE_TIME")
    private LocalDateTime expectLeaveTime;

    /**
     * 出发港代码
     */
    @TableField("PORT_CODE_FROM")
    private String portCodeFrom;

    /**
     * 到达港代码
     */
    @TableField("PORT_CODE_TO")
    private String portCodeTo;

    /**
     * 是否引水(1是,0否)
     */
    @TableField("NEED_LEAD")
    private String needLead;

    /**
     * 是否减载(1是,0否)
     */
    @TableField("IS_DELOAD")
    private String isDeload;

    /**
     * 备车时间(小时)
     */
    @TableField("CAR_RAEDY_TIME")
    private BigDecimal carRaedyTime;

    /**
     * 重点船舶标记(1是,0否)
     */
    @TableField("IS_IMPORTANT")
    private Integer isImportant;

    /**
     * 特殊船舶标记（非生产性船舶）(1是,0否)
     */
    @TableField("IS_SPECIAL")
    private Integer isSpecial;

    /**
     * 当前状态
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 货主
     */
    @TableField("CARGO_OWNER")
    private String cargoOwner;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATOR")
    private String creator;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField("REVISE_DATE")
    private LocalDateTime reviseDate;

    /**
     * 创建公司
     */
    @TableField("CREATE_COMPANY")
    private String createCompany;

    /**
     * 算法状态
     * {@link AlgorithmEnum}
     */
    @TableField("ALGORITHM_STATE")
    private Integer algorithmState;

    /**
     * 需要拖轮
     */
    @TableField("NEED_TRACTOR")
    private Integer needTractor;

    /**
     * 装卸要求（1直取|2存放）
     * {@link CargoWhereaboutsRequirementEnum}
     */
    @TableField("CARGO_WHEREABOUTS")
    private Integer cargoWhereabouts;

    /**
     * 疏港方式
     * {@link com.jeesite.modules.algorithm.enums.CargoEvacuationEnum}
     */
    @TableField("CARGO_EVACUATION")
    private Integer cargoEvacuation;






}
