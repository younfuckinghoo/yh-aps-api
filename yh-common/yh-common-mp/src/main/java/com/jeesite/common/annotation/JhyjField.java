package com.jeesite.common.annotation;


import java.lang.annotation.*;

/**
 * 查询条件注解
 *
 * @author zhudi
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JhyjField
{
	/**
	 * 是否为表中的字段， 必填，默认为Y
	 */
	public boolean tableField() default true;
	/**
	 * 关联数据库中的字段名， 如果类中字段名 与 数据库中字段名完全符合驼峰规则；可不填；否则需要填写库中字段名
	 */
	public String fieldName() default "";
	/**
	 * 与filedName配合使用，默认为主表的alias;
	 */
	public String fieldAlias() default "";
	/**
	 * 相等查询
	 * 用于MybatisPlus的 eq()
	 */
	public boolean eq() default false;
	/**
	 * 相等查询
	 * 用于MybatisPlus的 ge()
	 */
	public boolean ge() default false;
	/**
	 * 相等查询
	 * 用于MybatisPlus的 gt()
	 */
	public boolean gt() default false;
	/**
	 * 相等查询
	 * 用于MybatisPlus的 le()
	 */
	public boolean le() default false;
	/**
	 * 相等查询
	 * 用于MybatisPlus的 lt()
	 */
	public boolean lt() default false;
	/**
	 * 不等于查询
	 * 用于MybatisPlus的 ne()
	 */
	public boolean ne() default false;
	/**
	 * 包含查询, 多个值时用逗号','拼接字符串
	 * 用于MybatisPlus的 in()
	 */
	public boolean in() default false;
	/**
	 * 模糊查询（左右模糊）
	 * 用于MybatisPlus的 like()
	 */
	public boolean like() default false;
	/**
	 * 模糊查询（左模糊）
	 * 用于MybatisPlus的 like()
	 */
	public boolean likeLeft() default false;
	/**
	 * 模糊查询（右模糊）
	 * 用于MybatisPlus的 like()
	 */
	public boolean likeRight() default false;

	/**
	 * 不为NULL查询
	 * 用于MybatisPlus的 notNULL()
	 */
	public boolean notNull() default false;

	/**
	 * 为NULL查询， 需要为null查询的字段放到fieldName中
	 * 用于MybatisPlus的 isNULL()
	 */
	public boolean nullField() default false;


	/**
	 * 日期 相等
	 */
	public boolean dateEq() default false;
	/**
	 * 日期 大于等于
	 */
	public boolean dateGe() default false;
	/**
	 * 日期 小于等于
	 */
	public boolean dateLe() default false;
	/**
	 * 日期 大于
	 */
	public boolean dateGt() default false;
	/**
	 * 日期 小于
	 */
	public boolean dateLt() default false;


	/**
	 * 日期时间 相等
	 */
	public boolean datetimeEq() default false;
	/**
	 * 日期时间 大于等于
	 */
	public boolean datetimeGe() default false;
	/**
	 * 日期时间 小于等于
	 */
	public boolean datetimeLe() default false;
	/**
	 * 日期时间 大于
	 */
	public boolean datetimeGt() default false;
	/**
	 * 日期时间 小于
	 */
	public boolean datetimeLt() default false;



	/**
	 * 日期 年份 相等
	 */
	public boolean dateYearEq() default false;

	/**
	 * 日期 月份 相等
	 */
	public boolean dateMonthEq() default false;

	/**
	 * 日期 日 相等
	 */
	public boolean dateDayEq() default false;

	/**
	 * 日期 季度 相等
	 */
	public boolean dateQuarterEq() default false;

	/**
	 * 组合模糊查询， 组合查询内容用逗号分割存到fieldName中
	 */
	public boolean searchLike() default false;

	/**
	 * 仅供查询使用
	 */
	public boolean queryOnly() default false;

	/**
	 * 仅供展示使用
	 */
	public boolean showOnly() default false;
}
