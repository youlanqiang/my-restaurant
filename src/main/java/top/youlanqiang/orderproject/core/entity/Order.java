package top.youlanqiang.orderproject.core.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import top.youlanqiang.orderproject.core.entity.enums.PaymentMethod;
import top.youlanqiang.orderproject.core.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@ApiModel("订单")
@Data
@TableName("orders")
@ToString
public class Order {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String shopName;

    @ApiModelProperty(hidden = true)
    @JSONField(deserialize = false)
    @TableField(exist = false)
    private PaymentMethod paymentMethod;

    @ApiModelProperty("支付方式")
    private Integer paymentType;

    @ApiModelProperty("支付金额")
    private BigDecimal paymentPrice;

    @ApiModelProperty("系统计算金额")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "桌号",example = "10")
    private Integer tableId;

    @ApiModelProperty(value = "人数",example = "6")
    private Integer numberOfPeople;

    @ApiModelProperty("订单状态 true: 正常 false: 取消")
    private Boolean status;

    @ApiModelProperty("支付状态 true: 已付款 false: 取消")
    private Boolean paymentStatus;

    @ApiModelProperty(value = "客户姓名", example = "先生")
    private String clientName;

    @ApiModelProperty(value = "备注",example = "都不放辣")
    private String remarks;

    @ApiModelProperty("用餐时间")
    private LocalDateTime startDate;

    @ApiModelProperty("结束时间")
    private LocalDateTime endDate;

    @ApiModelProperty("线上订单支付")
    private String onlineCode;

    @ApiModelProperty("点餐详情,接口中不需要带入")
    @TableField(exist = false)
    private List<OrderFood> foods;

    @ApiModelProperty("桌号")
    @TableField(exist = false)
    private DiningTable diningTable;



    /**
     * 使用这个方法来对支付方式进行操作
     * @param paymentMethod
     */
    public void setPaymentMethod(PaymentMethod paymentMethod){
        this.paymentMethod = paymentMethod;
        this.paymentType =paymentMethod.getType();
    }

    @ApiModelProperty(hidden = true, value = "格式化后的价格")
    public String getFormatPaymentAmount(){
        return StringUtils.formatPrice(paymentPrice);
    }




}
