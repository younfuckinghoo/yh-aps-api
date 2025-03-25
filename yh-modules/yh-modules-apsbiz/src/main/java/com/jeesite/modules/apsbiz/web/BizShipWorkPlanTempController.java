package com.jeesite.modules.apsbiz.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeesite.common.base.R;
import com.jeesite.common.data.DataUtil;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.apsbiz.entity.*;
import com.jeesite.modules.apsbiz.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/bizShipWorkPlanTemp")
@Tag(name = "bizShipWorkPlanTemp - (预)作业计划")
public class BizShipWorkPlanTempController {
    private final BizShipWorkPlanTempService bizShipWorkPlanTempService;

    private final BizShipForecastService bizShipForecastService;

    private final BizShipRealTimeService bizShipRealTimeService;

    private final BizShipInfoService bizShipInfoService;

    private final BizShipPlanService bizShipPlanService;

    private final BizBerthInfoService bizBerthInfoService;

    private final DockService dockService;


    /**
     * 分页查询
     * @param bizShipWorkPlanTemp (预)作业计划
     * @param page 分页-页
     * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:workplantemp:view")
    public R getAlgShipWorkPlanTempPage(BizShipWorkPlanTemp bizShipWorkPlanTemp, Integer page, Integer size) {

        // 补偿计划日期
        if(null == bizShipWorkPlanTemp.getPlanTime()){
            bizShipWorkPlanTemp.setPlanTime(dockService.getPlanDate());
        }
        bizShipWorkPlanTemp.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE12.getStatus(), AlgorithmEnum.STATE13.getStatus()));
        // 查询船舶信息表维护长宽字段
        IPage<BizShipWorkPlanTemp> pageRes = bizShipWorkPlanTempService.queryPage(bizShipWorkPlanTemp, page, size);
        if(CollUtil.isNotEmpty(pageRes.getRecords())){
            pageRes.getRecords().forEach(obj -> {
                if(StrUtil.isNotEmpty(obj.getVoyageNo())){
                    BizShipForecast bizShipForecast = bizShipForecastService.infoByVoyageNo(obj.getVoyageNo());

                    if(null != bizShipForecast){
                        obj.setCarryWeight(bizShipForecast.getCarryWeight());
                        obj.setCargoSubTypeCode(bizShipForecast.getCargoSubTypeCode());
                        obj.setCargoTypeName(bizShipForecast.getCargoTypeName());
                        obj.setCargoOwner(bizShipForecast.getCargoOwner());
                        obj.setAgentCompany(bizShipForecast.getAgentCompany());

                        BizShipPlan bizShipPlan = bizShipPlanService.getByForecastId(bizShipForecast.getId());
                        if(null != bizShipPlan){
                            obj.setPlanBerthTime(bizShipPlan.getPlanBerthTime());
                        }

                        BizShipInfo bizShipInfo = bizShipInfoService.infoByCode(bizShipForecast.getShipCode());
                        if(null != bizShipInfo){
                            obj.setShipLength(Convert.toStr(bizShipInfo.getShipLength()));
                            obj.setCabinNum(bizShipInfo.getCabinNum());
                        }
                    }
                }
            });
        }
        return R.ok(pageRes);
    }

    /**
     * 锁定排泊计划
     * @param id 船期ID
     * @return R
     */
    @Operation(summary = "锁定(预)作业计划")
    @PostMapping("lock")
    @RequiresPermissions("apsbiz:workplantemp:lock")
    public R lock(String id) {
        // 1. 判断ID是否为空
        if(id == null){
            return R.failed_empty("id");
        }
        // 2. 判断ID是存在
        BizShipWorkPlanTemp bizShipWorkPlanTemp = bizShipWorkPlanTempService.infoById(id);
        if(null == bizShipWorkPlanTemp){
            return R.failed_null("id");
        }
        BizShipRealTime bizShipRealTime = bizShipRealTimeService.infoByVoyageNo(bizShipWorkPlanTemp.getVoyageNo());
        if(null == bizShipRealTime){
            return R.failed_null("id");
        }
        // 3. 判断ID是否可锁定 (预)作业计划
        if(!AlgorithmEnum.STATE12.getStatus().equals(bizShipRealTime.getAlgorithmState())){
            return R.failed_biz("当前状态不可锁定");
        }

        // 4. 锁定 (预)作业计划
        bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE13.getStatus());
        if(bizShipRealTimeService.updateOrCleanById(bizShipRealTime)){
            return R.ok();
        }
        return R.failed();
    }

    /**
     * 取消锁定(预)作业计划
     * @param id 船期ID
     * @return R
     */
    @Operation(summary = "取消锁定(预)作业计划")
    @PostMapping("unlock")
    @RequiresPermissions("apsbiz:workplantemp:lock")
    public R unlock(String id) {
        // 1. 判断ID是否为空
        if(id == null){
            return R.failed_empty("id");
        }
        // 2. 判断ID是存在
        BizShipWorkPlanTemp bizShipWorkPlanTemp = bizShipWorkPlanTempService.infoById(id);
        if(null == bizShipWorkPlanTemp){
            return R.failed_null("id");
        }
        BizShipRealTime bizShipRealTime = bizShipRealTimeService.infoByVoyageNo(bizShipWorkPlanTemp.getVoyageNo());
        if(null == bizShipRealTime){
            return R.failed_null("id");
        }
        // 3. 判断ID是否可取消锁定 (预)作业计划
        if(!AlgorithmEnum.STATE13.getStatus().equals(bizShipRealTime.getAlgorithmState())){
            return R.failed_biz("当前状态不可取消锁定");
        }

        // 4. 取消锁定 (预)作业计划
        bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE12.getStatus());
        if(bizShipRealTimeService.updateOrCleanById(bizShipRealTime)){
            return R.ok();
        }
        return R.failed();
    }

    /**
     * 修改(预)作业计划
     * @param bizShipWorkPlanTemp (预)作业计划
     * @return R
     */
    @Operation(summary = "修改(预)作业计划")
    @PutMapping
    @RequiresPermissions("apsbiz:workplantemp:edit")
    public R updateById(@RequestBody BizShipWorkPlanTemp bizShipWorkPlanTemp) {
        if(StrUtil.isEmpty(bizShipWorkPlanTemp.getId())){
            return R.failed_empty("id");
        }
        BizShipWorkPlanTemp base = bizShipWorkPlanTempService.infoById(bizShipWorkPlanTemp.getId());
        if(null == base){
            return R.failed_null("id");
        }
        BizShipRealTime bizShipRealTime = bizShipRealTimeService.infoByVoyageNo(base.getVoyageNo());
        if(null == bizShipRealTime){
            return R.failed_null("id");
        }
        // 判断ID是否可修改
        if(!AlgorithmEnum.STATE12.getStatus().equals(bizShipRealTime.getAlgorithmState())){
            return R.failed_biz("当前状态不可修改");
        }

        // 维护不可更改项目
        bizShipWorkPlanTemp.setVoyageNo(base.getVoyageNo());

        if(bizShipWorkPlanTempService.updateOrCleanById(bizShipWorkPlanTemp)){
            return R.ok();
        }
        return R.failed();
    }


    /**
     * 删除 (预)作业计划
     * @param id 预排泊记录ID
     * @return R
     */
    @Operation(summary = "删除(预)作业计划")
    @DeleteMapping
    @RequiresPermissions("apsbiz:workplantemp:del")
    public R delById(String id) {
        return bizShipWorkPlanTempService.deleteById(id);
    }

    /**
     * 计划确认
     * @return R
     */
    @Operation(summary = "计划确认")
    @PostMapping("submit")
    @RequiresPermissions("apsbiz:workplantemp:submit")
    public R submit(String ids) {
        return bizShipWorkPlanTempService.submit(ids);
    }

    /**
     * 导出排泊计划
     * @return
     */
    @Operation(summary = "导出(预)作业计划")
    @GetMapping("/export")
    @RequiresPermissions("apsbiz:workplantemp:export")
    public R exportSchedulingPlan(BizShipWorkPlanTemp bizShipWorkPlanTemp, HttpServletResponse response) {
        if (null == bizShipWorkPlanTemp.getPlanTime()) {
            bizShipWorkPlanTemp.setPlanTime(dockService.getPlanDate());
        }
        bizShipWorkPlanTemp.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE12.getStatus(), AlgorithmEnum.STATE13.getStatus()));
        // 查询船舶信息表维护长宽字段
        List<BizShipWorkPlanTemp> list = bizShipWorkPlanTempService.queryList(bizShipWorkPlanTemp);

        if (CollUtil.isNotEmpty(list)) {
            // 船舶预报 列表
            BizShipForecast form1 = new BizShipForecast();
            form1.setVoyageNoIn(list.stream().map(BizShipWorkPlanTemp::getVoyageNo).collect(Collectors.joining(",")));
            List<BizShipForecast> forecastList = bizShipForecastService.queryList(form1);
            Map<Object, BizShipForecast> forecastMap = DataUtil.list2map(forecastList, "voyageNo");

            // 泊位 列表
            BizBerthInfo form2 = new BizBerthInfo();
            form2.setBerthNameIn(list.stream().map(BizShipWorkPlanTemp::getBerthNo).collect(Collectors.joining(",")));
            List<BizBerthInfo> berthInfoList = bizBerthInfoService.queryList(form2);
            Map<Object, BizBerthInfo> berthInfoMap = DataUtil.list2map(berthInfoList, "berthName");

            // 船舶列表
            BizShipInfo form3 = new BizShipInfo();
            form3.setShipCodeIn(list.stream().map(BizShipWorkPlanTemp::getShipCode).collect(Collectors.joining(",")));
            List<BizShipInfo> shipInfoList = bizShipInfoService.queryList(form3);
            Map<Object, BizShipInfo> shipInfoMap = DataUtil.list2map(shipInfoList, "shipCode");

            // 靠离泊计划列表
            BizShipPlan form4 = new BizShipPlan();
            form4.setShipForcastIdIn(forecastList.stream().map(BizShipForecast::getId).collect(Collectors.joining(",")));
            List<BizShipPlan> shipPlanList = bizShipPlanService.queryList(form4);
            Map<Object, BizShipPlan> shipPlanMap = DataUtil.list2map(shipPlanList, "shipForcastId");


            list.forEach(obj -> {
                // 泊位
                BizBerthInfo bizBerthInfo = berthInfoMap.get(obj.getBerthNo());
                if (null != bizBerthInfo) {
                    obj.setBerth(bizBerthInfo.getBerthName() + "(" + bizBerthInfo.getBerthNo() + ")");
                }

                // 缆柱
                if (StrUtil.isAllNotEmpty(obj.getHeadBollardNo(), obj.getTailBollardNo())) {
                    obj.setBollardNo(obj.getHeadBollardNo() + " - " + obj.getTailBollardNo());
                }

                // 船长 船宽
                BizShipInfo bizShipInfo = shipInfoMap.get(bizShipWorkPlanTemp.getShipCode());
                if (null != bizShipInfo) {
                    obj.setShipLength(Convert.toStr(bizShipInfo.getShipLength()));
                    obj.setCabinNum(bizShipInfo.getCabinNum());
                }

                // 货种 货主 代理公司 作业公司
                BizShipForecast bizShipForecast = forecastMap.get(obj.getVoyageNo());
                if (null != bizShipForecast) {
                    obj.setCargoSubTypeCode(bizShipForecast.getCargoSubTypeCode());
                    obj.setCargoTypeName(bizShipForecast.getCargoTypeName());
                    obj.setCargoOwner(bizShipForecast.getCargoOwner());
                    obj.setWorkingCompany(bizShipForecast.getWorkingCompany());
                    obj.setAgentCompany(bizShipForecast.getAgentCompany());

                    // 计划靠泊时间
                    BizShipPlan bizShipPlan = shipPlanMap.get(bizShipForecast.getId());
                    if (null != bizShipPlan) {
                        obj.setPlanBerthTime(bizShipPlan.getPlanBerthTime());
                    }
                }
            });
        }
        String fileName = "智能预排计划" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try (ExcelExport ee = new ExcelExport("智能预排计划", BizShipWorkPlanTemp.class)) {
            ee.setDataList(list).write(response, fileName);
        }
        return R.ok();
    }
}
