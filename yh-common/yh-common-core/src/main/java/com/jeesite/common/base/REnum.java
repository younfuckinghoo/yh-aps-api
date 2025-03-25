package com.jeesite.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口类型
 * @author zhudi
 * @date 2020-11-18
 * <p>
 */
@Getter
@AllArgsConstructor
public enum REnum {

	SUCCESS(0, "成功标记")
	,
	FAIL(1, "系统错误")
	,
	FAIL_BIZ(2, "业务错误")
	,
	FAIL_PARAM(3, "参数错误")
	,
	FAIL_DATABASE(4, "数据库操作")
	,
	FAIL_401(401, "登录信息失效")
	;


	/**
	 * 类型
	 */
	private Integer value;

	/**
	 * 描述
	 */
	private String label;

}
