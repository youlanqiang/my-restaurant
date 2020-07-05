package top.youlanqiang.orderproject.backmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import top.youlanqiang.orderproject.backmanager.backkitchen.KitchenWebsocket;
import top.youlanqiang.orderproject.backmanager.service.ReservationService;
import top.youlanqiang.orderproject.backmanager.service.TableService;
import top.youlanqiang.orderproject.core.controller.UserController;
import top.youlanqiang.orderproject.core.entity.DiningTable;
import top.youlanqiang.orderproject.core.entity.Result;
import top.youlanqiang.orderproject.core.entity.builder.TableBuilder;
import top.youlanqiang.orderproject.core.exception.UnmessageException;
import top.youlanqiang.orderproject.core.mapper.DiningTableMapper;
import top.youlanqiang.orderproject.core.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class TableServiceImpl implements TableService {

    @Resource
    DiningTableMapper diningTableMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    ReservationService reservationService;

    @Resource
    UserController userController;

    @Override
    public List<DiningTable> listInfo() {
        List<DiningTable> tables = listForShow();
        for(DiningTable table : tables){
            table.setOrder(orderMapper.selectById(table.getOrderId()));
            table.setReservationInfoList(reservationService.getReservationByTableId(table.getId()));
        }
        return tables;
    }

    @Override
    public List<DiningTable> listForUsed() {
        QueryWrapper<DiningTable> wrapper = new QueryWrapper<>();
        wrapper.eq("used", true);
        wrapper.eq("shop_name", userController.getShopName());
        return diningTableMapper.selectList(wrapper);
    }

    @Override
    public List<DiningTable> listForShow() {
        QueryWrapper<DiningTable> wrapper = new QueryWrapper<>();
        wrapper.eq("open", true);
        wrapper.eq("shop_name", userController.getShopName());
        return diningTableMapper.selectList(wrapper);
    }

    @Override
    public List<DiningTable> list() {
        QueryWrapper<DiningTable> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_name", userController.getShopName());
        return diningTableMapper.selectList(wrapper);
    }

    @Override
    public Boolean createTable(String tableName, Integer capacity, Boolean open) {
        DiningTable table = TableBuilder.create().pushTableName(tableName)
                .pushCapacity(capacity).pushOpen(open).finish();
        table.setShopName(userController.getShopName());
        return diningTableMapper.insert(table) == 1;
    }

    @Override
    public Boolean updateTable(Integer id, String tableName, Integer capacity, Boolean open) {
        DiningTable table = getTableOfThrow(id);
        table.setName(tableName);
        table.setCapacity(capacity);
        table.setOpen(open);
        return diningTableMapper.updateById(table) == 1;
    }

    @Override
    public Boolean deleteTable(Integer id) {
        DiningTable table = getTableOfThrow(id);
        if(table.getUsed()){
            throw new UnmessageException("餐桌正在被使用！");
        }
        return diningTableMapper.deleteById(id) == 1;
    }

    @Override
    public boolean tableUsed(Integer tableId, Integer orderId){
        DiningTable table = getTableOfThrow(tableId);
        log.info("tableId:{} orderId:{} used:{}", tableId, orderId, table.getUsed());
        if(!table.getOpen()){
            throw new UnmessageException("餐桌未开放！");
        }
        if(table.getUsed()){
            throw new UnmessageException("餐桌正被使用。");
        }
        table.setOrderId(orderId);
        table.setUsed(true);
        return diningTableMapper.updateById(table) == 1;
    }

    @Override
    @Deprecated
    public boolean tableUnused(Integer tableId, Integer orderId) {
        DiningTable table = getTableOfThrow(tableId);

        log.info("tableId:{} orderId:{} used:{}", tableId, orderId, table.getUsed());
        if(!table.getUsed()){
            throw new UnmessageException("餐桌未被使用。");
        }
        if(!table.getOrderId().equals(orderId)){
            throw new UnmessageException("订单号未对应。");
        }
        table.setOrderId(0);
        table.setUsed(false);
        return diningTableMapper.updateById(table) == 1;
    }

    @Override
    public boolean tableUnused(Integer tableId) {
        DiningTable table = getTableOfThrow(tableId);

        if(!table.getUsed()){
            throw new UnmessageException("餐桌未被使用。");
        }

        table.setOrderId(0);
        table.setUsed(false);

        KitchenWebsocket.sendMessage(userController.getShopName(), Result.flush());

        return diningTableMapper.updateById(table) == 1;
    }



    @Override
    public DiningTable getTableOfThrow(Integer id) {
        DiningTable table = diningTableMapper.selectById(id);
        if(table == null){
            throw new UnmessageException("餐桌不存在");
        }
        return table;
    }
}
