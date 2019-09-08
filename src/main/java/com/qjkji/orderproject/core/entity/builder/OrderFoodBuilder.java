package com.qjkji.orderproject.core.entity.builder;

import com.qjkji.orderproject.core.entity.Food;
import com.qjkji.orderproject.core.entity.OrderFood;

public class OrderFoodBuilder {

    public static OrderFoodBuilder create(){
        return  new OrderFoodBuilder();
    }

    private OrderFood orderFood;

    private OrderFoodBuilder(){
        this.orderFood = new OrderFood();
    }

    public OrderFoodBuilder pushFood(Food food){
        this.orderFood.setDiscount(food.getDiscount());
        this.orderFood.setFoodId(food.getId());
        this.orderFood.setFoodName(food.getFoodName());
        this.orderFood.setSinglePrice(food.getPrice());
        return this;
    }

    public OrderFoodBuilder pushAmount(Integer amount){
        this.orderFood.setAmount(amount);
        return this;
    }

    public OrderFoodBuilder pushOrderId(Integer orderId){
        this.orderFood.setOrderId(orderId);
        return this;
    }

    public OrderFood finish(){
        //默认未退回
        this.orderFood.setReturnStatus(false);
        //默认未上齐
        this.orderFood.setStatus(false);
        return this.orderFood;
    }

}
