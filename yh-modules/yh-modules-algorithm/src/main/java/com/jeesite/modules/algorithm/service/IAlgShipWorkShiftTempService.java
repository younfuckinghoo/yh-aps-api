package com.jeesite.modules.algorithm.service;

import com.jeesite.modules.algorithm.entity.AlgShipWorkShiftTemp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jeesite.modules.algorithm.entity.AlgShipYardArrange;

import java.util.List;

/**
 * <p>
 * 作业班次明细表，记录每个班次的具体作业计划 服务类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-24
 */
public interface IAlgShipWorkShiftTempService extends IService<AlgShipWorkShiftTemp> {
    /**
     * 获取列表
     * @param algShipWorkShiftTemp
     * @return
     */
    List<AlgShipWorkShiftTemp> queryList(AlgShipWorkShiftTemp algShipWorkShiftTemp);
}
