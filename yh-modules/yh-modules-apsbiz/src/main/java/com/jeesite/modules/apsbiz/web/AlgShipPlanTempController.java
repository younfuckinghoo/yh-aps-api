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
 * (预)靠离泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bizshipplantemp")
@Tag(name = "bizshipplantemp - (预)靠离泊计划")
public class AlgShipPlanTempController {

    private final BizShipPlanTempService bizShipPlanTempService;

    private final BizShipForecastService bizShipForecastService;

    private final BizShipInfoService bizShipInfoService;

    private final BizBerthInfoService bizBerthInfoService;

    private final BizBollardInfoService bizBollardInfoService;


    /**
     * 全部查询
     * @param bizShipPlanTemp (预)靠离泊计划
     * @return
     */
    @Operation(summary = "全部查询")
    @GetMapping("/list")
    @RequiresPermissions("apsbiz:shipplantemp:view")
    public R getAlgShipPlanTempList(BizShipPlanTemp bizShipPlanTemp) {
        // 查询未提交 和 已锁定
        bizShipPlanTemp.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE2.getStatus(), AlgorithmEnum.STATE3.getStatus()));
        return R.ok(bizShipPlanTempService.queryList(bizShipPlanTemp));
    }

    /**
     * 分页查询
     * @param bizShipPlanTemp (预)靠离泊计划
     * @param page 分页-页
     * @param size 分页-数据量
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("apsbiz:shipplantemp:view")
    public R getAlgShipPlanTempPage(BizShipPlanTemp bizShipPlanTemp, Integer page, Integer size) {
        // 查询未提交 和 已锁定
        bizShipPlanTemp.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE2.getStatus(), AlgorithmEnum.STATE3.getStatus()));
        IPage<BizShipPlanTemp> pageRes = bizShipPlanTempService.queryPage(bizShipPlanTemp, page, size);
        return R.ok(pageRes);
    }

    /**
     * 锁定(预)靠离泊计划
     * @param id 船期ID
     * @return R
     */
    @Operation(summary = "锁定(预)靠离泊计划")
    @PostMapping("lock")
    @RequiresPermissions("apsbiz:shipplantemp:lock")
    public R lock(String id) {
        // 1. 判断ID是否为空
        if(id == null){
            return R.failed_empty("id");
        }
        // 2. 判断ID是存在
        BizShipPlanTemp bizShipPlanTemp = bizShipPlanTempService.infoById(id);
        if(null == bizShipPlanTemp){
            return R.failed_null("id");
        }
        BizShipForecast bizShipForecast = bizShipForecastService.infoById(bizShipPlanTemp.getShipForcastId());
        if(null == bizShipForecast){
            return R.failed_null("id");
        }

        // 3. 判断 (预)靠离泊计划 是否可锁定
        if(!AlgorithmEnum.STATE2.getStatus().equals(bizShipForecast.getAlgorithmState())){
            return R.failed_biz("当前(预)靠离泊计划不可锁定");
        }

        // 5. 锁定 (预)靠离泊计划
        bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE3.getStatus());

        if(!bizShipForecastService.updateOrCleanById(bizShipForecast)){
            return R.failed();
        }
        return R.ok();
    }

    /**
     * 取消锁定(预)靠离泊计划
     * @param id 船期ID
     * @return R
     */
    @Operation(summary = "取消锁定(预)靠离泊计划")
    @PostMapping("unlock")
    @RequiresPermissions("apsbiz:shipplantemp:lock")
    public R unlock(String id) {
        // 1. 判断ID是否为空
        if(id == null){
            return R.failed_empty("id");
        }
        // 2. 判断ID是存在
        BizShipPlanTemp bizShipPlanTemp = bizShipPlanTempService.infoById(id);
        if(null == bizShipPlanTemp){
            return R.failed_null("id");
        }
        BizShipForecast bizShipForecast = bizShipForecastService.infoById(bizShipPlanTemp.getShipForcastId());
        if(null == bizShipForecast){
            return R.failed_null("id");
        }

        // 3. 判断 (预)靠离泊计划 是否可锁定
        if(!AlgorithmEnum.STATE3.getStatus().equals(bizShipForecast.getAlgorithmState())){
            return R.failed_biz("当前(预)靠离泊计划不可取消锁定");
        }

        // 5. 锁定 (预)靠离泊计划
        bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE2.getStatus());

        if(!bizShipForecastService.updateOrCleanById(bizShipForecast)){
            return R.failed();
        }
        return R.ok();
    }

    /**
     * 修改(预)靠离泊计划
     * @param bizShipPlanTemp (预)靠离泊计划
     * @return R
     */
    @Operation(summary = "修改(预)靠离泊计划")
    @PutMapping
    @RequiresPermissions("apsbiz:shipplantemp:edit")
    public R updateById(@RequestBody BizShipPlanTemp bizShipPlanTemp) {
        if(StrUtil.isEmpty(bizShipPlanTemp.getId())){
            return R.failed_empty("id");
        }
        BizShipPlanTemp base = bizShipPlanTempService.infoById(bizShipPlanTemp.getId());
        if(null == base){
            return R.failed_null("id");
        }
        BizShipForecast bizShipForecast = bizShipForecastService.infoById(base.getShipForcastId());
        if(null == bizShipForecast){
            return R.failed_null("id");
        }

        // 判断ID是否可修改
//        if(!AlgorithmEnum.STATE2.getStatus().equals(bizShipForecast.getAlgorithmState())){
//            return R.failed_biz("当前(预)靠离泊计划不可修改");
//        }

        // 维护不可更改项目
        bizShipPlanTemp.setShipForcastId(base.getShipForcastId());
        bizShipPlanTemp.setPlanStatus(base.getPlanStatus());
        bizShipPlanTemp.setCreator(base.getCreator());
        bizShipPlanTemp.setCreateTime(base.getCreateTime());
        bizShipPlanTemp.setModifier(UserUtils.getUser().getUserName());
        bizShipPlanTemp.setModifyTime(new Date());

        if(bizShipPlanTempService.updateShipPlan(bizShipPlanTemp)){
            return R.ok();
        }
        return R.failed();
    }


    /**
     * 删除预排泊
     * @param ids 预排泊记录ID
     * @return R
     */
    @Operation(summary = "删除预排泊")
    @DeleteMapping
    @RequiresPermissions("apsbiz:shipplantemp:del")
    public R delById(String ids) {
        return bizShipPlanTempService.deleteByIds(ids);
    }

    /**
     * 提交审核
     * @return R
     */
    @Operation(summary = "提交审核")
    @PostMapping("submit")
    @RequiresPermissions("apsbiz:shipplantemp:submit")
    public R submit(String ids) {
        return bizShipPlanTempService.submit(ids);
    }

    /**
     * 导出排泊计划
     * @return
     */
    @Operation(summary = "导出排泊计划")
    @GetMapping("/export")
    @RequiresPermissions("apsbiz:shipplantemp:export")
    public R exportSchedulingPlan(BizShipPlanTemp bizShipPlanTemp, HttpServletResponse response) {
        // 查询未提交 和 已锁定
        bizShipPlanTemp.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE2.getStatus(), AlgorithmEnum.STATE3.getStatus()));
        List<BizShipPlanTemp> list = bizShipPlanTempService.queryList(bizShipPlanTemp);

        if(CollUtil.isNotEmpty(list)){
            // 泊位
            BizBerthInfo form1 = new BizBerthInfo();
            form1.setBerthNoIn(list.stream().map(BizShipPlanTemp::getBerthNo).collect(Collectors.joining(",")));
            List<BizBerthInfo> berthInfoList = bizBerthInfoService.queryList(form1);
            Map<Object, BizBerthInfo> berthInfoMap = DataUtil.list2map(berthInfoList, "berthNo");

            // 缆柱
            BizBollardInfo form2 = new BizBollardInfo();
            List<String> bollardList = new ArrayList<>();
            bollardList.addAll(list.stream().map(BizShipPlanTemp::getHeadBollardId).collect(Collectors.toList()));
            bollardList.addAll(list.stream().map(BizShipPlanTemp::getTailBollardId).collect(Collectors.toList()));
            form2.setIdIn(CollUtil.join(bollardList, ","));
            List<BizBollardInfo> bollardInfoList = bizBollardInfoService.queryList(form2);
            Map<Object, BizBollardInfo> bollardInfoMap = DataUtil.list2map(bollardInfoList, "id");

            // 船舶
//            BizShipInfo form3 = new BizShipInfo();
//            form3.setShipCodeIn(list.stream().map(BizShipPlanTemp::getShipCode).collect(Collectors.joining(",")));
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

        String fileName = "智能预排计划" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
        try(ExcelExport ee = new ExcelExport("智能预排计划", BizShipPlanTemp.class)){
            ee.setDataList(list).write(response, fileName);
        }
        return R.ok();
    }








}
