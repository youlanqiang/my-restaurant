package com.qjkji.orderproject.core.entity.builder;

import com.qjkji.orderproject.core.entity.Order;
import com.qjkji.orderproject.core.exception.UnmessageException;

import java.time.LocalDateTime;

public class OrderBuilder {


    public static OrderBuilder create(){
        return new OrderBuilder();
    }

    private Order order;


    private OrderBuilder(){
        this.order = new Order();
    }


    public OrderBuilder pushTableId(Integer id){
        this.order.setTableId(id);
        return this;
    }

    public OrderBuilder pushCreateTime(LocalDateTime createTime){
        this.order.setStartDate(createTime);
        return this;
    }

    public OrderBuilder pushNumberOfPeople(Integer amount){
        if(amount <= 0){
            throw new UnmessageException("人数不能小于0");
        }else {
            this.order.setNumberOfPeople(amount);
        }
        return this;
    }

    public OrderBuilder pushClientName(String name){
        if(name != null){
            this.order.setClientName(name);
        }else{
            this.order.setClientName("顾客");
        }
        return this;
    }

    public OrderBuilder pushRemarks(String remarks){
        if(remarks != null) {
            this.order.setRemarks(remarks);
        }else{
            this.order.setRemarks("无备注");
        }
        return this;
    }

    public Order finish(){
        //订单未取消
        this.order.setStatus(true);
        //订单未付款
        this.order.setPaymentStatus(false);
        return this.order;
    }

}
