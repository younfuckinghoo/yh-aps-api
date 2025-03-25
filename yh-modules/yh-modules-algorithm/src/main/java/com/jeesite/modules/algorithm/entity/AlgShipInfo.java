package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 船舶基础信息表
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_SHIP_INFO")
public class AlgShipInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     * 来港航次
     */
    @TableField("VOYAGE_NO")
    private String voyageNo;

    /**
     * 船舶代码
     */
    @TableField("SHIP_CODE")
    private String shipCode;

    /**
     * 中文船名
     */
    @TableField("SHIP_NAME_CN")
    private String shipNameCn;

    /**
     * 英文船名
     */
    @TableField("SHIP_NAME_EN")
    private String shipNameEn;

    /**
     * 船舶呼号
     */
    @TableField("CALL_NO")
    private String callNo;

    /**
     * 船舶公司
     */
    @TableField("SHIP_COMPANY")
    private String shipCompany;

    /**
     * 船舶经营人
     */
    @TableField("SHIP_OPERATOR")
    private String shipOperator;

    /**
     * 船舶所有人
     */
    @TableField("SHIP_OWNER")
    private String shipOwner;

    /**
     * 船长
     */
    @TableField("SHIP_LENGTH")
    private  BigDecimal shipLength;

    /**
     * 船宽
     */
    @TableField("SHIP_WIDTH")
    private  BigDecimal shipWidth;

    /**
     * 国籍
     */
    @TableField("NATIONALITY")
    private String nationality;

    /**
     * 型深
     */
    @TableField("DEPTH")
    private  BigDecimal depth;

    /**
     * 总吨
     */
    @TableField("TOTAL_TON")
    private  BigDecimal totalTon;

    /**
     * 净吨
     */
    @TableField("NET_TON")
    private  BigDecimal netTon;

    /**
     * 载重吨
     */
    @TableField("WEIGHT_TON")
    private  BigDecimal weightTon;

    /**
     * 空载吃水
     */
    @TableField("EMPTY_DRAFT")
    private  BigDecimal emptyDraft;

    /**
     * 满载吃水
     */
    @TableField("FULL_DRAFT")
    private BigDecimal fullDraft;

    /**
     * 舱数
     */
    @TableField("CABIN_NUM")
    private  Integer cabinNum;

    /**
     * 舱容
     */
    @TableField("CABIN_CAPACITY")
    private String cabinCapacity;

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
