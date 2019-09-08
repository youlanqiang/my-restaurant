package com.qjkji.orderproject.backmanager.service;

import com.qjkji.orderproject.core.entity.DiningTable;

import java.util.List;

public interface TableService {

    List<DiningTable> listForUsed();

    List<DiningTable> listForShow();

    List<DiningTable> listInfo();

    List<DiningTable> list();

    Boolean createTable(String tableName, Integer capacity, Boolean open);

    Boolean updateTable(Integer id, String tableName,
                        Integer capacity, Boolean open);

    Boolean deleteTable(Integer id);

    /**
     * 设置餐桌正在使用中
     * @param tableId
     * @param orderId
     * @return
     */
    boolean tableUsed(Integer tableId, Integer orderId);

    /**
     * 设置餐桌未被使用
     * @param tableId
     * @param orderId
     * @return
     */
    boolean tableUnused(Integer tableId, Integer orderId);

    boolean tableUnused(Integer tableId);

    DiningTable getTableOfThrow(Integer id);


}
