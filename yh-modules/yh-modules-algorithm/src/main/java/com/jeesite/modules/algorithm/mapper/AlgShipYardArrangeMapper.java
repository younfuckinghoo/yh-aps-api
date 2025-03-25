package com.jeesite.modules.algorithm.mapper;

import com.jeesite.modules.algorithm.entity.AlgShipYardArrange;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 船舶作业堆场分配表，记录船舶与堆场的关联 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Mapper
public interface AlgShipYardArrangeMapper extends BaseMapper<AlgShipYardArrange> {

}
