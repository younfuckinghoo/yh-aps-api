package com.jeesite.modules.apsbiz.utils.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rectangle {
    public long xStart;
    public long xEnd;
    public int yStart;
    public int yEnd;

//    public Rectangle(long xStart, long xEnd, int yStart, int yEnd) {
//        this.xStart = xStart;
//        this.xEnd = xEnd;
//        this.yStart = yStart;
//        this.yEnd = yEnd;
//    }
//
//    boolean isOverlap(Rectangle r) {
//        return !(this.xEnd < r.xStart || this.xStart > r.xEnd || this.yStart > r.yEnd || this.yEnd < r.yStart);
//    }
}