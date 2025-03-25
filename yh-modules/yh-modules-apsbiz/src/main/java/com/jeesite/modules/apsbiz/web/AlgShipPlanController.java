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
import com.jeesite.modules.apsbiz.entity.BizBerthInfo;
import com.jeesite.modules.sys.utils.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 靠离泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bizshipplan")
@Tag(name = "bizshipplan - 靠离泊计划")
public class AlgShipPlanController {

    private final BizShipPlanService bizShipPlanService;

    private final BizShipInfoService bizShipInfoService;

    private final BizBerthInfoService bizBerthInfoService;

    private final BizShipForecastService bizShipForecastService;

    private final BizBollardInfoService bizBollardInfoService;

    /**
     * 全部查询
     * @param bizShipPlan 靠离泊计划
     * @return
     */
    @Operation(summary = "全部查询")
    @GetMapping("/list")
    @RequiresPermissions("apsbiz:shipplan:view")
    public R getAlgShipPlanList(BizShipPlan bizShipPlan) {
        // 查询 [4手动排泊未提交][5已提交待审核][6已提交审核通过][7已提交审核不通过]
        bizShipPlan.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE4.getStatus(), AlgorithmEnum.STATE5.getStatus(), AlgorithmEnum.STATE6.getStatus(), AlgorithmEnum.STATE7.getStatus()));
        return R.ok(bizShipPlanService.queryList(bizShipPlan));
    }

    /**
     * 分页查询
     * @param bizShipPlan 靠离泊计划
     * @param page 分页-页
     * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:shipplan:view")
    public R getAlgShipPlanPage(BizShipPlan bizShipPlan, Integer page, Integer size) {
        // 查询 [4手动排泊未提交][5已提交待审核][6已提交审核通过][7已提交审核不通过]
        bizShipPlan.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE4.getStatus(), AlgorithmEnum.STATE5.getStatus(), AlgorithmEnum.STATE6.getStatus(), AlgorithmEnum.STATE7.getStatus()));

        IPage<BizShipPlan> pageRes = bizShipPlanService.queryPage(bizShipPlan, page, size);
//        if(CollUtil.isNotEmpty(pageRes.getRecords())){
//            pageRes.getRecords().forEach(obj -> {
//                BizShipInfo bizShipInfo = bizShipInfoService.infoByCode(obj.getShipCode());
//                if(null != bizShipInfo){
//                    obj.setShipLength(Convert.toStr(bizShipInfo.getShipLength()));
//                    obj.setShipWidth(Convert.toStr(bizShipInfo.getShipWidth()));
//                }
//            });
//        }
        return R.ok(pageRes);
    }

    /**
     * 修改靠离泊计划
     * @param bizShipPlan 靠离泊计划
     * @return R
     */
    @Operation(summary = "修改靠离泊计划")
    @PutMapping
    @RequiresPermissions("apsbiz:shipplan:edit")
    public R updateById(@RequestBody BizShipPlan bizShipPlan) {
        // 校验必填项
        if(StrUtil.isEmpty(bizShipPlan.getId())){
            return R.failed_empty("id");
        }
        // 校验数据是否存在
        BizShipPlan base = bizShipPlanService.infoById(bizShipPlan.getId());
        if(null == base){
            return R.failed_null("id");
        }
        BizShipForecast bizShipForecast = bizShipForecastService.infoById(base.getShipForcastId());
        if(null == bizShipForecast){
            return R.failed_null("id");
        }

        // 判断数据状态是否可修改-靠离泊计划中只有[4手动排泊未提交][7不通过]可修改
//        if(!AlgorithmEnum.STATE4.getStatus().equals(bizShipForecast.getAlgorithmState())
//                && !AlgorithmEnum.STATE7.getStatus().equals(bizShipForecast.getAlgorithmState())
//            ){
//            return R.failed_biz("当前状态不可修改");
//        }

        // 维护不可更改项目
        bizShipPlan.setShipForcastId(base.getShipForcastId());
        bizShipPlan.setPlanStatus(base.getPlanStatus());
        bizShipPlan.setCreator(base.getCreator());
        bizShipPlan.setCreateTime(base.getCreateTime());
        bizShipPlan.setModifier(UserUtils.getUser().getUserName());
        bizShipPlan.setModifyTime(new Date());

        // 执行修改
        if(bizShipPlanService.updateShipPlan(bizShipPlan)){
            return R.ok();
        }
        return R.failed();
    }

    /**
     * 删除靠离泊计划
     * @param ids 靠离泊计划ID
     * @return R
     */
    @Operation(summary = "删除靠离泊计划")
    @DeleteMapping
    @RequiresPermissions("apsbiz:shipplan:del")
    public R delById(String ids) {
        return bizShipPlanService.deleteByIds(ids);
    }



    /**
     * 新增靠离泊计划
     * @param bizShipPlan 靠离泊计划
     * @return R
     */
    @Operation(summary = "新增靠离泊计划")
    @PostMapping("add")
    @RequiresPermissions("apsbiz:shipplan:add")
    public R add(@RequestBody BizShipPlan bizShipPlan) {
        return bizShipPlanService.addPlan(bizShipPlan, false);
    }


    /**
     * 新增靠离泊计划 并 提交
     * @param bizShipPlan 靠离泊计划
     * @return R
     */
    @Operation(summary = "新增靠离泊计划并提交")
    @PostMapping("addAndSubmit")
    @RequiresPermissions("apsbiz:shipplan:submit")
    public R delById(@RequestBody BizShipPlan bizShipPlan) {
        return bizShipPlanService.addPlan(bizShipPlan, true);
    }

    /**
     * 提交审核
     * @return R
     */
    @Operation(summary = "提交审核")
    @PostMapping("submit")
    @RequiresPermissions("apsbiz:shipplan:submit")
    public R submit(String ids) {
        return bizShipPlanService.submit(ids);
    }

    /**
     * 导出靠离泊计划
     * @param bizShipPlan 靠离泊计划
     * @return
     */
    @Operation(summary = "导出靠离泊计划")
    @GetMapping("/export")
    @RequiresPermissions("apsbiz:shipplan:export")
    public R exportSchedulingPlan(BizShipPlan bizShipPlan, HttpServletResponse response) {
        // 查询未提交 和 已锁定
        bizShipPlan.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE4.getStatus(), AlgorithmEnum.STATE5.getStatus(), AlgorithmEnum.STATE6.getStatus(), AlgorithmEnum.STATE7.getStatus()));
        List<BizShipPlan> list = bizShipPlanService.queryList(bizShipPlan);

        if(CollUtil.isNotEmpty(list)){
            // 泊位
            BizBerthInfo form1 = new BizBerthInfo();
//            form1.setBerthNoIn(list.stream().map(BizShipPlan::getBerthNo).collect(Collectors.joining(",")));
            List<BizBerthInfo> berthInfoList = bizBerthInfoService.queryList(form1);
            Map<Object, BizBerthInfo> berthInfoMap = DataUtil.list2map(berthInfoList, "berthNo");

            // 缆柱
            BizBollardInfo form2 = new BizBollardInfo();
            List<String> bollardList = new ArrayList<>();
            bollardList.addAll(list.stream().map(BizShipPlan::getHeadBollardId).collect(Collectors.toList()));
            bollardList.addAll(list.stream().map(BizShipPlan::getTailBollardId).collect(Collectors.toList()));
            form2.setIdIn(CollUtil.join(bollardList, ","));
            List<BizBollardInfo> bollardInfoList = bizBollardInfoService.queryList(form2);
            Map<Object, BizBollardInfo> bollardInfoMap = DataUtil.list2map(bollardInfoList, "id");

            // 船舶
//            BizShipInfo form3 = new BizShipInfo();
//            form3.setShipCodeIn(list.stream().map(BizShipPlan::getShipCode).collect(Collectors.joining(",")));
//            List<BizShipInfo> shipInfoList = bizShipInfoService.queryList(form3);
//            Map<Object, BizShipInfo> shipInfoMap = DataUtil.list2map(shipInfoList, "shipCode");

            list.forEach(obj -> {
                // 泊位
                BizBerthInfo bizBerthInfo = berthInfoMap.getOrDefault(obj.getBerthNo(), new BizBerthInfo());
                obj.setBerth(bizBerthInfo.getBerthName()+"("+bizBerthInfo.getBerthNo()+")");

                // 缆柱
                if(StrUtil.isAllNotEmpty(obj.getHeadBollardId(), obj.getTailBollardId())){
                    BizBollardInfo head = bollardInfoMap.getOrDefault(obj.getHeadBollardId(), new BizBollardInfo());
                    BizBollardInfo tail = bollardInfoMap.getOrDefault(obj.getTailBollardId(), new BizBollardInfo());

                    BizBerthInfo headBerth = berthInfoMap.getOrDefault(head.getBerthNo(), new BizBerthInfo());
                    BizBerthInfo tailBerth = berthInfoMap.getOrDefault(tail.getBerthNo(), new BizBerthInfo());

                    obj.setBollardNo(headBerth.getBerthName()+"("+head.getBollardNo()+") - " + tailBerth.getBerthName()+"("+tail.getBollardNo()+")");
                }

                // 船长 船宽
//                BizShipInfo bizShipInfo = shipInfoMap.get(obj.getShipCode());
//                if(null != bizShipInfo){
//                    obj.setShipLength(Convert.toStr(bizShipInfo.getShipLength()));
//                    obj.setShipWidth(Convert.toStr(bizShipInfo.getShipWidth()));
//                }

                // 装卸计划
                if(obj.getLoadUnload() == 1){
                    obj.setLoadUnloadWeight(obj.getLoadPlanWeight().toString());
                }
                if(obj.getLoadUnload() == 2){
                    obj.setLoadUnloadWeight(obj.getUnloadPlanWeight().toString());
                }
                if(obj.getLoadUnload() == 3){
                    obj.setLoadUnloadWeight(obj.getLoadPlanWeight().toString() + "/" + obj.getUnloadPlanWeight().toString());
                }
            });
        }

        String fileName = "船舶靠离泊计划" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try(ExcelExport ee = new ExcelExport("船舶靠离泊计划", BizShipPlanTemp.class)){
            ee.setDataList(list).write(response, fileName);
        }
        return R.ok();
    }
}
