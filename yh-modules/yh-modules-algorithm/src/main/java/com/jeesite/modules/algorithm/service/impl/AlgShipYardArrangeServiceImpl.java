package com.jeesite.modules.algorithm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.algorithm.entity.AlgShipSiloArrange;
import com.jeesite.modules.algorithm.entity.AlgShipYardArrange;
import com.jeesite.modules.algorithm.mapper.AlgShipYardArrangeMapper;
import com.jeesite.modules.algorithm.service.IAlgShipYardArrangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 船舶作业堆场分配表，记录船舶与堆场的关联 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Service
public class AlgShipYardArrangeServiceImpl extends ServiceImpl<AlgShipYardArrangeMapper, AlgShipYardArrange> implements IAlgShipYardArrangeService {
    @Override
    public List<AlgShipYardArrange> queryList(AlgShipYardArrange algShipYardArrange) {
        QueryWrapper<AlgShipYardArrange> queryWrapper = MybatisPlusUtils.getQueryWrapper(algShipYardArrange, null);
        queryWrapper.eq(algShipYardArrange.getVoyageNo() != null, "VOYAGE_NO", algShipYardArrange.getVoyageNo());
        return list(queryWrapper);
    }
}
