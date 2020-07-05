package top.youlanqiang.orderproject.backmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.youlanqiang.orderproject.backmanager.service.ClassificationService;
import top.youlanqiang.orderproject.core.controller.UserController;
import top.youlanqiang.orderproject.core.entity.Classification;
import top.youlanqiang.orderproject.core.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "分类管理")
@RequestMapping("/classification")
@Slf4j
public class ClassificationController {

    @Resource
    ClassificationService classificationService;

    @Resource
    UserController userController;

    
    @ApiOperation("添加分类")
    @PostMapping("/add")
    public Result addClassification(@ApiParam(value = "分类名称", example = "川菜") @RequestParam("name") String name){
        Classification classification = new Classification();
        classification.setName(name);
        classification.setShopName(userController.getShopName());
        if(classificationService.addClassification(classification)){
            return Result.success("创建成功！");
        }
        return Result.error("创建失败！");
    }
    
    @ApiOperation("删除分类")
    @DeleteMapping("/delete/{id}")
    public Result deleteClassification(@ApiParam(value = "分类id",example = "1") @PathVariable Integer id){
        if(classificationService.deleteClassificationById(id)) {
            return Result.success("删除成功！");
        }
        return Result.error("创建失败！");
    }

    @ApiOperation("分类分页")
    @GetMapping("/page/{current}/{size}")
    public Result classificationPage(@ApiParam(value = "页面", example = "1") @PathVariable Integer current,
                                     @ApiParam(value = "项数", example = "10") @PathVariable Integer size){
        IPage<Classification> page = classificationService.page(current, size);
        return Result.success("返回分页。", page);
    }

    @ApiOperation("全部分类")
    @GetMapping("/all")
    public Result allClassification(){
        List<Classification> list = classificationService.getAllClassification();
        return Result.success("返回全部分类。", list);
    }

}
