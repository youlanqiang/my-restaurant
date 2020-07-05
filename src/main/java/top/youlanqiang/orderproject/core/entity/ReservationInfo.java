package top.youlanqiang.orderproject.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import top.youlanqiang.orderproject.core.entity.enums.ReservationMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ApiModel("预约信息")
@TableName("reservation_info")
public class ReservationInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String shopName;

    @ApiModelProperty("开始预约时间")
    private LocalDateTime startDate;

    @ApiModelProperty("结束预约时间")
    private LocalDateTime endDate;

    @ApiModelProperty("预约人数")
    private Integer numberOfPeople;

    @ApiModelProperty("预约桌号")
    private Integer tableId;

    @ApiModelProperty("预约用户名")
    private String clientName;

    @ApiModelProperty("预约电话")
    private String clientTel;

    @ApiModelProperty("预约状态")
    private Integer status;

    @ApiModelProperty(hidden = true)
    @JSONField(deserialize = false)
    @TableField(exist = false)
    private ReservationMethod reservationMethod;

    public void setReservationMethod(ReservationMethod reservationMethod){
        this.reservationMethod = reservationMethod;
        this.status =reservationMethod.getType();
    }

}
