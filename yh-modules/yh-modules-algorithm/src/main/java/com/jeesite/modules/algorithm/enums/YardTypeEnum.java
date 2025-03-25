package com.jeesite.modules.algorithm.enums;

import com.jeesite.modules.algorithm.base.BaseEnum;

/**
 * 库场类型 1库房 2堆场
 */
public enum YardTypeEnum implements BaseEnum {
	KU(1, "库房"),
	DUI(2, "堆场");


	private final int code;
	private final String name;

	YardTypeEnum(int code, String name) {
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

	public static YardTypeEnum getInstanceByCode(int code) {
		YardTypeEnum[] values = YardTypeEnum.values();
		for (YardTypeEnum value : values) {
			if (value.getCode() == code)
				return value;
		}
		 throw new EnumConstantNotPresentException(YardTypeEnum.class,"库场类型");
	}
}
