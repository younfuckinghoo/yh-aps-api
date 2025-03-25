package com.jeesite.modules.algorithm.enums;

import com.jeesite.modules.algorithm.base.BaseEnum;

public enum DayNightEnum implements BaseEnum {
	DAY(1, "白班"),
	NIGHT(2, "夜班");


	private final int code;
	private final String name;

	DayNightEnum(int code, String name) {
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

	public static DayNightEnum getInstanceByCode(int code) {
		DayNightEnum[] values = DayNightEnum.values();
		for (DayNightEnum value : values) {
			if (value.getCode() == code)
				return value;
		}
		 throw new EnumConstantNotPresentException(DayNightEnum.class,"班次（白班/夜班）");
	}
}
