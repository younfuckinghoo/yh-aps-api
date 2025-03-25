package com.jeesite.modules.apsbiz.web;

import com.jeesite.common.base.R;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.apsbiz.entity.BizBerthInfo;
import com.jeesite.modules.apsbiz.service.BizBerthInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 泊位表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:16
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "bizberthinfo")
@Tag(name = "bizberthinfo - 泊位表管理")
public class AlgBerthInfoController extends BaseController {

    private final BizBerthInfoService bizBerthInfoService;

    /**
     * 分页查询
	 * @param bizBerthInfo 泊位表
	 * @param page 分页-页
	 * @param size 分页-数据量
     * @return
     */
    @Operation(summary="分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:berth:view")
    public R getAlgBerthInfoPage(BizBerthInfo bizBerthInfo, Integer page, Integer size) {
        return R.ok(bizBerthInfoService.queryPage(bizBerthInfo, page, size));
    }

	/**
	 * 全部查询
	 * @param bizBerthInfo 泊位表
	 * @return
	 */
	@Operation(summary="全部查询")
	@GetMapping("/list")
    @RequiresPermissions("apsbiz:berth:view")
	public R getAlgBerthInfoList(BizBerthInfo bizBerthInfo) {
        bizBerthInfo.setAvailable(1);
        bizBerthInfo.setOrderBy(Arrays.asList("sort;asc"));
        List<BizBerthInfo> list = bizBerthInfoService.queryList(bizBerthInfo);
		return R.ok(list);
	}

    /**
     * 全部查询
     * @param bizBerthInfo 泊位表
     * @return
     */
    @Operation(summary="字典")
    @GetMapping("/dict")
    @RequiresPermissions("apsbiz:berth:view")
    public R berthDict(BizBerthInfo bizBerthInfo) {
        bizBerthInfo.setAvailable(1);
        bizBerthInfo.setOrderBy(Arrays.asList("berthNo;asc"));
        List<BizBerthInfo> list = bizBerthInfoService.queryFieldsList(bizBerthInfo, "id,berthName");
        return R.ok(list);
    }

    /**
     * 查询指定字段
     * @param bizBerthInfo 泊位表
     * @param fields
     * @return
     */
    @Operation(summary="查询指定字段")
    @Parameters({
            @Parameter(name = "fields = 待查询的字段,多个时用','拼接"),
    })
    @GetMapping("/listFields")
    @RequiresPermissions("apsbiz:berth:view")
    public R getAlgBerthInfoFiledsList(BizBerthInfo bizBerthInfo, String fields) {
        return R.ok(bizBerthInfoService.queryFieldsList(bizBerthInfo, fields));
    }

	/**
	 * 查询数量
	 * @param bizBerthInfo 泊位表
	 * @return
	 */
	@Operation(summary="查询数量")
	@GetMapping("/count")
    @RequiresPermissions("apsbiz:berth:view")
	public R getAlgBerthInfoCount(BizBerthInfo bizBerthInfo) {
		return R.ok(bizBerthInfoService.queryCount(bizBerthInfo));
	}

    /**
     * 通过id查询泊位表
     * @param id 编号
     * @return R
     */
    @Operation(summary="通过id查询")
    @GetMapping("/{id}")
    @RequiresPermissions("apsbiz:berth:view")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bizBerthInfoService.infoById(id));
    }

    /**
     * 新增泊位表
     * @param bizBerthInfo 泊位表
     * @return R
     */
    @Operation(summary="新增泊位表")
    ////@SysLog("新增泊位表")
    @PostMapping
    @RequiresPermissions("apsbiz:berth:add")
    public R save(@RequestBody BizBerthInfo bizBerthInfo) {
        return R.ok(bizBerthInfoService.save(bizBerthInfo));
    }

    /**
     * 修改泊位表
     * @param bizBerthInfo 泊位表
     * @return R
     */
    @Operation(summary="修改泊位表")
    ////@SysLog("修改泊位表")
    @PutMapping
    @RequiresPermissions("apsbiz:berth:edit")
    public R updateById(@RequestBody BizBerthInfo bizBerthInfo) {
        return R.ok(bizBerthInfoService.updateOrCleanById(bizBerthInfo));
    }

    /**
     * 通过id删除泊位表
     * @param id id
     * @return R
     */
    @Operation(summary="通过id删除泊位表")
    ////@SysLog("通过id删除泊位表")
    @DeleteMapping("/{id}")
    @RequiresPermissions("apsbiz:berth:del")
    public R removeById(@PathVariable String id) {
        return R.ok(bizBerthInfoService.deleteById(id));
    }

	//自定义接口//////////////////////////////////////////////////
}
