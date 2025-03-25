package com.jeesite.modules.algorithm.mapper;

import com.jeesite.modules.algorithm.entity.AlgShipWorkShift;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 作业班次明细表，记录每个班次的具体作业计划 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Mapper
public interface AlgShipWorkShiftMapper extends BaseMapper<AlgShipWorkShift> {

}
