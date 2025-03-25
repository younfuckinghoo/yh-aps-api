package com.jeesite.modules.algorithm.feign.client;

import com.alibaba.fastjson2.JSONObject;
import com.jeesite.common.base.R;
import com.jeesite.modules.algorithm.feign.fallback.DmsClientFallBackFactory;
import com.jeesite.modules.algorithm.feign.interceptor.DmsFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "dms", url = "${dms.url}", fallbackFactory = DmsClientFallBackFactory.class, configuration = DmsFeignClientConfiguration.class)
public interface DmsClientService {

    @GetMapping("/dmssurroundpoint/getJson")
    R<JSONObject> getRulerConfig();


    @GetMapping(path = "/token/auth")
    R<String> login(@RequestHeader("auth") String auth);
}
