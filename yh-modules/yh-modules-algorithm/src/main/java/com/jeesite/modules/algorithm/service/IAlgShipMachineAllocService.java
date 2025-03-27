package com.jeesite.modules.algorithm.service;

import com.jeesite.modules.algorithm.entity.AlgShipMachineAlloc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jeesite.modules.algorithm.entity.AlgShipSiloArrange;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 船舶机械分配表，记录每条船使用的机械设备 服务类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
public interface IAlgShipMachineAllocService extends IService<AlgShipMachineAlloc> {

    /**
     * 查询完工时间在某个时间之后的机械安排
     * @param endTime
     * @return
     */
    List<AlgShipMachineAlloc> listAfterWorkingFinishTime(@Param("endTime") LocalDateTime endTime);
    /**
     * 获取列表
     * @param algShipMachineAlloc
     * @return
     */
    List<AlgShipMachineAlloc> queryList(AlgShipMachineAlloc algShipMachineAlloc);
}
