package com.jeesite.modules.apsbiz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jeesite.common.annotation.JhyjField;
import com.jeesite.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 货类表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:17
 */
@Data
@TableName("ALG_CARGO_TYPE")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "货类表")
public class BizCargoType extends BaseEntity<BizCargoType> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="id")
    private String id;

    /**
     * 大类
     */
    @JhyjField(eq = true)
    @Schema(description ="大类")
    private String typeName;

    /**
     * 货物代码
     */
    @JhyjField(eq = true)
    @Schema(description ="货物代码")
    private String cargoCode;

    /**
     * 货物名称
     */
    @JhyjField(eq = true)
    @Schema(description ="货物名称")
    private String cargoName;

    /**
     * 货物形态（1件杂货；2干散货；3液体散货
     */
    @JhyjField(eq = true)
    @Schema(description ="货物形态（1件杂货；2干散货；3液体散货")
    private Integer cargoForm;

    /**
     * 创建人
     */
    @JhyjField(eq = true)
    @Schema(description ="创建人")
    private String creator;

    /**
     * 创建时间
     */
    @JhyjField(eq = true)
    @Schema(description ="创建时间")
    private Date createDate;

    /**
     * 修改时间
     */
    @JhyjField(eq = true)
    @Schema(description ="修改时间")
    private Date reviseDate;


////仅供查询字段//////////////////////////////////////////////////

    /**
     * 货物名称(模糊查询)
     */
    @JhyjField(like = true, tableField = false, fieldName = "CARGO_NAME")
    @TableField(exist = false)
    @Schema(title ="货物名称(模糊查询)")
    private String cargoNameLike;


////仅供展示字段//////////////////////////////////////////////////

}
