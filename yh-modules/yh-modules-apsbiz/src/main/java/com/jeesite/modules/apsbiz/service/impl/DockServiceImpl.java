
package com.jeesite.modules.apsbiz.service.impl;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeesite.common.base.R;
import com.jeesite.common.enum1.AlgorithmEnum;
import com.jeesite.common.enum1.ShipStatusEnum;
import com.jeesite.modules.apsbiz.entity.*;
import com.jeesite.modules.apsbiz.service.*;
import com.jeesite.modules.apsbiz.utils.algorithm.Rectangle;
import com.jeesite.modules.apsbiz.utils.algorithm.RectanglePlacer;
import com.jeesite.modules.sys.utils.UserUtils;
import jakarta.annotation.Resource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 三方接口
 *
 * @author zhudi
 * @date 2024-12-08 21:19:22
 */
@Service
public class DockServiceImpl implements DockService {

    @Resource @Lazy
    private BizShipForecastService bizShipForecastService;
    @Resource @Lazy
    private BizCargoTypeService bizCargoTypeService;
    @Resource @Lazy
    private BizShipPlanService bizShipPlanService;
    @Resource @Lazy
    private BizShipPlanTempService bizShipPlanTempService;
    @Resource @Lazy
    private BizShipRealTimeService bizShipRealTimeService;
    @Resource
    private BizBollardInfoService bizBollardInfoService;
    @Resource
    private BizShipInfoService bizShipInfoService;
    @Resource @Lazy
    private BizDailyWorkPlanTempService bizDailyWorkPlanTempService;

    @Override
    public R submitAudit() {
        return R.ok();
    }

    @Override
    public R pullStatus() {
        return R.ok();
    }

    @Override
    public R apsShipScheduling() {
        return R.ok();
    }

    @Override
    public R apsWorkScheduling(Integer mode) {
        return R.ok();
    }


    @Override
    public void addForcast() {

        try {
            // 1.还是一样得到一个excel文件对象，但是我们这里是读 ， 所以要指定文件路径
            XSSFWorkbook excel = new XSSFWorkbook(ResourceUtil.getStream("forecast.xlsx"));
            // 2.读取excel文件中的sheet1页（因为这个文件就只创建了一个sheet页）
            XSSFSheet sheet = excel.getSheetAt(0);//这是直接拿第一个sheet页
            // 3.遍历所有行，但是我不知道有多少行，所以我可以直接获取最后有文字的那一行，这样就知道一个有多少行了
            int lastRowNum = sheet.getLastRowNum();//获取有文字的最后一行行号


            XSSFRow row = sheet.getRow(RandomUtil.randomInt(1, lastRowNum));

            //新增船舶
            String shipName = row.getCell(0).getStringCellValue();
            BizShipInfo bizShipInfo = bizShipInfoService.infoByName(shipName);
            if(bizShipInfo == null){
                bizShipInfo = new BizShipInfo();
//                bizShipInfo.setId(sysIdService.getId("ALG_SHIP_INFO"));
//                bizShipInfo.setVoyageNo(bizShipForecast.getVoyageNo());
                bizShipInfo.setShipCode(Convert.toStr(RandomUtil.randomInt(900000,1000000)));
                bizShipInfo.setShipNameCn(shipName);
                bizShipInfo.setShipNameEn("SHIP" + bizShipInfo.getId().substring(4, 8));
                bizShipInfo.setCallNo("CALLNO" + bizShipInfo.getId().substring(4, 8));
                bizShipInfo.setShipCompany("船舶公司"+RandomUtil.randomInt(1,11));
                bizShipInfo.setShipOperator("经营人"+RandomUtil.randomInt(1,11));
                bizShipInfo.setShipOwner("所有人"+RandomUtil.randomInt(1,11));
                bizShipInfo.setShipLength(Convert.toFloat((row.getCell(2).getNumericCellValue())));
                bizShipInfo.setShipWidth(bizShipInfo.getShipLength() / 7);
                bizShipInfo.setNationality("国籍");
                bizShipInfo.setCabinNum(RandomUtil.randomInt(3,10));
                bizShipInfoService.save(bizShipInfo);
            }


            BizShipForecast bizShipForecast = new BizShipForecast();
//            bizShipForecast.setId(sysIdService.getId("ALG_SHIP_FORECAST"));
            bizShipForecast.setVoyageNo(bizShipForecast.getId());
            bizShipForecast.setShipCode(bizShipInfo.getShipCode());
            bizShipForecast.setShipNameCn(bizShipInfo.getShipNameCn());
            bizShipForecast.setWorkingCompany("作业公司"+RandomUtil.randomInt(1,11));
            bizShipForecast.setAgentCompany(row.getCell(9).getStringCellValue());
            bizShipForecast.setDraftIn(RandomUtil.randomInt(5,15));
            bizShipForecast.setDraftOut(RandomUtil.randomInt(5,15));


            BizCargoType cargoForm = new BizCargoType();
            cargoForm.setCargoName(row.getCell(6).getStringCellValue());
            List<BizCargoType> cargoTypeList = bizCargoTypeService.queryList(cargoForm);
            BizCargoType bizCargoType = null;
            if(cargoTypeList.size() > 0){
                bizCargoType = cargoTypeList.get(0);
            }else{
                bizCargoType = bizCargoTypeService.infoById(Convert.toStr(RandomUtil.randomInt(1,117)));
            }

            bizShipForecast.setCargoSubTypeCode(bizCargoType.getCargoCode());
            bizShipForecast.setCargoTypeName(bizCargoType.getCargoName());

            String tradeType = row.getCell(4).getStringCellValue();
            bizShipForecast.setTradeType("外贸".equals(tradeType) ? 0 : 1 );


            String loadUnload = row.getCell(5).getStringCellValue();
            bizShipForecast.setTradeType("外贸".equals(tradeType) ? 0 : 1 );

            bizShipForecast.setLoadUnload("装".equals(loadUnload) ? 1 : 2);
            if(bizShipForecast.getLoadUnload() == 1){
                bizShipForecast.setLoadPlanWeight(Convert.toInt(row.getCell(1).getNumericCellValue()));
            }
            if(bizShipForecast.getLoadUnload() == 2){
                bizShipForecast.setUnloadPlanWeight(Convert.toInt(row.getCell(1).getNumericCellValue()));
            }
            bizShipForecast.setCarryWeight(Convert.toInt(row.getCell(1).getNumericCellValue()));

            bizShipForecast.setExpectArriveTime(DateUtil.offsetHour(new Date(), RandomUtil.randomInt(1,36)));
            bizShipForecast.setExpectLeaveTime(DateUtil.offsetHour(bizShipForecast.getExpectArriveTime(), RandomUtil.randomInt(12,24)));

            bizShipForecast.setPortCodeFrom(Convert.toStr(RandomUtil.randomInt(10000,90000)));
            bizShipForecast.setPortCodeTo(Convert.toStr(RandomUtil.randomInt(10000,90000)));

            bizShipForecast.setNeedLead(RandomUtil.randomBoolean()? "1" : "0");
            bizShipForecast.setIsDeload(RandomUtil.randomBoolean()? "1" : "0");
            bizShipForecast.setNeedTractor(RandomUtil.randomBoolean()? "1" : "0");

            bizShipForecast.setCarRaedyTime(RandomUtil.randomInt(1,7));
            bizShipForecast.setIsImportant(RandomUtil.randomBoolean()? 1 : 0);
            bizShipForecast.setIsSpecial(RandomUtil.randomBoolean()? 1 : 0);
            bizShipForecast.setStatus(ShipStatusEnum.STATE2.getStatus());
            bizShipForecast.setCargoOwner(row.getCell(7).getStringCellValue());
            bizShipForecast.setRemark("备注"+System.currentTimeMillis());
            bizShipForecast.setCreateDate(new Date());
            bizShipForecast.setCreateCompany("创建公司"+RandomUtil.randomInt(1,11));
            bizShipForecast.setAlgorithmState(AlgorithmEnum.STATE0.getStatus());
            bizShipForecast.setNoticeTicket(RandomUtil.randomBoolean()? 1 : 0);
            bizShipForecastService.save(bizShipForecast);

            // 4.关闭资源
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BizShipPlanTemp> getPlanTemp(List<BizShipForecast> bizShipForecastList) {
        // 获取 已排靠离泊 情况
        BizShipPlanTemp form1 = new BizShipPlanTemp();
        form1.setAlgorithmState(AlgorithmEnum.STATE3.getStatus());
        List<BizShipPlanTemp> list1 = bizShipPlanTempService.queryList(form1);


        BizShipPlan form2 = new BizShipPlan();
        form1.setAlgorithmStateIn(StrUtil.join(","
                , AlgorithmEnum.STATE3.getStatus()
                , AlgorithmEnum.STATE4.getStatus()
                , AlgorithmEnum.STATE5.getStatus()
                , AlgorithmEnum.STATE6.getStatus()
                , AlgorithmEnum.STATE7.getStatus()));
        List<BizShipPlan> list2 = bizShipPlanService.queryList(form2);


        RectanglePlacer packer1 = new RectanglePlacer(System.currentTimeMillis(),(System.currentTimeMillis() + 20*24*60*60*1000), 1, 16);
        RectanglePlacer packer2 = new RectanglePlacer(System.currentTimeMillis(),(System.currentTimeMillis() + 20*24*60*60*1000), 17, 28);
        RectanglePlacer packer3 = new RectanglePlacer(System.currentTimeMillis(),(System.currentTimeMillis() + 20*24*60*60*1000), 29, 48);


        for(BizShipPlanTemp bizShipPlanTemp : list1){
            Integer yStart = Convert.toInt(bizShipPlanTemp.getHeadBollardId());
            Integer yEnd = Convert.toInt(bizShipPlanTemp.getTailBollardId());
            if(yStart > yEnd) {
                Integer tmp = yStart;
                yStart = yEnd;
                yEnd = tmp;
            }
            if(yStart <= 16){
                packer1.rectangles.add(new Rectangle(bizShipPlanTemp.getPlanBerthTime().getTime(), bizShipPlanTemp.getPlanLeaveTime().getTime(), yStart, yEnd));
            }else if(yStart <= 28){
                packer2.rectangles.add(new Rectangle(bizShipPlanTemp.getPlanBerthTime().getTime(), bizShipPlanTemp.getPlanLeaveTime().getTime(), yStart, yEnd));
            }else if(yStart <= 48){
                packer3.rectangles.add(new Rectangle(bizShipPlanTemp.getPlanBerthTime().getTime(), bizShipPlanTemp.getPlanLeaveTime().getTime(), yStart, yEnd));
            }
        }

        for(BizShipPlan bizShipPlan : list2){
            Integer yStart = Convert.toInt(bizShipPlan.getHeadBollardId());
            Integer yEnd = Convert.toInt(bizShipPlan.getTailBollardId());
            if(yStart > yEnd) {
                Integer tmp = yStart;
                yStart = yEnd;
                yEnd = tmp;
            }

            if(yStart <= 16){
                packer1.rectangles.add(new Rectangle(bizShipPlan.getPlanBerthTime().getTime(), bizShipPlan.getPlanLeaveTime().getTime(), yStart, yEnd));
            }else if(yStart <= 28){
                packer2.rectangles.add(new Rectangle(bizShipPlan.getPlanBerthTime().getTime(), bizShipPlan.getPlanLeaveTime().getTime(), yStart, yEnd));
            }else if(yStart <= 48){
                packer3.rectangles.add(new Rectangle(bizShipPlan.getPlanBerthTime().getTime(), bizShipPlan.getPlanLeaveTime().getTime(), yStart, yEnd));
            }
        }


        List<BizShipPlanTemp> list = new ArrayList<>();
        for(BizShipForecast bizShipForecast : bizShipForecastList){
            // 基础数据
            BizShipPlanTemp bizShipPlanTemp = new BizShipPlanTemp();
//            bizShipPlanTemp.setId(sysIdService.getId("ALG_SHIP_PLAN_TEMP"));
//            bizShipPlanTemp.setAlgorithmState(AlgorithmEnum.STATE2.getStatus());
            bizShipPlanTemp.setShipForcastId(bizShipForecast.getId());
            bizShipPlanTemp.setPlanStatus(1);
            bizShipPlanTemp.setCreateTime(new Date());
            bizShipPlanTemp.setCreator(UserUtils.getUser().getUserName());

            // 模拟数据
            bizShipPlanTemp.setBerthRemark("备注"+System.currentTimeMillis());
            bizShipPlanTemp.setLeaveRemark("备注"+System.currentTimeMillis());



            // 模拟算法数据
            // 1. 计算作业时长
            Integer workHours = bizShipForecast.getCarryWeight() / 3000;
            Integer totalHours = workHours + 10;
            // 2. 占用桩柱数
            BizShipInfo bizShipInfo = bizShipInfoService.infoByCode(bizShipForecast.getShipCode());
            Integer bollardNums = Convert.toInt(bizShipInfo.getShipLength() / 20) + 1;

            // 计算缆柱

            Rectangle newBlock1 = packer1.placeNewRectangle(bizShipForecast.getExpectArriveTime().getTime(), totalHours*60*60*1000, bollardNums);
            Rectangle newBlock2 = packer2.placeNewRectangle(bizShipForecast.getExpectArriveTime().getTime(), totalHours*60*60*1000, bollardNums);
            Rectangle newBlock3 = packer3.placeNewRectangle(bizShipForecast.getExpectArriveTime().getTime(), totalHours*60*60*1000, bollardNums);

            Integer index = 0;
            Rectangle newBlock = null;
            if(null != newBlock1 && (null == newBlock || newBlock1.getXStart() < newBlock.getXStart())){
                index = 1;
                newBlock = newBlock1;
            }
            if(null != newBlock2 && (null == newBlock || newBlock2.getXStart() < newBlock.getXStart())){
                index = 2;
                newBlock = newBlock2;
            }
            if(null != newBlock3 && (null == newBlock || newBlock3.getXStart() < newBlock.getXStart())){
                index = 3;
                newBlock = newBlock3;
            }

            if(null != newBlock){
                bizShipPlanTemp.setBerthDirection(""+RandomUtil.randomInt(1,3));
                // 左舷（缆柱 大到小）
    //            if("1".equals(bizShipPlanTemp.getBerthDirection())){
    //                bizShipPlanTemp.setHeadBollardNo(Convert.toStr(newBlock.yEnd));
    //                bizShipPlanTemp.setTailBollardNo(Convert.toStr(newBlock.yStart));
    //            }
    //            // 右舷（缆柱 小到大）
    //            if("2".equals(bizShipPlanTemp.getBerthDirection())){
                    bizShipPlanTemp.setHeadBollardId(Convert.toStr(newBlock.getYStart()));
                    bizShipPlanTemp.setTailBollardId(Convert.toStr(newBlock.getYEnd()));
    //            }

                BizBollardInfo bizBollardInfo = bizBollardInfoService.infoById(bizShipPlanTemp.getHeadBollardId());
                bizShipPlanTemp.setBerthNo(bizBollardInfo.getBerthNo());

                // 靠泊时间 开工时间 完工时间 离泊时间
    //            bizShipPlanTemp.setPlanBerthTime(DateUtil.date(newBlock.xStart));
                bizShipPlanTemp.setPlanBerthTime(DateUtil.offsetMinute(DateUtil.date(newBlock.getXStart()), RandomUtil.randomInt(1,60)));
                bizShipPlanTemp.setPlanStartTime(DateUtil.offsetHour(bizShipPlanTemp.getPlanBerthTime(), RandomUtil.randomInt(1,3)));
                bizShipPlanTemp.setPlanFinishTime(DateUtil.offsetHour(bizShipPlanTemp.getPlanStartTime(), workHours));
    //            bizShipPlanTemp.setPlanLeaveTime(DateUtil.date(newBlock.xEnd));
                bizShipPlanTemp.setPlanLeaveTime(DateUtil.offsetMinute(DateUtil.date(newBlock.getXEnd()), 0-RandomUtil.randomInt(1,60)));

                if(index == 1){
                    packer1.rectangles.add(newBlock);
                }
                if(index == 2){
                    packer2.rectangles.add(newBlock);
                }
                if(index == 3){
                    packer3.rectangles.add(newBlock);
                }

                list.add(bizShipPlanTemp);
            }
        }

        return list;
    }

    @Override
    public BizShipPlan getAlgShipPlan(BizShipPlanTemp bizShipPlanTemp) {

        BizShipPlan bizShipPlan = new BizShipPlan();

//        bizShipPlan.setId(sysIdService.getId("ALG_SHIP_PLAN"));
        bizShipPlan.setShipForcastId(bizShipPlanTemp.getShipForcastId());
        bizShipPlan.setPlanStatus(5);
        bizShipPlan.setCreateTime(new Date());
        bizShipPlan.setCreator(UserUtils.getUser().getUserName());

        bizShipPlan.setBerthNo(bizShipPlanTemp.getBerthNo());
        bizShipPlan.setPlanBerthTime(bizShipPlanTemp.getPlanBerthTime());
        bizShipPlan.setPlanStartTime(bizShipPlanTemp.getPlanStartTime());
        bizShipPlan.setPlanFinishTime(bizShipPlanTemp.getPlanFinishTime());
        bizShipPlan.setPlanLeaveTime(bizShipPlanTemp.getPlanLeaveTime());
        bizShipPlan.setHeadBollardId(bizShipPlanTemp.getHeadBollardId());
        bizShipPlan.setTailBollardId(bizShipPlanTemp.getTailBollardId());
        bizShipPlan.setBerthDirection(bizShipPlanTemp.getBerthDirection());
        bizShipPlan.setBerthRemark(bizShipPlanTemp.getBerthRemark());
        bizShipPlan.setLeaveRemark(bizShipPlanTemp.getLeaveRemark());

        return bizShipPlan;
    }

    @Override
    public void addRealTime() {
        BizShipPlan form = new BizShipPlan();
        form.setAlgorithmState(AlgorithmEnum.STATE6.getStatus());
        List<BizShipPlan> bizShipPlanList = bizShipPlanService.queryList(form);

        for(BizShipPlan bizShipPlan : bizShipPlanList){
            if(RandomUtil.randomBoolean()){
                BizShipForecast bizShipForecast = bizShipForecastService.infoById(bizShipPlan.getShipForcastId());
                BizShipRealTime bizShipRealTime = bizShipRealTimeService.infoByVoyageNo(bizShipForecast.getVoyageNo());
                if(bizShipRealTime == null){
                    bizShipRealTime = new BizShipRealTime();
//                    bizShipRealTime.setId(sysIdService.getId("ALG_SHIP_REAL_TIME"));
                    bizShipRealTime.setVoyageNo(bizShipForecast.getVoyageNo());
                    bizShipRealTime.setShipCode(bizShipForecast.getShipCode());
                    bizShipRealTime.setShipNameCn(bizShipForecast.getShipNameCn());
                    bizShipRealTime.setRealTimeBerth(bizShipPlan.getBerthNo());
                    bizShipRealTime.setBerthDirection(Convert.toInt(bizShipPlan.getBerthDirection()));
                    bizShipRealTime.setBollardHead(bizShipPlan.getHeadBollardId());
                    bizShipRealTime.setBollardTail(bizShipPlan.getTailBollardId());
                    bizShipRealTime.setRealTimeDraft(bizShipPlan.getDraftIn());
                    bizShipRealTime.setLoadUnload(bizShipForecast.getLoadUnload());
                    bizShipRealTime.setLoadPlanWeight(bizShipForecast.getLoadPlanWeight());
                    bizShipRealTime.setUnloadPlanWeight(bizShipForecast.getUnloadPlanWeight());
                    bizShipRealTime.setCarryWeight(bizShipForecast.getCarryWeight());
                    bizShipRealTime.setExpectArriveTime(bizShipForecast.getExpectArriveTime());
                    bizShipRealTime.setExpectLeaveTime(bizShipForecast.getExpectLeaveTime());
                    bizShipRealTime.setArriveTime(DateUtil.offsetHour(bizShipForecast.getExpectArriveTime(), RandomUtil.randomInt(1,3)));
                    bizShipRealTime.setBerthTime(DateUtil.offsetHour(bizShipPlan.getPlanBerthTime(), RandomUtil.randomInt(1,3)));
                    bizShipRealTime.setWorkingStartTime(DateUtil.offsetHour(bizShipPlan.getPlanStartTime(), RandomUtil.randomInt(1,3)));
                    bizShipRealTime.setWorkingFinishTime(DateUtil.offsetHour(bizShipPlan.getPlanFinishTime(), RandomUtil.randomInt(1,3)));
                    bizShipRealTime.setDayClassProgress(RandomUtil.randomInt(1000,5000));
                    bizShipRealTime.setNightClassProgress(RandomUtil.randomInt(1000,5000));
                    bizShipRealTime.setLoadProgress(RandomUtil.randomInt(1000,5000));
                    bizShipRealTime.setUnloadProgress(RandomUtil.randomInt(1000,5000));
                    bizShipRealTime.setRealProgress(RandomUtil.randomInt(1000,5000));
                    bizShipRealTime.setStatus(ShipStatusEnum.STATE3.getStatus());
                    bizShipRealTime.setRemark("备注"+System.currentTimeMillis());
//                    bizShipRealTime.setCreator();
                    bizShipRealTime.setCreateDate(new Date());
                    bizShipRealTime.setCreateCompany(bizShipForecast.getCreateCompany());
                    bizShipRealTime.setCreateDept("部门"+System.currentTimeMillis());
                    bizShipRealTime.setAlgorithmState(AlgorithmEnum.STATE10.getStatus());
                    bizShipRealTimeService.save(bizShipRealTime);


//                    bizShipForecast.setAlgorithmState(bizShipRealTime.getAlgorithmState());
//                    bizShipForecastService.updateOrCleanById(bizShipForecast);
                }
            }
        }
    }

    @Override
    public BizDailyWorkPlanTemp getWorkPlanTemp(BizShipRealTime bizShipRealTime) {
        BizDailyWorkPlanTemp bizDailyWorkPlanTemp = new BizDailyWorkPlanTemp();
//        bizDailyWorkPlanTemp.setId(sysIdService.getId("ALG_DAILY_WORK_PLAN_TEMP"));
        bizDailyWorkPlanTemp.setShipRealTimeId(bizShipRealTime.getId());
        bizDailyWorkPlanTemp.setPlanDate(getPlanDate());


        bizDailyWorkPlanTemp.setPlanFinishTime(bizShipRealTime.getWorkingFinishTime());
        bizDailyWorkPlanTemp.setHandleWeight(RandomUtil.randomInt(1000,5000) + "");
        bizDailyWorkPlanTemp.setRemainWeight(RandomUtil.randomInt(1000,3000) + "");
        bizDailyWorkPlanTemp.setWorkContent("工作内容" + System.currentTimeMillis());
        bizDailyWorkPlanTemp.setNightWorkAmount(RandomUtil.randomInt(1000,3000) + "");
        bizDailyWorkPlanTemp.setNightMachine("夜班机械"+System.currentTimeMillis());
        bizDailyWorkPlanTemp.setNightPerson("夜班人员"+System.currentTimeMillis());
        bizDailyWorkPlanTemp.setDayWorkAmount(RandomUtil.randomInt(1000,3000) + "");
        bizDailyWorkPlanTemp.setDayMachine("夜班机械"+System.currentTimeMillis());
        bizDailyWorkPlanTemp.setDayPerson("夜班人员"+System.currentTimeMillis());
        bizDailyWorkPlanTemp.setCreator(UserUtils.getUser().getUserName());
        bizDailyWorkPlanTemp.setCreateDate(new Date());

        return bizDailyWorkPlanTemp;
    }

    @Override
    public BizDailyWorkPlan getDailyWorkPlan(BizDailyWorkPlanTemp bizDailyWorkPlanTemp) {

        BizDailyWorkPlan bizDailyWorkPlan = new BizDailyWorkPlan();
        Field[] fields =  ReflectUtil.getFields(BizDailyWorkPlanTemp.class);
        for(Field field : fields){
            if("serialVersionUID".equals(field.getName())){
                continue;
            }
            Object val = ReflectUtil.getFieldValue(bizDailyWorkPlanTemp, field);
            if(null != val){
                ReflectUtil.setFieldValue(bizDailyWorkPlan, field.getName(), val);
            }
        }
//        bizDailyWorkPlan.setId(sysIdService.getId("ALG_DAILY_WORK_PLAN"));
        bizDailyWorkPlan.setCreator(UserUtils.getUser().getUserName());
        return bizDailyWorkPlan;
    }

    @Override
    @Transactional
    public void refreshRealTime() {
        BizShipRealTime bizShipRealTime = new BizShipRealTime();
        bizShipRealTime.setIsFinish(0);
        List<BizShipRealTime> list = bizShipRealTimeService.queryList(bizShipRealTime);

        // 存储历史记录
        list.forEach(realTime -> {
            //查询预计划
            BizDailyWorkPlanTemp bizDailyWorkPlanTemp = bizDailyWorkPlanTempService.infoByRealTimeId(realTime.getId());
            if(null != bizDailyWorkPlanTemp){
                bizDailyWorkPlanTemp.setStayAlgorithmState(realTime.getAlgorithmState());
                bizDailyWorkPlanTempService.updateOrCleanById(bizDailyWorkPlanTemp);
            }

            // 判断是否完工
            if(NumberUtil.add(realTime.getLoadProgress(), realTime.getUnloadProgress()).compareTo(NumberUtil.add(realTime.getLoadPlanWeight(), realTime.getUnloadPlanWeight())) >= 0){
                realTime.setIsFinish(1);
            } else {
                realTime.setAlgorithmState(AlgorithmEnum.STATE10.getStatus());
            }
        });
        bizShipRealTimeService.updateBatchById(list);
    }

    @Override
    public Date getPlanDate() {
        return DateUtil.beginOfDay(new Date());
    }
}
