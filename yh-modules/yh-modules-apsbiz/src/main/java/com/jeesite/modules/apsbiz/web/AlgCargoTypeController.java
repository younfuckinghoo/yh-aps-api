package com.jeesite.modules.apsbiz.web;

import com.jeesite.common.base.R;
import com.jeesite.modules.apsbiz.entity.BizCargoType;
import com.jeesite.modules.apsbiz.service.BizCargoTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;


/**
 * 货类表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bizcargotype")
@Tag(name = "bizcargotype - 货类表管理")
public class AlgCargoTypeController {

    private final BizCargoTypeService bizCargoTypeService;

    /**
     * 分页查询
	 * @param bizCargoType 货类表
	 * @param page 分页-页
	 * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:cargo:view")
    public R getAlgCargoTypePage(BizCargoType bizCargoType, Integer page, Integer size) {
        return R.ok(bizCargoTypeService.queryPage(bizCargoType, page, size));
    }

	/**
	 * 全部查询
	 * @param bizCargoType 货类表
	 * @return
	 */
	@Operation(summary = "全部查询")
	@GetMapping("/list")
    @RequiresPermissions("apsbiz:cargo:view")
	public R getAlgCargoTypeList(BizCargoType bizCargoType) {
		return R.ok(bizCargoTypeService.queryList(bizCargoType));
	}

    /**
     * 查询指定字段
     * @param bizCargoType 货类表
     * @param fields
     * @return
     */
    @Operation(summary = "查询指定字段")
    @Parameters({
            @Parameter(name = "fields = 待查询的字段,多个时用','拼接"),
    })
    @GetMapping("/listFields")
    @RequiresPermissions("apsbiz:cargo:view")
    public R getAlgCargoTypeFiledsList(BizCargoType bizCargoType, String fields) {
        return R.ok(bizCargoTypeService.queryFieldsList(bizCargoType, fields));
    }

	/**
	 * 查询数量
	 * @param bizCargoType 货类表
	 * @return
	 */
	@Operation(summary = "查询数量")
	@GetMapping("/count")
    @RequiresPermissions("apsbiz:cargo:view")
	public R getAlgCargoTypeCount(BizCargoType bizCargoType) {
		return R.ok(bizCargoTypeService.queryCount(bizCargoType));
	}

    /**
     * 通过id查询货类表
     * @param id 编号
     * @return R
     */
    @Operation(summary = "通过id查询")
    @GetMapping("/{id}")
    @RequiresPermissions("apsbiz:cargo:view")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bizCargoTypeService.infoById(id));
    }

    /**
     * 新增货类表
     * @param bizCargoType 货类表
     * @return R
     */
    @Operation(summary = "新增货类表")
    //@SysLog("新增货类表")
    @PostMapping
    @RequiresPermissions("apsbiz:cargo:add")
    public R save(@RequestBody BizCargoType bizCargoType) {
        return R.ok(bizCargoTypeService.save(bizCargoType));
    }

    /**
     * 修改货类表
     * @param bizCargoType 货类表
     * @return R
     */
    @Operation(summary = "修改货类表")
    //@SysLog("修改货类表")
    @PutMapping
    @RequiresPermissions("apsbiz:cargo:edit")
    public R updateById(@RequestBody BizCargoType bizCargoType) {
        return R.ok(bizCargoTypeService.updateOrCleanById(bizCargoType));
    }

    /**
     * 通过id删除货类表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除货类表")
    //@SysLog("通过id删除货类表")
    @DeleteMapping("/{id}")
    @RequiresPermissions("apsbiz:cargo:del")
    public R removeById(@PathVariable String id) {
        return R.ok(bizCargoTypeService.deleteById(id));
    }

	//自定义接口//////////////////////////////////////////////////
}
