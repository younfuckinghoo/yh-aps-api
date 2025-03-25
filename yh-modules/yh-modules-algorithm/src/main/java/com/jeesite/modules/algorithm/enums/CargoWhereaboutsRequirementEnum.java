package com.jeesite.modules.algorithm.enums;

import com.jeesite.modules.algorithm.base.BaseEnum;

/**
 * 货物去向要求
 */
public enum CargoWhereaboutsRequirementEnum implements BaseEnum {
	PICK(1, "直取"),
	STORE(2, "存放");


	private final int code;
	private final String name;

	CargoWhereaboutsRequirementEnum(int code, String name) {
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

	public static CargoWhereaboutsRequirementEnum getInstanceByCode(int code) {
		CargoWhereaboutsRequirementEnum[] values = CargoWhereaboutsRequirementEnum.values();
		for (CargoWhereaboutsRequirementEnum value : values) {
			if (value.getCode() == code)
				return value;
		}
		 throw new EnumConstantNotPresentException(CargoWhereaboutsRequirementEnum.class,"货物去向要求");
	}
}
