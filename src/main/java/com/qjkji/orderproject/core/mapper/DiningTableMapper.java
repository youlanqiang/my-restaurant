package com.qjkji.orderproject.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjkji.orderproject.core.entity.DiningTable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiningTableMapper  extends BaseMapper<DiningTable> {

    @Select(" SELECT * FROM dining_table WHERE   shop_name = #{shopName} AND  open = 1 AND capacity >= #{number} ORDER BY capacity ")
    List<DiningTable> getRecommendTableByNumberOfPeople(@Param("number") Integer numberOfPeople, @Param("shopName") String shopName);



}
