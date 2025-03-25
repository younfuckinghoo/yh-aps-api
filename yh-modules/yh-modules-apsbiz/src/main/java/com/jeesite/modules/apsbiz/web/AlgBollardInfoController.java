package com.jeesite.modules.apsbiz.web;

import com.jeesite.common.base.R;
import com.jeesite.common.data.DataUtil;
import com.jeesite.modules.apsbiz.entity.BizBerthInfo;
import com.jeesite.modules.apsbiz.entity.BizBollardInfo;
import com.jeesite.modules.apsbiz.service.BizBerthInfoService;
import com.jeesite.modules.apsbiz.service.BizBollardInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 缆柱表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bizbollardinfo")
@Tag(name = "bizbollardinfo - 缆柱表管理")
public class AlgBollardInfoController {

    private final BizBollardInfoService bizBollardInfoService;

    private final BizBerthInfoService bizBerthInfoService;

    /**
     * 分页查询
	 * @param bizBollardInfo 缆柱表
	 * @param page 分页-页
	 * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:bollard:view")
    public R getAlgBollardInfoPage(BizBollardInfo bizBollardInfo, Integer page, Integer size) {
        return R.ok(bizBollardInfoService.queryPage(bizBollardInfo, page, size));
    }

	/**
	 * 全部查询
	 * @param bizBollardInfo 缆柱表
	 * @return
	 */
	@Operation(summary = "全部查询")
	@GetMapping("/list")
    @RequiresPermissions("apsbiz:bollard:view")
	public R getAlgBollardInfoList(BizBollardInfo bizBollardInfo) {
        List<BizBerthInfo> berthList =  bizBerthInfoService.queryList(new BizBerthInfo());
        Map<Object, BizBerthInfo> berthMap = DataUtil.list2map(berthList, "berthNo");

        bizBollardInfo.setAvailable(1);
        bizBollardInfo.setOrderBy(Arrays.asList("berth_no;asc","sort;asc"));
        List<BizBollardInfo> bollardList = bizBollardInfoService.queryList(bizBollardInfo);

        bollardList.forEach(b -> {
            BizBerthInfo berthInfo = berthMap.get(b.getBerthNo());
            b.setBerthName(berthInfo == null ? b.getBerthNo() : berthInfo.getBerthName());
        });
		return R.ok(bollardList);
	}

    /**
     * 查询指定字段
     * @param bizBollardInfo 缆柱表
     * @param fields
     * @return
     */
    @Operation(summary = "查询指定字段")
    @Parameters({
            @Parameter(name = "fields = 待查询的字段,多个时用','拼接"),
    })
    @GetMapping("/listFields")
    @RequiresPermissions("apsbiz:bollard:view")
    public R getAlgBollardInfoFiledsList(BizBollardInfo bizBollardInfo, String fields) {
        return R.ok(bizBollardInfoService.queryFieldsList(bizBollardInfo, fields));
    }

	/**
	 * 查询数量
	 * @param bizBollardInfo 缆柱表
	 * @return
	 */
	@Operation(summary = "查询数量")
	@GetMapping("/count")
    @RequiresPermissions("apsbiz:bollard:view")
	public R getAlgBollardInfoCount(BizBollardInfo bizBollardInfo) {
		return R.ok(bizBollardInfoService.queryCount(bizBollardInfo));
	}

    /**
     * 通过id查询缆柱表
     * @param id 编号
     * @return R
     */
    @Operation(summary = "通过id查询")
    @GetMapping("/{id}")
    @RequiresPermissions("apsbiz:bollard:view")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bizBollardInfoService.infoById(id));
    }

    /**
     * 新增缆柱表
     * @param bizBollardInfo 缆柱表
     * @return R
     */
    @Operation(summary = "新增缆柱表")
    //@SysLog("新增缆柱表")
    @PostMapping
    @RequiresPermissions("apsbiz:bollard:add")
    public R save(@RequestBody BizBollardInfo bizBollardInfo) {
        return R.ok(bizBollardInfoService.save(bizBollardInfo));
    }

    /**
     * 修改缆柱表
     * @param bizBollardInfo 缆柱表
     * @return R
     */
    @Operation(summary = "修改缆柱表")
    //@SysLog("修改缆柱表")
    @PutMapping
    @RequiresPermissions("apsbiz:bollard:edit")
    public R updateById(@RequestBody BizBollardInfo bizBollardInfo) {
        return R.ok(bizBollardInfoService.updateOrCleanById(bizBollardInfo));
    }

    /**
     * 通过id删除缆柱表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id删除缆柱表")
    //@SysLog("通过id删除缆柱表")
    @DeleteMapping("/{id}")
    @RequiresPermissions("apsbiz:bollard:del")
    public R removeById(@PathVariable String id) {
        return R.ok(bizBollardInfoService.deleteById(id));
    }

	//自定义接口//////////////////////////////////////////////////
}
