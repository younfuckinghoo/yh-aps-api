package com.jeesite.modules.algorithm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 船舶作业皮带占用表，记录船舶占用的皮带流程
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("ALG_BELT_PROCESS_OCC")
public class AlgBeltProcessOcc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的航次号
     */
    @TableField("VOYAGE_NO")
    private String voyageNo;

    /**
     * 被占用的皮带流程编号
     */
    @TableField("OCCUPIED_PROCESS_NO")
    private String occupiedProcessNo;
}
