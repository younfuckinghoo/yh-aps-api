package com.jeesite.modules.algorithm.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
@MappedJdbcTypes(JdbcType.DATE)  //数据库类型
@MappedTypes({LocalDate.class})
public class LocalDateTypeHandler  extends BaseTypeHandler<LocalDate> {
    public LocalDateTypeHandler() {
    }

    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (LocalDate)rs.getObject(columnName, LocalDate.class);
    }

    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (LocalDate)rs.getObject(columnIndex, LocalDate.class);
    }

    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (LocalDate)cs.getObject(columnIndex, LocalDate.class);
    }
}