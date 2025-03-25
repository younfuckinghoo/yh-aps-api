package com.jeesite.modules.algorithm.feign.interceptor;

import com.jeesite.modules.algorithm.runner.DMSLoginApplicationRunner;
import feign.RequestInterceptor;
import feign.RequestTemplate;


public class DmsClientInterceptor implements RequestInterceptor {

    private final DMSLoginApplicationRunner runner;

    public DmsClientInterceptor(DMSLoginApplicationRunner runner) {
        this.runner = runner;
    }

    @Override
    public void apply(RequestTemplate template) {
        String path = template.path();
        if (!"/token/auth".equals(path)) {
            String tenantId = runner.getDmsRequestTenantId();
            String token = runner.getDmsRequestToken();
            template.header("Authorization", "Bearer " + token);
            template.header("Tenant-Id", tenantId);
        }

    }
}
