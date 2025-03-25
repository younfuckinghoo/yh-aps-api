package com.jeesite.modules.algorithm.service.impl;

import com.jeesite.modules.algorithm.entity.AlgTide;
import com.jeesite.modules.algorithm.mapper.AlgTideMapper;
import com.jeesite.modules.algorithm.service.AlgTideService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 潮汐表 服务实现类
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Service
public class AlgTideServiceImpl extends ServiceImpl<AlgTideMapper, AlgTide> implements AlgTideService {


    @Override
    public List<AlgTide> listTideByDate(LocalDate tideDate) {
        return this.baseMapper.listTideByDate(tideDate);
    }
}
