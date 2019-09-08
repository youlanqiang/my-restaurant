package com.qjkji.orderproject.backmanager.controller;


import com.qjkji.orderproject.backmanager.service.ReservationService;
import com.qjkji.orderproject.backmanager.service.TableService;
import com.qjkji.orderproject.core.entity.DiningTable;
import com.qjkji.orderproject.core.entity.ReservationInfo;
import com.qjkji.orderproject.core.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "餐桌管理")
@Slf4j
@RequestMapping("/table")
public class TableController {

    @Resource
    TableService tableService;

    @Resource
    ReservationService reservationService;

    @PostMapping("/release/{tableId}")
    @ApiOperation("释放餐桌")
    public Result releaseTable(@ApiParam("桌号") @PathVariable("tableId") Integer tableId){
        if(tableService.tableUnused(tableId)){

            return Result.success("执行成功.");
        }
        return Result.error("执行失败.");
    }

    @GetMapping("/list-for-show")
    @ApiOperation("获取所有可显示的餐桌")
    public Result listForShow(){
        List<DiningTable> list = tableService.listForShow();
        return Result.success("成功。", list);
    }

    @GetMapping("/list-info")
    @ApiOperation("餐桌详细情况")
    public Result listInfo(){
        List<DiningTable> list = tableService.listInfo();
        return Result.success("成功。", list);
    }

    @GetMapping("/list")
    @ApiOperation("获取所有餐桌")
    public Result list(){
        List<DiningTable> list = tableService.list();
        return Result.success("成功。", list);
    }

    @PostMapping("/create")
    @ApiOperation("创建餐桌")
    public Result createTable(@ApiParam(value = "桌号", example = "1号桌") @RequestParam("tableName") String tableName,
                              @ApiParam(value = "可容纳人数", example = "6") @RequestParam("capacity") Integer capacity,
                              @ApiParam(value = "是否显示", example = "true") @RequestParam("show") Boolean open){
        if(tableService.createTable(tableName, capacity, open)){
            return Result.success("创建成功。");
        }
        return Result.error("创建失败。");
    }

    @PostMapping("/update/{id}")
    @ApiOperation("更新餐桌")
    public Result updateTable(@ApiParam(value = "餐桌id", example = "1")
                                  @PathVariable Integer id,
                              @ApiParam(value = "桌号", example = "1号桌")
                                @RequestParam("tableName") String tableName,
                              @ApiParam(value = "可容纳人数", example = "6")
                                  @RequestParam("capacity") Integer capacity,
                              @ApiParam(value = "是否显示", example = "true")
                                  @RequestParam("open") Boolean open){
        if(tableService.updateTable(id, tableName, capacity, open)){
            return Result.success("更新成功。");
        }
        return Result.error("更新失败。");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除餐桌")
    public Result deleteTable(@ApiParam(value = "餐桌ID") @PathVariable Integer id){
        if(tableService.deleteTable(id)){
            return Result.success("删除成功。");
        }
        return Result.error("删除失败。");
    }

    @GetMapping("/reservation-info/{id}")
    @ApiOperation("查看餐桌预定情况")
    public Result infoTable(@ApiParam(value = "餐桌ID") @PathVariable Integer id){

        List<ReservationInfo> list = reservationService.getReservationByTableId(id);

        return Result.success("完成。", list);
    }


}
