package com.jeesite.modules.algorithm.runner;

import com.jeesite.common.base.R;
import com.jeesite.modules.algorithm.feign.client.DmsClientService;
import com.jeesite.modules.algorithm.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class DMSLoginApplicationRunner implements ApplicationRunner {

    // 目前来说默认aps可以直接调dms 所以没有用户限制 不用区分
    // public static ThreadLocal<String>  DMS_REQUEST_TOKEN = new ThreadLocal<>();

    @Value("${dms.auth.username:yulang}")
    private String username;
    @Value("${dms.auth.password:yulang}")
    private String password;

    private final DmsClientService dmsClientService;

    private volatile String dmsRequestToken;

    @Value("${dms.auth.tenant-id:1844185625532571649}")
    private String dmsRequestTenantId;
    private String authedEncrypt;

    public DMSLoginApplicationRunner(@Lazy DmsClientService dmsClientService) {
        this.dmsClientService = dmsClientService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Timer timer = new Timer();
        TimerTask task = new LoginTask();
        timer.schedule(task, 0, 1000L * 60 * 30);

    }


    public void refreshDmsRequestToken() {
        try {
            DMSLoginApplicationRunner.this.authedEncrypt = AuthUtil.authEncrypt(username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        R<String> r = DMSLoginApplicationRunner.this.dmsClientService.login(DMSLoginApplicationRunner.this.authedEncrypt);
        this.dmsRequestToken = r.getData();
    }

    public String getDmsRequestTenantId() {
        return dmsRequestTenantId;
    }

    public String getDmsRequestToken() {
        return dmsRequestToken;
    }

    public class LoginTask extends TimerTask {

        @Override
        public void run() {
            try {
                DMSLoginApplicationRunner.this.authedEncrypt = AuthUtil.authEncrypt(username, password);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            R<String> r = DMSLoginApplicationRunner.this.dmsClientService.login(DMSLoginApplicationRunner.this.authedEncrypt);
            DMSLoginApplicationRunner.this.dmsRequestToken = r.getData();
        }

    }


}
