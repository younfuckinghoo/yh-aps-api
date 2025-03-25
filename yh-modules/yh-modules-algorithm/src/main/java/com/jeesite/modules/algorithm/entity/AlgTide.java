package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jeesite.modules.algorithm.handler.LocalDateTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * <p>
 * 潮汐表
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value="ALG_TIDE",autoResultMap = true)
public class AlgTide implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("ID")
    private String id;

    /**
     * 日期
     */
    @TableField(value="TIDE_DATE",jdbcType = JdbcType.DATE,typeHandler = LocalDateTypeHandler.class )
    private LocalDate tideDate;

    @TableField("TIDE_HEIGHT1")
    private BigDecimal tideHeight1;

    @TableField("TIDE_HEIGHT2")
    private BigDecimal tideHeight2;

    @TableField("TIDE_HEIGHT3")
    private BigDecimal tideHeight3;

    @TableField("TIDE_HEIGHT4")
    private BigDecimal tideHeight4;

    @TableField("TIDE_TIME1")
    private LocalTime tideTime1;

    @TableField("TIDE_TIME2")
    private LocalTime tideTime2;

    @TableField("TIDE_TIME3")
    private LocalTime tideTime3;

    @TableField("TIDE_TIME4")
    private LocalTime tideTime4;

    @TableField("H0")
    private BigDecimal h0;

    @TableField("H1")
    private BigDecimal h1;

    @TableField("H2")
    private BigDecimal h2;

    @TableField("H3")
    private BigDecimal h3;

    @TableField("H4")
    private BigDecimal h4;

    @TableField("H5")
    private BigDecimal h5;

    @TableField("H6")
    private BigDecimal h6;

    @TableField("H7")
    private BigDecimal h7;

    @TableField("H8")
    private BigDecimal h8;

    @TableField("H9")
    private BigDecimal h9;

    @TableField("H10")
    private BigDecimal h10;

    @TableField("H11")
    private BigDecimal h11;

    @TableField("H12")
    private BigDecimal h12;

    @TableField("H13")
    private BigDecimal h13;

    @TableField("H14")
    private BigDecimal h14;

    @TableField("H15")
    private BigDecimal h15;

    @TableField("H16")
    private BigDecimal h16;

    @TableField("H17")
    private BigDecimal h17;

    @TableField("H18")
    private BigDecimal h18;

    @TableField("H19")
    private BigDecimal h19;

    @TableField("H20")
    private BigDecimal h20;

    @TableField("H21")
    private BigDecimal h21;

    @TableField("H22")
    private BigDecimal h22;

    @TableField("H23")
    private BigDecimal h23;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("REVISE_TIME")
    private LocalDateTime reviseTime;


}
