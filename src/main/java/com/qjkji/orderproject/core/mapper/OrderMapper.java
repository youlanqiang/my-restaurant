package com.qjkji.orderproject.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjkji.orderproject.core.entity.Order;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderMapper extends BaseMapper<Order> {


    boolean updateOrder(@ApiParam("id") Integer id,
                        @ApiParam("tableId") Integer tableId,
                        @ApiParam("numberOfPeople") Integer numberOfPeople,
                        @ApiParam("clientName") String clientName,
                        @ApiParam("remarks") String remarks);




}
