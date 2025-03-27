package com.jeesite.modules.algorithm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.common.base.R;
import com.jeesite.common.utils.MybatisPlusOracleUtils;
import com.jeesite.modules.algorithm.entity.AlgShoreMachine;
import com.jeesite.modules.algorithm.entity.AlgSiloBase;
import com.jeesite.modules.algorithm.service.AlgShoreMachineService;
import com.jeesite.modules.algorithm.service.IAlgSiloBaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 筒仓基础数据表，记录筒仓静态属性 前端控制器
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@RestController
@RequestMapping("/algSiloBase")
public class AlgSiloBaseController {
    @Resource
    private IAlgSiloBaseService algSiloBaseService;
    /**
     * 获取列表
     * @param algSiloBase 筒仓基础数据表
     * @return R
     */
    @Operation(summary = "获取列表")
    @GetMapping("/list")
//    @RequiresPermissions("apsbiz:algShoreMachine:view")
    public R queryList(AlgSiloBase algSiloBase) {
        QueryWrapper<AlgSiloBase> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algSiloBase, null);
        List<AlgSiloBase> list = algSiloBaseService.list(queryWrapper);
        return R.ok(list);
    }
}
