package com.jeesite.common.annotation;


import java.lang.annotation.*;

/**
 * 逻辑删除标识
 *
 * @author zhudi
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JhyjDeleted
{
	/**
	 * 关联数据库中的字段名， 必填
	 */
	public String fieldName() default "deleted";
	/**
	 * 默认假删逻辑值(有效值标识)
	 */
	public int available() default 0;
	/**
	 * 默认假删逻辑值(被删除标识)
	 */
	public int deleted() default 1;
}
