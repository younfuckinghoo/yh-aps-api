package com.jeesite.modules.apsbiz.web;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeesite.common.base.R;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.modules.apsbiz.entity.BizShipForecast;
import com.jeesite.modules.apsbiz.entity.BizShipRealTime;
import com.jeesite.modules.apsbiz.service.BizShipForecastService;
import com.jeesite.modules.apsbiz.service.BizShipRealTimeService;
import com.jeesite.modules.apsbiz.service.DockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


/**
 * 在泊船舶动态数据
 *
 * @author zhudi
 * @date 2024-12-08 21:19:20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bizshiprealtime")
@Tag(name = "bizshiprealtime - 在泊船舶动态数据管理")
public class AlgShipRealTimeController {

    private final BizShipRealTimeService bizShipRealTimeService;

    private final BizShipForecastService bizShipForecastService;

    private final DockService dockService;

    /**
     * 分页查询
	 * @param bizShipRealTime 在泊船舶动态数据
	 * @param page 分页-页
	 * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:shiprealtime:view")
    public R getAlgShipRealTimePage(BizShipRealTime bizShipRealTime, Integer page, Integer size) {
        // 未完成工作（审核）
        bizShipRealTime.setIsFinish(0);
        // 审核通过（过滤）
//        bizShipRealTime.setForecastAlgorithmState(AlgorithmEnum.STATE6.getStatus());

        IPage<BizShipRealTime> ipage = bizShipRealTimeService.queryPage(bizShipRealTime, page, size);

        for(BizShipRealTime shipRealTime : ipage.getRecords()){
            BizShipForecast bizShipForecast = bizShipForecastService.infoByVoyageNo(shipRealTime.getVoyageNo());
            if(null != bizShipForecast){
                shipRealTime.setWorkingCompany(bizShipForecast.getWorkingCompany());
                shipRealTime.setAgentCompany(bizShipForecast.getAgentCompany());
                shipRealTime.setCargoTypeName(bizShipForecast.getCargoTypeName());
                shipRealTime.setCargoOwner(bizShipForecast.getCargoOwner());
                shipRealTime.setTradeType(bizShipForecast.getTradeType());
            }
        }
        return R.ok(ipage);
    }

    /**
     * 预排
     * @param ids 船期ID集合
     * @return
     */
    @Operation(summary = "预排")
    @GetMapping("/scheduling")
    @RequiresPermissions("apsbiz:shiprealtime:schedule")
    public R scheduling(String ids) {
        return bizShipRealTimeService.scheduling(ids);
    }

    /**
     * 取消预排
     * @param id 船期ID
     * @return
     */
    @Operation(summary = "取消预排")
    @GetMapping("/canelScheduling")
    @RequiresPermissions("apsbiz:shiprealtime:schedule")
    public R canelScheduling(String id) {
        return bizShipRealTimeService.canelScheduling(id);
    }

    /**
     * 智能排泊
     * @return
     */
    @Operation(summary = "智能排泊")
    @GetMapping("/apsScheduling")
    @RequiresPermissions("apsbiz:shiprealtime:aps")
    public R apsScheduling(Integer mode) {
        return bizShipRealTimeService.apsScheduling(mode);
    }


    /**
     * 更新在港船舶
     * @return
     */
    @Operation(summary = "更新在港船舶")
    @GetMapping("/refreshRealTime")
    public R refreshRealTime() {
        dockService.refreshRealTime();
        return R.ok();
    }

    //自定义接口//////////////////////////////////////////////////
}
