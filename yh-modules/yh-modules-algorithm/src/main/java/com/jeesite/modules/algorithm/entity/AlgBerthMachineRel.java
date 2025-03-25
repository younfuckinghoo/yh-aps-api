package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_BERTH_MACHINE_REL")
public class AlgBerthMachineRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机械编号
     */
    @TableField("MACHINE_NO")
    private String machineNo;

    /**
     * 泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;


    /**
     * 排序序号
     */
    @TableField("ORDER_IDX")
    private String orderIdx;



}
