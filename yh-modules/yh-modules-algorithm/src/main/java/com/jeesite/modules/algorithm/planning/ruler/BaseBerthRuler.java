package com.jeesite.modules.algorithm.planning.ruler;

import lombok.Data;

@Data
public abstract class BaseBerthRuler {

    /**
     * 排序
     */
    protected Integer index;

    /**
     * 优先级 越大越高
     */
    protected Integer privilege;


    /**
     * 缆柱顺序
     */
    protected Boolean bollardSort;

    /**
     * 泊位
     */
    protected String berthNo;

}
