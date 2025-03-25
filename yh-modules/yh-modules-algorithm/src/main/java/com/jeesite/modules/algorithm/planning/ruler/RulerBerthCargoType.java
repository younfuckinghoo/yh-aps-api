package com.jeesite.modules.algorithm.planning.ruler;

import lombok.Data;

/**
 * 货种泊位关系
 */
@Data
public class RulerBerthCargoType extends BaseBerthRuler {

    /**
     * 装卸类型
     * {@link com.jeesite.modules.algorithm.enums.LoadUnloadEnum}
     */
    private Integer loadUnload;

    /**
     * AlgCargoType .typeName
     * 货物种类
     */
    private String typeName;


    public RulerBerthCargoType(Integer index,Integer privilege,Boolean bollardSort,String berthNo ,String typeName,Integer loadUnload) {
        this.index = index;
        this.privilege = privilege;
        this.bollardSort = bollardSort;
        this.berthNo = berthNo;
        this.typeName = typeName;
        this.loadUnload = loadUnload;
    }
}
