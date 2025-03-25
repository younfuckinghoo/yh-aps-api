package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 堆场与货种关联关系表
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_YARD_CARGO_REL")
public class AlgYardCargoRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的堆场编号
     */
    @TableField("YARD_NO")
    private String yardNo;

    /**
     * 允许堆放的货种大类（如：煤炭/铁矿）
     */
    @TableField("CARGO_TYPE_NAME")
    private String cargoTypeName;
}
