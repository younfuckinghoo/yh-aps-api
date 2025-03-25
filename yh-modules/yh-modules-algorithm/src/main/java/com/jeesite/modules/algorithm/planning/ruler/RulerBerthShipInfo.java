package com.jeesite.modules.algorithm.planning.ruler;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 船舶基础数据和泊位关系
 */
@Data
public class RulerBerthShipInfo extends BaseBerthRuler {



    /**
     * 最大船长
     */
    private BigDecimal maxShipLength;
    /**
     * 吃水
     */
    private BigDecimal maxShipDraft;
    /**
     * 吨级
     */
    private BigDecimal maxShipWeight;





    public RulerBerthShipInfo(Integer index,Integer privilege, Boolean bollardSort, String berthNo, String maxShipLength, String maxShipDraft, String maxShipWeight) {
        this.index = index;
        this.privilege = privilege;
        this.bollardSort = bollardSort;
        this.berthNo = berthNo;
        this.maxShipLength = new BigDecimal(maxShipLength);
        this.maxShipDraft = new BigDecimal(maxShipDraft);
        this.maxShipWeight = new BigDecimal(maxShipWeight);
    }
}
