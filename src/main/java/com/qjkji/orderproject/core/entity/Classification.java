package com.qjkji.orderproject.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 食物分类
 */
@TableName("classification")
@Data
@NoArgsConstructor
@ApiModel("菜品分类")
public class Classification {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private int id;

    @NotNull(message = "名称不能为空")
    @ApiModelProperty(value = "分类名", example = "川菜")
    private String name;


    private String shopName;


}
