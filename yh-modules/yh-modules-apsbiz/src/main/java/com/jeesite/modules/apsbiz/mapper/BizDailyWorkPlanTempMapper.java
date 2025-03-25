package com.jeesite.modules.apsbiz.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jeesite.common.base.YhBaseMapper;
import com.jeesite.modules.apsbiz.entity.BizDailyWorkPlanTemp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * （预）作业计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Mapper
public interface BizDailyWorkPlanTempMapper extends YhBaseMapper<BizDailyWorkPlanTemp> {



    @Select("SELECT " +
            "   A.*, " +
            "   B.SHIP_NAME_CN AS shipNameCn, " +
            "   B.SHIP_CODE AS shipCode, " +
            "	B.REAL_TIME_BERTH AS berthNo, " +
            "	B.VOYAGE_NO AS voyageNo, " +
            "	B.BERTH_DIRECTION AS berthDirection, " +
            "	B.BOLLARD_HEAD AS headBollardNo, " +
            "	B.BOLLARD_TAIL AS tailBollardNo, " +
            "	B.REAL_TIME_DRAFT AS draft, " +
            "	B.BERTH_TIME AS berthTime, " +
            "	B.STATUS AS status, " +
            "	B.CARRY_WEIGHT AS carryWeight, " +
            "	B.ALGORITHM_STATE AS algorithmState " +
            "FROM ALG_DAILY_WORK_PLAN_TEMP A LEFT JOIN ALG_SHIP_REAL_TIME B ON A.SHIP_REAL_TIME_ID = B.ID " +
            "${ew.customSqlSegment}")
    List<BizDailyWorkPlanTemp> queryList(@Param(Constants.WRAPPER) QueryWrapper<BizDailyWorkPlanTemp> queryWrapper);



    @Select(
            "SELECT " +
                    "COUNT(A.ID) " +
                    "FROM ALG_DAILY_WORK_PLAN_TEMP A LEFT JOIN ALG_SHIP_REAL_TIME B ON A.SHIP_REAL_TIME_ID = B.ID " +
                    "${ew.customSqlSegment}"
    )
    Long pageCount(@Param(Constants.WRAPPER) QueryWrapper<BizDailyWorkPlanTemp> queryWrapper);


    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tTMP.*,\n" +
            "\t\tROWNUM ROW_ID \n" +
            "\tFROM\n" +
            "\t\t( " +
            "SELECT " +
            "   A.*, " +
            "   B.SHIP_NAME_CN AS shipNameCn, " +
            "	B.REAL_TIME_BERTH AS berthNo, " +
            "	B.VOYAGE_NO AS voyageNo, " +
            "	B.BERTH_DIRECTION AS berthDirection, " +
            "	B.BOLLARD_HEAD AS headBollardNo, " +
            "	B.BOLLARD_TAIL AS tailBollardNo, " +
            "	B.REAL_TIME_DRAFT AS draft, " +
            "	B.BERTH_TIME AS berthTime, " +
            "	B.STATUS AS status, " +
            "	B.ALGORITHM_STATE AS algorithmState " +
            "FROM ALG_DAILY_WORK_PLAN_TEMP A LEFT JOIN ALG_SHIP_REAL_TIME B ON A.SHIP_REAL_TIME_ID = B.ID " +
            "${ew.customSqlSegment}" +
            " ) TMP \n" +
            "\tWHERE\n" +
            "\t\tROWNUM <= #{size} \n" +
            "\t) \n" +
            "WHERE\n" +
            "\tROW_ID > #{page}")
    List<BizDailyWorkPlanTemp> page(@Param(Constants.WRAPPER) QueryWrapper<BizDailyWorkPlanTemp> queryWrapper, @Param("page") Integer page, @Param("size") Integer size);

}
