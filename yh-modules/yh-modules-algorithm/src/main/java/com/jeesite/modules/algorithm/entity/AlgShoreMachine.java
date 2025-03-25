package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.jeesite.modules.algorithm.enums.MachineTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 泊位岸机数据
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_SHORE_MACHINE")
public class AlgShoreMachine implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     *   岸机类型名称 
     */
    @TableField("MACHINE_NAME")
    private String machineName;

    /**
     *   岸机类型编码
     *   {@link MachineTypeEnum}
     */
    @TableField("MACHINE_TYPE_CODE")
    private Integer machineCode;

    /**
     *   岸机编号
     */
    @TableField("MACHINE_NO")
    private String machineNo;

    /**
     * 泊位编号
     */
//    @TableField("BERTH_NO")
//    private String berthNo;

    /**
     * 被占用时间（截止时间）
     */
//    @TableField("OCCUPIED_UNTIL")
//    private LocalDateTime occupiedUntil;

    /**
     * 设备状态
     */
    @TableField("STATUS")
    private Integer status;


}
