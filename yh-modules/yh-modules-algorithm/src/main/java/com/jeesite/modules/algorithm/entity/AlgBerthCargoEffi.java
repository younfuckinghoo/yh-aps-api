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
 * 泊位货物作业效率
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ALG_BERTH_CARGO_EFFI")
public class AlgBerthCargoEffi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     * 泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;

    /**
     * 装卸类型（1装；2卸
     */
    @TableField("LOAD_UNLOAD")
    private Integer loadUnload;

    /**
     * 货种代码
     */
    @TableField("CARGO_TYPE_CODE")
    private String cargoTypeCode;

    /**
     * 货钟名称
     */
    @TableField("CARGO_TYPE_NAME")
    private String cargoTypeName;

    /**
     * 货主简称
     */
    @TableField("CARGO_OWNER")
    private String cargoOwner;

    /**
     * 效率
     */
    @TableField("EFFICIENCY")
    private BigDecimal efficiency;

    /**
     * 创建人
     */
    @TableField("CREATOR")
    private String creator;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;


}
