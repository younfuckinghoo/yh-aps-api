package com.jeesite.modules.algorithm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.common.base.R;
import com.jeesite.common.utils.MybatisPlusOracleUtils;
import com.jeesite.modules.algorithm.entity.AlgYard;
import com.jeesite.modules.algorithm.entity.AlgYard;
import com.jeesite.modules.algorithm.service.IAlgYardService;
import com.jeesite.modules.algorithm.service.IAlgYardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 堆场基础数据表，记录堆场静态属性 前端控制器
 * </p>
 *
 * @author haoyong
 * @since 2025-03-17
 */
@RestController
@RequestMapping("/algYard")
public class AlgYardController {
    @Resource
    private IAlgYardService algYardService;
    /**
     * 获取列表
     * @param algYard 堆场基础数据表
     * @return R
     */
    @Operation(summary = "获取列表")
    @GetMapping("/list")
//    @RequiresPermissions("apsbiz:algShoreMachine:view")
    public R queryList(AlgYard algYard) {
        QueryWrapper<AlgYard> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algYard, null);
        List<AlgYard> list = algYardService.list(queryWrapper);
        return R.ok(list);
    }
}
