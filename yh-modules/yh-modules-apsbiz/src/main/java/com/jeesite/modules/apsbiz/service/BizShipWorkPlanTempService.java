package com.jeesite.modules.apsbiz.service;

import com.jeesite.common.base.R;
import com.jeesite.common.base.YhService;
import com.jeesite.modules.apsbiz.entity.BizShipWorkPlanTemp;

public interface BizShipWorkPlanTempService  extends YhService<BizShipWorkPlanTemp> {
    /**
     * 通过在港ID查询
     * @param voyageNo
     * @return
     */
    BizShipWorkPlanTemp infoByVoyageNo(String voyageNo);

    /**
     * 通过ID删除
     * @param id
     * @return
     */
    R deleteById(String id);

    /**
     * 提交审核
     * @param ids
     * @return
     */
    R submit(String ids);

    /**
     * 更新基础信息后，更新其他表的信息
     * @param bizShipWorkPlanTemp
     * @return
     */
    void updateOtherInfo(BizShipWorkPlanTemp bizShipWorkPlanTemp, BizShipWorkPlanTemp base);
}
