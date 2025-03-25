package com.jeesite.modules.algorithm.enums;

import com.jeesite.modules.algorithm.base.BaseEnum;

public enum BerthDirectionEnum implements BaseEnum {
	LEFT(1, "左"),
	RIGHT(2, "右");


	private int code;
	private String name;

	BerthDirectionEnum(int code, String name) {
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

	public static BerthDirectionEnum getInstanceByCode(int code) {
		BerthDirectionEnum[] values = BerthDirectionEnum.values();
		for (BerthDirectionEnum value : values) {
			if (value.getCode() == code)
				return value;
		}
		 throw new EnumConstantNotPresentException(BerthDirectionEnum.class,"着岸方向");
	}
}
