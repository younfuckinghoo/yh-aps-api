package com.jeesite.modules.algorithm.service;

import com.jeesite.modules.algorithm.entity.AlgShipSiloArrange;
import com.jeesite.modules.algorithm.entity.AlgShipYardArrange;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 船舶作业堆场分配表，记录船舶与堆场的关联 服务类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
public interface IAlgShipYardArrangeService extends IService<AlgShipYardArrange> {
    /**
     * 获取列表
     * @param algShipYardArrange
     * @return
     */
    List<AlgShipYardArrange> queryList(AlgShipYardArrange algShipYardArrange);
}
