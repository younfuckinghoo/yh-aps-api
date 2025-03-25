/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.callback;

/**
 * 方法回调接口
 * @author Winhai
 */
@FunctionalInterface
public interface MethodCallback {

	Object execute(Object... params);
	
}
