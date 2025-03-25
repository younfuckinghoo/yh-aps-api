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
 * 船舶信息表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:16
 */
@Data
@TableName("ALG_SHIP_INFO")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "泊位表")
public class BizShipInfo extends BaseEntity<BizShipInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description ="id")
    private String id;

//    /**
//     * 来港航次
//     */
//    @JhyjField(eq = true)
//    @Schema(description ="来港航次")
//    private String voyageNo;

    /**
     * 船舶代码
     */
    @JhyjField(eq = true)
    @Schema(description ="船舶代码")
    private String shipCode;

    /**
     * 中文船名
     */
    @JhyjField(eq = true)
    @Schema(description ="中文船名")
    private String shipNameCn;

    /**
     * 英文船名
     */
    @JhyjField(eq = true)
    @Schema(description ="英文船名")
    private String shipNameEn;

    /**
     * 船舶呼号
     */
    @JhyjField(eq = true)
    @Schema(description ="船舶呼号")
    private String callNo;

    /**
     * 船舶公司
     */
    @JhyjField(eq = true)
    @Schema(description ="船舶公司")
    private String shipCompany;

    /**
     * 船舶经营人
     */
    @JhyjField(eq = true)
    @Schema(description ="船舶经营人")
    private String shipOperator;

    /**
     * 船舶所有人
     */
    @JhyjField(eq = true)
    @Schema(description ="船舶所有人")
    private String shipOwner;

    /**
     * 船长
     */
    @JhyjField(eq = true)
    @Schema(description ="船长")
    private Float shipLength;

    /**
     * 船宽
     */
    @JhyjField(eq = true)
    @Schema(description ="船宽")
    private Float shipWidth;

    /**
     * 国籍
     */
    @JhyjField(eq = true)
    @Schema(description ="国籍")
    private String nationality;

    /**
     * 型深
     */
    @JhyjField(eq = true)
    @Schema(description ="型深")
    private Float depth;

    /**
     * 总吨
     */
    @JhyjField(eq = true)
    @Schema(description ="总吨")
    private Float totalTon;

    /**
     * 净吨
     */
    @JhyjField(eq = true)
    @Schema(description ="净吨")
    private Float netTon;

    /**
     * 载重吨
     */
    @JhyjField(eq = true)
    @Schema(description ="载重吨")
    private Float weightTon;

    /**
     * 空载吃水
     */
    @JhyjField(eq = true)
    @Schema(description ="空载吃水")
    private Float emptyDraft;

    /**
     * 满载吃水
     */
    @JhyjField(eq = true)
    @Schema(description ="满载吃水")
    private Float fullDraft;

    /**
     * 舱数
     */
    @JhyjField(eq = true)
    @Schema(description ="舱数")
    private Integer cabinNum;

    /**
     * 舱容
     */
    @JhyjField(eq = true)
    @Schema(description ="舱容")
    private String cabinCapacity;

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
     * 船舶代码(IN)
     */
    @JhyjField(in = true, tableField = false, fieldName = "SHIP_CODE")
    @TableField(exist = false)
    @Schema(title ="船舶代码(IN)")
    private String shipCodeIn;


////仅供展示字段//////////////////////////////////////////////////

}
