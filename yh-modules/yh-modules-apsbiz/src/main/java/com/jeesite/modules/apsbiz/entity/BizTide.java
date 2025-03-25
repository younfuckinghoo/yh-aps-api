package com.jeesite.modules.apsbiz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jeesite.common.annotation.JhyjField;
import com.jeesite.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 潮汐表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:22
 */
@Data
@TableName("ALG_TIDE")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "潮汐表")
public class BizTide extends BaseEntity<BizTide> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="ID")
    private String id;

    /**
     * 日期
     */
    @JhyjField(eq = true)
    @Schema(description ="日期")
    private Date tideDate;

    /**
     * tideHeight1
     */
    @JhyjField(eq = true)
    @Schema(description ="tideHeight1")
    private Integer tideHeight1;

    /**
     * tideHeight2
     */
    @JhyjField(eq = true)
    @Schema(description ="tideHeight2")
    private Integer tideHeight2;

    /**
     * tideHeight3
     */
    @JhyjField(eq = true)
    @Schema(description ="tideHeight3")
    private Integer tideHeight3;

    /**
     * tideHeight4
     */
    @JhyjField(eq = true)
    @Schema(description ="tideHeight4")
    private Integer tideHeight4;

    /**
     * tideTime1
     */
    @JhyjField(eq = true)
    @Schema(description ="tideTime1")
    private Date tideTime1;

    /**
     * tideTime2
     */
    @JhyjField(eq = true)
    @Schema(description ="tideTime2")
    private Date tideTime2;

    /**
     * tideTime3
     */
    @JhyjField(eq = true)
    @Schema(description ="tideTime3")
    private Date tideTime3;

    /**
     * tideTime4
     */
    @JhyjField(eq = true)
    @Schema(description ="tideTime4")
    private Date tideTime4;

    /**
     * h0
     */
    @JhyjField(eq = true)
    @Schema(description ="h0")
    private Integer h0;

    /**
     * h1
     */
    @JhyjField(eq = true)
    @Schema(description ="h1")
    private Integer h1;

    /**
     * h2
     */
    @JhyjField(eq = true)
    @Schema(description ="h2")
    private Integer h2;

    /**
     * h3
     */
    @JhyjField(eq = true)
    @Schema(description ="h3")
    private Integer h3;

    /**
     * h4
     */
    @JhyjField(eq = true)
    @Schema(description ="h4")
    private Integer h4;

    /**
     * h5
     */
    @JhyjField(eq = true)
    @Schema(description ="h5")
    private Integer h5;

    /**
     * h6
     */
    @JhyjField(eq = true)
    @Schema(description ="h6")
    private Integer h6;

    /**
     * h7
     */
    @JhyjField(eq = true)
    @Schema(description ="h7")
    private Integer h7;

    /**
     * h8
     */
    @JhyjField(eq = true)
    @Schema(description ="h8")
    private Integer h8;

    /**
     * h9
     */
    @JhyjField(eq = true)
    @Schema(description ="h9")
    private Integer h9;

    /**
     * h10
     */
    @JhyjField(eq = true)
    @Schema(description ="h10")
    private Integer h10;

    /**
     * h11
     */
    @JhyjField(eq = true)
    @Schema(description ="h11")
    private Integer h11;

    /**
     * h12
     */
    @JhyjField(eq = true)
    @Schema(description ="h12")
    private Integer h12;

    /**
     * h13
     */
    @JhyjField(eq = true)
    @Schema(description ="h13")
    private Integer h13;

    /**
     * h14
     */
    @JhyjField(eq = true)
    @Schema(description ="h14")
    private Integer h14;

    /**
     * h15
     */
    @JhyjField(eq = true)
    @Schema(description ="h15")
    private Integer h15;

    /**
     * h16
     */
    @JhyjField(eq = true)
    @Schema(description ="h16")
    private Integer h16;

    /**
     * h17
     */
    @JhyjField(eq = true)
    @Schema(description ="h17")
    private Integer h17;

    /**
     * h18
     */
    @JhyjField(eq = true)
    @Schema(description ="h18")
    private Integer h18;

    /**
     * h19
     */
    @JhyjField(eq = true)
    @Schema(description ="h19")
    private Integer h19;

    /**
     * h20
     */
    @JhyjField(eq = true)
    @Schema(description ="h20")
    private Integer h20;

    /**
     * h21
     */
    @JhyjField(eq = true)
    @Schema(description ="h21")
    private Integer h21;

    /**
     * h22
     */
    @JhyjField(eq = true)
    @Schema(description ="h22")
    private Integer h22;

    /**
     * h23
     */
    @JhyjField(eq = true)
    @Schema(description ="h23")
    private Integer h23;

    /**
     * createTime
     */
    @JhyjField(eq = true)
    @Schema(description ="createTime")
    private Date createTime;

    /**
     * reviseTime
     */
    @JhyjField(eq = true)
    @Schema(description ="reviseTime")
    private Date reviseTime;


////仅供查询字段//////////////////////////////////////////////////


////仅供展示字段//////////////////////////////////////////////////

}
