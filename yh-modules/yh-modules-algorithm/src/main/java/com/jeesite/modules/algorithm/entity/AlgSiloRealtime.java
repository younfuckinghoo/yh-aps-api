package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 筒仓实时数据表，记录动态库存变化
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_SILO_REALTIME")
public class AlgSiloRealtime implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实时记录唯一ID
     */
    @TableId("ID")
    private String id;

    /**
     * 关联的筒仓编号
     */
    @TableField("SILO_NO")
    private String siloNo;

    /**
     * 数据统计时间点（精确到秒）
     */
    @TableField("STAT_DATE")
    private LocalDateTime statDate;

    /**
     * 当前存储货物名称
     */
    @TableField("CARGO_NAME")
    private String cargoName;

    /**
     * 货物原产地（如：澳大利亚）
     */
    @TableField("CARGO_ORIGIN")
    private String cargoOrigin;

    /**
     * 货物所有权归属方
     */
    @TableField("CARGO_OWNER")
    private String cargoOwner;

    /**
     * 筒仓总容量（单位：吨）
     */
    @TableField("TOTAL_CAPACITY")
    private BigDecimal totalCapacity;

    /**
     * 交班时记录的货物数量
     */
    @TableField("HANDOVER_AMOUNT")
    private BigDecimal handoverAmount;

    /**
     * 接班时接收的货物数量
     */
    @TableField("TAKEOVER_AMOUNT")
    private BigDecimal takeoverAmount;

    /**
     * 本班次进仓货物量（单位：吨）
     */
    @TableField("INBOUND_QTY")
    private BigDecimal inboundQty;

    /**
     * 本班次出仓货物量（单位：吨）
     */
    @TableField("OUTBOUND_QTY")
    private BigDecimal outboundQty;

    /**
     * 清仓时发现的货差（单位：吨）
     */
    @TableField("CLEARANCE_DIFF")
    private BigDecimal clearanceDiff;
}
