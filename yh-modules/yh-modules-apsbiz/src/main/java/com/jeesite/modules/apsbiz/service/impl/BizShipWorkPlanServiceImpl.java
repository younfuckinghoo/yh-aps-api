package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeesite.common.base.R;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.utils.MybatisPlusOracleUtils;
import com.jeesite.modules.apsbiz.entity.BizShipRealTime;
import com.jeesite.modules.apsbiz.entity.BizShipWorkPlan;
import com.jeesite.modules.apsbiz.mapper.BizShipWorkPlanMapper;
import com.jeesite.modules.apsbiz.service.BizShipRealTimeService;
import com.jeesite.modules.apsbiz.service.BizShipWorkPlanService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Service
public class BizShipWorkPlanServiceImpl extends YhServiceImpl<BizShipWorkPlanMapper, BizShipWorkPlan> implements BizShipWorkPlanService {
    @Resource
    @Lazy
    private BizShipRealTimeService bizShipRealTimeService;
    @Value("${default.page}")
    private Integer defPage;

    @Value("${default.size}")
    private Integer defSize;

    @Override
    public List<BizShipWorkPlan> queryList(BizShipWorkPlan algShipWorkPlan) {
        QueryWrapper<BizShipWorkPlan> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algShipWorkPlan, "a");

        // 船名模糊查询
        queryWrapper.like(StrUtil.isNotEmpty(algShipWorkPlan.getShipNameCnLike()), "B.SHIP_NAME_CN", algShipWorkPlan.getShipNameCnLike());
        // 当前状态
        queryWrapper.eq(null != algShipWorkPlan.getStatus(), "B.STATUS", algShipWorkPlan.getStatus());
        // 泊位
        queryWrapper.eq(null != algShipWorkPlan.getBerthNo(), "B.REAL_TIME_BERTH", algShipWorkPlan.getBerthNo());
        // 算法状态
        queryWrapper.eq(null != algShipWorkPlan.getAlgorithmState(), "B.ALGORITHM_STATE", algShipWorkPlan.getAlgorithmState());
        // 算法状态
        queryWrapper.in(StrUtil.isNotEmpty(algShipWorkPlan.getAlgorithmStateIn()), "B.ALGORITHM_STATE", StrUtil.split(algShipWorkPlan.getAlgorithmStateIn(), ","));

        // 靠泊时间（范围开始）
        queryWrapper.apply(null != algShipWorkPlan.getBerthTimeStart(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algShipWorkPlan.getBerthTimeStart());
        // 靠泊时间（范围结束）
        queryWrapper.apply(null != algShipWorkPlan.getBerthTimeEnd(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algShipWorkPlan.getBerthTimeEnd());

        return baseMapper.queryList(queryWrapper);
    }


    @Override
    public IPage<BizShipWorkPlan> queryPage(BizShipWorkPlan algShipWorkPlan, Integer page, Integer size) {
        if (page == null) {
            page = defPage;
        }
        if (size == null) {
            size = defSize;
        }
        IPage<BizShipWorkPlan> p = new Page<>(page, size);
        QueryWrapper<BizShipWorkPlan> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algShipWorkPlan, "a");

        // 船名模糊查询
        queryWrapper.like(StrUtil.isNotEmpty(algShipWorkPlan.getShipNameCnLike()), "B.SHIP_NAME_CN", algShipWorkPlan.getShipNameCnLike());
        // 当前状态
        queryWrapper.eq(null != algShipWorkPlan.getStatus(), "B.STATUS", algShipWorkPlan.getStatus());
        // 算法状态
        queryWrapper.eq(null != algShipWorkPlan.getAlgorithmState(), "B.ALGORITHM_STATE", algShipWorkPlan.getAlgorithmState());
        // 算法状态
        queryWrapper.in(StrUtil.isNotEmpty(algShipWorkPlan.getAlgorithmStateIn()), "B.ALGORITHM_STATE", StrUtil.split(algShipWorkPlan.getAlgorithmStateIn(), ","));
        // 泊位
        queryWrapper.eq(null != algShipWorkPlan.getBerthNo(), "B.REAL_TIME_BERTH", algShipWorkPlan.getBerthNo());


        // 靠泊时间（范围开始）
        queryWrapper.apply(null != algShipWorkPlan.getBerthTimeStart(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algShipWorkPlan.getBerthTimeStart());
        // 靠泊时间（范围结束）
        queryWrapper.apply(null != algShipWorkPlan.getBerthTimeEnd(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algShipWorkPlan.getBerthTimeEnd());


        Long total = baseMapper.pageCount(queryWrapper);
        List<BizShipWorkPlan> list = new ArrayList<>();
        if(total > 0){
            list = baseMapper.page(queryWrapper, (page - 1) * size, page * size);
        }
        IPage<BizShipWorkPlan> iPage = new Page<>();
        iPage.setSize(size);
        iPage.setCurrent(page);
        iPage.setPages((total / size) + (total % size > 0 ? 1 : 0));
        iPage.setTotal(total);
        iPage.setRecords(list);

        return iPage;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public R deleteById(String id) {
        if(StrUtil.isEmpty(id)){
            return R.failed_empty("id");
        }
        BizShipWorkPlan algShipWorkPlan = infoById(id);
        if(null == algShipWorkPlan){
            return R.failed_null("id");
        }
        BizShipRealTime algShipRealTime = bizShipRealTimeService.infoByVoyageNo(algShipWorkPlan.getVoyageNo());
        if(null == algShipRealTime){
            return R.failed_null("id");
        }
        if(!AlgorithmEnum.STATE14.getStatus().equals(algShipRealTime.getAlgorithmState())){
            return R.failed_biz("["+algShipWorkPlan.getShipNameCn()+"]当前状态不可删除");
        }

        // 2. 删除 作业计划
        Boolean success = removeById(id);

        // 3. 修改 （预）作业计划 状态
        if(null != algShipRealTime){
            algShipRealTime.setAlgorithmState(AlgorithmEnum.STATE10.getStatus());
            success = success && bizShipRealTimeService.updateOrCleanById(algShipRealTime);
        }

        if(!success){
            //手动强制回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.failed();
        }
        return R.ok();

    }
}
