package top.youlanqiang.orderproject.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel("餐桌")
@TableName("dining_table")
@ToString
public class DiningTable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String shopName;

    @ApiModelProperty(value = "桌号或名称", example = "1号")
    private String name;

    @ApiModelProperty(value = "可容纳人数", example = "6")
    private Integer capacity;

    @ApiModelProperty(value = "是否开放", example = "true")
    private Boolean open;

    @ApiModelProperty(value = "在用餐", example = "false")
    private Boolean used;

    @ApiModelProperty(value = "对应订单ID", example = "5")
    private Integer orderId;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Order order;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<ReservationInfo> reservationInfoList;

}
