package com.jeesite.modules.algorithm.service;

import com.jeesite.modules.algorithm.entity.AlgShipWorkShift;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jeesite.modules.algorithm.entity.AlgShipWorkShiftTemp;

import java.util.List;

/**
 * <p>
 * 作业班次明细表，记录每个班次的具体作业计划 服务类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
public interface IAlgShipWorkShiftService extends IService<AlgShipWorkShift> {
    /**
     * 获取列表
     * @param algShipWorkShift
     * @return
     */
    List<AlgShipWorkShift> queryList(AlgShipWorkShift algShipWorkShift);
}
