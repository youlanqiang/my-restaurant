package com.qjkji.orderproject.backmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qjkji.orderproject.backmanager.service.ReservationService;
import com.qjkji.orderproject.core.entity.DiningTable;
import com.qjkji.orderproject.core.entity.ReservationInfo;
import com.qjkji.orderproject.core.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@Api(tags = "预约管理")
public class ReservationController {


    @Resource
    ReservationService reservationService;


    @ApiOperation("预约分页")
    @GetMapping("/page/{current}/{size}")
    public Result page(@ApiParam(value = "页数", example = "1") @PathVariable Integer current,
                       @ApiParam(value = "项数", example = "10") @PathVariable Integer size) {
        IPage<ReservationInfo> infoIPage = reservationService.getPage(current, size);
        return Result.success("成功。", infoIPage);
    }

    @ApiOperation("预约完成")
    @PostMapping("/success/{id}")
    public Result success(@ApiParam("预约单ID") @PathVariable
                                  Integer id) {
        if(reservationService.successInfo(id)){
            return Result.success("执行成功。");
        }
        return Result.error("执行失败。");
    }



    @ApiOperation("删除预约")
    @DeleteMapping("/delete/{id}")
    public Result delete(@ApiParam("预约单id") @PathVariable Integer id) {
        if(reservationService.deleteInfo(id)){
            return Result.success("执行成功。");
        }
        return Result.error("执行失败。");
    }


    @ApiOperation("取消预约")
    @DeleteMapping("/cancel/{id}")
    public Result cancel(@ApiParam("预约单id") @PathVariable Integer id) {

        if(reservationService.cancelInfo(id)){
            return Result.success("执行成功。");
        }
        return Result.error("执行失败。");
    }

    @ApiOperation("创建预约")
    @PostMapping("/create")
    public Result create(@ApiParam(value = "客户姓名", example = "琪琪国王") @RequestParam("clientName") String clientName,
                         @ApiParam(value = "电话号码", example = "15758130392") @RequestParam("clientTel") String clientTel,
                         @ApiParam(value = "预约桌号", example = "3") @RequestParam("tableId") Integer tableId,
                         @ApiParam(value = "预约人数", example = "7") @RequestParam("numberOfPeople") Integer numberOfPeople,
                         @ApiParam(value = "开始时间", example = "2019-08-11 14:30:00")
                         @RequestParam(value = "startTime")
                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                         @ApiParam(value = "结束时间", example = "2019-08-11 15:30:00")
                         @RequestParam(value = "endTime")
                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        if(reservationService.createInfo(clientName, clientTel, tableId, numberOfPeople, startTime, endTime)){
            return Result.success("预约成功。");
        }
        return Result.error("预约失败。");
    }

    @ApiOperation("查询预约")
    @GetMapping("/search")
    public Result searchByTel(@ApiParam(value = "预约电话", example = "15758130392")
                              @RequestParam("tel") String tel) {
        List<ReservationInfo> list = reservationService.searchByTel(tel);
        if(list.isEmpty()){
            return Result.error("没有发现对应手机号的预约。");
        }
        return Result.success("成功.", list);
    }

    @ApiOperation("推荐预约桌号")
    @GetMapping("/get-recommend")
    public Result getRecommend(@ApiParam(value = "预约人数", example = "6") @RequestParam("number") Integer number,
                               @ApiParam(value = "开始时间", example = "2019-08-11 14:30:00")
                               @RequestParam(value = "startTime")
                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                               @ApiParam(value = "结束时间", example = "2019-08-11 15:30:00")
                               @RequestParam(value = "endTime")
                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<DiningTable> diningTables = reservationService.getRecommend(number, startTime, endTime);
        if (diningTables.isEmpty()) {
            return Result.error("没有推荐的餐桌。");
        }
        return Result.success("成功。", diningTables);
    }


}
