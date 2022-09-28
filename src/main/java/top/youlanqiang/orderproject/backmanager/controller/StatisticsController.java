package top.youlanqiang.orderproject.backmanager.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.youlanqiang.orderproject.backmanager.service.StatisticsService;
import top.youlanqiang.orderproject.core.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 年统计， 月统计， 日统计
 * 客流量统计 (餐桌接客统计，总人数统计[日统计显示今天一天的， 月统计显示这个月的，年统计显示12个月的])
 * 收入统计 (支付宝，微信，现金，全部)
 * 菜品统计 (销量最佳top5, 销量最差top5)
 */

@RestController
@RequestMapping("/statistics")
@Api(tags = "统计接口")
public class StatisticsController {

    @Resource
    StatisticsService statisticsService;

    @ApiOperation("日客流量统计")
    @GetMapping("/day-client")
    public Result clientStatisticsForToday(@ApiParam(value = "年份", example = "2019")
                                           @RequestParam("year") Integer year,
                                           @ApiParam(value = "月份", example = "8")
                                           @RequestParam("month") Integer month,
                                           @ApiParam(value = "日期", example = "13")
                                           @RequestParam("day") Integer day) {
//        List<StatisticsEntity> list = statisticsService.clientStatisticsForDay(year, month, day);
//
//        return Result.success("成功。", list);
        return Result.error("接口未开放");
    }

    @ApiOperation("月客流量统计")
    @GetMapping("/month-client")
    public Result clientStatisticsForMonth(@ApiParam(value = "年份", example = "2019")
                                           @RequestParam("year") Integer year,
                                           @ApiParam(value = "月份", example = "8")
                                           @RequestParam("month") Integer month) {
        JSONObject object = statisticsService.clientStatisticsForMonth(year, month);
        return Result.success("成功。", object);
        //return Result.error("接口未开放");
    }

    @ApiOperation("年客流量统计")
    @GetMapping("/year-client")
    public Result clientStatisticsForYear(@ApiParam(value = "年份", example = "2019")
                                          @RequestParam("year") Integer year) {

//        List<StatisticsEntity>  list = statisticsService.clientStatisticsForYear(year);
//
//        return Result.success("成功。", list);

        return Result.error("接口未开放");
    }


    @ApiOperation("日收入统计")
    @GetMapping("/day-income")
    public Result incomeStatisticsForDay(@ApiParam(value = "年份", example = "2019")
                                             @RequestParam("year") Integer year,
                                         @ApiParam(value = "月份", example = "8")
                                             @RequestParam("month") Integer month,
                                         @ApiParam(value = "日期", example = "13")
                                             @RequestParam("day") Integer day){
       JSONObject object = statisticsService.incomeStatisticsForDay(year, month, day);
       return Result.success("成功。", object);
    }

    @ApiOperation("月收入统计")
    @GetMapping("/month-income")
    public Result incomeStatisticsForMonth(@ApiParam(value = "年份", example = "2019")
                                               @RequestParam("year") Integer year,
                                           @ApiParam(value = "月份", example = "8")
                                               @RequestParam("month") Integer month){
        JSONArray object = statisticsService.incomeStatisticsForMonth(year, month);
        return Result.success("成功。", object);

        //return Result.error("接口未开放");
    }

    @ApiOperation("年收入统计")
    @GetMapping("/year-income")
    public Result incomeStatisticsForYear(@ApiParam(value = "年份", example = "2019")
                                              @RequestParam("year") Integer year){
//        List<StatisticsEntity> list = statisticsService.incomeStatisticsForYear(year);
//        return Result.success("成功。", list);

        return Result.error("接口未开放");
    }

    @ApiOperation("当月销量最佳top5")
    @GetMapping("/best-sales")
    public Result bestSales(@ApiParam(value = "年份", example = "2019")
                                @RequestParam("year") Integer year,
                            @ApiParam(value = "月份", example = "8")
                                @RequestParam("month") Integer month){
        JSONObject object = statisticsService.bestSales(year, month);

        return Result.success("成功。", object);
        //return Result.error("接口未开放");
    }

    @ApiOperation("当月销量最差top5")
    @GetMapping("/worst-sales")
    public Result worstSales(@ApiParam(value = "年份", example = "2019")
                                 @RequestParam("year") Integer year,
                             @ApiParam(value = "月份", example = "8")
                                 @RequestParam("month") Integer month){
        JSONObject object = statisticsService.worstSales(year, month);

        return Result.success("成功。", object);
       //return Result.error("接口未开放");
    }
}
