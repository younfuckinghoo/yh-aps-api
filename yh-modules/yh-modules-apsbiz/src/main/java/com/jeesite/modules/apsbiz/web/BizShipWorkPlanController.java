package com.jeesite.modules.apsbiz.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeesite.common.base.R;
import com.jeesite.common.data.DataUtil;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.modules.algorithm.entity.*;
import com.jeesite.modules.algorithm.service.*;
import com.jeesite.modules.apsbiz.entity.*;
import com.jeesite.modules.apsbiz.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/bizShipWorkPlan")
@Tag(name = "bizShipWorkPlan - 作业计划")
public class BizShipWorkPlanController {
    private final BizShipWorkPlanService bizShipWorkPlanService;

    private final BizShipForecastService bizShipForecastService;

    private final BizShipRealTimeService bizShipRealTimeService;

    private final BizShipInfoService bizShipInfoService;

    private final BizShipPlanService bizShipPlanService;

    private final BizBerthInfoService bizBerthInfoService;

    private final DockService dockService;
    private final IAlgShipWorkShiftService algShipWorkShiftService;

    private final IAlgShipSiloArrangeService algShipSiloArrangeService;

    private final IAlgShipYardArrangeService algShipYardArrangeService;
    private final IAlgShipMachineAllocService algShipMachineAllocService;
    /**
     * 分页查询
     * @param bizShipWorkPlan 作业计划
     * @param page 分页-页
     * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:workplan:view")
    public R getAlgShipWorkPlanPage(BizShipWorkPlan bizShipWorkPlan, Integer page, Integer size) {
        if(null == bizShipWorkPlan.getPlanTime()){
            bizShipWorkPlan.setPlanTime(dockService.getPlanDate());
        }
        bizShipWorkPlan.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE14.getStatus(), AlgorithmEnum.STATE15.getStatus()));
        // 查询船舶信息表维护长宽字段
        IPage<BizShipWorkPlan> pageRes = bizShipWorkPlanService.queryPage(bizShipWorkPlan, page, size);
        if(CollUtil.isNotEmpty(pageRes.getRecords())){
            pageRes.getRecords().forEach(obj -> {
                BizShipForecast bizShipForecast = bizShipForecastService.infoByVoyageNo(obj.getVoyageNo());
                AlgShipSiloArrange algShipSiloArrange = new AlgShipSiloArrange();
                algShipSiloArrange.setVoyageNo(obj.getVoyageNo());
                List<AlgShipSiloArrange> siloList = algShipSiloArrangeService.queryList(algShipSiloArrange);
                AlgShipYardArrange algShipyardArrange = new AlgShipYardArrange();
                algShipyardArrange.setVoyageNo(obj.getVoyageNo());
                List<AlgShipYardArrange> yardList = algShipYardArrangeService.queryList(algShipyardArrange);
                AlgShipWorkShift shipWorkShift = new AlgShipWorkShift();
                shipWorkShift.setShipWorkPlanId(obj.getId());
                List<AlgShipWorkShift> workShiftList = algShipWorkShiftService.queryList(shipWorkShift);
                AlgShipMachineAlloc machineAlloc = new AlgShipMachineAlloc();
                machineAlloc.setVoyageNo(obj.getVoyageNo());
                List<AlgShipMachineAlloc> machineAllocList = algShipMachineAllocService.queryList(machineAlloc);
                obj.setYardList(yardList);
                obj.setSiloList(siloList);
                obj.setShiftList(workShiftList);
                obj.setMachineList(machineAllocList);
                if(null != bizShipForecast){
                    obj.setCarryWeight(bizShipForecast.getCarryWeight());
                    obj.setCargoSubTypeCode(bizShipForecast.getCargoSubTypeCode());
                    obj.setCargoTypeName(bizShipForecast.getCargoTypeName());
                    obj.setCargoOwner(bizShipForecast.getCargoOwner());
                    obj.setAgentCompany(bizShipForecast.getAgentCompany());
                    obj.setTradeType(bizShipForecast.getTradeType());
                    obj.setLoadUnload(bizShipForecast.getLoadUnload());
                    BizShipPlan bizShipPlan = bizShipPlanService.getByForecastId(bizShipForecast.getId());
                    if(null != bizShipPlan){
                        obj.setPlanBerthTime(bizShipPlan.getPlanBerthTime());
                    }

                    BizShipInfo bizShipInfo = bizShipInfoService.infoByCode(bizShipForecast.getShipCode());
                    if(null != bizShipInfo){
                        obj.setShipLength(Convert.toStr(bizShipInfo.getShipLength()));
                        obj.setShipWidth(Convert.toStr(bizShipInfo.getShipWidth()));
                        obj.setCabinNum(bizShipInfo.getCabinNum());
                    }
                }
            });
        }
        return R.ok(pageRes);
    }
    /**
     * 新增作业计划
     * @param bizShipWorkPlan 作业计划
     * @return R
     */
    @Operation(summary = "新增作业计划")
    @PostMapping
    @RequiresPermissions("apsbiz:workplan:add")
    public R addPlan(@RequestBody BizShipWorkPlan bizShipWorkPlan) {
        BizShipRealTime bizShipRealTime = bizShipRealTimeService.infoByVoyageNo(bizShipWorkPlan.getVoyageNo());
        if(null == bizShipRealTime){
            return R.failed_null("id");
        }
        bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE14.getStatus());
        bizShipWorkPlan.setPlanTime(DateUtil.beginOfDay(new Date()));
        if(bizShipWorkPlanService.save(bizShipWorkPlan)){
            bizShipRealTimeService.updateOrCleanById(bizShipRealTime);
            bizShipWorkPlanService.updateOtherInfo(bizShipWorkPlan, bizShipWorkPlan);
            return R.ok();
        }
        return R.failed();
    }

    /**
     * 修改 作业计划
     * @param bizShipWorkPlan 作业计划
     * @return R
     */
    @Operation(summary = "修改 作业计划")
    @PutMapping
    @RequiresPermissions("apsbiz:workplan:edit")
    public R updateById(@RequestBody BizShipWorkPlan bizShipWorkPlan) {
        if(StrUtil.isEmpty(bizShipWorkPlan.getId())){
            return R.failed_empty("id");
        }
        BizShipWorkPlan base = bizShipWorkPlanService.infoById(bizShipWorkPlan.getId());
        if(null == base){
            return R.failed_null("id");
        }

        BizShipRealTime bizShipRealTime = bizShipRealTimeService.infoByVoyageNo(base.getVoyageNo());
        if(null == bizShipRealTime){
            return R.failed_null("id");
        }

        if(bizShipWorkPlanService.updateOrCleanById(bizShipWorkPlan)){
            bizShipWorkPlanService.updateOtherInfo(bizShipWorkPlan, base);
            return R.ok();
        }
        return R.failed();
    }

    /**
     * 锁定 作业计划
     * @param id 船期ID
     * @return R
     */
    @Operation(summary = "锁定 作业计划")
    @PostMapping("lock")
    @RequiresPermissions("apsbiz:workplan:lock")
    public R lock(String id) {
        // 1. 判断ID是否为空
        if(id == null){
            return R.failed_empty("id");
        }
        // 2. 判断ID是存在
        BizShipWorkPlan bizShipWorkPlan = bizShipWorkPlanService.infoById(id);
        if(null == bizShipWorkPlan){
            return R.failed_null("id");
        }

        BizShipRealTime bizShipRealTime = bizShipRealTimeService.infoByVoyageNo(bizShipWorkPlan.getVoyageNo());
        if(null == bizShipRealTime){
            return R.failed_null("id");
        }
        // 3. 判断ID是否可锁定 作业计划
        if(!AlgorithmEnum.STATE14.getStatus().equals(bizShipRealTime.getAlgorithmState())){
            return R.failed_biz("当前状态不可锁定");
        }

        // 4. 锁定 作业计划
        bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE15.getStatus());
        bizShipWorkPlan.setAlgorithmState(AlgorithmEnum.STATE15.getStatus());
        if(bizShipRealTimeService.updateOrCleanById(bizShipRealTime)){
            bizShipWorkPlanService.updateOrCleanById(bizShipWorkPlan);
            return R.ok();
        }
        return R.failed();
    }

    /**
     * 取消锁定 作业计划
     * @param id 船期ID
     * @return R
     */
    @RequiresPermissions("apsbiz:workplan:lock")
    @PostMapping("unlock")
//    @RequiresPermissions("base_bizshipplantemp_lock')")
    public R unlock(String id) {
        // 1. 判断ID是否为空
        if(id == null){
            return R.failed_empty("id");
        }
        // 2. 判断ID是存在
        BizShipWorkPlan bizShipWorkPlan = bizShipWorkPlanService.infoById(id);
        if(null == bizShipWorkPlan){
            return R.failed_null("id");
        }

        BizShipRealTime bizShipRealTime = bizShipRealTimeService.infoByVoyageNo(bizShipWorkPlan.getVoyageNo());
        if(null == bizShipRealTime){
            return R.failed_null("id");
        }
        // 3. 判断ID是否可取消锁定 作业计划
        if(!AlgorithmEnum.STATE15.getStatus().equals(bizShipRealTime.getAlgorithmState())){
            return R.failed_biz("当前状态不可锁定");
        }

        // 4. 取消锁定 作业计划
        bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE14.getStatus());
        bizShipWorkPlan.setAlgorithmState(AlgorithmEnum.STATE14.getStatus());
        if(bizShipRealTimeService.updateOrCleanById(bizShipRealTime)){
            bizShipWorkPlanService.updateOrCleanById(bizShipWorkPlan);
            return R.ok();
        }
        return R.failed();
    }

    /**
     * 作业计划
     * @param id 作业计划ID
     * @return R
     */
    @Operation(summary = "删除 作业计划")
    @DeleteMapping
    @RequiresPermissions("apsbiz:workplan:del")
    public R delById(String id) {
        return bizShipWorkPlanService.deleteById(id);
    }

    /**
     * 导出 作业计划
     * @return
     */
    @Operation(summary = "导出 作业计划")
    @GetMapping("/export")
    @RequiresPermissions("apsbiz:workplan:export")
    public R exportSchedulingPlan(BizShipWorkPlan bizShipWorkPlan, HttpServletResponse response) {

        if(null == bizShipWorkPlan.getPlanTime()){
            bizShipWorkPlan.setPlanTime(dockService.getPlanDate());
        }
        bizShipWorkPlan.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE14.getStatus(), AlgorithmEnum.STATE15.getStatus()));
        // 查询船舶信息表维护长宽字段
        List<BizShipWorkPlan> list = bizShipWorkPlanService.queryList(bizShipWorkPlan);
        if(CollUtil.isNotEmpty(list)){
            // 船舶预报 列表
            BizShipForecast form1 = new BizShipForecast();
            form1.setVoyageNoIn(list.stream().map(BizShipWorkPlan::getVoyageNo).collect(Collectors.joining(",")));
            List<BizShipForecast> forecastList = bizShipForecastService.queryList(form1);
            if(CollUtil.isNotEmpty(forecastList)){
                Map<Object, BizShipForecast> forecastMap = DataUtil.list2map(forecastList, "voyageNo");

                // 泊位 列表
                BizBerthInfo form2 = new BizBerthInfo();
                form2.setBerthNameIn(list.stream().map(BizShipWorkPlan::getBerthNo).collect(Collectors.joining(",")));
                List<BizBerthInfo> berthInfoList = bizBerthInfoService.queryList(form2);
                Map<Object, BizBerthInfo> berthInfoMap = DataUtil.list2map(berthInfoList, "berthName");

                // 船舶列表
                BizShipInfo form3 = new BizShipInfo();
                form3.setShipCodeIn(forecastList.stream().map(BizShipForecast::getShipCode).collect(Collectors.joining(",")));
                List<BizShipInfo> shipInfoList = bizShipInfoService.queryList(form3);
                Map<Object, BizShipInfo> shipInfoMap = DataUtil.list2map(shipInfoList, "shipCode");

                // 船舶列表
                BizShipPlan form4 = new BizShipPlan();
                form4.setShipForcastIdIn(forecastList.stream().map(BizShipForecast::getId).collect(Collectors.joining(",")));
                List<BizShipPlan> shipPlanList = bizShipPlanService.queryList(form4);
                Map<Object, BizShipPlan> shipPlanMap = DataUtil.list2map(shipPlanList, "shipForcastId");



                list.forEach(obj -> {
                    if(StrUtil.isNotEmpty(obj.getVoyageNo())){
                        BizShipForecast bizShipForecast = forecastMap.get(obj.getVoyageNo());

                        if(null != bizShipForecast){
                            // 泊位
                            BizBerthInfo bizBerthInfo = berthInfoMap.get(obj.getBerthNo());
                            if(null != bizBerthInfo){
                                obj.setBerth(bizBerthInfo.getBerthName()+"("+bizBerthInfo.getBerthNo()+")");
                            }

                            // 缆柱
                            if(StrUtil.isAllNotEmpty(obj.getHeadBollardId(), obj.getTailBollardId())){
                                obj.setBollardNo(obj.getHeadBollardId() + " - " + obj.getTailBollardId());
                            }

                            // 船长 船宽
                            BizShipInfo bizShipInfo = shipInfoMap.get(bizShipForecast.getShipCode());
                            if(null != bizShipInfo){
                                obj.setShipLength(Convert.toStr(bizShipInfo.getShipLength()));
                                obj.setCabinNum(bizShipInfo.getCabinNum());
                            }

                            obj.setCarryWeight(bizShipForecast.getCarryWeight());
                            obj.setCargoSubTypeCode(bizShipForecast.getCargoSubTypeCode());
                            obj.setCargoTypeName(bizShipForecast.getCargoTypeName());
                            obj.setCargoOwner(bizShipForecast.getCargoOwner());
                            obj.setWorkingCompany(bizShipForecast.getWorkingCompany());
                            obj.setAgentCompany(bizShipForecast.getAgentCompany());

                            BizShipPlan bizShipPlan = shipPlanMap.get(bizShipForecast.getId());
                            if(null != bizShipPlan){
                                obj.setPlanBerthTime(bizShipPlan.getPlanBerthTime());
                            }

                        }
                    }
                });
            }
        }
        String fileName = "船舶昼夜作业计划" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try(ExcelExport ee = new ExcelExport("船舶昼夜作业计划", BizShipWorkPlan.class)){
            ee.setDataList(list).write(response, fileName);
        }
        return R.ok();
    }
}
