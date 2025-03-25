package com.jeesite.modules.apsbiz.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeesite.common.base.R;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.modules.apsbiz.entity.BizShipForecast;
import com.jeesite.modules.apsbiz.entity.BizShipInfo;
import com.jeesite.modules.apsbiz.service.BizShipForecastService;
import com.jeesite.modules.apsbiz.service.BizShipInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 船舶预报
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bizshipforecast")
@Tag(name = "bizshipforecast - 船舶预报")
public class AlgShipForecastController {

    private final BizShipForecastService bizShipForecastService;

    private final BizShipInfoService bizShipInfoService;

    /**
     * 列表
     * @param bizShipForecast 船舶预报
     * @return
     */
    @Operation(summary = "列表", description = "列表")
    @GetMapping("/list")
    @RequiresPermissions("apsbiz:forecast:view")
    public R getAlgShipForecastList(BizShipForecast bizShipForecast) {
        return R.ok(bizShipForecastService.queryList(bizShipForecast));
    }

    /**
     * 分页查询
	 * @param bizShipForecast 船舶预报
	 * @param page 分页-页
	 * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:forecast:view")
    public R getAlgShipForecastPage(BizShipForecast bizShipForecast, Integer page, Integer size) {
        // 查询船舶信息表维护长宽字段
        IPage<BizShipForecast> pageRes = bizShipForecastService.queryPage(bizShipForecast, page, size);
        if(CollUtil.isNotEmpty(pageRes.getRecords())){
            pageRes.getRecords().forEach(obj -> {
                BizShipInfo bizShipInfo = bizShipInfoService.infoByCode(obj.getShipCode());
                if(null != bizShipInfo){
                    obj.setShipLength(Convert.toStr(bizShipInfo.getShipLength()));
                    obj.setShipWidth(Convert.toStr(bizShipInfo.getShipWidth()));
                }
            });
        }
        return R.ok(pageRes);
    }

    /**
     * 预排
     * @param ids 船期ID
     * @return
     */
    @Operation(summary = "预排")
    @GetMapping("/scheduling")
    @RequiresPermissions("apsbiz:forecast:schedule")
    public R scheduling(String ids) {
        return bizShipForecastService.scheduling(ids);
    }

    /**
     * 取消预排
     * @param id 船期ID
     * @return
     */
    @Operation(summary = "取消预排")
    @GetMapping("/canelScheduling")
    @RequiresPermissions("apsbiz:forecast:schedule")
    public R canelScheduling(String id) {
        return bizShipForecastService.canelScheduling(id);
    }

    /**
     * 智能排泊
     * @return
     */
    @Operation(summary = "智能排泊")
    @RequiresPermissions("apsbiz:forecast:aps")
    @GetMapping("/apsScheduling")
    public R apsScheduling() {
        return bizShipForecastService.apsScheduling();
    }


	//自定义接口//////////////////////////////////////////////////
}
