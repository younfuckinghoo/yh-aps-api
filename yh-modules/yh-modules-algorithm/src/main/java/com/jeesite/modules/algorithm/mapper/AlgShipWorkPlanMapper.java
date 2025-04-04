package com.jeesite.modules.algorithm.mapper;

import com.jeesite.modules.algorithm.entity.AlgShipWorkPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 船舶作业计划主表，记录船舶作业核心信息 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Mapper
public interface AlgShipWorkPlanMapper extends BaseMapper<AlgShipWorkPlan> {

}
