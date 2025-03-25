package com.jeesite.modules.apsbiz.web;

import com.jeesite.common.base.R;
import com.jeesite.modules.apsbiz.entity.BizShoreMachine;
import com.jeesite.modules.apsbiz.service.BizShoreMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;


/**
 * 泊位岸机数据
 *
 * @author zhudi
 * @date 2024-12-08 21:19:22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bizshoremachine")
@Tag(name = "bizshoremachine - 泊位岸机数据管理")
public class AlgShoreMachineController {

    private final BizShoreMachineService bizShoreMachineService;

    /**
     * 分页查询
	 * @param bizShoreMachine 泊位岸机数据
	 * @param page 分页-页
	 * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:shoremachine:view")
    public R getAlgShoreMachinePage(BizShoreMachine bizShoreMachine, Integer page, Integer size) {
        return R.ok(bizShoreMachineService.queryPage(bizShoreMachine, page, size));
    }

	/**
	 * 全部查询
	 * @param bizShoreMachine 泊位岸机数据
	 * @return
	 */
	@Operation(summary = "全部查询")
	@GetMapping("/list")
    @RequiresPermissions("apsbiz:shoremachine:view")
	public R getAlgShoreMachineList(BizShoreMachine bizShoreMachine) {
		return R.ok(bizShoreMachineService.queryList(bizShoreMachine));
	}

    /**
     * 查询指定字段
     * @param bizShoreMachine 泊位岸机数据
     * @param fields
     * @return
     */
    @Operation(summary = "查询指定字段")
    @Parameters({
            @Parameter(name = "fields = 待查询的字段,多个时用','拼接"),
    })
    @GetMapping("/listFields")
    @RequiresPermissions("apsbiz:shoremachine:view")
    public R getAlgShoreMachineFiledsList(BizShoreMachine bizShoreMachine, String fields) {
        return R.ok(bizShoreMachineService.queryFieldsList(bizShoreMachine, fields));
    }

	/**
	 * 查询数量
	 * @param bizShoreMachine 泊位岸机数据
	 * @return
	 */
	@Operation(summary = "查询数量")
	@GetMapping("/count")
    @RequiresPermissions("apsbiz:shoremachine:view")
	public R getAlgShoreMachineCount(BizShoreMachine bizShoreMachine) {
		return R.ok(bizShoreMachineService.queryCount(bizShoreMachine));
	}

    /**
     * 通过id查询泊位岸机数据
     * @param id 编号
     * @return R
     */
    @Operation(summary = "通过id查询")
    @GetMapping("/{id}")
    @RequiresPermissions("apsbiz:shoremachine:view")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bizShoreMachineService.infoById(id));
    }

    /**
     * 新增泊位岸机数据
     * @param bizShoreMachine 泊位岸机数据
     * @return R
     */
    @Operation(summary = "新增泊位岸机数据")
    //@SysLog("新增泊位岸机数据")
    @PostMapping
    @RequiresPermissions("apsbiz:shoremachine:add")
    public R save(@RequestBody BizShoreMachine bizShoreMachine) {
        return R.ok(bizShoreMachineService.save(bizShoreMachine));
    }

    /**
     * 修改泊位岸机数据
     * @param bizShoreMachine 泊位岸机数据
     * @return R
     */
    @Operation(summary = "修改泊位岸机数据")
    //@SysLog("修改泊位岸机数据")
    @PutMapping
    @RequiresPermissions("apsbiz:shoremachine:edit")
    public R updateById(@RequestBody BizShoreMachine bizShoreMachine) {
        return R.ok(bizShoreMachineService.updateOrCleanById(bizShoreMachine));
    }

    /**
     * 通过id删除泊位岸机数据
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除泊位岸机数据")
    //@SysLog("通过id删除泊位岸机数据")
    @DeleteMapping("/{id}")
    @RequiresPermissions("apsbiz:shoremachine:del")
    public R removeById(@PathVariable String id) {
        return R.ok(bizShoreMachineService.deleteById(id));
    }

	//自定义接口//////////////////////////////////////////////////
}
