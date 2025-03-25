package com.jeesite.modules.algorithm.enums;

import com.jeesite.modules.algorithm.base.BaseEnum;

/**
 * 货物疏港要求
 */
public enum CargoEvacuationEnum implements BaseEnum {
	AUTOMOBILE(1, " 汽运疏港"),
	TRAIN(2, "火车疏港"),
	FLOW(3, "流程疏港");


	private final int code;
	private final String name;

	CargoEvacuationEnum(int code, String name) {
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

	public static CargoEvacuationEnum getInstanceByCode(int code) {
		CargoEvacuationEnum[] values = CargoEvacuationEnum.values();
		for (CargoEvacuationEnum value : values) {
			if (value.getCode() == code)
				return value;
		}
		 throw new EnumConstantNotPresentException(CargoEvacuationEnum.class,"货物疏港要求");
	}
}
