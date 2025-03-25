package com.jeesite.common.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface YhBaseMapper<T> extends BaseMapper<T> {

    @Select("SELECT ${er.customSqlSegment} FROM ${tableName} ${ew.customSqlSegment}")
    List<T> queryFieldsList(@Param("er") QueryWrapper<T> readWrapper, @Param(Constants.WRAPPER) QueryWrapper<T> queryWrapper, @Param("tableName") String tableName);

    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tTMP.*,\n" +
            "\t\tROWNUM ROW_ID \n" +
            "\tFROM\n" +
            "\t\t( SELECT * FROM ${tableName} ${ew.customSqlSegment} ) TMP \n" +
            "\tWHERE\n" +
            "\t\tROWNUM <= #{size} \n" +
            "\t) \n" +
            "WHERE\n" +
            "\tROW_ID > #{page}")
    List<T> page(@Param(Constants.WRAPPER) QueryWrapper<T> queryWrapper, @Param("page") Integer page, @Param("size") Integer size, @Param("tableName") String tableName);
}