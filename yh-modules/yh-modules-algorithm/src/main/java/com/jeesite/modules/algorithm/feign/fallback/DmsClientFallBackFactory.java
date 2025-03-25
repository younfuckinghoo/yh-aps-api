package com.jeesite.modules.algorithm.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.jeesite.common.base.R;
import com.jeesite.modules.algorithm.feign.client.DmsClientService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DmsClientFallBackFactory implements FallbackFactory<DmsClientService> {
    @Override
    public DmsClientService create(Throwable cause) {
        return new DmsClientService() {
            @Override
            public R<JSONObject> getRulerConfig() {
                return R.failed();
            }

            @Override
            public R<String> login(String auth) {
                return R.failed();
            }
        };
    }
}
