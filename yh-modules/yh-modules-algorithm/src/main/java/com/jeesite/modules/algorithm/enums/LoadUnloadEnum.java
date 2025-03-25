package com.jeesite.modules.algorithm.enums;

import com.jeesite.modules.algorithm.base.BaseEnum;

public enum LoadUnloadEnum implements BaseEnum {
	LOAD(1, "装"),
	UNLOAD(2, "卸");


	private int code;
	private String name;

	LoadUnloadEnum(int code, String name) {
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

	public static LoadUnloadEnum getInstanceByCode(int code) {
		LoadUnloadEnum[] values = LoadUnloadEnum.values();
		for (LoadUnloadEnum value : values) {
			if (value.getCode() == code)
				return value;
		}
		 throw new EnumConstantNotPresentException(LoadUnloadEnum.class,"装卸类型");
	}
}
