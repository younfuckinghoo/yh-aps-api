package com.jeesite.modules.apsbiz.task;


import cn.hutool.core.util.RandomUtil;
import com.jeesite.modules.apsbiz.entity.*;
import com.jeesite.modules.apsbiz.service.*;
import com.jeesite.common.enum1.AlgorithmEnum;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.List;

@Component
public class TestTask {
    @Resource
    private BizShipPlanService algShipPlanService;
    @Resource
    private BizShipForecastService algShipForecastService;
    @Resource
    private DockService dockService;

    /**
     * 随机生成预报数据
     */
//    @Scheduled(cron = "0 0 0/1 * * ?")
    public void addForcast() {
        dockService.addForcast();
    }

    /**
     * 审核预报数据
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void audit(){
        BizShipPlan form = new BizShipPlan();
        form.setAlgorithmState(AlgorithmEnum.STATE5.getStatus());
        List<BizShipPlan> algShipPlanList = algShipPlanService.queryList(form);
        for(BizShipPlan algShipPlan : algShipPlanList){
            if(RandomUtil.randomInt(1,10) > 6){
                algShipForecastService.updateStatus(AlgorithmEnum.STATE6.getStatus(), algShipPlan.getShipForcastId());

            }
            if(RandomUtil.randomInt(1,10) < 4){
                algShipForecastService.updateStatus(AlgorithmEnum.STATE6.getStatus(), algShipPlan.getShipForcastId());
            }
        }
    }

    /**
     * 随机生成在港船舶
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void addRealTime(){
        dockService.addRealTime();
    }




    /**
     * 定时初始化 在港船舶算法状态
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshRealTime(){
        dockService.refreshRealTime();
    }
}
