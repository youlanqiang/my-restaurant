package top.youlanqiang.orderproject.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.youlanqiang.orderproject.core.entity.Classification;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificationMapper extends BaseMapper<Classification> {

    @Select("SELECT COUNT(*) FROM classification WHERE name = #{name} AND shop_name = #{shopName}")
    boolean checkNameIsCreated(@Param("name") String name,@Param("shopName") String shopName);

}
