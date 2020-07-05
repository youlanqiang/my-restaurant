package top.youlanqiang.orderproject.core.entity.enums;

import lombok.Getter;
import lombok.Setter;

public enum EmployeeType{

    manager("店长", 0),
    chef("厨师", 1),
    waiter("服务员", 2);


    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Integer type;


    EmployeeType(String name, Integer type){
        this.name = name;
        this.type = type;
    }
}
