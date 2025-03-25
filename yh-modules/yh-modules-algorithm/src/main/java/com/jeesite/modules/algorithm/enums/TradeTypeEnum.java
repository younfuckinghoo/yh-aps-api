package com.jeesite.modules.algorithm.enums;


import com.jeesite.modules.algorithm.base.BaseEnum;

public enum TradeTypeEnum implements BaseEnum {
    IN(1, "内贸"),
    OUT(2, "外贸");


    private int code;
    private String name;

    TradeTypeEnum(int code, String name) {
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

    public static TradeTypeEnum getInstanceByCode(int code) {
        TradeTypeEnum[] values = TradeTypeEnum.values();
        for (TradeTypeEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
		throw new EnumConstantNotPresentException(TradeTypeEnum.class,"作业类型");
    }
}
