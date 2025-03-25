package com.jeesite.modules.apsbiz.web;

import com.jeesite.common.base.R;
import com.jeesite.modules.apsbiz.entity.BizCargoOwner;
import com.jeesite.modules.apsbiz.service.BizCargoOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;


/**
 * 货主简称对照表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bizcargoowner")
@Tag(name = "bizcargoowner - 货主简称对照表管理")
public class AlgCargoOwnerController {

    private final BizCargoOwnerService bizCargoOwnerService;

    /**
     * 分页查询
	 * @param bizCargoOwner 货主简称对照表
	 * @param page 分页-页
	 * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:cargoowner:view")
    public R getAlgCargoOwnerPage(BizCargoOwner bizCargoOwner, Integer page, Integer size) {
        return R.ok(bizCargoOwnerService.queryPage(bizCargoOwner, page, size));
    }

	/**
	 * 全部查询
	 * @param bizCargoOwner 货主简称对照表
	 * @return
	 */
	@Operation(summary = "全部查询")
	@GetMapping("/list")
    @RequiresPermissions("apsbiz:cargoowner:view")
	public R getAlgCargoOwnerList(BizCargoOwner bizCargoOwner) {
		return R.ok(bizCargoOwnerService.queryList(bizCargoOwner));
	}

    /**
     * 查询指定字段
     * @param bizCargoOwner 货主简称对照表
     * @param fields
     * @return
     */
    @Operation(summary = "查询指定字段")
    @Parameters({
            @Parameter(name = "fields = 待查询的字段,多个时用','拼接"),
    })
    @GetMapping("/listFields")
    @RequiresPermissions("apsbiz:cargoowner:view")
    public R getAlgCargoOwnerFiledsList(BizCargoOwner bizCargoOwner, String fields) {
        return R.ok(bizCargoOwnerService.queryFieldsList(bizCargoOwner, fields));
    }

	/**
	 * 查询数量
	 * @param bizCargoOwner 货主简称对照表
	 * @return
	 */
	@Operation(summary = "查询数量")
	@GetMapping("/count")
    @RequiresPermissions("apsbiz:cargoowner:view")
	public R getAlgCargoOwnerCount(BizCargoOwner bizCargoOwner) {
		return R.ok(bizCargoOwnerService.queryCount(bizCargoOwner));
	}

    /**
     * 通过id查询货主简称对照表
     * @param id 编号
     * @return R
     */
    @Operation(summary = "通过id查询")
    @GetMapping("/{id}")
    @RequiresPermissions("apsbiz:cargoowner:view")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bizCargoOwnerService.infoById(id));
    }

    /**
     * 新增货主简称对照表
     * @param bizCargoOwner 货主简称对照表
     * @return R
     */
    @Operation(summary = "新增货主简称对照表")
    //@SysLog("新增货主简称对照表")
    @PostMapping
    @RequiresPermissions("apsbiz:cargoowner:add")
    public R save(@RequestBody BizCargoOwner bizCargoOwner) {
        return R.ok(bizCargoOwnerService.save(bizCargoOwner));
    }

    /**
     * 修改货主简称对照表
     * @param bizCargoOwner 货主简称对照表
     * @return R
     */
    @Operation(summary = "修改货主简称对照表")
    //@SysLog("修改货主简称对照表")
    @PutMapping
    @RequiresPermissions("apsbiz:cargoowner:edit")
    public R updateById(@RequestBody BizCargoOwner bizCargoOwner) {
        return R.ok(bizCargoOwnerService.updateOrCleanById(bizCargoOwner));
    }

    /**
     * 通过id删除货主简称对照表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除货主简称对照表")
    //@SysLog("通过id删除货主简称对照表")
    @DeleteMapping("/{id}")
    @RequiresPermissions("apsbiz:cargoowner:del")
    public R removeById(@PathVariable String id) {
        return R.ok(bizCargoOwnerService.deleteById(id));
    }

	//自定义接口//////////////////////////////////////////////////
}
