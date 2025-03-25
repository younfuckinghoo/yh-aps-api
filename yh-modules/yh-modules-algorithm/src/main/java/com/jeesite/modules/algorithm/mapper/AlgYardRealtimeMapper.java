package com.jeesite.modules.algorithm.mapper;

import com.jeesite.modules.algorithm.entity.AlgYardRealtime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 堆场实时数据表，记录动态库存变化 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Mapper
public interface AlgYardRealtimeMapper extends BaseMapper<AlgYardRealtime> {

}
