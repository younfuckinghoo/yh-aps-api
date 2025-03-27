package com.jeesite.modules.apsbiz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jeesite.common.annotation.JhyjField;
import com.jeesite.common.base.BaseEntity;
import com.jeesite.modules.algorithm.enums.CargoWhereaboutsRequirementEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 预报船舶表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Data
@TableName("ALG_SHIP_FORECAST")
@EqualsAndHashCode(callSuper = true)
@Schema(title = "预报船舶表")
public class BizShipForecast extends BaseEntity<BizShipForecast> {

    private static final long serialVersionUID = 1L;

    /**
     * ID（申报记录唯一标识）
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JhyjField(eq = true)
    @Schema(description = "ID（申报记录唯一标识）")
    private String id;

    /**
     * 来港航次
     */
    @JhyjField(eq = true)
    @Schema(description ="来港航次")
    private String voyageNo;

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
     * 作业公司
     */
    @JhyjField(eq = true)
    @Schema(description ="作业公司")
    private String workingCompany;

    /**
     * 代理公司
     */
    @JhyjField(eq = true)
    @Schema(description ="代理公司")
    private String agentCompany;

    /**
     * 进口吃水
     */
    @JhyjField(eq = true)
    @Schema(description ="进口吃水")
    private Integer draftIn;

    /**
     * 出口吃水
     */
    @JhyjField(eq = true)
    @Schema(description ="出口吃水")
    private Integer draftOut;

    /**
     * 货物小类代码
     */
    @JhyjField(eq = true)
    @Schema(description ="货物小类代码")
    private String cargoSubTypeCode;

    /**
     * 装卸货种
     */
    @JhyjField(eq = true)
    @Schema(description ="装卸货种")
    private String cargoTypeName;

    /**
     * 贸别(1内贸,0外贸)
     */
    @JhyjField(eq = true)
    @Schema(description ="贸别(1内贸,0外贸)")
    private Integer tradeType;

    /**
     * 装卸类别(1装,2卸,3装卸)
     */
    @JhyjField(eq = true)
    @Schema(description ="装卸类别(1装,2卸,3装卸)")
    private Integer loadUnload;

    /**
     * 装计划(吨)	
     */
    @JhyjField(eq = true)
    @Schema(description ="装计划(吨)	")
    private Integer loadPlanWeight;

    /**
     * 卸计划(吨)
     */
    @JhyjField(eq = true)
    @Schema(description ="卸计划(吨)")
    private Integer unloadPlanWeight;

    /**
     * 配载(吨)
     */
    @JhyjField(eq = true)
    @Schema(description ="配载(吨)")
    private Integer carryWeight;

    /**
     * 预到时间
     */
    @JhyjField(eq = true)
    @Schema(description ="预到时间")
    private Date expectArriveTime;

    /**
     * 预离时间
     */
    @JhyjField(eq = true)
    @Schema(description ="预离时间")
    private Date expectLeaveTime;

    /**
     * 出发港代码
     */
    @JhyjField(eq = true)
    @Schema(description ="出发港代码")
    private String portCodeFrom;

    /**
     * 到达港代码
     */
    @JhyjField(eq = true)
    @Schema(description ="到达港代码")
    private String portCodeTo;

    /**
     * 是否引水(1是,0否)
     */
    @JhyjField(eq = true)
    @Schema(description ="是否引水(1是,0否)")
    private String needLead;

    /**
     * 是否减载(1是,0否)
     */
    @JhyjField(eq = true)
    @Schema(description ="是否减载(1是,0否)")
    private String isDeload;

    /**
     * 是否拖轮(1是,0否)
     */
    @JhyjField(eq = true)
    @Schema(description ="是否拖轮(1是,0否)")
    private String needTractor;

    /**
     * 备车时间(小时)
     */
    @JhyjField(eq = true)
    @Schema(description ="备车时间(小时)")
    private Integer carRaedyTime;

    /**
     * 重点船舶标记(1是,0否)
     */
    @JhyjField(eq = true)
    @Schema(description ="重点船舶标记(1是,0否)")
    private Integer isImportant;

    /**
     * 特殊船舶标记（非生产性船舶）(1是,0否)
     */
    @JhyjField(eq = true)
    @Schema(description ="特殊船舶标记（非生产性船舶）(1是,0否)")
    private Integer isSpecial;

    /**
     * 当前状态
     */
    @JhyjField(eq = true)
    @Schema(description ="当前状态")
    private Integer status;

    /**
     * 货主
     */
    @JhyjField(eq = true)
    @Schema(description ="货主")
    private String cargoOwner;

    /**
     * 备注
     */
    @JhyjField(eq = true)
    @Schema(description ="备注")
    private String remark;

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

    /**
     * 创建公司
     */
    @JhyjField(eq = true)
    @Schema(description ="创建公司")
    private String createCompany;
    /**
     * 装卸要求（1直取|2存放）
     * {@link CargoWhereaboutsRequirementEnum}
     */
    @TableField("CARGO_WHEREABOUTS")
    private Integer cargoWhereabouts;

    /**
     * 疏港方式
     * {@link com.jeesite.modules.algorithm.enums.CargoEvacuationEnum}
     */
    @TableField("CARGO_EVACUATION")
    private Integer cargoEvacuation;
    /**
     * 算法状态
     */
    @JhyjField(eq = true)
    @Schema(title = "算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private Integer algorithmState;

    /**
     * 通知单
     */
    @JhyjField(eq = true)
    @Schema(title = "通知单")
    private Integer noticeTicket;


////仅供查询字段//////////////////////////////////////////////////

    /**
     * 预到时间（范围开始）
     */
    @JhyjField(datetimeGe = true, tableField = false, fieldName = "expect_arrive_time")
    @TableField(exist = false)
    @Schema(description ="预到时间（范围开始）")
    private Date expectArriveTimeStart;

    /**
     * 预到时间（范围结束）
     */
    @JhyjField(datetimeLe = true, tableField = false, fieldName = "expect_arrive_time")
    @TableField(exist = false)
    @Schema(description ="预到时间（范围结束）")
    private Date expectArriveTimeEnd;

    /**
     * 船名（模糊查询）
     */
    @JhyjField(like = true, tableField = false, fieldName = "SHIP_NAME_CN")
    @TableField(exist = false)
    @Schema(description ="船名（模糊查询）")
    private String shipNameCnLike;


    /**
     * 算法状态
     */
    @JhyjField(in = true, tableField = false, fieldName = "ALGORITHM_STATE")
    @TableField(exist = false)
    @Schema(title ="算法状态", description = "字典（APS_ALGORITHM_STATE）")
    private String algorithmStateIn;



    /**
     * ID集合
     */
    @JhyjField(in = true, tableField = false, fieldName = "ID")
    @TableField(exist = false)
    @Schema(description ="ID（集合）")
    private String idIn;

    /**
     * 来港航次(IN)
     */
    @JhyjField(in = true, tableField = false, fieldName = "VOYAGE_NO")
    @TableField(exist = false)
    @Schema(title ="来港航次(IN)")
    private String voyageNoIn;


////仅供展示字段//////////////////////////////////////////////////


    /**
     * 船长
     */
    @TableField(exist = false)
    @Schema(description ="船长")
    private String shipLength;


    /**
     * 船宽
     */
    @TableField(exist = false)
    @Schema(description ="船宽")
    private String shipWidth;

}
