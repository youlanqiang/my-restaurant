package top.youlanqiang.orderproject.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import top.youlanqiang.orderproject.core.entity.Food;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodMapper extends BaseMapper<Food> {

    @Select("SELECT COUNT(*) FROM food WHERE food_name = #{name}")
    boolean checkFoodNameCreated(String foodName);


    List<Food>  selectFoodsByCid(Integer cid);

    Food selectOneById(Integer id);


    IPage<Food> pageByNameAndClassId(
            IPage<Food> page,
            @Param("name") String name,
            @Param("cId") Integer classificationId,
            @Param("shopName") String shopName);

}
