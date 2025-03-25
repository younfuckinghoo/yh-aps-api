package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 筒仓与泊位关联关系表
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_SILO_BERTH_REL")
public class AlgSiloBerthRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的筒仓编号
     */
    @TableField("SILO_NO")
    private String siloNo;

    /**
     * 关联的泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;



    /**
     * 优先级，越大越高
     */
    @TableField("PRIORITY")
    private String priority;

}
