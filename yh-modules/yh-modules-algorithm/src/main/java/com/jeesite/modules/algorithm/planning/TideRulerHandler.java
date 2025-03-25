package com.jeesite.modules.algorithm.planning;

import com.jeesite.modules.algorithm.domain.PlanningShipDO;
import com.jeesite.modules.algorithm.domain.PlanningTideDO;
import com.jeesite.modules.algorithm.utils.MathUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 泊位上一条船舶完工时间后，计算可乘潮水的时间（根据潮汐表查看潮水合适高度时间）作为靠泊时间。
 *  乘潮水高度计算公式：船舶吃水*1.1-航道水深（港池实际水深）= 最低的乘潮靠泊米数
 */

@Data
public class TideRulerHandler {

    private List<PlanningTideDO> tideDOList;

    public TideRulerHandler(List<PlanningTideDO> tideDOList) {
        this.tideDOList = tideDOList;
    }


    /**
     *
     * @param planningShipDO
     * @param berthTime
     * @return
     */
    public boolean validTide(PlanningShipDO planningShipDO, LocalDateTime berthTime) {

        final LocalDate localDate = berthTime.toLocalDate();
        final LocalTime localTime = berthTime.toLocalTime();
        final Optional<PlanningTideDO> tideDOOptional = tideDOList.stream().filter(t -> t.getTide().getTideDate().equals(localDate)).findFirst();
        final PlanningTideDO planningTideDO = tideDOOptional.orElse(null);
        if (planningTideDO!=null){
            final BigDecimal tideHeight = planningTideDO.getTideHeight(localTime);
            return MathUtil.isMoreEqualThan(tideHeight,planningShipDO.getRequiredTideHeight());
        }


        return true;

    }
}
