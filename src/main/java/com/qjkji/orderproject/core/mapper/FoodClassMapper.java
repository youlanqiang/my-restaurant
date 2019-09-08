package com.qjkji.orderproject.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjkji.orderproject.core.entity.FoodClass;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodClassMapper extends BaseMapper<FoodClass> {

    @Select("SELECT * FROM food_class WHERE classification_id = #{classId}")
    List<FoodClass> selectFoodClassByClassId(Integer classId);

    @Select("SELECT * FROM food_class WHERE food_id = #{foodId}")
    List<FoodClass> selectFoodClassByFoodId(Integer foodId);

    @Delete("DELETE FROM food_class WHERE food_id = #{foodId}")
    boolean deleteFoodClassByFoodId(Integer foodId);

    @Delete("DELETE FROM food_class WHERE classification_id = #{classId}")
    boolean deleteFoodClassByClassId(Integer classId);

    @Insert("INSERT INTO food_class(food_id, classification_id) VALUES(#{foodId}, #{classId})")
    boolean insertFoodClass(@Param("foodId") Integer foodId,
                            @Param("classId") Integer classId);

    @Select("SELECT COUNT(*) FROM food_class WHERE food_id = #{foodId} AND classification_id = #{classId}")
    boolean checkFoodClassCreated(@Param("foodId") Integer foodId,
                                  @Param("classId") Integer classId);

    @Delete("DELETE FROM food_class WHERE food_id = #{foodId} AND classification_id = #{classId}")
    boolean deleteOneById(@Param("foodId") Integer foodId, @Param("classId") Integer classId);

}
