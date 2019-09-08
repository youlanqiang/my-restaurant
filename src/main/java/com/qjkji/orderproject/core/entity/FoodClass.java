package com.qjkji.orderproject.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("food_class")
@Data
@NoArgsConstructor
/**
 * food 和 classification对应关系
 */
public class FoodClass {

    private int foodId;

    private int classificationId;


}
