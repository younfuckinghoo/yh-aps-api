package com.jeesite.modules.algorithm.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.jeesite.common.annotation.JhyjField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 在港船舶动态数据
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_SHIP_REAL_TIME")
public class AlgShipRealTime implements Serializable {

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
     * 预靠泊位动态
     */
    @TableField("REAL_TIME_BERTH")
    private String realTimeBerth;

    /**
     * 着岸(1左舷,2右舷,0任意)
     */
    @TableField("BERTH_DIRECTION")
    private Integer berthDirection;

    /**
     * 前岸桩号(实时)
     */
    @TableField("BOLLARD_HEAD")
    private String bollardHead;

    /**
     * 后岸桩号(实时)
     */
    @TableField("BOLLARD_TAIL")
    private String bollardTail;

    /**
     * 动态吃水(米)
     */
    @TableField("REAL_TIME_DRAFT")
    private BigDecimal realTimeDraft;

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
     * 到港时间(锚地下锚时间)
     */
    @TableField("ARRIVE_TIME")
    private LocalDateTime arriveTime;

    /**
     * 靠泊时间
     */
    @TableField("BERTH_TIME")
    private LocalDateTime berthTime;

    /**
     * 开工时间
     */
    @TableField("WORKING_START_TIME")
    private LocalDateTime workingStartTime;

    /**
     * 完工时间
     */
    @TableField("WORKING_FINISH_TIME")
    private LocalDateTime workingFinishTime;

    /**
     * 白班进度(吨)
     */
    @TableField("DAY_CLASS_PROGRESS")
    private BigDecimal dayClassProgress;

    /**
     * 夜班进度(吨)
     */
    @TableField("NIGHT_CLASS_PROGRESS")
    private BigDecimal nightClassProgress;

    /**
     * 装进度(吨)
     */
    @TableField("LOAD_PROGRESS")
    private BigDecimal loadProgress;

    /**
     * 卸进度(吨)
     */
    @TableField("UNLOAD_PROGRESS")
    private BigDecimal unloadProgress;

    /**
     * 实际进度（门机称重）
     */
    @TableField("REAL_PROGRESS")
    private BigDecimal realProgress;

    /**
     * 当前状态(1到港下锚;2作业;3停工)
     */
    @TableField("STATUS")
    private Integer status;

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
     * 创建部门名称
     */
    @TableField("CREATE_DEPT")
    private String createDept;


    /**
     * 算法状态
     * {@link com.jeesite.common.enum1.AlgorithmEnum}
     */
    @TableField("ALGORITHM_STATE")
    @Schema(title ="算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private Integer algorithmState;

    /**
     * 是否完成
     */
    @TableField("IS_FINISH")
    @Schema(title ="是否完成")
    private Integer isFinish;


}
