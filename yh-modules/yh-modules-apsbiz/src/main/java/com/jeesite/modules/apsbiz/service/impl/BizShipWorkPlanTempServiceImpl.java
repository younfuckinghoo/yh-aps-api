package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import com.jeesite.modules.apsbiz.entity.BizShipWorkPlanTemp;
import com.jeesite.modules.apsbiz.mapper.BizShipWorkPlanTempMapper;
import com.jeesite.modules.apsbiz.service.BizShipRealTimeService;
import com.jeesite.modules.apsbiz.service.BizShipWorkPlanService;
import com.jeesite.modules.apsbiz.service.BizShipWorkPlanTempService;
import com.jeesite.modules.apsbiz.service.DockService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BizShipWorkPlanTempServiceImpl extends YhServiceImpl<BizShipWorkPlanTempMapper, BizShipWorkPlanTemp> implements BizShipWorkPlanTempService {

    @Resource
    private BizShipWorkPlanService bizShipWorkPlanService;

    @Resource @Lazy
    private BizShipRealTimeService bizShipRealTimeService;

    @Resource
    private DockService dockService;


    @Value("${default.page}")
    private Integer defPage;

    @Value("${default.size}")
    private Integer defSize;

    @Override
    public BizShipWorkPlanTemp infoByVoyageNo(String voyageNo) {
        BizShipWorkPlanTemp algShipWorkPlanTemp = new BizShipWorkPlanTemp();
        algShipWorkPlanTemp.setVoyageNo(voyageNo);
        QueryWrapper<BizShipWorkPlanTemp> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algShipWorkPlanTemp,null);
        return getOne(queryWrapper);
    }


    @Override
    public List<BizShipWorkPlanTemp> queryList(BizShipWorkPlanTemp algShipWorkPlanTemp) {
        QueryWrapper<BizShipWorkPlanTemp> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algShipWorkPlanTemp, "a");

        // 船名模糊查询
        queryWrapper.like(StrUtil.isNotEmpty(algShipWorkPlanTemp.getShipNameCnLike()), "B.SHIP_NAME_CN", algShipWorkPlanTemp.getShipNameCnLike());
        // 当前状态
        queryWrapper.eq(null != algShipWorkPlanTemp.getStatus(), "B.STATUS", algShipWorkPlanTemp.getStatus());
        // 算法状态
        queryWrapper.eq(null != algShipWorkPlanTemp.getAlgorithmState(), "B.ALGORITHM_STATE", algShipWorkPlanTemp.getAlgorithmState());
        // 算法状态
        if(StrUtil.isNotEmpty(algShipWorkPlanTemp.getAlgorithmStateIn())){
            queryWrapper.and(wrapper -> wrapper
                    .in("B.ALGORITHM_STATE", StrUtil.split(algShipWorkPlanTemp.getAlgorithmStateIn(), ","))
                    .or()
                    .in("A.STAY_ALGORITHM_STATE", StrUtil.split(algShipWorkPlanTemp.getAlgorithmStateIn(), ","))
            );
        }
        // 泊位
        queryWrapper.eq(null != algShipWorkPlanTemp.getBerthNo(), "B.REAL_TIME_BERTH", algShipWorkPlanTemp.getBerthNo());

        // 靠泊时间（范围开始）
        queryWrapper.apply(null != algShipWorkPlanTemp.getBerthTimeStart(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algShipWorkPlanTemp.getBerthTimeStart());
        // 靠泊时间（范围结束）
        queryWrapper.apply(null != algShipWorkPlanTemp.getBerthTimeEnd(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algShipWorkPlanTemp.getBerthTimeEnd());

        return baseMapper.queryList(queryWrapper);
    }

    @Override
    public IPage<BizShipWorkPlanTemp> queryPage(BizShipWorkPlanTemp algShipWorkPlanTemp, Integer page, Integer size) {
        if (page == null) {
            page = defPage;
        }
        if (size == null) {
            size = defSize;
        }
        IPage<BizShipWorkPlanTemp> p = new Page<>(page, size);
        QueryWrapper<BizShipWorkPlanTemp> queryWrapper = MybatisPlusOracleUtils.getQueryWrapper(algShipWorkPlanTemp, "a");

        // 船名模糊查询
        queryWrapper.like(StrUtil.isNotEmpty(algShipWorkPlanTemp.getShipNameCnLike()), "B.SHIP_NAME_CN", algShipWorkPlanTemp.getShipNameCnLike());
        // 当前状态
        queryWrapper.eq(null != algShipWorkPlanTemp.getStatus(), "B.STATUS", algShipWorkPlanTemp.getStatus());
        // 算法状态
        queryWrapper.eq(null != algShipWorkPlanTemp.getAlgorithmState(), "B.ALGORITHM_STATE", algShipWorkPlanTemp.getAlgorithmState());
        // 算法状态
        if(StrUtil.isNotEmpty(algShipWorkPlanTemp.getAlgorithmStateIn())){
            queryWrapper.and(wrapper -> wrapper
                    .in("B.ALGORITHM_STATE", StrUtil.split(algShipWorkPlanTemp.getAlgorithmStateIn(), ","))
                    .or()
                    .in("A.STAY_ALGORITHM_STATE", StrUtil.split(algShipWorkPlanTemp.getAlgorithmStateIn(), ","))
            );
        }
        // 泊位
        queryWrapper.eq(null != algShipWorkPlanTemp.getBerthNo(), "B.REAL_TIME_BERTH", algShipWorkPlanTemp.getBerthNo());

        // 靠泊时间（范围开始）
        queryWrapper.apply(null != algShipWorkPlanTemp.getBerthTimeStart(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algShipWorkPlanTemp.getBerthTimeStart());
        // 靠泊时间（范围结束）
        queryWrapper.apply(null != algShipWorkPlanTemp.getBerthTimeEnd(),  "TO_CHAR(B.BERTH_TIME,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')", algShipWorkPlanTemp.getBerthTimeEnd());




        Long total = baseMapper.pageCount(queryWrapper);
        List<BizShipWorkPlanTemp> list = new ArrayList<>();
        if(total > 0){
            list = baseMapper.page(queryWrapper, (page - 1) * size, page * size);
        }

        IPage<BizShipWorkPlanTemp> iPage = new Page<>();
        iPage.setSize(size);
        iPage.setCurrent(page);
        iPage.setPages((total / size) + (total % size > 0 ? 1 : 0));
        iPage.setTotal(total);
        iPage.setRecords(list);

        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteById(String id) {
        if(StrUtil.isEmpty(id)){
            return R.failed_empty("id");
        }
        BizShipWorkPlanTemp algShipWorkPlanTemp = infoById(id);
        if(null == algShipWorkPlanTemp){
            return R.failed_null("id");
        }
        BizShipRealTime algShipRealTime = bizShipRealTimeService.infoByVoyageNo(algShipWorkPlanTemp.getVoyageNo());
        if(null == algShipRealTime){
            return R.failed_null("id");
        }

        if(!AlgorithmEnum.STATE12.getStatus().equals(algShipRealTime.getAlgorithmState())){
            return R.failed_biz("当前状态不可删除");
        }
        // 1. 删除预排泊记录
        Boolean success = removeById(id);

        // 2. 修改船舶预报
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

    @Override
    public Boolean updateOrCleanById(BizShipWorkPlanTemp algShipWorkPlanTemp) {
        return update(MybatisPlusOracleUtils.getUpdateWrapper(algShipWorkPlanTemp));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R submit(String ids) {
        // 1. 判断ID是否为空
        if(StrUtil.isEmpty(ids)){
            return R.failed_empty("ids");
        }
        // 2. 判断ID是存在
        BizShipWorkPlanTemp form1 = new BizShipWorkPlanTemp();
        form1.setIdIn(ids);
        List<BizShipWorkPlanTemp> algShipWorkPlanTempList = queryList(form1);
        if(algShipWorkPlanTempList.size() != ids.split(",").length){
            return R.failed_null("id");
        }
        // 3. 判断ID是否可提交计划确认
        List<BizShipWorkPlan> algShipWorkPlanList = new ArrayList<>();

        // 为了保证 入库顺序与temp一致。对查出的内容反转
        algShipWorkPlanTempList = CollUtil.reverse(algShipWorkPlanTempList);
        for(BizShipWorkPlanTemp algShipWorkPlanTemp : algShipWorkPlanTempList){
            // 3.1 检查是否可提交计划确认
            if(!AlgorithmEnum.STATE13.getStatus().equals(algShipWorkPlanTemp.getAlgorithmState())
                    && !AlgorithmEnum.STATE12.getStatus().equals(algShipWorkPlanTemp.getAlgorithmState())
            ){
                return R.failed_biz("当前状态不可提交计划确认");
            }
            // 3.3
            algShipWorkPlanList.add(dockService.getShipWorkPlan(algShipWorkPlanTemp));
        }

        // 6. 修改状态
        boolean success = updateBatchById(algShipWorkPlanTempList);

        // 7. 生成排泊计划
        success = success && bizShipWorkPlanService.saveBatch(algShipWorkPlanList);

        // 8. 批量修改状态
        success = success && bizShipRealTimeService.updateStatus(
                AlgorithmEnum.STATE14.getStatus(),
                CollUtil.join(algShipWorkPlanTempList.stream().map(BizShipWorkPlanTemp::getVoyageNo).collect(Collectors.toList()), ",")
        );

        if(!success){
            //手动强制回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.failed();
        }

        return R.ok();
    }

}
