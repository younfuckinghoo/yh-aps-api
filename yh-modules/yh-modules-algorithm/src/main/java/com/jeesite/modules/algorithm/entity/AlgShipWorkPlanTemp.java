package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 船舶作业计划主表，记录船舶作业核心信息
 * </p>
 *
 * @author haoyong
 * @since 2025-03-24
 */
@Getter
@Setter
@TableName("ALG_SHIP_WORK_PLAN_TEMP")
public class AlgShipWorkPlanTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主表唯一标识符
     */
    @TableId("ID")
    private String id;

    /**
     * 航次号（唯一标识申报航次）
     */
    @TableField("VOYAGE_NO")
    private String voyageNo;

    /**
     * 船舶停靠的泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;

    /**
     * 靠泊方向描述（如：左舷靠泊、右舷靠泊）
     */
    @TableField("BERTH_DIRECTION")
    private String berthDirection;

    /**
     * 船舶首部固定缆绳的缆桩编号
     */
    @TableField("HEAD_BOLLARD_ID")
    private String headBollardId;

    /**
     * 船舶尾部固定缆绳的缆桩编号
     */
    @TableField("TAIL_BOLLARD_ID")
    private String tailBollardId;

    /**
     * 船舶注册名称
     */
    @TableField("SHIP_NAME")
    private String shipName;

    /**
     * 船舶货舱总数
     */
    @TableField("CABIN_NUM")
    private Short cabinNum;

    /**
     * 船舶载货总重量（单位：吨）
     */
    @TableField("CARRY_WEIGHT")
    private BigDecimal carryWeight;

    /**
     * 船舶总长度（单位：米）
     */
    @TableField("SHIP_LENGTH")
    private BigDecimal shipLength;

    /**
     * 船舶吃水深度（单位：米）
     */
    @TableField("DRAFT")
    private BigDecimal draft;

    /**
     * 装载货物名称
     */
    @TableField("CARGO_NAME")
    private String cargoName;

    /**
     * 货物所有权归属方
     */
    @TableField("CARGO_OWNER")
    private String cargoOwner;

    /**
     * 船舶代理公司名称
     */
    @TableField("AGENT")
    private String agent;

    /**
     * 作业内容
     */
    @TableField("WORK_CONTENT")
    private String workContent;

    /**
     * 作业时间
     */
    @TableField("PLAN_TIME")
    private LocalDateTime planTime;

    /**
     * 算法状态
     */
    @TableField("ALGORITHM_STATE")
    private Short algorithmState;

    /**
     * 计划靠泊时间（精确到秒）
     */
    @TableField("PLAN_BERTH_TIME")
    private LocalDateTime planBerthTime;

    /**
     * 实际靠泊完成时间
     */
    @TableField("BERTH_TIME")
    private LocalDateTime berthTime;

    /**
     * 计划作业完成时间
     */
    @TableField("PLAN_FINISH_TIME")
    private LocalDateTime planFinishTime;

    /**
     * 已完成货物吞吐量（单位：吨）
     */
    @TableField("THROUGHPUT_WEIGHT")
    private BigDecimal throughputWeight;

    /**
     * 剩余待作业货物量（单位：吨）
     */
    @TableField("REMAINING_WEIGHT")
    private BigDecimal remainingWeight;


    /**
     * 作业要求
     */
    @TableField(exist = false,value = "WORK_REQUIRE")
    @Schema(title ="作业要求")
    private String workRequire;
}
