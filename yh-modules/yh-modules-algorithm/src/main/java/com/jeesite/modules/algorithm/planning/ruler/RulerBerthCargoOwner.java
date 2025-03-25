package com.jeesite.modules.algorithm.planning.ruler;

import lombok.Data;

@Data
/**
 * 货主泊位关系
 */
public class RulerBerthCargoOwner extends BaseBerthRuler{


    /**
     * 货主简称
     */
    private String ownerShortName;

    public RulerBerthCargoOwner(Integer index,Integer privilege,Boolean bollardSort,String berthNo ,String ownerShortName) {
        this.index = index;
        this.privilege = privilege;
        this.bollardSort = bollardSort;
        this.berthNo = berthNo;
        this.ownerShortName = ownerShortName;
    }
}
