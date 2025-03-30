package com.jeesite.modules.algorithm.mapper;

import com.jeesite.modules.algorithm.entity.AlgBeltProcessOcc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 船舶作业皮带占用表，记录船舶占用的皮带流程 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@Mapper
public interface AlgBeltProcessOccMapper extends BaseMapper<AlgBeltProcessOcc> {

    List<AlgBeltProcessOcc> listAdditonalQueryColumns();
}
