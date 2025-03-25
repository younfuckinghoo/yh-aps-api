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
 * 缆柱表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:17
 */
@Data
@TableName("ALG_BOLLARD_INFO")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "缆柱表")
public class BizBollardInfo extends BaseEntity<BizBollardInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="id")
    private String id;

    /**
     * 泊位ID
     */
    @JhyjField(eq = true)
    @Schema(description ="泊位ID")
    private String berthId;

    /**
     * 泊位编号
     */
    @JhyjField(eq = true)
    @Schema(description ="泊位编号")
    private String berthNo;

    /**
     * 榄桩号
     */
    @JhyjField(eq = true)
    @Schema(description ="榄桩号")
    private Integer bollardNo;

    /**
     * 榄桩名称
     */
    @JhyjField(eq = true)
    @Schema(description ="榄桩名称")
    private String bollardName;

    /**
     * 距上一缆柱距离
     */
    @JhyjField(eq = true)
    @Schema(description ="距上一缆柱距离")
    private Integer lastBollardDistance;

    /**
     * 被占用时间
     */
    @JhyjField(eq = true)
    @Schema(description ="被占用时间")
    private Date occupyUntil;

    /**
     * 创建日期
     */
    @JhyjField(eq = true)
    @Schema(description ="创建日期")
    private Date createDate;

    /**
     * 修改日期
     */
    @JhyjField(eq = true)
    @Schema(description ="修改日期")
    private Date reviseDate;

    /**
     * 是否可用
     */
    @JhyjField(eq = true)
    @Schema(description ="是否可用")
    private Integer available;

    /**
     * 排序
     */
    @JhyjField(eq = true)
    @Schema(description ="排序")
    private Integer sort;


////仅供查询字段//////////////////////////////////////////////////

    /**
     * 缆桩名称(模糊查询)
     */
    @JhyjField(like = true, tableField = false, fieldName = "BOLLARD_NAME")
    @TableField(exist = false)
    @Schema(title ="缆桩名称(模糊查询)")
    private String bollardNameLike;

    /**
     * ID(IN)
     */
    @JhyjField(in = true, tableField = false, fieldName = "ID")
    @TableField(exist = false)
    @Schema(title ="ID(IN)")
    private String idIn;


////仅供展示字段//////////////////////////////////////////////////

    /**
     * 泊位明层
     */
    @TableField(exist = false)
    @Schema(title ="泊位名称")
    private String berthName;

}
