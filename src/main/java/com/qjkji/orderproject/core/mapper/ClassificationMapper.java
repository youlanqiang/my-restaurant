package com.qjkji.orderproject.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjkji.orderproject.core.entity.Classification;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificationMapper extends BaseMapper<Classification> {

    @Select("SELECT COUNT(*) FROM classification WHERE name = #{name}")
    boolean checkNameIsCreated(String name);

}
