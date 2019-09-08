package com.qjkji.orderproject.core.entity.enums;


import com.qjkji.orderproject.core.exception.UnmessageException;
import lombok.Getter;
import lombok.Setter;

/**
 * 预约状态
 */
public enum ReservationMethod {

    WAIT("等待中", 1),
    SUCCESS("成功", 2),
    CANCEL("取消", 3);

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Integer type;

    ReservationMethod(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public static ReservationMethod valueOf(Integer type){
        switch(type){
            case 1:
                return WAIT;
            case 2:
                return SUCCESS;
            case 3:
                return CANCEL;
        }
        throw new UnmessageException("无法识别的预约方式。");
    }




}
