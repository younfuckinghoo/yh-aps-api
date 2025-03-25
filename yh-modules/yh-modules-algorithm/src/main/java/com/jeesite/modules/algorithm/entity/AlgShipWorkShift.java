package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jeesite.modules.algorithm.enums.DayNightEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 作业班次明细表，记录每个班次的具体作业计划
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_SHIP_WORK_SHIFT")
public class AlgShipWorkShift implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班次唯一标识符
     */
    @TableId("ID")
    private String id;

    /**
     * 关联的船舶作业计划ID
     */
    @TableField("SHIP_WORK_PLAN_ID")
    private String shipWorkPlanId;

    /**
     * 班次类型（1=夜班，2=昼班）
     * {@link DayNightEnum}
     */
    @TableField("SHIFT_TYPE")
    private Short shiftType;

    /**
     * 本班次计划作业量（单位：吨）
     */
    @TableField("WORKLOAD")
    private BigDecimal workload;

    /**
     * 班次开始时间（精确到秒）
     */
    @TableField("START_TIME")
    private LocalDateTime startTime;

    /**
     * 班次结束时间（精确到秒）
     */
    @TableField("END_TIME")
    private LocalDateTime endTime;

    /**
     * 实际作业时长（单位：小时）
     */
    @TableField("WORK_HOURS")
    private BigDecimal workHours;

    /**
     * 机械综合效率
     */
    @TableField("MECH_EFFICIENCY")
    private BigDecimal mechEfficiency;

    /**
     * 人力配置详情（如：装卸工10人）
     */
    @TableField("MANPOWER_ARRANGE")
    private String manpowerArrange;
}
