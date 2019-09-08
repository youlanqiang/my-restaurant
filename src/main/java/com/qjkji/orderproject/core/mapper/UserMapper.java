package com.qjkji.orderproject.core.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjkji.orderproject.core.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {


    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectUserByName(String userName);

    @Select("SELECT * FROM user WHERE telephone = #{telephone}")
    User selectUserByTelephone(String telephone);

    @Select("SELECT COUNT(*) FROM food WHERE shop_name = #{shopName}")
    boolean checkShopNameCreated(String shopName);




}
