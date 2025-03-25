package com.jeesite.modules.algorithm.planning.ruler;

import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;

/**
 * 一周内泊位哪天休息
 */
@Data
public class BerthRestInWeek {

    private String berthNo;

    /**
     * 1 (Monday) to 7 (Sunday)
     * {@link DayOfWeek}
     */

    private List<DayOfWeek> daysList;

}
