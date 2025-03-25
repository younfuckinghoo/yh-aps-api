package com.jeesite.modules.algorithm.domain;

import com.jeesite.modules.algorithm.entity.AlgTide;
import lombok.Data;
import org.springframework.util.ReflectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlanningTideDO {

    private AlgTide tide;

    public PlanningTideDO(AlgTide tide) {
        this.tide = tide;
    }

    private List<TidePoint> tidePointList;



    public static class TidePoint{

        private BigDecimal tideHeight;
        private LocalDateTime tideDateTime;
        private boolean highTide;
        private boolean isDelete;
        public TidePoint(){

        }
        public TidePoint(BigDecimal tideHeight, LocalDate tideDate, LocalTime tideTime) {
            this.tideHeight = tideHeight;
            this.tideDateTime = LocalDateTime.of(tideDate,tideTime);
        }

        public BigDecimal getTideHeight() {
            return tideHeight;
        }

        public void setTideHeight(BigDecimal tideHeight) {
            this.tideHeight = tideHeight;
        }

        public LocalDateTime getTideDateTime() {
            return tideDateTime;
        }

        public void setTideDateTime(LocalDateTime tideDateTime) {
            this.tideDateTime = tideDateTime;
        }

        public boolean isHighTide() {
            return highTide;
        }

        public void setHighTide(boolean highTide) {
            this.highTide = highTide;
        }

        public boolean isDelete() {
            return isDelete;
        }

        public void setDelete(boolean delete) {
            isDelete = delete;
        }
    }


    public List<TidePoint> getTidePointList(){
        if (this.tidePointList!=null) return this.tidePointList;
        this.tidePointList = new ArrayList<>();
        TidePoint tidePoint1 = new TidePoint(this.tide.getTideHeight1(), this.tide.getTideDate(), this.tide.getTideTime1());
        if (this.tide.getTideHeight1().compareTo(this.tide.getTideHeight2())>0){
            tidePoint1.setHighTide(true);
        }
        TidePoint tidePoint2 = new TidePoint(this.tide.getTideHeight2(), this.tide.getTideDate(),this.tide.getTideTime2());
        tidePoint2.setHighTide(!tidePoint1.highTide);
        TidePoint tidePoint3 = new TidePoint(this.tide.getTideHeight3(), this.tide.getTideDate(),this.tide.getTideTime3());
        tidePoint3.setHighTide(!tidePoint2.highTide);
        this.tidePointList.add(tidePoint1);
        this.tidePointList.add(tidePoint2);
        this.tidePointList.add(tidePoint3);
        if (this.tide.getTideHeight4()!=null && this.tide.getTideHeight4().compareTo(BigDecimal.ZERO)>0){
            TidePoint tidePoint4 = new TidePoint(this.tide.getTideHeight4(), this.tide.getTideDate(),this.tide.getTideTime4()   );
            tidePoint4.setHighTide(!tidePoint3.highTide);
            this.tidePointList.add(tidePoint4);
        }
        return this.tidePointList;


    }


    public BigDecimal getTideHeight(LocalTime localTime){
        final int hour = localTime.getHour();
        final Object result = ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(AlgTide.class, "getH" + hour), this.tide);
        return (BigDecimal) result;

    }

}
