package top.youlanqiang.orderproject.weixin.controller;

import com.alibaba.fastjson.JSONArray;
import top.youlanqiang.orderproject.backmanager.service.OrderService;
import top.youlanqiang.orderproject.core.entity.Result;
import top.youlanqiang.orderproject.weixin.service.WeixinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@RequestMapping("/weixin")
@Controller
@Slf4j
@Api(tags = "微信端api")
public class WeixinController {


    @Resource
    OrderService orderService;

    @Resource
    WeixinService weixinService;


    @PostMapping("/add-food/{orderId}/{foodId}")
    @ApiOperation("加菜接口")
    public Result addFoodForOrder(@ApiParam(value = "订单ID", example = "1") @PathVariable Integer orderId,
                                  @ApiParam(value = "菜品ID", example = "1") @PathVariable Integer foodId) {
        if (orderService.addFoodForOrder(orderId, foodId)) {
            return Result.success("加菜成功！");
        }
        return Result.error("加菜失败！");
    }

    @PostMapping("/all-food")
    @ApiOperation("获取餐单列表")
    public Result allFood(@ApiParam("餐厅名称") @RequestParam("shopName") String shopName){
        JSONArray array = weixinService.getAllFoods(shopName);
        return Result.success("成功。", array);
    }


    @PostMapping("/create-with-foods")
    @ApiOperation("创建订单和菜品一起")
    public Result createOrderWithFoods(@ApiParam(value = "桌号", example = "1") @RequestParam("tableId") Integer tableId,
                                       @ApiParam(value = "人数", example = "6") @RequestParam("amount") Integer numberOfPeople,
                                       @ApiParam(value = "姓名", example = "客户") @RequestParam("name") String name,
                                       @ApiParam(value = "备注", example = "不要放辣") @RequestParam("remarks") String remarks,
                                       @ApiParam(value = "foods") @RequestParam("foods") String foods) {

        Integer orderId = weixinService.createOrderWithFoods(tableId, numberOfPeople, name, remarks, foods);
        return Result.success("订单创建成功！", orderId);
    }





}
