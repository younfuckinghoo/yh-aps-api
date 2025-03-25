package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 货主简称对照表
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_CARGO_OWNER")
public class AlgCargoOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     * 货主
     */
    @TableField("CARGO_OWNER")
    private String cargoOwner;

    /**
     * 简称
     */
    @TableField("OWNER_SHORT_NAME")
    private String ownerShortName;

    /**
     * 通用疏港方式-参考
     */
    @TableField("EVACUATION_METHOD")
    private String evacuationMethod;


}
