package com.qjkji.orderproject.pay.config;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PayConfig {


    @Value("${pay.alipay.appid}")
    private String alipayAppId;

    @Value("${pay.alipay.alipayPublicKey}")
    private String alipayPublicKey;

    @Value("${pay.alipay.appPrivateKey}")
    private String alipayPrivateKey;

    @Value("${pay.alipay.gatewayUrl}")
    private String alipayGatewayUrl;


    /**
     * 支付宝api设置
     * @return
     */
    @Bean
    public AlipayClient alipayClient(){
        AlipayClient client = new DefaultAlipayClient(
                alipayGatewayUrl, alipayAppId, alipayPrivateKey,
                "JSON","UTF-8",alipayPublicKey,"RSA2"
        );
        return client;
    }

//    @Resource
//    WeixinProperties weixinProperties;
//
//    @Bean
//    public WXPay wxPay() {
//        return new WXPay(weixinProperties, WXPayConstants.SignType.MD5, weixinProperties.getUseSandbox() );
//    }
//
//    @Bean
//    public WXPayClient wxPayClient() {
//        return new WXPayClient(weixinProperties, WXPayConstants.SignType.MD5, weixinProperties.getUseSandbox());
//    }


}
