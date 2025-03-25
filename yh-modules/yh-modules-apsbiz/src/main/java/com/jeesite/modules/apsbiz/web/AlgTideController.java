package com.jeesite.modules.apsbiz.web;

import com.jeesite.common.base.R;
import com.jeesite.modules.apsbiz.entity.BizTide;
import com.jeesite.modules.apsbiz.service.BizTideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;


/**
 * 潮汐表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/biztide")
@Tag(name = "biztide - 潮汐表管理")
public class AlgTideController {

    private final BizTideService bizTideService;

    /**
     * 分页查询
	 * @param bizTide 潮汐表
	 * @param page 分页-页
	 * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:tide:view")
    public R getAlgTidePage(BizTide bizTide, Integer page, Integer size) {
        return R.ok(bizTideService.queryPage(bizTide, page, size));
    }

	/**
	 * 全部查询
	 * @param bizTide 潮汐表
	 * @return
	 */
	@Operation(summary = "全部查询")
	@GetMapping("/list")
    @RequiresPermissions("apsbiz:tide:view")
	public R getAlgTideList(BizTide bizTide) {
		return R.ok(bizTideService.queryList(bizTide));
	}

    /**
     * 查询指定字段
     * @param bizTide 潮汐表
     * @param fields
     * @return
     */
    @Operation(summary = "查询指定字段")
    @Parameters({
            @Parameter(name = "fields = 待查询的字段,多个时用','拼接"),
    })
    @GetMapping("/listFields")
    @RequiresPermissions("apsbiz:tide:view")
    public R getAlgTideFiledsList(BizTide bizTide, String fields) {
        return R.ok(bizTideService.queryFieldsList(bizTide, fields));
    }

	/**
	 * 查询数量
	 * @param bizTide 潮汐表
	 * @return
	 */
	@Operation(summary = "查询数量")
	@GetMapping("/count")
    @RequiresPermissions("apsbiz:tide:view")
	public R getAlgTideCount(BizTide bizTide) {
		return R.ok(bizTideService.queryCount(bizTide));
	}

    /**
     * 通过id查询潮汐表
     * @param id 编号
     * @return R
     */
    @Operation(summary = "通过id查询")
    @GetMapping("/{id}")
    @RequiresPermissions("apsbiz:tide:view")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bizTideService.infoById(id));
    }

    /**
     * 新增潮汐表
     * @param bizTide 潮汐表
     * @return R
     */
    @Operation(summary = "新增潮汐表")
    //@SysLog("新增潮汐表")
    @PostMapping
    @RequiresPermissions("apsbiz:tide:add")
    public R save(@RequestBody BizTide bizTide) {
        return R.ok(bizTideService.save(bizTide));
    }

    /**
     * 修改潮汐表
     * @param bizTide 潮汐表
     * @return R
     */
    @Operation(summary = "修改潮汐表")
    //@SysLog("修改潮汐表")
    @PutMapping
    @RequiresPermissions("apsbiz:tide:edit")
    public R updateById(@RequestBody BizTide bizTide) {
        return R.ok(bizTideService.updateOrCleanById(bizTide));
    }

    /**
     * 通过id删除潮汐表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除潮汐表")
    //@SysLog("通过id删除潮汐表")
    @DeleteMapping("/{id}")
    @RequiresPermissions("apsbiz:tide:del")
    public R removeById(@PathVariable String id) {
        return R.ok(bizTideService.deleteById(id));
    }

	//自定义接口//////////////////////////////////////////////////
}
