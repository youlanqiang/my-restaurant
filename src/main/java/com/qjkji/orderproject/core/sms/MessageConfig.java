package com.qjkji.orderproject.core.sms;

import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

    @Bean
    public ZhenziSmsClient zhenziSmsClient(){

        String apiUrl = "https://sms_developer.zhenzikj.com"; //个人开发者
        //String apiUrl = "https://sms.zhenzikj.com"; 企业开发者

        String appId = "102409";
        String appSecret = "ad47208f-4bcb-4aec-8836-77dfe1a199cc";

        ZhenziSmsClient client = new ZhenziSmsClient(
                apiUrl, appId, appSecret
        );
        return client;
    }






}
