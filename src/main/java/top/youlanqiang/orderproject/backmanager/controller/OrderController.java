package top.youlanqiang.orderproject.backmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.youlanqiang.orderproject.backmanager.service.OrderService;
import top.youlanqiang.orderproject.core.entity.Order;
import top.youlanqiang.orderproject.core.entity.enums.PaymentMethod;
import top.youlanqiang.orderproject.core.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Api(tags = "订单管理")
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService orderService;

    @ApiOperation("获取所有正在使用的订单")
    @GetMapping("/used-order")
    public Result getUsedOrder(){
        List<Order> list = orderService.getUsedOrder();
        return Result.success("成功。", list);
    }

    @ApiOperation("获取所有未完成的订单以及食物")
    @GetMapping("/not-success-order")
    public Result getNotSuccessOrderWithFoods(){
        List<Order> list = orderService.getNotSuccessOrderWithFoods();
        return Result.success("成功。", list);
    }

    @ApiOperation("获取所有未支付的订单")
    @GetMapping("/not-pay-order")
    public Result getNotPayOrder(){
        List<Order> list = orderService.getNotPayOrder();
        return Result.success("成功。", list);
    }

    @ApiOperation("订单分页")
    @GetMapping("/page/{current}/{size}")
    public Result pageOrder(@ApiParam(value = "页数", example = "1") @PathVariable Integer current,
                            @ApiParam(value = "项数", example = "10") @PathVariable Integer size,
                            @ApiParam(value = "开始时间",example = "2019-08-01 00:00:00")
                                @RequestParam(value = "startTime", required = false)
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME ) LocalDateTime startTime,
                            @ApiParam(value = "结束时间", example = "2020-08-01 00:00:00")
                                        @RequestParam(value = "endTime", required = false)
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME ) LocalDateTime endTime,
                            @ApiParam(value = "姓名", example = "")
                                        @RequestParam(value = "clientName", defaultValue = "") String clientName,
                            @ApiParam(value = "桌号", example = "0")
                                        @RequestParam(value = "tableId", defaultValue = "0") Integer tableId){

        IPage<Order> page = orderService.pageOrder(current, size, startTime, endTime, clientName, tableId);
        if(page != null){
            return Result.success("返回成功！", page);
        }
        return Result.error("返回失败。");
    }

    @ApiOperation("创建订单和菜品一起")
    @PostMapping("/create-with-foods")
    public Result createOrderWithFoods(@ApiParam(value = "桌号", example = "1") @RequestParam("tableId") Integer tableId,
                                       @ApiParam(value = "人数", example = "6") @RequestParam("amount") Integer numberOfPeople,
                                       @ApiParam(value = "姓名", example = "客户") @RequestParam("name") String name,
                                       @ApiParam(value = "备注", example = "不要放辣") @RequestParam("remarks") String remarks,
                                       @ApiParam(value = "foods") @RequestParam("foods") String foods) {
        if (orderService.createOrderWithFoods(tableId, numberOfPeople, name, remarks, foods)) {
            return Result.success("订单创建成功。");
        }
        return Result.error("订单创建失败！");
    }

    @ApiOperation("创建订单")
    @PostMapping("/create")
    public Result createOrder(@ApiParam(value = "桌号", example = "1") @RequestParam("tableId") Integer tableId,
                              @ApiParam(value = "人数", example = "6") @RequestParam("amount") Integer numberOfPeople,
                              @ApiParam(value = "姓名", example = "客户") @RequestParam("name") String name,
                              @ApiParam(value = "备注", example = "不要放辣") @RequestParam("remarks") String remarks) {
        if (orderService.createOrder(tableId, numberOfPeople, name, remarks)) {
            return Result.success("订单创建成功。");
        }
        return Result.error("订单创建失败！");
    }

    @ApiOperation("催促订单")
    @PostMapping("/urge-order/{id}")
    public Result urgeOrder(@ApiParam("订单号") @PathVariable Integer id){
        //todo 催菜接口， 一般催菜 在流程中毫无意义所以 这里设置为假接口 消息提醒
        orderService.urgeOrder(id);
        return Result.success("催单成功。");
    }

    @ApiOperation("更新订单")
    @PostMapping("/update/{id}")
    public Result updateOrder(
            @ApiParam(value = "订单号", example = "1") @PathVariable("id") Integer orderId,
            @ApiParam(value = "桌号", example = "1") @RequestParam("tableId") Integer tableId,
            @ApiParam(value = "人数", example = "6") @RequestParam("amount") Integer numberOfPeople,
            @ApiParam(value = "姓名", example = "客户") @RequestParam("name") String name,
            @ApiParam(value = "备注", example = "不要放辣") @RequestParam("remarks") String remarks) {
        if (orderService.updateOrder(orderId, tableId, numberOfPeople, name, remarks)) {
            return Result.success("订单更新成功。");
        }
        return Result.error("订单更新失败。");
    }

    @ApiOperation("获取订单的带付价格")
    @GetMapping("/get-actual-price/{id}")
    public Result getOrderActualAmount(@ApiParam(value = "订单ID", example = "1")
                                           @PathVariable Integer id){
        BigDecimal decimal = orderService.getActualPrice(id);
        return Result.success("成功。", decimal);
    }

    @ApiOperation("获取订单")
    @GetMapping("/get/{id}")
    public Result getOrder(@ApiParam(value = "订单ID", example = "1")
                           @PathVariable Integer id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return Result.error("没有相关数据");
        }
        return Result.success("成功", order);
    }

    @ApiOperation("取消订单")
    @DeleteMapping("/cancel/{id}")
    public Result cancelOrder(@ApiParam("订单ID")
                              @PathVariable Integer id) {
        if (orderService.cancelOrder(id)) {
            return Result.success("取消成功！");
        }
        return Result.error("取消失败！");
    }

    @ApiOperation("订单完成")
    @PostMapping("/success/{id}")
    public Result successOrder(@ApiParam("订单ID") @PathVariable Integer id,
                               @ApiParam(value = "付款方式 1:支付宝，2:微信，3:现金", example = "1")
                               @RequestParam("type") Integer type,
                               @ApiParam(value = "支付金额", example = "100.55") @RequestParam("price") Double price) {
        if (orderService.successOrder(id, BigDecimal.valueOf(price),
                PaymentMethod.valueOf(type))) {
            return Result.success("订单完成。");
        }
        return Result.error("订单完成失败！");
    }

    @ApiOperation("加菜")
    @PostMapping("/add-food/{orderId}/{foodId}")
    public Result addFoodForOrder(@ApiParam(value = "订单ID", example = "1") @PathVariable Integer orderId,
                                  @ApiParam(value = "菜品ID", example = "1") @PathVariable Integer foodId) {
        if (orderService.addFoodForOrder(orderId, foodId)) {
            return Result.success("加菜成功！");
        }
        return Result.error("加菜失败！");
    }

    @ApiOperation("删菜")
    @DeleteMapping("/delete-food/{orderFoodId}")
    public Result deleteFoodForOrder(@ApiParam(value = "订单菜品ID", example = "1")
                                     @PathVariable Integer orderFoodId) {
        if (orderService.deleteFoodForOrder(orderFoodId)) {
            return Result.success("删菜成功！");
        }
        return Result.error("删菜失败！");
    }

    @ApiOperation("修改菜品的数量")
    @PostMapping("/modify-food/{orderFoodId}/{amount}")
    public Result modifyFoodAmountForOrder(
            @ApiParam("订单菜品ID") @PathVariable Integer orderFoodId,
            @ApiParam("数量") @PathVariable Integer amount) {
        if (orderService.modifyFoodAmountForOrder(orderFoodId, amount)) {
            return Result.success("修改数量成功。");
        }
        return Result.success("修改数量失败。");
    }

    @ApiOperation("菜品上齐")
    @PostMapping("/complete-food/{orderFoodId}")
    public Result completeFoodForOrder(
            @ApiParam("订单菜品ID") @PathVariable Integer orderFoodId) {
        if (orderService.completeFoodForOrder(orderFoodId)) {
            return Result.success("订单菜品完成。");
        }
        return Result.error("操作失败！");
    }

    @ApiOperation("退菜")
    @PostMapping("/return-food/{orderFoodId}")
    public Result returnFoodForOrder(
            @ApiParam("订单菜品ID") @PathVariable Integer orderFoodId,
            @ApiParam(value = "退菜原因", example = "鱼香肉丝没有鱼！") @RequestParam("msg") String msg) {
        if(orderService.returnFoodForOrder(orderFoodId, msg)){
            return Result.success("退菜成功。");
        }
        return Result.error("退菜失败！");
    }

}
