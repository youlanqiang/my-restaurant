package top.youlanqiang.orderproject.weixin.service;

import com.alibaba.fastjson.JSONArray;

public interface WeixinService {

    Integer createOrderWithFoods(Integer tableId, Integer numberOfPeople, String name, String remarks, String foods);

    JSONArray getAllFoods(String shopName);

}
