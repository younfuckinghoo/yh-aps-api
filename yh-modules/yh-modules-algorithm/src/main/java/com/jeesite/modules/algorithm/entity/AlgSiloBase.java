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
 * 筒仓基础数据表，记录筒仓静态属性
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_SILO_BASE")
public class AlgSiloBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 筒仓唯一标识符
     */
    @TableId("ID")
    private String id;

    /**
     * 筒仓编号（如：SILO-101）
     */
    @TableField("SILO_NO")
    private String siloNo;

    /**
     * 所属运营期（如：三期工程）
     */
    @TableField("BELONG_PERIOD")
    private String belongPeriod;

    /**
     * 筒仓设计总容量（单位：吨）
     */
    @TableField("TOTAL_CAPACITY")
    private BigDecimal totalCapacity;

    /**
     * 当前实时存储量（单位：吨）
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
