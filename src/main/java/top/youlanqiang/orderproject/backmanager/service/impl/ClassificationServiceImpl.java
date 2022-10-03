package top.youlanqiang.orderproject.backmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.youlanqiang.orderproject.backmanager.service.ClassificationService;
import top.youlanqiang.orderproject.core.controller.UserController;
import top.youlanqiang.orderproject.core.entity.Classification;
import top.youlanqiang.orderproject.core.exception.UnmessageException;
import top.youlanqiang.orderproject.core.mapper.ClassificationMapper;
import top.youlanqiang.orderproject.core.mapper.FoodClassMapper;
import top.youlanqiang.orderproject.core.util.SecurityUtil;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClassificationServiceImpl implements ClassificationService {

    @Resource
    ClassificationMapper classificationMapper;

    @Resource
    FoodClassMapper foodClassMapper;

    @Resource
    UserController userController;

    @Override
    public boolean addClassification(Classification classification) {
        String name = classification.getName();
        if (classificationMapper.checkNameIsCreated(name, SecurityUtil.getShopName())) {
            throw new UnmessageException("名称已经创建！");
        }
        return classificationMapper.insert(classification) == 1;
    }

    @Override
    public boolean deleteClassificationById(int id) {
        if (!foodClassMapper.selectFoodClassByClassId(id).isEmpty()) {
            throw new UnmessageException("分类下有菜品！");
        }
        return classificationMapper.deleteById(id) == 1;
    }

    @Override
    public List<Classification> getAllClassification() {
        QueryWrapper<Classification> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_name", userController.getShopName());
        wrapper.orderByDesc("id");
        return classificationMapper.selectList(wrapper);
    }

    @Override
    public IPage<Classification> page(int current, int size) {
        IPage<Classification> page = new Page<>(current, size);
        QueryWrapper<Classification> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_name", userController.getShopName()).orderByDesc("id");
        return classificationMapper.selectPage(page,
                wrapper);
    }
}
