package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.jeesite.modules.algorithm.enums.MachineClassEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 船舶机械分配表，记录每条船使用的机械设备
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_SHIP_MACHINE_ALLOC")
public class AlgShipMachineAlloc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分配记录唯一ID
     */
    @TableId(value = "ID",type = IdType.AUTO)
    private String id;

    /**
     * 船舶唯一航次号
     */
    @TableField("VOYAGE_NO")
    private String voyageNo;

    /**
     * 机械设备编号（如：岸吊-001）
     */
    @TableField("MACHINE_ID")
    private String machineId;

    /**
     * 机械设备名称（如：门座起重机）
     */
    @TableField("MACHINE_NAME")
    private String machineName;

    /**
     * 机械类型编码
     * {@link MachineClassEnum}
     */
    @TableField("MACHINE_CLASS_CODE")
    private Integer machineClassCode;

    /**
     * 分配数量（同一类型机械数量）
     */
    @TableField("MACHINE_COUNT")
    private Integer machineCount;
}
