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
 * 船舶作业堆场分配表，记录船舶与堆场的关联
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_SHIP_YARD_ARRANGE")
public class AlgShipYardArrange implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识符
     */
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private String id;


    /**
     * id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private String id;
    /**
     * 关联的航次号
     */
    @TableField("VOYAGE_NO")
    private String voyageNo;

    /**
     * 分配的堆场名称
     */
    @TableField("YARD_NAME")
    private String yardName;

    /**
     * 分配的堆场编号
     */
    @TableField("YARD_NO")
    private String yardNo;
}
