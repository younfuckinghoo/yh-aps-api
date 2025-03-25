package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 堆场基础数据表，记录堆场静态属性
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_YARD")
public class AlgYard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 堆场唯一标识符
     */
    @TableId("ID")
    private String id;

    /**
     * 堆场名称（如：三期散货堆场）
     */
    @TableField("YARD_NAME")
    private String yardName;

    /**
     * 堆场编号（如：YARD-03）
     */
    @TableField("YARD_NO")
    private String yardNo;
    /**
     * 库场类型 1库房 2堆场
     * {@link com.jeesite.modules.algorithm.enums.YardTypeEnum}
     */
    @TableField("YARD_TYPE")
    private String yardType;

    /**
     * 所属区域（如：北区/南区）
     */
    @TableField("BELONG_ZONE")
    private String belongZone;

    /**
     * 堆场设计总容量（单位：吨）
     */
    @TableField("TOTAL_CAPACITY")
    private BigDecimal totalCapacity;

    /**
     * 当前堆存货量（单位：吨）
     */
    @TableField("REAL_TIME_CAPACITY")
    private BigDecimal realTimeCapacity;

    /**
     * 已占用容量（单位：吨）
     */
    @TableField("OCCUPIED_CAPACITY")
    private BigDecimal occupiedCapacity;

    /**
     * 剩余可用容量（单位：吨）
     */
    @TableField("AVAILABLE_CAPACITY")
    private BigDecimal availableCapacity;
}
