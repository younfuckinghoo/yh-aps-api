package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 船舶长期计划
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_SHIP_PLAN")
public class AlgShipPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID",type= IdType.INPUT)
    private String id;

    /**
     * 申报唯一ID
     */
    @TableField("SHIP_FORCAST_ID")
    private String shipForcastId;

    /**
     * 泊位
     */
    @TableField("BERTH_NO")
    private String berthNo;

    /**
     * 计划靠泊时间
     */
    @TableField("PLAN_BERTH_TIME")
    private LocalDateTime planBerthTime;

    /**
     * 计划开工时间
     */
    @TableField("PLAN_START_TIME")
    private LocalDateTime planStartTime;

    /**
     * 计划完工时间
     */
    @TableField("PLAN_FINISH_TIME")
    private LocalDateTime planFinishTime;

    /**
     * 计划离泊时间
     */
    @TableField("PLAN_LEAVE_TIME")
    private LocalDateTime planLeaveTime;

    /**
     * 首榄桩
     */
    @TableField("HEAD_BOLLARD_ID")
    private String headBollardId;

    /**
     * 尾榄桩
     */
    @TableField("TAIL_BOLLARD_ID")
    private String tailBollardId;

    /**
     * 计划状态(1未提交2已提交3已通过4已拒绝)
     */
    @TableField("PLAN_STATUS")
    private Integer planStatus;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("CREATOR")
    private String creator;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private LocalDateTime modifyTime;

    /**
     * 修改人
     */
    @TableField("MODIFIER")
    private String modifier;

    /**
     * 着岸(左右舷停靠) 1左 2右
     */
    @TableField("BERTH_DIRECTION")
    private Integer berthDirection;

    /**
     * 靠泊备注
     */
    @TableField("BERTH_REMARK")
    private String berthRemark;

    /**
     * 离泊备注
     */
    @TableField("LEAVE_REMARK")
    private String leaveRemark;

    /**
     * 算法状态
     */
//    @TableField("ALGORITHM_STATE")
//    private Integer algorithmState;

    /**
     * 引水 （1是 0否
     */
    @TableField("NEED_LEAD")
    private Integer needLead;

    /**
     * 拖轮 （1是 0否
     */
    @TableField("NEED_TRACTOR")
    private Integer needTractor;

    /**
     * 锁定 （1是 0否
     */
    @TableField("LOCKED")
    private Integer locked;



}
