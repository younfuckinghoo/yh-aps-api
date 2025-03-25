package com.jeesite.modules.algorithm.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    /**
     * 找到下一个半时
     *
     * @param localTime
     * @return
     */
    public static LocalDateTime findAfterHalfHour(LocalDateTime localTime) {
        LocalDateTime time = localTime.plusMinutes(30);
        int minute = time.getMinute();
        if (BigDecimal.valueOf(minute).divide(BigDecimal.valueOf(30), 0, RoundingMode.FLOOR).intValue() > 0) {
            return time.withMinute(30);
        } else {
            return time.withMinute(0);
        }
    }


	public static LocalDate parseDate(String dateStr, String formatter){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
		return LocalDate.parse(dateStr, dateTimeFormatter);
	}

	public static LocalTime parseTime(String dateStr, String formatter){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
		return LocalTime.parse(dateStr, dateTimeFormatter);
	}






}
