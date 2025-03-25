package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 堆场与泊位关联关系表
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_YARD_BERTH_REL")
public class AlgYardBerthRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的堆场编号
     */
    @TableField("YARD_NO")
    private String yardNo;

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
