package top.youlanqiang.orderproject.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.youlanqiang.orderproject.core.entity.OrderFood;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFoodMapper extends BaseMapper<OrderFood> {


    @Select("SELECT * FROM order_food WHERE order_id = #{orderId}")
    List<OrderFood> selectOrderFoodListByOrderId(Integer orderId);





}
