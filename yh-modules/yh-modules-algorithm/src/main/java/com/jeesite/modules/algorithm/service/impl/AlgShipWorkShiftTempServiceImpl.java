package com.jeesite.modules.algorithm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.algorithm.entity.AlgShipWorkShiftTemp;
import com.jeesite.modules.algorithm.entity.AlgShipYardArrange;
import com.jeesite.modules.algorithm.mapper.AlgShipWorkShiftTempMapper;
import com.jeesite.modules.algorithm.service.IAlgShipWorkShiftTempService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 作业班次明细表，记录每个班次的具体作业计划 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-24
 */
@Service
public class AlgShipWorkShiftTempServiceImpl extends ServiceImpl<AlgShipWorkShiftTempMapper, AlgShipWorkShiftTemp> implements IAlgShipWorkShiftTempService {
    @Override
    public List<AlgShipWorkShiftTemp> queryList(AlgShipWorkShiftTemp algShipWorkShiftTemp) {
        QueryWrapper<AlgShipWorkShiftTemp> queryWrapper = MybatisPlusUtils.getQueryWrapper(algShipWorkShiftTemp, null);
        queryWrapper.eq(algShipWorkShiftTemp.getShipWorkPlanId() != null, "SHIP_WORK_PLAN_ID", algShipWorkShiftTemp.getShipWorkPlanId());
        return list(queryWrapper);
    }
}
