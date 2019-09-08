package com.qjkji.orderproject.pay.properties;

import com.github.wxpay.sdk.WXPayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@ConfigurationProperties(prefix = "pay.wxpay")
public class WeixinProperties implements WXPayConfig {

    private static final Logger logger = LoggerFactory.getLogger(WeixinProperties.class);

    /** 公众账号ID */
    private String appID;

    /** 商户号 */
    private String mchID;

    private String subMchId;

    /** API 密钥 */
    private String key;

    /** API 沙箱环境密钥 */
    private String sandboxKey;

    /** API证书绝对路径 */
    private String certPath;

    /** 退款异步通知地址 */
    private String notifyUrl;

    private Boolean useSandbox;

    /** HTTP(S) 连接超时时间，单位毫秒 */
    private int httpConnectTimeoutMs = 8000;

    /** HTTP(S) 读数据超时时间，单位毫秒 */
    private int httpReadTimeoutMs = 10000;


    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    @Override
    public InputStream getCertStream()  {
        File certFile = new File(certPath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(certFile);
        } catch (FileNotFoundException e) {
            logger.error("cert file not found, path={}, exception is:{}", certPath, e);
        }
        return inputStream;
    }

    @Override
    public String getKey(){
        return key;
    }


    @Override
    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    @Override
    public String getMchID() {
        return mchID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSandboxKey() {
        return sandboxKey;
    }

    public void setSandboxKey(String sandboxKey) {
        this.sandboxKey = sandboxKey;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Boolean getUseSandbox() {
        return useSandbox;
    }

    public void setUseSandbox(Boolean useSandbox) {
        this.useSandbox = useSandbox;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return httpConnectTimeoutMs;
    }

    public void setHttpConnectTimeoutMs(int httpConnectTimeoutMs) {
        this.httpConnectTimeoutMs = httpConnectTimeoutMs;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return httpReadTimeoutMs;
    }

    public void setHttpReadTimeoutMs(int httpReadTimeoutMs) {
        this.httpReadTimeoutMs = httpReadTimeoutMs;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    @Override
    public String toString() {
        return "WeixinProperties{" +
                "appID='" + appID + '\'' +
                ", mchID='" + mchID + '\'' +
                ", key='" + key + '\'' +
                ", sandboxKey='" + sandboxKey + '\'' +
                ", certPath='" + certPath + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", useSandbox=" + useSandbox +
                ", httpConnectTimeoutMs=" + httpConnectTimeoutMs +
                ", httpReadTimeoutMs=" + httpReadTimeoutMs +
                '}';
    }
}

