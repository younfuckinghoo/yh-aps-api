package com.jeesite.modules.algorithm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.algorithm.entity.AlgShipMachineAlloc;
import com.jeesite.modules.algorithm.entity.AlgShipSiloArrange;
import com.jeesite.modules.algorithm.mapper.AlgShipMachineAllocMapper;
import com.jeesite.modules.algorithm.service.IAlgShipMachineAllocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 船舶机械分配表，记录每条船使用的机械设备 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Service
public class AlgShipMachineAllocServiceImpl extends ServiceImpl<AlgShipMachineAllocMapper, AlgShipMachineAlloc> implements IAlgShipMachineAllocService {

    @Override
    public List<AlgShipMachineAlloc> listAfterWorkingFinishTime(LocalDateTime endTime) {
        return this.baseMapper.listAfterWorkingFinishTime(endTime);
    }
    @Override
    public List<AlgShipMachineAlloc> queryList(AlgShipMachineAlloc algShipMachineAlloc) {
        QueryWrapper<AlgShipMachineAlloc> queryWrapper = MybatisPlusUtils.getQueryWrapper(algShipMachineAlloc, null);
        queryWrapper.eq(algShipMachineAlloc.getVoyageNo() != null, "VOYAGE_NO", algShipMachineAlloc.getVoyageNo());
        return list(queryWrapper);
    }
}
