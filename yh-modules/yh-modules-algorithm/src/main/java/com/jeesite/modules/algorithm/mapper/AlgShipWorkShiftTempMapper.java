package com.jeesite.modules.algorithm.mapper;

import com.jeesite.modules.algorithm.entity.AlgShipWorkShiftTemp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 作业班次明细表，记录每个班次的具体作业计划 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2025-03-24
 */
@Mapper
public interface AlgShipWorkShiftTempMapper extends BaseMapper<AlgShipWorkShiftTemp> {

}
