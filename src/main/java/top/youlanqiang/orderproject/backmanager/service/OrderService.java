package top.youlanqiang.orderproject.backmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.youlanqiang.orderproject.core.entity.Order;
import top.youlanqiang.orderproject.core.entity.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    List<Order> getUsedOrder();

    List<Order> getNotSuccessOrderWithFoods();

    List<Order> getNotPayOrder();

    IPage<Order> pageOrder(Integer current, Integer size,
                           LocalDateTime startTime,
                           LocalDateTime endTime,
                           String clientName,
                           Integer tableId);


    boolean createOrder(Integer tableId,
                        Integer numberOfPeople,
                        String name, String remarks);

    boolean createOrderWithFoods(Integer tableId,
                                 Integer numberOfPeople,
                                 String name, String remarks, String foods);


    boolean updateOrder(Integer id, Integer tableId,
                        Integer numberOfPeople,
                        String name, String remarks);


    Order getOrderById(Integer id);


    boolean cancelOrder(Integer id);


    boolean successOrder(Integer orderId, BigDecimal price, PaymentMethod paymentMethod);

    boolean addFoodForOrder(Integer orderId, Integer foodId);

    boolean deleteFoodForOrder(Integer orderFoodId);

    boolean modifyFoodAmountForOrder(Integer orderFoodId, Integer amount);

    boolean completeFoodForOrder(Integer orderFoodId);

    boolean returnFoodForOrder(Integer orderFoodId, String msg);

    Order getOrderOrThrow(Integer id);

    BigDecimal getActualPrice(Integer orderId);

    void urgeOrder(Integer orderId);

}



