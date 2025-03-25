/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.web.http;

import com.jeesite.common.web.http.HttpClientUtils;

/**
 * HTTP客户端测试工具类（支持HTTPS）
 * @author Winhai
 * @version 2017-3-27
 */
public class HttpClientUtilsTest {

	public static void main(String[] args) {
		String content = HttpClientUtils.get("https://jeesite.com");
		System.out.println(content);
	}

}
