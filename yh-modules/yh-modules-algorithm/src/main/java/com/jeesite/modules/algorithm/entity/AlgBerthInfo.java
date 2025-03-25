package com.jeesite.modules.algorithm.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 泊位表
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_BERTH_INFO")
public class AlgBerthInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     * 泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;

    /**
     * 泊位名称
     */
    @TableField("BERTH_NAME")
    private String berthName;

    /**
     * 泊位长度
     */
    @TableField("BERTH_LENGTH")
    private Integer berthLength;

    /**
     * 吃水限制（米）
     */
    @TableField("SHIP_DRAFT_LIMIT")
    private BigDecimal shipDraftLimit;

    /**
     * 吨位限制（万吨）
     */
    @TableField("SHIP_WEIGHT_LIMIT")
    private BigDecimal shipWeightLimit;

    /**
     * 船长限制（米）
     */
    @TableField("SHIP_LENGTH_LIMIT")
    private BigDecimal shipLengthLimit;

    /**
     * 是否可用
     */
    @TableField("AVAILABLE")
    private Integer available;

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

    /**
     * 是否借用
     */
    @TableField("IS_LEND")
    private Integer isLend;

    /**
     * 是否被借用
     */
    @TableField("IS_BE_LEND")
    private Integer isBeLend;


    /**
     * BERTH_DEPTH 港池水深
     */
    @TableField("BERTH_DEPTH")
    private BigDecimal berthDepth;
}
