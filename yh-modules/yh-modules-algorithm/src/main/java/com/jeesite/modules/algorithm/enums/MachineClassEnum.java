package com.jeesite.modules.algorithm.enums;


import com.jeesite.modules.algorithm.base.BaseEnum;

/**
 * 机械大类
 */
public enum MachineClassEnum implements BaseEnum {

    SHORE(1, "岸机"),
    MOBILE(2, "流动机械");


    private final Integer code;
    private final String name;

    MachineClassEnum(Integer code, String name) {
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

    public static MachineClassEnum getInstanceByCode(Integer code) {
        MachineClassEnum[] values = MachineClassEnum.values();
        for (MachineClassEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new EnumConstantNotPresentException(ShipStatusEnum.class, "机械大类");
    }
}
