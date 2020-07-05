package top.youlanqiang.orderproject.core.mapper;

import top.youlanqiang.orderproject.core.entity.OrderFood;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsMapper {

    Integer selectNumberOfPeopleByDay(@Param("time") LocalDateTime dateTime, @Param("shopName") String shopName);

    Integer selectNumberOfPeopleByYearWithMonth(@Param("time") LocalDateTime time, @Param("shopName") String shopName);


    BigDecimal selectDecimalByDay(@Param("dateTime") LocalDateTime dateTime,
                                  @Param("paymentType") Integer paymentType,
                                  @Param("shopName") String shopName);

    BigDecimal selectDecimalByYearWithMonth(@Param("time") LocalDateTime time,
                                            @Param("paymentType") Integer paymentType,
                                            @Param("shopName") String shopName);


    List<OrderFood> selectBestFoodByYearWithMonth(@Param("time") LocalDateTime time, @Param("shopName") String shopName);

    List<OrderFood> selectWorstFoodByYearWithMonth(@Param("time") LocalDateTime time, @Param("shopName") String shopName);

}
