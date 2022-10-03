package top.youlanqiang.orderproject.backmanager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.youlanqiang.orderproject.backmanager.service.FoodService;
import top.youlanqiang.orderproject.core.controller.UserController;
import top.youlanqiang.orderproject.core.entity.Food;
import top.youlanqiang.orderproject.core.exception.UnmessageException;
import top.youlanqiang.orderproject.core.mapper.ClassificationMapper;
import top.youlanqiang.orderproject.core.mapper.FoodClassMapper;
import top.youlanqiang.orderproject.core.mapper.FoodMapper;
import top.youlanqiang.orderproject.core.util.SecurityUtil;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    @Resource
    FoodMapper foodMapper;

    @Resource
    ClassificationMapper classificationMapper;

    @Resource
    FoodClassMapper foodClassMapper;

    @Resource
    UserController userController;

    @Override
    public List<Food> getFoodsByClassId(Integer cid) {
        List<Food> foods = foodMapper.selectFoodsByCid(cid);
        return foods;
    }

    @Override
    public Food getFoodById(Integer foodID) {
        Food food = foodMapper.selectOneById(foodID);
        if(food == null){
            throw new UnmessageException("没有这个菜品");
        }
        return food;
    }

    @Override
    public boolean addFood(Food food) {
        
        if(foodMapper.checkFoodNameCreated(food.getFoodName(), SecurityUtil.getShopName())){
            throw new UnmessageException("菜品名称已经存在！");
        }
        if(food.getDiscount().equals(BigDecimal.ZERO)){
            food.setDiscount(BigDecimal.ONE);
        }
        if(food.getPrice() == null){
            food.setPrice(BigDecimal.ZERO);
        }
        food.setShopName(userController.getShopName());
        return foodMapper.insert(food) == 1;
    }

    @Override
    public boolean deleteFoodById(Integer foodId) {
        if(foodMapper.deleteById(foodId) == 1){
            //删除分类对应关系
            foodClassMapper.deleteFoodClassByFoodId(foodId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateFoodById(Food food) {
        return foodMapper.updateById(food) == 1;
    }

    @Override
    public IPage<Food> getPageBySearch(Integer current, Integer size,
                                       String name, Integer classificationId) {
        IPage<Food> foodPage = new Page<>(current, size);
        foodPage = foodMapper.pageByNameAndClassId(foodPage, name, classificationId,
                userController.getShopName());
        return foodPage;
    }

    @Override
    public boolean deleteClassForFood(Integer foodId, Integer classId) {
        checkClassAndFoodCreated(foodId, classId);
        return foodClassMapper.deleteOneById(foodId, classId);
    }

    @Override
    public boolean addClassForFood(Integer foodId, Integer classId) {
        checkClassAndFoodCreated(foodId, classId);
        if(foodClassMapper.checkFoodClassCreated(foodId, classId)){
            throw new UnmessageException("对应关系已经存在！");
        }
        return foodClassMapper.insertFoodClass(foodId, classId);
    }

    private void checkClassAndFoodCreated(Integer foodId, Integer classId){
        if(foodMapper.selectById(foodId) == null){
            throw new UnmessageException("菜品id，不存在");
        }
        if(classificationMapper.selectById(classId) == null){
            throw new UnmessageException("分类id，不存在");
        }
    }

    public Food getFoodOrThrow(Integer id){
        Food food = foodMapper.selectById(id);
        if(food == null){
            throw new UnmessageException("菜品id, 不存在");
        }
        return food;
    }



}
