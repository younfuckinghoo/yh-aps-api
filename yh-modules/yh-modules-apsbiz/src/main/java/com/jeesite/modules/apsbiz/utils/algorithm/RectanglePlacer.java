package com.jeesite.modules.apsbiz.utils.algorithm;

import java.util.ArrayList;
import java.util.List;

public class RectanglePlacer {
    public List<Rectangle> rectangles;
    private long minX;
    private long maxX;
    private int minY;
    private int maxY;

    public RectanglePlacer(long minX, long maxX, int minY, int maxY){
        this.rectangles = new ArrayList<>();
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }


    public Rectangle placeNewRectangle(long startX, long width, int height) {
        for (long x = startX > minX ? startX : minX; x < maxX ; ) {
            for (int y = minY; y < maxY - height + 1; y++) {
                Rectangle newRectangle = new Rectangle(x, x + width, y, y + height);
                if (!isOverlap(newRectangle)) {
//                    rectangles.add(newRectangle);
                    return newRectangle;
                }
            }
            x = x + 30*60*1000;
        }
        return null;  // 如果找不到合适的位置，返回null
    }

    private boolean isOverlap(Rectangle newRectangle) {
        for (Rectangle rectangle : rectangles) {
            if (rectangle.xStart <= newRectangle.xEnd && rectangle.xEnd >= newRectangle.xStart &&
                    rectangle.yStart <= newRectangle.yEnd && rectangle.yEnd >= newRectangle.yStart) {
                return true;
            }
        }
        return false;
    }
}