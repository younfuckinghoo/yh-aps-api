
package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeesite.common.base.R;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.utils.MybatisPlusOracleUtils;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.algorithm.service.IPlanningService;
import com.jeesite.modules.apsbiz.entity.BizDailyWorkPlanTemp;
import com.jeesite.modules.apsbiz.entity.BizShipRealTime;
import com.jeesite.modules.apsbiz.mapper.BizShipRealTimeMapper;
import com.jeesite.modules.apsbiz.service.BizDailyWorkPlanTempService;
import com.jeesite.modules.apsbiz.service.BizShipRealTimeService;
import com.jeesite.modules.apsbiz.service.DockService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在泊船舶动态数据
 *
 * @author zhudi
 * @date 2024-12-08 21:19:20
 */
@Service
public class BizShipRealTimeServiceImpl extends YhServiceImpl<BizShipRealTimeMapper, BizShipRealTime> implements BizShipRealTimeService {

    @Resource
    private BizDailyWorkPlanTempService bizDailyWorkPlanTempService;
    @Resource
    private IPlanningService planningService;
    @Resource
    private DockService dockService;


    @Value("${default.page}")
    private Integer defPage;

    @Value("${default.size}")
    private Integer defSize;


    @Override
    public IPage<BizShipRealTime> queryPage(BizShipRealTime bizShipRealTime, Integer page, Integer size) {
        if (page == null) {
            page = defPage;
        }
        if (size == null) {
            size = defSize;
        }
        IPage<BizShipRealTime> p = new Page<>(page, size);
        QueryWrapper<BizShipRealTime> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(bizShipRealTime, "a");

        queryWrapper.eq(bizShipRealTime.getForecastAlgorithmState() != null, "B.ALGORITHM_STATE", bizShipRealTime.getForecastAlgorithmState());

        Long total = baseMapper.pageCount(queryWrapper);
        List<BizShipRealTime> list = new ArrayList<>();
        if (total > 0) {
            list = baseMapper.page(queryWrapper, (page - 1) * size, page * size);
        }

        IPage<BizShipRealTime> iPage = new Page<>();
        iPage.setSize(size);
        iPage.setCurrent(page);
        iPage.setPages((total / size) + (total % size > 0 ? 1 : 0));
        iPage.setTotal(total);
        iPage.setRecords(list);

        return iPage;
    }


    @Override
    public BizShipRealTime infoByVoyageNo(String voyageNo) {
        QueryWrapper<BizShipRealTime> queryWrapper = MybatisPlusUtils.getQueryWrapper(new BizShipRealTime(), null);
        queryWrapper.eq("VOYAGE_NO", voyageNo);
        return getOne(queryWrapper);
    }

    @Transactional
    @Override
    public R apsScheduling(Integer mode) {
        // 1. 判断当前预排数据是否满足智能排泊
        // 默认只查询 APS排泊预排(1), APS排泊未提交(2)  的数据
        BizShipRealTime form1 = new BizShipRealTime();
        form1.setIsFinish(0);
        form1.setAlgorithmStateIn(StrUtil.join(",", AlgorithmEnum.STATE11.getStatus(), AlgorithmEnum.STATE12.getStatus()));
        List<BizShipRealTime> list = queryList(form1);
        if (CollUtil.isEmpty(list)) {
            return R.ok();
        }
        // 2. 进行智能排泊（调用算法）
        R r = dockService.apsWorkScheduling(mode);
        if (!R.checkOk(r)) {
            return r;
        }

        // 3. 生成 预靠泊计划 + 调整预报船舶的算法状态
        List<BizDailyWorkPlanTemp> bizDailyWorkPlanTempList = new ArrayList<>();

        // 为了保证 入库顺序与realtime一致。对查出的内容反转
        list = CollUtil.reverse(list);
        list.forEach(bizShipRealTime -> {
            // 3.1 生成 预船舶计划
            bizDailyWorkPlanTempList.add(dockService.getWorkPlanTemp(bizShipRealTime));

            // 3.2 调整预报船舶的算法状态 为（已排泊）
            bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE12.getStatus());
        });

        // 4. 删除预排泊中的数据
        BizDailyWorkPlanTemp form2 = new BizDailyWorkPlanTemp();
        form2.setShipRealTimeIdIn(CollUtil.join(list.stream().map(BizShipRealTime::getId).collect(Collectors.toList()), ","));
        List<BizDailyWorkPlanTemp> oldAlgDailyWorkPlanTempList = bizDailyWorkPlanTempService.queryList(form2);

        boolean success = true;
        if (CollUtil.isNotEmpty(oldAlgDailyWorkPlanTempList)) {
            success = bizDailyWorkPlanTempService.removeBatchByIds(oldAlgDailyWorkPlanTempList);
        }

        // 4. 新增预排泊数据
        success = success && bizDailyWorkPlanTempService.saveBatch(bizDailyWorkPlanTempList);

        // 5.修改预报船舶状态
        success = success && updateBatchById(list);

        if (!success) {
            //手动强制回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.failed();
        }

        return R.ok();
    }

    @Override
    public R scheduling(String ids) {
        // 1. 判断ID是否为空
        if (StrUtil.isEmpty(ids)) {
            return R.failed_empty("ids");
        }
        // 2. 判断ID是存在
        BizShipRealTime form1 = new BizShipRealTime();
        form1.setIdIn(ids);
        List<BizShipRealTime> bizShipRealTimeList = queryList(form1);
        if (StrUtil.split(ids, ",").size() != bizShipRealTimeList.size()) {
            return R.failed_null("ids");
        }

        // 3. 判断ID是否可进行预排
        for (BizShipRealTime bizShipRealTime : bizShipRealTimeList) {
            if (!AlgorithmEnum.STATE10.getStatus().equals(bizShipRealTime.getAlgorithmState())) {
                return R.failed_biz("[" + bizShipRealTime.getShipNameCn() + "]当前状态不可进行预排");
            }
            // 4. 进行预排
            bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE11.getStatus());
        }

        if (updateBatchById(bizShipRealTimeList)) {
            return R.ok();
        }
        return R.failed();
    }

    @Override
    @Transactional
    public R canelScheduling(String id) {
        // 1. 判断ID是否为空
        if (StrUtil.isEmpty(id)) {
            return R.failed_empty("id");
        }
        // 2. 判断ID是存在
        BizShipRealTime bizShipRealTime = infoById(id);

        // 3. 判断是否可以取消预排
        if (!AlgorithmEnum.STATE11.getStatus().equals(bizShipRealTime.getAlgorithmState())
                && !AlgorithmEnum.STATE12.getStatus().equals(bizShipRealTime.getAlgorithmState())
        ) {
            return R.failed_biz("[" + bizShipRealTime.getShipNameCn() + "]当前状态不可取消预排");
        }
        Boolean success = true;

        // 删除预计划
        if (AlgorithmEnum.STATE2.getStatus().equals(bizShipRealTime.getAlgorithmState())) {
            BizDailyWorkPlanTemp bizDailyWorkPlanTemp = bizDailyWorkPlanTempService.infoByRealTimeId(bizShipRealTime.getId());
            if (null != bizDailyWorkPlanTemp) {
                success = success && bizDailyWorkPlanTempService.removeById(bizDailyWorkPlanTemp.getId());
            }
        }

        // 4. 重置预排状态
        bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE10.getStatus());

        // 5. 修改状态
        success = success && updateOrCleanById(bizShipRealTime);

        if (!success) {
            //手动强制回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.failed();
        }

        return R.ok();
    }

    @Override
    public boolean updateStatus(Integer status, String ids) {
        if (StrUtil.isEmpty(ids)) {
            return true;
        }
        UpdateWrapper<BizShipRealTime> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("ID", StrUtil.split(ids, ","));
        updateWrapper.set("ALGORITHM_STATE", status);
        return update(updateWrapper);
    }
}
