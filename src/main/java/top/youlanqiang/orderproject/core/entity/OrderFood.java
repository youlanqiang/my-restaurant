package top.youlanqiang.orderproject.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import top.youlanqiang.orderproject.core.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单菜品对应关系
 */
@ApiModel("订单菜品对应关系")
@TableName("order_food")
@Data
public class OrderFood {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单id")
    private Integer orderId;

    @ApiModelProperty(value = "菜品id", example = "1")
    private Integer foodId;

    @ApiModelProperty(value = "菜品名称", example = "鱼香肉丝")
    private String foodName;

    @ApiModelProperty("单价，未打折")
    private BigDecimal singlePrice;

    @ApiModelProperty("折扣")
    private BigDecimal discount;

    @ApiModelProperty("数量")
    private Integer amount;

    @ApiModelProperty("是否上齐 true:上齐 false:未上齐")
    private Boolean status;

    @ApiModelProperty("是否退菜 true:退菜 false:未退菜")
    private Boolean returnStatus;

    @ApiModelProperty("退菜原因")
    private String returnMessage;

    @ApiModelProperty(hidden = true, value = "折后单价")
    public BigDecimal getRealSinglePrice(){
        return singlePrice.multiply(discount).setScale(2, BigDecimal.ROUND_DOWN);
    }

    @ApiModelProperty(hidden = true, value = "折后全价")
    public BigDecimal getRealPrice(){
        BigDecimal price = getRealSinglePrice().multiply(BigDecimal.valueOf(amount));
        return  price.setScale(2, BigDecimal.ROUND_DOWN);
    }

    @ApiModelProperty(hidden = true, value = "格式化折后单价")
    public String getFormatRealSinglePrice(){
        return StringUtils.formatPrice(getRealSinglePrice());
    }

    @ApiModelProperty(hidden = true, value = "格式化折后全价")
    public String getFormatRealPrice(){
        return StringUtils.formatPrice(getRealPrice());
    }


}
