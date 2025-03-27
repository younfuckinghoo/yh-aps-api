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
 * 泊位岸机数据
 *
 * @author zhudi
 * @date 2024-12-08 21:19:22
 */
@Data
@TableName("ALG_SHORE_MACHINE")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "泊位岸机数据")
public class BizShoreMachine extends BaseEntity<BizShoreMachine> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="id")
    private String id;

    /**
     *   岸机类型名称 
     */
    @JhyjField(eq = true)
    @Schema(description ="  岸机类型名称 ")
    private String machineName;

    /**
     *   岸机类型编码 
     */
    @JhyjField(eq = true)
    @Schema(description ="  岸机类型编码 ")
    private String machineTypeCode;

    /**
     *   岸机编号
     */
    @JhyjField(eq = true)
    @Schema(description ="  岸机编号")
    private String machineNo;


    /**
     * 被占用时间（截止时间）
     */
    @JhyjField(eq = true)
    @Schema(description ="被占用时间（截止时间）")
    private Date occupiedUntil;

    /**
     * 设备状态
     */
    @JhyjField(eq = true)
    @Schema(description ="设备状态")
    private Integer status;


////仅供查询字段//////////////////////////////////////////////////

    /**
     * 岸机名称(模糊查询)
     */
    @JhyjField(like = true, tableField = false, fieldName = "MACHINE_NAME")
    @TableField(exist = false)
    @Schema(title ="岸机名称(模糊查询)")
    private String machineNameLike;


////仅供展示字段//////////////////////////////////////////////////

}
