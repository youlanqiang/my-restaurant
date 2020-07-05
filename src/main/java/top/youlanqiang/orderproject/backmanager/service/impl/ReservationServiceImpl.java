package top.youlanqiang.orderproject.backmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.youlanqiang.orderproject.backmanager.service.ReservationService;
import top.youlanqiang.orderproject.core.controller.UserController;
import top.youlanqiang.orderproject.core.entity.DiningTable;
import top.youlanqiang.orderproject.core.entity.ReservationInfo;
import top.youlanqiang.orderproject.core.entity.builder.ReservationBuilder;
import top.youlanqiang.orderproject.core.entity.enums.ReservationMethod;
import top.youlanqiang.orderproject.core.exception.UnmessageException;
import top.youlanqiang.orderproject.core.mapper.DiningTableMapper;
import top.youlanqiang.orderproject.core.mapper.ReservationInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Resource
    ReservationInfoMapper reservationInfoMapper;

    @Resource
    DiningTableMapper diningTableMapper;

    @Resource
    UserController userController;

    @Override
    public IPage<ReservationInfo> getPage(Integer current, Integer size) {
        IPage<ReservationInfo> page = new Page<>(current, size);
        QueryWrapper<ReservationInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_name", userController.getShopName());
        wrapper.orderByDesc("id");
        return reservationInfoMapper.selectPage(page, wrapper);
    }

    @Override
    public Boolean successInfo(Integer id) {
        ReservationInfo info = getInfoOrThrow(id);
        if (info.getReservationMethod() == ReservationMethod.CANCEL) {
            throw new UnmessageException("预约已取消!");
        }
        if (info.getReservationMethod() == ReservationMethod.SUCCESS) {
            throw new UnmessageException("预约已完成!");
        }
        info.setReservationMethod(ReservationMethod.SUCCESS);
        return reservationInfoMapper.updateById(info) == 1;
    }

    @Override
    public Boolean cancelInfo(Integer id) {
        ReservationInfo info = getInfoOrThrow(id);
        if (info.getReservationMethod() == ReservationMethod.CANCEL) {
            throw new UnmessageException("预约已取消!");
        }
        if (info.getReservationMethod() == ReservationMethod.SUCCESS) {
            throw new UnmessageException("预约已完成!");
        }
        info.setReservationMethod(ReservationMethod.CANCEL);
        return reservationInfoMapper.updateById(info) == 1;
    }

    @Override
    public Boolean deleteInfo(Integer id) {
        return reservationInfoMapper.deleteById(id) == 1;
    }

    @Override
    public Boolean createInfo(String clientName,
                              String clientTel,
                              Integer tableId,
                              Integer numberOfPeople,
                              LocalDateTime startTime,
                              LocalDateTime endTime) {
        if(startTime == null || endTime == null){
            throw new UnmessageException("预约时间不能为空。");
        }


        ReservationInfo info = ReservationBuilder.create()
                .pushClientName(clientName).pushClientTel(clientTel)
                .pushStartDate(startTime).pushEndDate(endTime).pushNumberOfPeople(numberOfPeople)
                .pushTableId(tableId).finish();
        info.setShopName(userController.getShopName());

        List<ReservationInfo> infoList = reservationInfoMapper.getInfoByTableIdForReservation(tableId);


        for (ReservationInfo temp : infoList) {

            if(temp.getStartDate().isEqual(info.getStartDate())){
                throw new UnmessageException("时间段已预定");
            }

            if(temp.getEndDate().isEqual(info.getEndDate())){
                throw new UnmessageException("时间段已预定");
            }

            //检查对应tableId在时间段上有没有预约的订单
            if (temp.getEndDate().isAfter(info.getEndDate())
                    && temp.getStartDate().isBefore(info.getStartDate())) {
                throw new UnmessageException("时间段已预定");
            }

            if (temp.getEndDate().isAfter(info.getEndDate())
                    && temp.getStartDate().isBefore(info.getEndDate())) {
                throw new UnmessageException("时间段已预定");
            }

            if (temp.getStartDate().isAfter(info.getStartDate())
                    && temp.getStartDate().isBefore(info.getEndDate())) {
                throw new UnmessageException("时间段已预定");
            }
        }

        return reservationInfoMapper.insert(info) == 1;
    }

    @Override
    public List<ReservationInfo> searchByTel(String tel) {
        QueryWrapper<ReservationInfo> wrapper = new QueryWrapper<>();
        wrapper.like("client_tel", tel);
        return reservationInfoMapper.selectList(wrapper);
    }

    @Override
    public List<DiningTable> getRecommend(Integer number, LocalDateTime startTime,
                                          LocalDateTime endTime) {
        List<DiningTable> recommendTables = new ArrayList<>();
        List<DiningTable> tables = diningTableMapper.getRecommendTableByNumberOfPeople(number, userController.getShopName());
        for (DiningTable table : tables) {
            List<ReservationInfo> infoList = reservationInfoMapper.getInfoByTableIdForReservation(table.getId());
            boolean canAdd = true;

            for (ReservationInfo temp : infoList) {
                //检查对应tableId在时间段上有没有预约的订单
                if(temp.getStartDate().isEqual(startTime)){
                    canAdd = false;
                }else if(temp.getEndDate().isEqual(endTime)){
                    canAdd = false;
                } else if (temp.getEndDate().isAfter(endTime)
                        && temp.getStartDate().isBefore(startTime)) {
                    canAdd = false;
                } else if (temp.getEndDate().isAfter(endTime)
                        && temp.getStartDate().isBefore(endTime)) {
                    canAdd = false;
                } else if (temp.getStartDate().isAfter(startTime)
                        && temp.getStartDate().isBefore(endTime)) {
                    canAdd = false;
                }
            }

            if (canAdd) {
                recommendTables.add(table);
            }
        }
        return recommendTables;
    }


    @Override
    public List<ReservationInfo> getReservationByTableId(Integer tableId) {
        List<ReservationInfo> list = reservationInfoMapper.getInfoByTableIdForReservation(tableId);
        return list;
    }

    @Override
    public ReservationInfo getInfoOrThrow(Integer id) {
        ReservationInfo info = reservationInfoMapper.selectById(id);
        if (info == null) {
            throw new UnmessageException("没有这个预约id");
        }
        return info;
    }

    @Override
    public Boolean checkHaveReservation(Integer tableId, LocalDateTime time) {
        QueryWrapper<ReservationInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("table_id", tableId).ge("start_date", time)
                .le("end_date", time);
        return reservationInfoMapper.selectCount(wrapper) > 0;
    }
}
