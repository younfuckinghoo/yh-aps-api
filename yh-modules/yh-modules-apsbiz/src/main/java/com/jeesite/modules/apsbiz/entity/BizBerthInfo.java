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

import java.math.BigDecimal;
import java.util.Date;


/**
 * 泊位表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:16
 */
@Data
@TableName("ALG_BERTH_INFO")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "泊位表")
public class BizBerthInfo extends BaseEntity<BizBerthInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="id")
    private String id;

    /**
     * 泊位编号
     */
    @JhyjField(eq = true)
    @Schema(description ="泊位编号")
    private String berthNo;

    /**
     * 泊位名称
     */
    @JhyjField(eq = true)
    @Schema(description ="泊位名称")
    private String berthName;

    /**
     * 泊位长度
     */
    @JhyjField(eq = true)
    @Schema(description ="泊位长度")
    private BigDecimal berthLength;

    /**
     * 吃水限制（米）
     */
    @JhyjField(eq = true)
    @Schema(description ="吃水限制（米）")
    private BigDecimal shipDraftLimit;

    /**
     * 吨位限制（万吨）
     */
    @JhyjField(eq = true)
    @Schema(description ="吨位限制（万吨）")
    private BigDecimal shipWeightLimit;

    /**
     * 船长限制（米）
     */
    @JhyjField(eq = true)
    @Schema(description ="船长限制（米）")
    private BigDecimal shipLengthLimit;

    /**
     * 是否可用
     */
    @JhyjField(eq = true)
    @Schema(description ="是否可用")
    private Integer available;

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

    /**
     * 是否借用
     */
    @JhyjField(eq = true)
    @Schema(description ="是否借用")
    private Integer isLend;

    /**
     * 是否被借用
     */
    @JhyjField(eq = true)
    @Schema(description ="是否被借用")
    private Integer isBeLend;

    /**
     * 流程装船
     */
    @JhyjField(eq = true)
    @Schema(description ="流程装船")
    private Integer flowLoad;

    /**
     * 流程卸船
     */
    @JhyjField(eq = true)
    @Schema(description ="流程卸船")
    private Integer flowUnload;

    /**
     * 搬到装船
     */
    @JhyjField(eq = true)
    @Schema(description ="搬到装船")
    private Integer moveLoad;

    /**
     * 搬到卸船
     */
    @JhyjField(eq = true)
    @Schema(description ="搬到卸船")
    private Integer moveUnload;

    /**
     * 排序
     */
    @JhyjField(eq = true)
    @Schema(description ="排序")
    private Integer sort;







////仅供查询字段//////////////////////////////////////////////////

    /**
     * 泊位编号(IN)
     */
    @JhyjField(in = true, tableField = false, fieldName = "BERTH_NO")
    @TableField(exist = false)
    @Schema(title ="泊位编号(IN)")
    private String berthNoIn;

    /**
     * 泊位名称(IN)
     */
    @JhyjField(in = true, tableField = false, fieldName = "BERTH_NAME")
    @TableField(exist = false)
    @Schema(title ="泊位名称(IN)")
    private String berthNameIn;

    /**
     * 泊位编号(模糊查询)
     */
    @JhyjField(like = true, tableField = false, fieldName = "BERTH_NAME")
    @TableField(exist = false)
    @Schema(title ="泊位名称(模糊查询)")
    private String berthNameLike;


////仅供展示字段//////////////////////////////////////////////////

}
