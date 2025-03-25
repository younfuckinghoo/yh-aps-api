package com.jeesite.modules.algorithm.enums;


import com.jeesite.modules.algorithm.base.BaseEnum;

public enum MachineTypeEnum implements BaseEnum {

    MEN(1, "门机"),
    XIE(2, "卸船机");


    private final Integer code;
    private final String name;

    MachineTypeEnum(Integer code, String name) {
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

    public static MachineTypeEnum getInstanceByCode(Integer code) {
        MachineTypeEnum[] values = MachineTypeEnum.values();
        for (MachineTypeEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new EnumConstantNotPresentException(ShipStatusEnum.class, "船舶状态");
    }
}
