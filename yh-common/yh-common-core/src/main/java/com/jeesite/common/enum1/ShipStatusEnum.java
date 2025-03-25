package com.jeesite.common.enum1;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhudi
 * @date 2022-04-26
 * <p>
 * 船舶状态枚举
 */
@Getter
@AllArgsConstructor
public enum ShipStatusEnum {


	// 靠泊计划涉及状态
//	/**
//	 * 已预报未预排
//	 */
//	STATE0(0, "已预报未预排"),
//
//	/**
//	 * APS排泊预排
//	 */
//	STATE1(1, "APS排泊预排"),

	/**
	 * 未抵港
	 */
	STATE2(2, "未抵港"),

	/**
	 * 到港下锚
	 */
	STATE3(3, "到港下锚"),

	/**
	 * 靠泊
	 */
	STATE4(4, "靠泊"),

//	/**
//	 * 已提交待审核
//	 */
//	STATE5(5, "已提交待审核"),
//
//	/**
//	 * 已提交审核通过
//	 */
//	STATE6(6, "已提交审核通过"),
//
//	/**
//	 * 已提交审核不通过
//	 */
//	STATE7(7, "已提交审核不通过"),

	/**
	 * 作业
	 */
	STATE8(8, "作业"),

	/**
	 * 停工
	 */
	STATE9(9, "停工"),

//	/**
//	 * 到港未排
//	 */
//	STATE10(10, "到港未排"),
//
//	/**
//	 * 到港预排
//	 */
//	STATE11(11, "到港预排"),
//
//	/**
//	 * APS预排
//	 */
//	STATE12(12, "APS预排"),
//
//	/**
//	 * APS预排锁定
//	 */
//	STATE13(13, "APS预排锁定"),
//
//	/**
//	 * APS确排
//	 */
//	STATE14(14, "APS确排"),
//
//	/**
//	 * APS确排锁定
//	 */
//	STATE15(15, "APS确排锁定")
	;



	/**
	 * 类型
	 */
	private final Integer status;

	/**
	 * 描述
	 */
	private final String description;

}
