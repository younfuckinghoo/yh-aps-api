package com.jeesite.modules.algorithm.mapper;

import com.jeesite.modules.algorithm.entity.AlgShipMachineAlloc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 船舶机械分配表，记录每条船使用的机械设备 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Mapper
public interface AlgShipMachineAllocMapper extends BaseMapper<AlgShipMachineAlloc> {


    /**
     * 查询某个时间点之后的
     * @param endTime
     * @return
     */
     List<AlgShipMachineAlloc> listAfterWorkingFinishTime(@Param("endTime")LocalDateTime endTime);
}
