package top.youlanqiang.orderproject.core.entity.enums;

import top.youlanqiang.orderproject.core.exception.UnmessageException;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付方式
 */
public enum PaymentMethod {

    ALIPAY("支付宝", 1),
    WEIXIN("微信", 2),
    CASH("现金", 3);

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Integer type;

    PaymentMethod(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public static PaymentMethod valueOf(Integer type){
        switch(type){
            case 1:
                return ALIPAY;
            case 2:
                return WEIXIN;
            case 3:
                return CASH;
        }
        throw new UnmessageException("无法识别的支付方式。");
    }

}
