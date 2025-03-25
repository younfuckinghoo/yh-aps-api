package com.jeesite.modules.algorithm.feign.interceptor;

import com.jeesite.modules.algorithm.runner.DMSLoginApplicationRunner;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class DmsFeignClientConfiguration {


    @Bean
    public RequestInterceptor dmsFeignClientInterceptor(DMSLoginApplicationRunner dmsLoginApplicationRunner) {
        return new DmsClientInterceptor(dmsLoginApplicationRunner);
    }

}
