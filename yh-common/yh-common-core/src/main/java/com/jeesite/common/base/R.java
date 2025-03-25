
package com.jeesite.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author zd
 */
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "响应信息主体")
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Schema(description = "返回标记：成功标记=0，失败标记=1")
	private int code;

	@Getter
	@Setter
	@Schema(description = "返回信息")
	private String msg;

	@Getter
	@Setter
	@Schema(description = "数据")
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, REnum.SUCCESS.getValue(), null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, REnum.SUCCESS.getValue(), null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, REnum.SUCCESS.getValue(), msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, REnum.FAIL.getValue(), null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, REnum.FAIL.getValue(), msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, REnum.FAIL.getValue(), null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, REnum.FAIL.getValue(), msg);
	}

	public static <T> R<T> failed(Integer code, String msg) {
		return restResult(null, code, msg);
	}

	static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

	// 自定义错误

	/**
	 * 为空错误
	 * @param field
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed_empty(String field) {
		return restResult(null, REnum.FAIL_PARAM.getValue(), "["+field+"]不能为空");
	}

	/**
	 * 为空数据
	 * @param field
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed_null(String field) {
		return restResult(null, REnum.FAIL_PARAM.getValue(), "["+field+"]未查询到数据");
	}

	/**
	 * token认证失败
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed_token() {
		return restResult(null, REnum.FAIL_401.getValue(), REnum.FAIL_401.getLabel());
	}

	/**
	 * 取值范围错误
	 * @param field
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed_scope(String field) {
		return restResult(null, REnum.FAIL_PARAM.getValue(), "["+field+"]取值范围错误");
	}

	/**
	 * 数据库操作错误
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed_database(String data) {
		return restResult(null, REnum.FAIL_DATABASE.getValue(), "["+data+"]操作错误");
	}

	/**
	 * 业务错误
	 * @param msg
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed_biz(String msg) {
		return restResult(null, REnum.FAIL_BIZ.getValue(), "[业务问题]"+msg);
	}


	/**
	 * 校验是否正确
	 * @param r
	 * @return
	 */
	public static Boolean checkOk(R r){
		if(r == null || r.getCode() != 0){
			return false;
		}
		return true;
	}
}
