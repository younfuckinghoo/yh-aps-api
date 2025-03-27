package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 船舶作业筒仓分配表，记录船舶作业占用的筒仓
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_SHIP_SILO_ARRANGE")
public class AlgShipSiloArrange implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识符
     */
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 关联的航次号
     */
    @TableField("VOYAGE_NO")
    private String voyageNo;

    /**
     * 分配的筒仓编号
     */
    @TableField("SILO_NO")
    private String siloNo;
}
