package com.qjkji.orderproject.weixin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qjkji.orderproject.backmanager.service.FoodService;
import com.qjkji.orderproject.backmanager.service.ReservationService;
import com.qjkji.orderproject.backmanager.service.TableService;
import com.qjkji.orderproject.core.entity.*;
import com.qjkji.orderproject.core.entity.builder.OrderBuilder;
import com.qjkji.orderproject.core.entity.builder.OrderFoodBuilder;
import com.qjkji.orderproject.core.exception.UnmessageException;
import com.qjkji.orderproject.core.mapper.ClassificationMapper;
import com.qjkji.orderproject.core.mapper.OrderFoodMapper;
import com.qjkji.orderproject.core.mapper.OrderMapper;
import com.qjkji.orderproject.weixin.service.WeixinService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class WeixinServiceImpl implements WeixinService {

    @Resource
    ReservationService reservationService;

    @Resource
    OrderMapper orderMapper;

    @Resource
    TableService tableService;

    @Resource
    FoodService foodService;

    @Resource
    OrderFoodMapper orderFoodMapper;

    @Resource
    ClassificationMapper classificationMapper;


    @Override
    public JSONArray getAllFoods(String shopName) {
        JSONArray array = new JSONArray();
        QueryWrapper<Classification> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_name", shopName);
        wrapper.orderByDesc("id");
        List<Classification> classifications =  classificationMapper.selectList(wrapper);
        for(Classification classification : classifications){
            JSONObject object = new JSONObject();
            object.put("className", classification.getName());
            object.put("classId", classification.getId());
            object.put("foods", foodService.getFoodsByClassId(classification.getId()));
            array.add(object);
        }
        return array;
    }

    @Override
    public Integer createOrderWithFoods(Integer tableId, Integer numberOfPeople, String name, String remarks, String foods) {
        Order order = OrderBuilder.create().pushClientName(name)
                .pushNumberOfPeople(numberOfPeople).pushRemarks(remarks)
                .pushTableId(tableId).pushCreateTime(LocalDateTime.now()).finish();
        DiningTable table = tableService.getTableOfThrow(tableId);
        order.setShopName(table.getShopName());
        log.info("创建订单：" + order.toString());

        //判断餐桌在此时间点是否有预定
        if (reservationService.checkHaveReservation(tableId, LocalDateTime.now())) {
            throw new UnmessageException("当前餐桌已经存在预定用户。");
        }

        if (orderMapper.insert(order) == 1) {
            if (!tableService.tableUsed(order.getTableId(), order.getId())) {
                throw new UnmessageException("餐桌管理");
            }

            List<OrderFood> orderFoods = JSONArray.parseArray(foods, OrderFood.class);
            for (OrderFood orderFood : orderFoods) {
                log.info("orderId:{}, foodId:{}, amount:{}", order.getId(), orderFood.getFoodId(), orderFood.getAmount());
                addFoodForOrder(order.getId(), orderFood.getFoodId(), orderFood.getAmount());
            }
            return order.getId();
        }
        throw new UnmessageException("创建订单失败。");
    }

    public void addFoodForOrder(Integer orderId, Integer foodId, Integer amount) {
        Food food = foodService.getFoodOrThrow(foodId);
        Order order = getOrderOrThrow(orderId);
        checkOrderIsWorking(order);

        OrderFood orderFood = OrderFoodBuilder.create()
                .pushFood(food).pushOrderId(orderId)
                .pushAmount(amount).finish();
        if (orderFoodMapper.insert(orderFood) != 1) {
            throw new UnmessageException("添加食物失败。");
        }
    }

    public Order getOrderOrThrow(Integer id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new UnmessageException("没有发现这个订单！");
        }
        return order;
    }

    private void checkOrderIsWorking(Order order) {
        if (order == null) {
            throw new UnmessageException("没有发现这个订单！");
        }
        if (!order.getStatus()) {
            throw new UnmessageException("订单已被取消。");
        }
        if (order.getPaymentStatus()) {
            throw new UnmessageException("订单已被支付。");
        }

    }



}
