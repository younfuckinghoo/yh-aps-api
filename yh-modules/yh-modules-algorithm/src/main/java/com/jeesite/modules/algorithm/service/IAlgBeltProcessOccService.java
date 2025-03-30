package com.jeesite.modules.algorithm.service;

import com.jeesite.modules.algorithm.entity.AlgBeltProcessOcc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 船舶作业皮带占用表，记录船舶占用的皮带流程 服务类
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
public interface IAlgBeltProcessOccService extends IService<AlgBeltProcessOcc> {

    List<AlgBeltProcessOcc> listAdditonalQueryColumns();
}
