package com.jeesite.modules.algorithm.handler;

import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 重写LocalDateTimeTypeHandler ，解决shardingsphere + mybatis LocalDateTime转换问题
 *
 * @author xudongmaster
 */
@Component
public class LocalDateTimeTypeHandler extends org.apache.ibatis.type.LocalDateTimeTypeHandler {
    public LocalDateTimeTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, new Timestamp(this.toTimeMillis(parameter)));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(columnName);
        return sqlTimestamp != null ? this.toLocalDateTime(sqlTimestamp.getTime()) : null;
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
        return sqlTimestamp != null ? this.toLocalDateTime(sqlTimestamp.getTime()) : null;
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
        return sqlTimestamp != null ? this.toLocalDateTime(sqlTimestamp.getTime()) : null;
    }

    private long toTimeMillis(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private LocalDateTime toLocalDateTime(long timeMillis) {
        return new Date(timeMillis).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}