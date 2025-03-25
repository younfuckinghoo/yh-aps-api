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
 * 堆场实时数据表，记录动态库存变化
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_YARD_REALTIME")
public class AlgYardRealtime implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实时记录唯一ID
     */
    @TableId("ID")
    private String id;

    /**
     * 关联的堆场编号
     */
    @TableField("YARD_NO")
    private String yardNo;

    /**
     * 数据统计时间点（精确到秒）
     */
    @TableField("STAT_DATE")
    private LocalDateTime statDate;

    /**
     * 当前堆存货种类型
     */
    @TableField("CARGO_TYPE")
    private String cargoType;

    /**
     * 货物原产地
     */
    @TableField("CARGO_ORIGIN")
    private String cargoOrigin;

    /**
     * 货主名称
     */
    @TableField("CARGO_OWNER")
    private String cargoOwner;

    /**
     * 堆场总容量（单位：吨）
     */
    @TableField("STORAGE_CAPACITY")
    private BigDecimal storageCapacity;

    /**
     * 所属区域（如：三期北区）
     */
    @TableField("BELONG_AREA")
    private String belongArea;

    /**
     * 昨日库存数量（单位：吨）
     */
    @TableField("YESTERDAY_STOCK")
    private BigDecimal yesterdayStock;

    /**
     * 最近一次入库时间
     */
    @TableField("INBOUND_TIME")
    private LocalDateTime inboundTime;

    /**
     * 上昼夜入库量（单位：吨）
     */
    @TableField("YESTERDAY_INBOUND")
    private BigDecimal yesterdayInbound;

    /**
     * 上昼夜倒运入库量（单位：吨）
     */
    @TableField("YESTERDAY_TRANSFER_IN")
    private BigDecimal yesterdayTransferIn;

    /**
     * 当前盘点数量（单位：吨）
     */
    @TableField("INVENTORY")
    private BigDecimal inventory;

    /**
     * 上昼夜出库量（单位：吨）
     */
    @TableField("YESTERDAY_OUTBOUND")
    private BigDecimal yesterdayOutbound;

    /**
     * 上昼夜倒运出库量（单位：吨）
     */
    @TableField("YESTERDAY_TRANSFER_OUT")
    private BigDecimal yesterdayTransferOut;

    /**
     * 当前实际库存量（单位：吨）
     */
    @TableField("CURRENT_STOCK")
    private BigDecimal currentStock;
}
