package com.qjkji.orderproject.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qjkji.orderproject.core.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.List;

@TableName("food")
@Data
@NoArgsConstructor
@ApiModel("菜品")
public class Food {

    @ApiModelProperty("菜品id,在添加操作下可以无视掉")
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String shopName;


    @ApiModelProperty(value = "菜名", example = "鱼香肉丝")
    private String foodName;


    @ApiModelProperty(value = "菜品简介", example = "鱼香肉丝是一道名菜")
    private String foodInfo;

    @ApiModelProperty(value = "单价", example = "32.50")
    private BigDecimal price;

    @ApiModelProperty(value = "折扣", example = "1.00")
    private BigDecimal discount;

    @ApiModelProperty(value = "图片路径", example = "xxx.jpg")
    private String imgPath;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类关系，直接无视掉就好了")
    private List<Classification> classifications;


    @ApiModelProperty(hidden = true, value = "得到折扣后得单价")
    public BigDecimal getRealPrice(){
        return price.multiply(discount).setScale(2, BigDecimal.ROUND_DOWN);
    }


    @ApiModelProperty(hidden = true, value = "格式化的单价")
    public String getFormatSinglePrice(){
        return StringUtils.formatPrice(price);
    }

    @ApiModelProperty(hidden = true, value = "格式化的折后单价")
    public String getFormatRealPrice(){
        return StringUtils.formatPrice(getRealPrice());
    }

}
