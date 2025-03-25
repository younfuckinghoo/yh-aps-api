package com.jeesite.modules.apsbiz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jeesite.common.annotation.JhyjField;
import com.jeesite.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 货主简称对照表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:17
 */
@Data
@TableName("ALG_CARGO_OWNER")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "货主简称对照表")
public class BizCargoOwner extends BaseEntity<BizCargoOwner> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="id")
    private String id;

    /**
     * 货主
     */
    @JhyjField(eq = true)
    @Schema(description ="货主")
    private String cargoOwner;

    /**
     * 简称
     */
    @JhyjField(eq = true)
    @Schema(description ="简称")
    private String ownerShortName;

    /**
     * 通用疏港方式-参考
     */
    @JhyjField(eq = true)
    @Schema(description ="通用疏港方式-参考")
    private String evacuationMethod;


////仅供查询字段//////////////////////////////////////////////////


////仅供展示字段//////////////////////////////////////////////////

}
