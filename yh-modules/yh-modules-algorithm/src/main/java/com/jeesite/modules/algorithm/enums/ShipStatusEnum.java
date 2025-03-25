package com.jeesite.modules.algorithm.enums;


import com.jeesite.modules.algorithm.base.BaseEnum;

public enum ShipStatusEnum implements BaseEnum {
//    1:待定;2:预报;3:确报;4:靠泊;5:离泊;6:离港;7:预排;
    PRE_PLAN(7,"预排");


    private int code;
    private String name;

    ShipStatusEnum(int code, String name){
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

    public static ShipStatusEnum getInstanceByCode(int code){
        ShipStatusEnum[] values = ShipStatusEnum.values();
        for (ShipStatusEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new EnumConstantNotPresentException(ShipStatusEnum.class,"船舶状态");
    }
}
