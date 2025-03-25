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
 * 皮带流程基础数据表，记录皮带运输系统配置
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_BELT_PROCESS")
public class AlgBeltProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程配置唯一ID
     */
    @TableId("ID")
    private String id;

    /**
     * 皮带流程编号（如：BELT-01）
     */
    @TableField("PROCESS_NO")
    private String processNo;

    /**
     * 皮带流程名称（如：煤炭运输线）
     */
    @TableField("PROCESS_NAME")
    private String processName;

    /**
     * 设计流量（单位：吨/小时）
     */
    @TableField("FLOW_RATE")
    private BigDecimal flowRate;

    /**
     * 流程属性（如：双向运输/单向运输）
     */
    @TableField("PROCESS_ATTR")
    private String processAttr;

    /**
     * 流程头部设备编号
     */
    @TableField("HEAD_DEVICE")
    private String headDevice;

    /**
     * 头部设备连接的岸基设备
     */
    @TableField("HEAD_LINK_SHORE")
    private String headLinkShore;

    /**
     * 头部设备关联的筒仓编号
     */
    @TableField("HEAD_LINK_SILO")
    private String headLinkSilo;

    /**
     * 流程中间设备编号
     */
    @TableField("MID_DEVICE")
    private String midDevice;

    /**
     * 流程尾部设备编号
     */
    @TableField("TAIL_DEVICE")
    private String tailDevice;

    /**
     * 尾部设备关联的筒仓编号
     */
    @TableField("TAIL_LINK_SILO")
    private String tailLinkSilo;
}
