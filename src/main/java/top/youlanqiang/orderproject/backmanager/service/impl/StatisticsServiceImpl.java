package top.youlanqiang.orderproject.backmanager.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.youlanqiang.orderproject.backmanager.service.StatisticsService;
import top.youlanqiang.orderproject.core.controller.UserController;
import top.youlanqiang.orderproject.core.entity.OrderFood;
import top.youlanqiang.orderproject.core.entity.StatisticsEntity;
import top.youlanqiang.orderproject.core.entity.enums.PaymentMethod;
import top.youlanqiang.orderproject.core.mapper.StatisticsMapper;
import top.youlanqiang.orderproject.core.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;


@Service
public class StatisticsServiceImpl implements StatisticsService {


    @Resource
    StatisticsMapper statisticsMapper;

    @Resource
    UserController userController;

    @Override
    public JSONObject clientStatisticsForDay(Integer year,
                                             Integer month,
                                             Integer day) {
        LocalDate date = LocalDate.of(year, month, day);
        LocalDateTime dateTime = date.atStartOfDay();
        List<StatisticsEntity> list = new ArrayList<>();
        Integer result = statisticsMapper.selectNumberOfPeopleByDay(dateTime, userController.getShopName());
        Optional<Integer> optional = Optional.ofNullable(result);
        list.add(new StatisticsEntity(StringUtils.formatDate(date), optional.orElse(0)));



        return null;
    }

    @Override
    public JSONObject clientStatisticsForMonth(Integer year,
                                                           Integer month) {
        List<String> monthList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, 1);
        Month theMonth = date.getMonth();
        int maxLen = theMonth.minLength();
        for (int i = 1; i <= maxLen; i++) {
            LocalDate dayOfMonth = LocalDate.of(year, month, i);
            LocalDateTime dateTime = dayOfMonth.atStartOfDay();
            Integer result = statisticsMapper.selectNumberOfPeopleByDay(dateTime, userController.getShopName());
            Optional<Integer> optional = Optional.ofNullable(result);
            monthList.add(StringUtils.formatDate(dayOfMonth));
            valueList.add(optional.orElse(0));
        }

        JSONObject object = new JSONObject();
       // obj.xAxisData
        object.put("xAxisData", monthList);
        object.put("data", valueList);
        return object;
    }

    @Override
    public JSONObject clientStatisticsForYear(Integer year) {
        List<StatisticsEntity> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String yearStr = year + "-" + i;
            LocalDateTime time = LocalDateTime.of(year, i, 1, 0, 0, 0);
            Integer result = statisticsMapper.selectNumberOfPeopleByYearWithMonth(time, userController.getShopName());
            Optional<Integer> optional = Optional.ofNullable(result);

            list.add(new StatisticsEntity(yearStr, optional.orElse(0)));
        }

        return null;

    }


    @Override
    public JSONObject incomeStatisticsForDay(Integer year, Integer month, Integer day) {
        LocalDate date = LocalDate.of(year, month, day);
        LocalDateTime dateTime = date.atStartOfDay();

        //支付宝支付
        BigDecimal alipayPrice = ofZero(statisticsMapper.selectDecimalByDay(dateTime, PaymentMethod.ALIPAY.getType(), userController.getShopName()));
        StatisticsEntity alipay = new StatisticsEntity("支付宝", alipayPrice);

        //微信支付
        BigDecimal weixinPrice = ofZero(statisticsMapper.selectDecimalByDay(dateTime, PaymentMethod.WEIXIN.getType(), userController.getShopName()));
        StatisticsEntity weixin = new StatisticsEntity("微信", weixinPrice);

        //现金支付
        BigDecimal cashPrice = ofZero(statisticsMapper.selectDecimalByDay(dateTime, PaymentMethod.CASH.getType(), userController.getShopName()));
        StatisticsEntity cash = new StatisticsEntity("现金", cashPrice);


        JSONObject result = new JSONObject();
        result.put("legendData", Arrays.asList("支付宝", "微信", "现金"));
        result.put("data", Arrays.asList(alipay, weixin, cash));
        return result;
    }

//    source: [
//              ['product', '2015', '2016', '2017'],
//              ['Matcha Latte', 43.3, 85.8, 93.7],
//              ['Milk Tea', 83.1, 73.4, 55.1],
//              ['Cheese Cocoa', 86.4, 65.2, 82.5],
//              ['Walnut Brownie', 72.4, 53.9, 39.1]
//            ]
    @Override
    public JSONArray incomeStatisticsForMonth(Integer year, Integer month) {

        JSONArray object = new JSONArray();

        object.add( Arrays.asList("product","支付宝", "微信", "现金"));
        LocalDate date = LocalDate.of(year, month, 1);
        Month theMonth = date.getMonth();
        int maxLen = theMonth.maxLength();
        for (int i = 1; i <= maxLen; i++) {
            LocalDate dayOfMonth = LocalDate.of(year, month, i);
            LocalDateTime dateTime = dayOfMonth.atStartOfDay();

            //支付宝支付
            BigDecimal alipayPrice = ofZero(statisticsMapper.selectDecimalByDay(dateTime, PaymentMethod.ALIPAY.getType(), userController.getShopName()));

            //微信支付
            BigDecimal weixinPrice = ofZero(statisticsMapper.selectDecimalByDay(dateTime, PaymentMethod.WEIXIN.getType(), userController.getShopName()));

            //现金支付
            BigDecimal cashPrice = ofZero(statisticsMapper.selectDecimalByDay(dateTime, PaymentMethod.CASH.getType(), userController.getShopName()));

            object.add( Arrays.asList(i+"日", alipayPrice, weixinPrice, cashPrice));

        }

        return object;
    }


    @Override
    public JSONObject incomeStatisticsForYear(Integer year) {
        List<StatisticsEntity> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String yearStr = year + "-" + i;
            StatisticsEntity entity = new StatisticsEntity();
            entity.setName(yearStr);

            LocalDateTime time = LocalDateTime.of(year, i, 1, 0, 0, 0);

            //支付宝支付
            BigDecimal alipayPrice = ofZero(statisticsMapper.selectDecimalByYearWithMonth(time, PaymentMethod.ALIPAY.getType(), userController.getShopName()));
            StatisticsEntity alipay = new StatisticsEntity("支付宝", alipayPrice);

            //微信支付
            BigDecimal weixinPrice = ofZero(statisticsMapper.selectDecimalByYearWithMonth(time, PaymentMethod.WEIXIN.getType(), userController.getShopName()));
            StatisticsEntity weixin = new StatisticsEntity("微信", weixinPrice);

            //现金支付
            BigDecimal cashPrice = ofZero(statisticsMapper.selectDecimalByYearWithMonth(time, PaymentMethod.CASH.getType(), userController.getShopName()));
            StatisticsEntity cash = new StatisticsEntity("现金", cashPrice);

            //总费
            BigDecimal price = alipayPrice.add(weixinPrice).add(cashPrice);
            StatisticsEntity all = new StatisticsEntity("总记", price);
            entity.setValue(Arrays.asList(alipay, weixin, cash, all));
            list.add(entity);
        }
        return null;
    }

    public JSONObject bestSales(Integer year, Integer month) {
        LocalDateTime dateTime = LocalDateTime.of(year, month, 1, 0, 0, 0);
        List<OrderFood> orderFoods =  statisticsMapper.selectBestFoodByYearWithMonth(dateTime, userController.getShopName());

        List<String> foodNames = new ArrayList<>();
        List<Integer> foodCount = new ArrayList<>();
        for(OrderFood orderFood: orderFoods){
            foodNames.add(orderFood.getFoodName());
            foodCount.add(orderFood.getAmount());
        }

        JSONObject object = new JSONObject();
        object.put("yAxis", foodNames);
        object.put("data", foodCount);
        return object;
    }


    public JSONObject worstSales(Integer year, Integer month) {
        LocalDateTime dateTime = LocalDateTime.of(year, month, 1, 0, 0, 0);
        List<OrderFood> orderFoods =  statisticsMapper.selectWorstFoodByYearWithMonth(dateTime, userController.getShopName());

        List<String> foodNames = new ArrayList<>();
        List<Integer> foodCount = new ArrayList<>();
        for(OrderFood orderFood: orderFoods){
            foodNames.add(orderFood.getFoodName());
            foodCount.add(orderFood.getAmount());
        }

        JSONObject object = new JSONObject();
        object.put("yAxis", foodNames);
        object.put("data", foodCount);
        return object;
    }

    private BigDecimal ofZero(BigDecimal decimal) {
        if (decimal == null) {
            return BigDecimal.ZERO;
        }
        return decimal;
    }


}
