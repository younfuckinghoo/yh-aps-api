package com.jeesite.modules.apsbiz.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jeesite.modules.apsbiz.entity.BizDailyWorkPlanTemp;
import com.jeesite.modules.apsbiz.entity.BizShipRealTime;
import com.jeesite.common.base.YhBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 在泊船舶动态数据
 *
 * @author zhudi
 * @date 2024-12-08 21:19:20
 */
@Mapper
public interface BizShipRealTimeMapper extends YhBaseMapper<BizShipRealTime> {

    @Select(
            "SELECT " +
                    "COUNT(A.ID) " +
                    "FROM ALG_SHIP_REAL_TIME A LEFT JOIN ALG_SHIP_FORECAST B ON A.VOYAGE_NO = B.VOYAGE_NO " +
                    "${ew.customSqlSegment}"
    )
    Long pageCount(@Param(Constants.WRAPPER) QueryWrapper<BizShipRealTime> queryWrapper);


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
            "	B.ALGORITHM_STATE AS forecastAlgorithmState " +
            "FROM ALG_SHIP_REAL_TIME A LEFT JOIN ALG_SHIP_FORECAST B ON A.VOYAGE_NO = B.VOYAGE_NO " +
            "${ew.customSqlSegment}" +
            " ) TMP \n" +
            "\tWHERE\n" +
            "\t\tROWNUM <= #{size} \n" +
            "\t) \n" +
            "WHERE\n" +
            "\tROW_ID > #{page}")
    List<BizShipRealTime> page(@Param(Constants.WRAPPER) QueryWrapper<BizShipRealTime> queryWrapper, @Param("page") Integer page, @Param("size") Integer size);

}
