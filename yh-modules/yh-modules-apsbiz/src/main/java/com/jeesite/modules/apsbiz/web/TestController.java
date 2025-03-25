/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.apsbiz.web;

import com.jeesite.common.web.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 账号自助服务Controller
 * @author Winhai
 * @version 2020-9-20
 */
@Controller
@Tag(name = "Test - 测试服务")
@RequestMapping(value = "/test")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class TestController extends BaseController{

	@PostMapping(value = "test")
	@Operation(summary = "测试")
	public String test() {
		return "11111";
	}

}
