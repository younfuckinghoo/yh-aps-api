package com.jeesite.modules.algorithm.service;

import com.jeesite.modules.algorithm.entity.AlgShipSiloArrange;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 船舶作业筒仓分配表，记录船舶作业占用的筒仓 服务类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
public interface IAlgShipSiloArrangeService extends IService<AlgShipSiloArrange> {
    /**
     * 获取列表
     * @param algShipSiloArrange
     * @return
     */
    List<AlgShipSiloArrange> queryList(AlgShipSiloArrange algShipSiloArrange);
}
