package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 货类表
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_CARGO_TYPE")
public class AlgCargoType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     * 大类
     */
    @TableField("TYPE_NAME")
    private String typeName;

    /**
     * 货物代码
     */
    @TableField("CARGO_CODE")
    private String cargoCode;

    /**
     * 货物名称
     */
    @TableField("CARGO_NAME")
    private String cargoName;

    /**
     * 货物形态（1件杂货；2干散货；3液体散货
     */
    @TableField("CARGO_FORM")
    private Integer cargoForm;

    /**
     * 创建人
     */
    @TableField("CREATOR")
    private String creator;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField("REVISE_DATE")
    private LocalDateTime reviseDate;


}
