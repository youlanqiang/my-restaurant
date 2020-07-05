package top.youlanqiang.orderproject.core.entity.builder;

import top.youlanqiang.orderproject.core.entity.ReservationInfo;
import top.youlanqiang.orderproject.core.entity.enums.ReservationMethod;
import top.youlanqiang.orderproject.core.exception.UnmessageException;

import java.time.LocalDateTime;

public class ReservationBuilder {

    private ReservationInfo info;

    private ReservationBuilder(){
        this.info = new ReservationInfo();
    }

    public static ReservationBuilder create(){
        return new ReservationBuilder();
    }

    public ReservationBuilder pushClientName(String clientName){
        info.setClientName(clientName);
        return this;
    }

    public ReservationBuilder pushClientTel(String clientTel){
        info.setClientTel(clientTel);
        return this;
    }

    public ReservationBuilder pushStartDate(LocalDateTime startDate){
        info.setStartDate(startDate);
        return this;
    }

    public ReservationBuilder pushEndDate(LocalDateTime endDate){
        info.setEndDate(endDate);
        return this;
    }

    public ReservationBuilder pushNumberOfPeople(Integer numberOfPeople){
        info.setNumberOfPeople(numberOfPeople);
        return this;
    }

    public ReservationBuilder pushTableId(Integer tableId){
        info.setTableId(tableId);
        return this;
    }

    public ReservationInfo finish(){
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime startTime = info.getStartDate();
        if(nowTime.isAfter(startTime)){
            throw new UnmessageException("预约时间应该发生在未来。");
        }
        LocalDateTime endTime = info.getEndDate();
        startTime = startTime.plusHours(2);
        if(endTime.isAfter(startTime)){
            throw new UnmessageException("最长预约时间为2小时。");
        }
        info.setReservationMethod(ReservationMethod.WAIT);
        return this.info;
    }
}
