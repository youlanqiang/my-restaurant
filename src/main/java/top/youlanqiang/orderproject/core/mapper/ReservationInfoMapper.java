package top.youlanqiang.orderproject.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.youlanqiang.orderproject.core.entity.ReservationInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationInfoMapper extends BaseMapper<ReservationInfo> {

    @Select("SELECT * FROM reservation_info WHERE table_id = #{tableId} AND status = 1")
    List<ReservationInfo>  getInfoByTableIdForReservation(
            Integer tableId
    );

}
