package com.jeesite.modules.algorithm.service;

import com.jeesite.modules.algorithm.entity.AlgTide;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 潮汐表 服务类
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
public interface AlgTideService extends IService<AlgTide> {

    List<AlgTide> listTideByDate(LocalDate tideDate);
}
