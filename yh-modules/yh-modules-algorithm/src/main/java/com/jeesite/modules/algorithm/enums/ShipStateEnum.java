package com.jeesite.modules.algorithm.enums;

import com.jeesite.modules.algorithm.base.BaseEnum;

public enum ShipStateEnum implements BaseEnum {
    NOT_READY(1,"未就绪"),
    READY(2,"已就绪"),
    WORKING(3,"作业中"),
    FINISH(4,"作业已完成");


    private int code;
    private String name;

    ShipStateEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ShipStateEnum getInstanceByCode(int code){
        ShipStateEnum[] values = ShipStateEnum.values();
        for (ShipStateEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
		throw new EnumConstantNotPresentException(ShipStateEnum.class,"算法状态");
    }
}
