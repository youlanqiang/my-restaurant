package top.youlanqiang.orderproject.backmanager.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface StatisticsService {

    /**
     * 日统计客户
     * @param year
     * @param month
     * @param day
     * @return
     */
    JSONObject clientStatisticsForDay(Integer year, Integer month, Integer day);

    JSONObject clientStatisticsForMonth(Integer year, Integer month);

    JSONObject clientStatisticsForYear(Integer year);

    JSONObject incomeStatisticsForDay(Integer year, Integer month, Integer day);

    JSONArray incomeStatisticsForMonth(Integer year, Integer month);

    JSONObject incomeStatisticsForYear(Integer year);

    JSONObject bestSales(Integer year, Integer month);

    JSONObject worstSales(Integer year, Integer month);

}
