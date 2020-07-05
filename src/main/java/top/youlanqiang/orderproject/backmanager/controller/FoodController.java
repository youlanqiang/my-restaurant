package top.youlanqiang.orderproject.backmanager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import top.youlanqiang.orderproject.backmanager.service.ClassificationService;
import top.youlanqiang.orderproject.backmanager.service.FoodService;
import top.youlanqiang.orderproject.core.entity.Classification;
import top.youlanqiang.orderproject.core.entity.Food;
import top.youlanqiang.orderproject.core.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "菜谱管理")
@RequestMapping("/food")
@Slf4j
public class FoodController {

    @Resource
    FoodService foodService;

    @Resource
    ClassificationService classificationService;

    @ApiOperation("获取全部的菜品，分类格式化")
    @GetMapping("/all")
    public Result getAllFood(){

        JSONArray array = new JSONArray();
        List<Classification> classifications = classificationService.getAllClassification();

        for(Classification classification : classifications){
            JSONObject object = new JSONObject();
            object.put("className", classification.getName());
            object.put("classId", classification.getId());
            object.put("foods", foodService.getFoodsByClassId(classification.getId()));
            array.add(object);
        }
        return Result.success("成功。", array);
    }

    @ApiOperation("获取分类下所有的菜品")
    @GetMapping("/get-by-class/{cid}")
    public Result getClassFood(@PathVariable Integer cid){
        List<Food> list = foodService.getFoodsByClassId(cid);
        if(list == null){
            return Result.error("分类下没有菜品");
        }
        return Result.success("成功。", list);
    }

    @ApiOperation("获取一个菜品")
    @GetMapping("/get/{id}")
    public Result getOneById(@ApiParam(value = "菜品id", example = "1") @PathVariable Integer id) {
        Food food = foodService.getFoodById(id);

        return Result.success("成功！", food);
    }

    @ApiOperation("给菜品添加一个分类")
    @PostMapping("/add-class/{foodId}/{classId}")
    public Result addFoodClassForFood(@ApiParam(value = "菜品id", example = "1") @PathVariable Integer foodId,
                                      @ApiParam(value = "分类id", example = "2") @PathVariable Integer classId) {
        if (foodService.addClassForFood(foodId, classId)) {
            return Result.success("添加分类成功！");
        }
        return Result.error("添加分类失败！");
    }

    @ApiOperation("给菜品删除一个分类")
    @DeleteMapping("/delete-class/{foodId}/{classId}")
    public Result deleteFoodClassForFood(
            @ApiParam(value = "菜品id", example = "1") @PathVariable Integer foodId,
            @ApiParam(value = "分类id", example = "2") @PathVariable Integer classId) {
        if (foodService.deleteClassForFood(foodId, classId)) {
            return Result.success("删除分类成功！");
        }
        return Result.error("删除分类失败！");
    }

    @ApiOperation("添加菜品")
    @PostMapping("/add")
    public Result addFood(Food food) {
        if (foodService.addFood(food)) {
            return Result.success("创建成功！");
        }
        return Result.error("创建失败！");
    }

    @ApiOperation("删除菜品")
    @DeleteMapping("/delete/{id}")
    public Result deleteFood(@ApiParam(value = "菜品id", example = "1") @PathVariable Integer id) {
        if (foodService.deleteFoodById(id)) {
            return Result.success("删除成功！");
        }
        return Result.error("删除失败！");
    }

    @ApiOperation("更新菜品")
    @PostMapping("/update")
    public Result updateFood(Food food) {
        if (foodService.updateFoodById(food)) {
            return Result.success("更新成功！");
        }
        return Result.error("更新失败！");
    }

    @ApiOperation("分页查询")
    @GetMapping("/page/{current}/{size}")
    public Result page(@ApiParam(value = "页数", example = "1") @PathVariable Integer current,
                       @ApiParam(value = "项数", example = "10") @PathVariable Integer size,
                       @ApiParam(value = "名称") @RequestParam(defaultValue = "") String name,
                       @ApiParam(value = "分类id", example = "0") @RequestParam(defaultValue = "0") Integer cId) {
        //log.info("current:{} size:{}  name:{} cid:{}", current, size, name, cId);

        IPage<Food> page = foodService.getPageBySearch(current, size, name, cId);
        return Result.success("成功！", page);
    }

}
