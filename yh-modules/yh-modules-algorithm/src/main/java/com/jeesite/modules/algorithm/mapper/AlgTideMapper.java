package com.jeesite.modules.algorithm.mapper;

import com.jeesite.modules.algorithm.entity.AlgTide;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 潮汐表 Mapper 接口
 * </p>
 *
 * @author HaoY
 * @since 2024-12-19
 */
@Mapper
public interface AlgTideMapper extends BaseMapper<AlgTide> {

    @Select("<script>" +
            "SELECT ID,TIDE_DATE,TIDE_HEIGHT1,TIDE_HEIGHT2,TIDE_HEIGHT3,TIDE_HEIGHT4,TIDE_TIME1,TIDE_TIME2,TIDE_TIME3,TIDE_TIME4,H0,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,H11,H12,H13,H14,H15,H16,H17,H18,H19,H20,H21,H22,H23,CREATE_TIME,REVISE_TIME FROM ALG_TIDE WHERE (TIDE_DATE > #{tideDate,jdbcType=DATE})"+
            "</script>")
    public List<AlgTide> listTideByDate(@Param("tideDate")LocalDate tideDate);

}
