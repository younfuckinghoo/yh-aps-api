package com.jeesite.modules.apsbiz.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jeesite.common.base.YhBaseMapper;
import com.jeesite.modules.apsbiz.entity.BizShipWorkPlanTemp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BizShipWorkPlanTempMapper  extends YhBaseMapper<BizShipWorkPlanTemp> {
    @Select("SELECT " +
            "   A.*, " +
            "   B.SHIP_NAME_CN AS shipNameCn " +
            "FROM ALG_SHIP_WORK_PLAN_TEMP A LEFT JOIN ALG_SHIP_REAL_TIME B ON A.VOYAGE_NO = B.VOYAGE_NO " +
            "${ew.customSqlSegment}")
    List<BizShipWorkPlanTemp> queryList(@Param(Constants.WRAPPER) QueryWrapper<BizShipWorkPlanTemp> queryWrapper);



    @Select(
            "SELECT " +
                    "COUNT(A.ID) " +
                    "FROM ALG_SHIP_WORK_PLAN_TEMP A LEFT JOIN ALG_SHIP_REAL_TIME B ON A.VOYAGE_NO = B.VOYAGE_NO " +
                    "${ew.customSqlSegment}"
    )
    Long pageCount(@Param(Constants.WRAPPER) QueryWrapper<BizShipWorkPlanTemp> queryWrapper);


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
            "   B.SHIP_NAME_CN AS shipNameCn " +
            "FROM ALG_SHIP_WORK_PLAN_TEMP A LEFT JOIN ALG_SHIP_REAL_TIME B ON A.VOYAGE_NO = B.VOYAGE_NO " +
            "${ew.customSqlSegment}" +
            " ) TMP \n" +
            "\tWHERE\n" +
            "\t\tROWNUM <= #{size} \n" +
            "\t) \n" +
            "WHERE\n" +
            "\tROW_ID > #{page}")
    List<BizShipWorkPlanTemp> page(@Param(Constants.WRAPPER) QueryWrapper<BizShipWorkPlanTemp> queryWrapper, @Param("page") Integer page, @Param("size") Integer size);

}
