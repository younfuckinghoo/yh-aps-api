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
 * 缆柱表
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_BOLLARD_INFO")
public class AlgBollardInfo implements Serializable,Cloneable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     * 泊位ID
     */
    @TableField("BERTH_ID")
    private String berthId;

    /**
     * 泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;

    /**
     * 榄桩号
     */
    @TableField("BOLLARD_NO")
    private  Integer bollardNo;

    /**
     * 榄桩名称
     */
    @TableField("BOLLARD_NAME")
    private String bollardName;

    /**
     * 距上一缆柱距离
     */
    @TableField("LAST_BOLLARD_DISTANCE")
    private BigDecimal lastBollardDistance;

    /**
     * 被占用时间
     */
    @TableField("OCCUPY_UNTIL")
    private LocalDateTime occupyUntil;

    /**
     * 创建日期
     */
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    /**
     * 修改日期
     */
    @TableField("REVISE_DATE")
    private LocalDateTime reviseDate;

    /**
     * 是否可用
     */
    @TableField("AVAILABLE")
    private Integer available;
    /**
     * 所属泊位组编号
     */
    @TableField("BERTH_GROUP_NO")
    private Integer berthGroupNo;


    @Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


}
