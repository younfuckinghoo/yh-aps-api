
package com.jeesite.common.base;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author zd
 * @date 2017/10/29
 */
public interface CommonConstants {

	/**
	 * header 中租户ID
	 */
	String TENANT_ID = "TENANT-ID";

	/**
	 * header 中版本信息
	 */
	String VERSION = "VERSION";

	/**
	 * 租户ID
	 */
	Long TENANT_ID_1 = 1L;

	/**
	 * 删除
	 */
	String STATUS_DEL = "1";

	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单树根节点
	 */
	Long MENU_TREE_ROOT_ID = -1L;

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "yh-ui";

	/**
	 * 移动端工程名
	 */
	String UNI_END_PROJECT = "yh-app";

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "yh";

	/**
	 * 公共参数
	 */
	String YH_PUBLIC_PARAM_KEY = "YH_PUBLIC_PARAM_KEY";

	/**
	 * 默认存储bucket
	 */
	String BUCKET_NAME = "zd";

	/**
	 * 滑块验证码
	 */
	String IMAGE_CODE_TYPE = "blockPuzzle";

	/**
	 * 验证码开关
	 */
	String CAPTCHA_FLAG = "captcha_flag";

	/**
	 * 密码传输是否加密
	 */
	String ENC_FLAG = "enc_flag";

	/**
	 * 客户端允许同时在线数量
	 */
	String ONLINE_QUANTITY = "online_quantity";

	/**
	 * 请求开始时间
	 */
	String REQUEST_START_TIME = "REQUEST-START-TIME";

	/**
	 * 当前页
	 */
	String CURRENT = "current";

	/**
	 * size
	 */
	String SIZE = "size";


	/**
	 * 字段清空标识
	 */
	String CLEAR_FLAG = "clearFlag";

	/**
	 * 数字字段清空标识
	 */
	String CLEAR_NUM_FLAG = "-999999999";

	/**
	 * 日期字段清空标识
	 */
	Date CLEAR_DATE_FLAG = DateUtil.parseDate("1900-01-01 00:00:00");
}
