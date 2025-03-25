
package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeesite.common.base.R;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.apsbiz.entity.BizShipForecast;
import com.jeesite.modules.apsbiz.entity.BizShipPlan;
import com.jeesite.modules.apsbiz.entity.BizShipPlanTemp;
import com.jeesite.modules.apsbiz.mapper.BizShipPlanTempMapper;
import com.jeesite.modules.apsbiz.service.BizShipForecastService;
import com.jeesite.modules.apsbiz.service.BizShipPlanService;
import com.jeesite.modules.apsbiz.service.BizShipPlanTempService;
import com.jeesite.modules.apsbiz.service.DockService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * (预)靠离泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Service
public class BizShipPlanTempServiceImpl extends YhServiceImpl<BizShipPlanTempMapper, BizShipPlanTemp> implements BizShipPlanTempService {

    @Resource
    @Lazy
    private BizShipForecastService bizShipForecastService;

    @Resource
    private BizShipPlanService bizShipPlanService;

    @Resource
    private DockService dockService;

    @Value("${default.page}")
    private Integer defPage;

    @Value("${default.size}")
    private Integer defSize;


    @Override
    public List<BizShipPlanTemp> queryList(BizShipPlanTemp bizShipPlanTemp) {
        QueryWrapper<BizShipPlanTemp> queryWrapper = MybatisPlusUtils.getQueryWrapper(bizShipPlanTemp, "a");

        // 船名模糊查询
        queryWrapper.like(StrUtil.isNotEmpty(bizShipPlanTemp.getShipNameCnLike()), "B.SHIP_NAME_CN", bizShipPlanTemp.getShipNameCnLike());
        // 当前状态
        queryWrapper.eq(null != bizShipPlanTemp.getStatus(), "B.STATUS", bizShipPlanTemp.getStatus());
        // 算法状态
        queryWrapper.eq(null != bizShipPlanTemp.getAlgorithmState(), "B.ALGORITHM_STATE", bizShipPlanTemp.getAlgorithmState());
        // 散算法状态
        queryWrapper.in(StrUtil.isNotEmpty(bizShipPlanTemp.getAlgorithmStateIn()), "B.ALGORITHM_STATE", StrUtil.split(bizShipPlanTemp.getAlgorithmStateIn(), ","));


        return baseMapper.queryList(queryWrapper);
    }

    @Override
    public IPage<BizShipPlanTemp> queryPage(BizShipPlanTemp bizShipPlanTemp, Integer page, Integer size) {
        if (page == null) {
            page = defPage;
        }
        if (size == null) {
            size = defSize;
        }
        IPage<BizShipPlanTemp> p = new Page<>(page, size);
        QueryWrapper<BizShipPlanTemp> queryWrapper = MybatisPlusUtils.getQueryWrapper(bizShipPlanTemp, "a");
        // 船名模糊查询
        queryWrapper.like(StrUtil.isNotEmpty(bizShipPlanTemp.getShipNameCnLike()), "B.SHIP_NAME_CN", bizShipPlanTemp.getShipNameCnLike());
        // 当前状态
        queryWrapper.eq(null != bizShipPlanTemp.getStatus(), "B.STATUS", bizShipPlanTemp.getStatus());
        // 算法状态
        queryWrapper.eq(null != bizShipPlanTemp.getAlgorithmState(), "B.ALGORITHM_STATE", bizShipPlanTemp.getAlgorithmState());
        // 散算法状态
        queryWrapper.in(StrUtil.isNotEmpty(bizShipPlanTemp.getAlgorithmStateIn()), "B.ALGORITHM_STATE", StrUtil.split(bizShipPlanTemp.getAlgorithmStateIn(), ","));


        Long total = baseMapper.pageCount(queryWrapper);

        List<BizShipPlanTemp> list = new ArrayList<>();
        if (total > 0) {
            list = baseMapper.page(queryWrapper, (page - 1) * size, page * size);
        }
        IPage<BizShipPlanTemp> iPage = new Page<>();
        iPage.setSize(size);
        iPage.setCurrent(page);
        iPage.setPages((total / size) + (total % size > 0 ? 1 : 0));
        iPage.setTotal(total);
        iPage.setRecords(list);

        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteByIds(String ids) {
        if (StrUtil.isEmpty(ids)) {
            return R.failed_empty("id");
        }
        List<String> idList = Arrays.asList(ids.split(","));
        List<BizShipForecast> shipList = new ArrayList<>();
        AtomicInteger result = new AtomicInteger(0);
        idList.forEach(id -> {
            BizShipPlanTemp bizShipPlanTemp = infoById(id);
            if (null == bizShipPlanTemp) {
                result.set(1);
            }
            BizShipForecast bizShipForecast = null;
            if (bizShipPlanTemp != null) {
                bizShipForecast = bizShipForecastService.infoById(bizShipPlanTemp.getShipForcastId());
            } else {
                result.set(1);
            }
            if (bizShipForecast != null && !AlgorithmEnum.STATE2.getStatus().equals(bizShipForecast.getAlgorithmState())) {
                result.set(2);
            }
            if (bizShipForecast != null) {
                bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE0.getStatus());
            }
            shipList.add(bizShipForecast);
        });
        if (result.get() == 1) {
            return R.failed_null("id");
        } else if (result.get() == 2) {
            return R.failed_biz("当前(预)靠离泊计划不可删除");
        }
        // 1. 删除 (预)靠离泊计划
        boolean success = removeBatchByIds(idList);

        // 2. 修改 (预)靠离泊计划
        if (!shipList.isEmpty()) {
            success = success && bizShipForecastService.updateBatchById(shipList);
        }
        if (!success) {
            //手动强制回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.failed();
        }
        return R.ok();
    }

    @Override
    public Boolean updateOrCleanById(BizShipPlanTemp bizShipPlanTemp) {
        return update(MybatisPlusUtils.getUpdateWrapper(bizShipPlanTemp));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R submit(String ids) {
        // 1. 判断ID是否为空
        if (StrUtil.isEmpty(ids)) {
            return R.failed_empty("ids");
        }
        // 2. 判断ID是存在
        BizShipPlanTemp form1 = new BizShipPlanTemp();
        form1.setIdIn(ids);
        List<BizShipPlanTemp> bizShipPlanTempList = queryList(form1);
        if (bizShipPlanTempList.size() != ids.split(",").length) {
            return R.failed_null("id");
        }
        // 3. 判断ID是否可提交审核
        List<BizShipPlan> bizShipPlanList = new ArrayList<>();
        // 4. 为了保证 入库顺序与temp一致。对查出的内容反转
        bizShipPlanTempList = CollUtil.reverse(bizShipPlanTempList);
        // 5. 校验状态
        for (BizShipPlanTemp bizShipPlanTemp : bizShipPlanTempList) {
            // 3.1 检查是否可提交审核
            if (!AlgorithmEnum.STATE2.getStatus().equals(bizShipPlanTemp.getAlgorithmState())
                    && !AlgorithmEnum.STATE3.getStatus().equals(bizShipPlanTemp.getAlgorithmState())
                    && !AlgorithmEnum.STATE4.getStatus().equals(bizShipPlanTemp.getAlgorithmState())
            ) {
                return R.failed_biz("[" + bizShipPlanTemp.getShipNameCn() + "]当前状态不可提交审核");
            }
            // 3.3 生成 靠离泊计划
            bizShipPlanList.add(dockService.getAlgShipPlan(bizShipPlanTemp));
        }

        // 5. 调用生产系统
        R r = dockService.submitAudit();
        if (!R.checkOk(r)) {
            return r;
        }

        // 6. 删除旧数据
        BizShipPlan form2 = new BizShipPlan();
        form2.setShipForcastIdIn(CollUtil.join(bizShipPlanTempList.stream().map(BizShipPlanTemp::getShipForcastId).collect(Collectors.toList()), ","));
        List<BizShipPlan> oldAlgShipPlan = bizShipPlanService.queryList(form2);
        if (CollUtil.isNotEmpty(oldAlgShipPlan)) {
            bizShipPlanService.removeBatchByIds(oldAlgShipPlan);
        }

        // 7. 修改船舶预报中的状态
        boolean success = bizShipForecastService.updateStatus(
                AlgorithmEnum.STATE5.getStatus(),
                CollUtil.join(bizShipPlanTempList.stream().map(BizShipPlanTemp::getShipForcastId).collect(Collectors.toList()), ",")
        );

        // 9. 生成排泊计划
        success = success && bizShipPlanService.saveBatch(bizShipPlanList);

        if (!success) {
            //手动强制回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.failed();
        }
        return R.ok();
    }

    @Override
    public BizShipPlanTemp infoByForeacastId(String forecastId) {
        BizShipPlanTemp bizShipPlanTemp = new BizShipPlanTemp();
        bizShipPlanTemp.setShipForcastId(forecastId);
        QueryWrapper<BizShipPlanTemp> queryWrapper = MybatisPlusUtils.getQueryWrapper(bizShipPlanTemp, null);
        return getOne(queryWrapper);
    }

    @Transactional
    @Override
    public boolean updateShipPlan(BizShipPlanTemp bizShipPlanTemp) {
//        Boolean success = true;
//        BizShipForecast bizShipForecast = bizShipForecastService.infoById(bizShipPlanTemp.getShipForcastId());
//        if (null != bizShipForecast) {
//            bizShipForecast.setNeedLead(bizShipPlanTemp.getNeedLead());
//            bizShipForecast.setNeedTractor(bizShipPlanTemp.getNeedTractor());
//            success = success && bizShipForecastService.updateOrCleanById(bizShipForecast);
//        }
//        success = success && updateOrCleanById(bizShipPlanTemp);
//
//        if (!success) {
//            //手动强制回滚事务
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
//
//        return success;

        return updateOrCleanById(bizShipPlanTemp);
    }
}
