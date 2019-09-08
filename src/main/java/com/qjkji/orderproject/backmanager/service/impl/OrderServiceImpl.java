package com.qjkji.orderproject.backmanager.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qjkji.orderproject.backmanager.backkitchen.KitchenWebsocket;
import com.qjkji.orderproject.backmanager.service.FoodService;
import com.qjkji.orderproject.backmanager.service.OrderService;
import com.qjkji.orderproject.backmanager.service.ReservationService;
import com.qjkji.orderproject.backmanager.service.TableService;
import com.qjkji.orderproject.core.controller.UserController;
import com.qjkji.orderproject.core.entity.*;
import com.qjkji.orderproject.core.entity.builder.OrderBuilder;
import com.qjkji.orderproject.core.entity.enums.PaymentMethod;
import com.qjkji.orderproject.core.entity.builder.OrderFoodBuilder;
import com.qjkji.orderproject.core.exception.UnmessageException;
import com.qjkji.orderproject.core.mapper.OrderFoodMapper;
import com.qjkji.orderproject.core.mapper.OrderMapper;
import com.qjkji.orderproject.core.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderFoodMapper orderFoodMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    FoodService foodService;

    @Resource
    TableService tableService;

    @Resource
    ReservationService reservationService;

    @Resource
    UserController userController;


    @Override
    public List<Order> getUsedOrder() {

        List<DiningTable> tables = tableService.listForUsed();

        List<Order> orders = new ArrayList<>();

        for(DiningTable table : tables){
            Order order = getOrderOrThrow(table.getOrderId());
            order.setFoods(orderFoodMapper.selectOrderFoodListByOrderId(order.getId()));
            order.setDiningTable(table);
            orders.add(order);
        }
        return orders;
    }

    @Override
    public List<Order> getNotSuccessOrderWithFoods() {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("status", true)
                .eq("payment_status", false)
                .eq("shop_name", userController.getShopName());
        List<Order> orders = orderMapper.selectList(wrapper);
        for(Order order : orders){
            order.setFoods(orderFoodMapper.selectOrderFoodListByOrderId(order.getId()));
            order.setDiningTable(tableService.getTableOfThrow(order.getTableId()));
        }
        return orders;
    }

    @Override
    public List<Order> getNotPayOrder() {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("status", true).eq("payment_status", false);
        return orderMapper.selectList(wrapper);
    }

    @Override
    public IPage<Order> pageOrder(Integer current, Integer size,
                                  LocalDateTime startTime, LocalDateTime endTime,
                                  String clientName, Integer tableId) {
        log.info("clientName:{} tableId:{} sTime:{}, eTime:{}", clientName, tableId, startTime, endTime);
        IPage<Order> page = new Page<>(current, size);

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        if (clientName != null && !clientName.trim().equals("")) {
            wrapper.like("client_name", clientName);
        }
        if (tableId != 0) {
            wrapper.eq("table_id", tableId);
        }
        if (startTime != null) {
            wrapper.ge("start_date", startTime);
        }
        if (endTime != null) {
            wrapper.le("end_date", endTime);
        }
        wrapper.eq("shop_name", userController.getShopName());

        wrapper.orderByDesc("id");

//        log.info("查询条件 name:{} table_id:{} start_date:{} end_date:{}",
//                clientName, tableId, startTime, endTime);
        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional
    public boolean createOrder(Integer tableId,
                               Integer numberOfPeople,
                               String name, String remarks) {
        Order order = OrderBuilder.create().pushClientName(name)
                .pushNumberOfPeople(numberOfPeople).pushRemarks(remarks)
                .pushTableId(tableId).pushCreateTime(LocalDateTime.now()).finish();
        order.setShopName(userController.getShopName());
        log.info("创建订单：" + order.toString());

        //判断餐桌在此时间点是否有预定
        if (reservationService.checkHaveReservation(tableId, LocalDateTime.now())) {
            throw new UnmessageException("当前餐桌已经存在预定用户。");
        }

        if (orderMapper.insert(order) == 1) {
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
            return tableService.tableUsed(order.getTableId(), order.getId());
        }

        return false;
    }

    @Override
    @Transactional
    public boolean createOrderWithFoods(Integer tableId, Integer numberOfPeople, String name, String remarks, String foods) {
        Order order = OrderBuilder.create().pushClientName(name)
                .pushNumberOfPeople(numberOfPeople).pushRemarks(remarks)
                .pushTableId(tableId).pushCreateTime(LocalDateTime.now()).finish();
        order.setShopName(userController.getShopName());
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
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateOrder(Integer id,
                               Integer tableId,
                               Integer numberOfPeople,
                               String clientName,
                               String remarks) {

        //开放当前桌子
        Order order = getOrderOrThrow(id);
        if(order.getTableId().equals(tableId)) {
            if (!tableService.tableUnused(order.getTableId(), id)) {
                throw new UnmessageException("换桌子失败");
            }
            if (!tableService.tableUsed(tableId, id)) {
                throw new UnmessageException("换桌子失败");
            }
        }
        boolean result =  orderMapper.updateOrder(id, tableId, numberOfPeople, clientName, remarks);
        if(result){
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }
        return result;
    }

    @Override
    public Order getOrderById(Integer id) {
        Order order = getOrderOrThrow(id);
        order.setFoods(orderFoodMapper.selectOrderFoodListByOrderId(id));

        return order;
    }

    @Override
    @Transactional
    public boolean cancelOrder(Integer id) {
        //取消订单 如果订单已经支付不能取消
        Order order = getOrderOrThrow(id);
        if (!order.getStatus()) {
            throw new UnmessageException("订单已取消!");
        }
        if (order.getPaymentStatus()) {
            throw new UnmessageException("订单已付款!");
        }
        //取消订单
        order.setStatus(false);
        order.setEndDate(LocalDateTime.now());

        if (!tableService.tableUnused(order.getTableId(), id)) {
            return false;
        }
        boolean result = orderMapper.updateById(order) == 1;
        if(result){
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }
        return result;
    }

    @Override
    @Transactional
    public boolean successOrder(Integer orderId, BigDecimal price, PaymentMethod paymentMethod) {
        //订单完成
        Order order = getOrderOrThrow(orderId);
        if (!order.getStatus()) {
            throw new UnmessageException("订单已取消!");
        }
        if (order.getPaymentStatus()) {
            throw new UnmessageException("订单已付款!");
        }

        BigDecimal actualAmount = BigDecimal.ZERO;
        List<OrderFood> foods = orderFoodMapper.selectOrderFoodListByOrderId(orderId);
        if (foods != null) {
            for (OrderFood food : foods) {
                //已经退菜
                if (food.getReturnStatus()) {
                    continue;
                }
                actualAmount = actualAmount.add(food.getRealSinglePrice()
                        .multiply(BigDecimal.valueOf(food.getAmount())));
            }
        }
        order.setActualAmount(actualAmount);

        order.setPaymentPrice(price);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(true);
        order.setEndDate(LocalDateTime.now());

//        if (!tableService.tableUnused(order.getTableId(), orderId)) {
//            return false;
//        }

        boolean result = orderMapper.updateById(order) == 1;
        if(result){
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }
        return result;
    }

    @Override
    public boolean addFoodForOrder(Integer orderId, Integer foodId) {
        Food food = foodService.getFoodOrThrow(foodId);
        Order order = getOrderOrThrow(orderId);
        checkOrderIsWorking(order);

        OrderFood orderFood = OrderFoodBuilder.create()
                .pushFood(food).pushOrderId(orderId)
                .pushAmount(1).finish();


        boolean result = orderFoodMapper.insert(orderFood) == 1;
        if(result){
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }
        return result;
    }


    public void addFoodForOrder(Integer orderId, Integer foodId, Integer amount) {
        Food food = foodService.getFoodOrThrow(foodId);
        Order order = getOrderOrThrow(orderId);
        checkOrderIsWorking(order);

        OrderFood orderFood = OrderFoodBuilder.create()
                .pushFood(food).pushOrderId(orderId)
                .pushAmount(amount).finish();

        boolean result = orderFoodMapper.insert(orderFood) == 1;
        if (result) {
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }else{
            throw new UnmessageException("添加食物失败。");
        }
    }


    @Override
    public boolean deleteFoodForOrder(Integer orderFoodId) {
        OrderFood orderFood = getOrderFoodOrThrow(orderFoodId);
        Order order = getOrderOrThrow(orderFood.getOrderId());
        checkOrderIsWorking(order);
        KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        boolean result = orderFoodMapper.deleteById(orderFoodId) == 1;
        if(result){
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }
        return result;
    }

    @Override
    public boolean modifyFoodAmountForOrder(Integer orderFoodId, Integer amount) {
        OrderFood orderFood = getOrderFoodOrThrow(orderFoodId);
        Order order = getOrderOrThrow(orderFood.getOrderId());
        checkOrderIsWorking(order);
        if (orderFood.getReturnStatus()) {
            throw new UnmessageException("该订单菜品已退菜！");
        }
        orderFood.setAmount(amount);

        boolean result = orderFoodMapper.updateById(orderFood) == 1;
        if(result){
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }
        return result;
    }

    @Override
    public boolean completeFoodForOrder(Integer orderFoodId) {
        OrderFood orderFood = getOrderFoodOrThrow(orderFoodId);
        Order order = getOrderOrThrow(orderFood.getOrderId());
        checkOrderIsWorking(order);
        if (orderFood.getReturnStatus()) {
            throw new UnmessageException("该订单菜品已退菜！");
        }
        if (orderFood.getStatus()) {
            throw new UnmessageException("该订单菜品已上齐");
        }
        orderFood.setStatus(true);
        boolean result = orderFoodMapper.updateById(orderFood) == 1;
        if(result){
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }
        return result;
    }

    @Override
    public boolean returnFoodForOrder(Integer orderFoodId, String msg) {
        OrderFood orderFood = getOrderFoodOrThrow(orderFoodId);
        Order order = getOrderOrThrow(orderFood.getOrderId());
        checkOrderIsWorking(order);
        if (orderFood.getReturnStatus()) {
            throw new UnmessageException("该订单菜品已退菜！");
        }
        if (orderFood.getStatus()) {
            throw new UnmessageException("该订单菜品已上齐");
        }
        orderFood.setReturnStatus(true);
        orderFood.setReturnMessage(msg);
        boolean result =  orderFoodMapper.updateById(orderFood) == 1;
        if(result){
            KitchenWebsocket.sendMessage(order.getShopName(), Result.flush());
        }
        return result;
    }


    public Order getOrderOrThrow(Integer id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new UnmessageException("没有发现这个订单！");
        }
        order.setActualAmount(getActualPrice(id));
        return order;
    }

    public BigDecimal getActualPrice(Integer orderId) {
        BigDecimal actualAmount = BigDecimal.ZERO;
        List<OrderFood> foods = orderFoodMapper.selectOrderFoodListByOrderId(orderId);
        if (foods != null) {
            for (OrderFood food : foods) {
                //已经退菜
                if (food.getReturnStatus()) {
                    continue;
                }
                actualAmount = actualAmount.add(food.getRealSinglePrice()
                        .multiply(BigDecimal.valueOf(food.getAmount())));
            }
        }
        return actualAmount;
    }

    @Override
    public void urgeOrder(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        DiningTable table = tableService.getTableOfThrow(order.getTableId());
        String message = table.getName() + "催菜。";
        KitchenWebsocket.sendMessage(order.getShopName(), Result.wsMsg(message));
    }

    private OrderFood getOrderFoodOrThrow(Integer orderFoodId) {
        OrderFood orderFood = orderFoodMapper.selectById(orderFoodId);
        if (orderFood == null) {
            throw new UnmessageException("没有发现订单菜品！");
        }
        return orderFood;
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
