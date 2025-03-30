package com.jeesite.modules.algorithm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.algorithm.entity.AlgShipWorkShift;
import com.jeesite.modules.algorithm.entity.AlgShipWorkShiftTemp;
import com.jeesite.modules.algorithm.mapper.AlgShipWorkShiftMapper;
import com.jeesite.modules.algorithm.service.IAlgShipWorkShiftService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 作业班次明细表，记录每个班次的具体作业计划 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Service
public class AlgShipWorkShiftServiceImpl extends ServiceImpl<AlgShipWorkShiftMapper, AlgShipWorkShift> implements IAlgShipWorkShiftService {
    @Override
    public List<AlgShipWorkShift> queryList(AlgShipWorkShift algShipWorkShift) {
        QueryWrapper<AlgShipWorkShift> queryWrapper = MybatisPlusUtils.getQueryWrapper(algShipWorkShift, null);
        queryWrapper.eq(algShipWorkShift.getShipWorkPlanId() != null, "SHIP_WORK_PLAN_ID", algShipWorkShift.getShipWorkPlanId());
        return list(queryWrapper);
    }
}
