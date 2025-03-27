package com.jeesite.modules.algorithm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.algorithm.entity.AlgShipSiloArrange;
import com.jeesite.modules.algorithm.mapper.AlgShipSiloArrangeMapper;
import com.jeesite.modules.algorithm.service.IAlgShipSiloArrangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 船舶作业筒仓分配表，记录船舶作业占用的筒仓 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Service
public class AlgShipSiloArrangeServiceImpl extends ServiceImpl<AlgShipSiloArrangeMapper, AlgShipSiloArrange> implements IAlgShipSiloArrangeService {
    @Override
    public List<AlgShipSiloArrange> queryList(AlgShipSiloArrange algShipSiloArrange) {
        QueryWrapper<AlgShipSiloArrange> queryWrapper = MybatisPlusUtils.getQueryWrapper(algShipSiloArrange, null);
        queryWrapper.eq(algShipSiloArrange.getVoyageNo() != null, "VOYAGE_NO", algShipSiloArrange.getVoyageNo());
        return list(queryWrapper);
    }
}
