package top.youlanqiang.orderproject.backmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.youlanqiang.orderproject.core.entity.DiningTable;
import top.youlanqiang.orderproject.core.entity.ReservationInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    IPage<ReservationInfo> getPage(Integer current, Integer size);

    Boolean successInfo(Integer id);

    Boolean cancelInfo(Integer id);

    Boolean deleteInfo(Integer id);

    Boolean createInfo(String clientName, String clientTel,
                       Integer tableId, Integer numberOfPeople,
                       LocalDateTime startTime, LocalDateTime endTime);

    List<ReservationInfo> searchByTel(String tel);

    List<DiningTable> getRecommend(Integer number, LocalDateTime startTime, LocalDateTime endTime);

    ReservationInfo getInfoOrThrow(Integer id);

    /**
     * 获取对应餐桌下的未完成预约情况
     * @param tableId
     * @return
     */
    List<ReservationInfo> getReservationByTableId(Integer tableId);


    /**
     * 检查当前时间段是否预定
     * @param tableId
     * @param time
     * @return
     */
    Boolean checkHaveReservation(Integer tableId, LocalDateTime time);
}
