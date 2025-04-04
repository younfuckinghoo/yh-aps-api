/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.interceptor.LogInterceptor;

/**
 * 后台管理日志记录拦截器
 * @author Winhai
 * @version 2018年1月10日
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name="web.interceptor.log.enabled", havingValue="true", matchIfMissing=true)
@EnableWebMvc
public class LogInterceptorConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration = registry.addInterceptor(new LogInterceptor());
		for (String uri : StringUtils.splitComma(Global.getProperty("web.interceptor.log.addPathPatterns"))){
			if (StringUtils.isNotBlank(uri)){
				registration.addPathPatterns(StringUtils.trim(uri));
			}
		}
		for (String uri : StringUtils.splitComma(Global.getProperty("web.interceptor.log.excludePathPatterns"))){
			if (StringUtils.isNotBlank(uri)){
				registration.excludePathPatterns(StringUtils.trim(uri));
			}
		}
	}

}