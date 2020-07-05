package top.youlanqiang.orderproject.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.youlanqiang.orderproject.core.entity.Result;
import top.youlanqiang.orderproject.core.entity.User;
import top.youlanqiang.orderproject.core.entity.enums.EmployeeType;
import top.youlanqiang.orderproject.core.service.UserService;
import top.youlanqiang.orderproject.core.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@Api(tags = "用户接口")
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;


    @PostMapping("/change-password")
    @ApiOperation("修改密码")
    @ResponseBody
    public Result changePassword(@ApiParam("新密码") @RequestParam("newPassword") String newPassword,
                                 @ApiParam("旧密码") @RequestParam("oldPassword") String oldPassword){
        if(userService.updatePassword(oldPassword, newPassword)){
            return Result.success("修改成功。");
        }
        return Result.error("修改失败。");
    }



    @PostMapping("/registered")
    @ApiOperation("用户注册接口")
    @ResponseBody
    public Result registered(@ApiParam("用户名") @RequestParam("username") String username,
                             @ApiParam("密码") @RequestParam("password") String password,
                             @ApiParam("电话") @RequestParam("telephone") String telephone,
                             @ApiParam("邮箱") @RequestParam("email") String email,
                             HttpServletRequest request){

        log.info("username:{} password:{} email:{} telephone:{}",username, password, email, telephone);
        User user = new User();
        user.setUsername(username);
        user.setTelephone(telephone);
        user.setPassword(password);
        user.setEmail(email);
        user.setShopName(username);
        user.setType(EmployeeType.manager.getType());

        if(userService.registeredUser(user)){
            return Result.success("注册成功。", user.getUsername());
        }else{
            return Result.error("失败。");
        }
    }

    @GetMapping("/info")
    @ApiOperation("获取对应id的用户")
    @ResponseBody
    public Result info(){
        String userName = SecurityUtil.getUserName();
        User user = userService.getUserByName(userName);
        if(user != null){
            return Result.success("成功.", user);
        }
        return  Result.error("失败。");
    }



    @GetMapping("/logout")
    @ApiOperation("用户退出")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        SecurityUtil.logout();
        return "login";
    }


    @GetMapping("/page/{current}/{size}")
    @ApiOperation("分页查询")
    @ResponseBody
    public Result page(@ApiParam("名称查询") @RequestParam(value = "name", required = false) String name,
                       @ApiParam("页数") @PathVariable Integer current,
                       @ApiParam("项数") @PathVariable Integer size){
        IPage<User> userPage = userService.getPageByName(current, size, name);
        return Result.success("成功。", userPage);
    }

    @DeleteMapping("/delete-employee/{id}")
    @ApiOperation("删除员工")
    @ResponseBody
    public Result deleteEmployee(@ApiParam("id") @PathVariable Integer id){
        if(userService.deleteUser(id)){
            return Result.success("删除成功.");
        }
        return Result.error("删除失败。");
    }


    @PostMapping("/update-employee-password/{id}")
    @ApiOperation("修改员工密码")
    @ResponseBody
    public Result updateEmployeePassword(@ApiParam("id") @PathVariable Integer id,
                                         @ApiParam("密码") @RequestParam("password") String password){
        if(userService.updateEmployeePassword(id, password)){
            return Result.success("密码更新成功.");
        }
        return Result.error("密码更新失败。");
    }


    @PostMapping("/add-employee")
    @ApiOperation("添加员工")
    @ResponseBody
    public Result addEmployee(@ApiParam("用户名") @RequestParam("username") String username,
                              @ApiParam("密码") @RequestParam("password") String password,
                              @ApiParam("电话") @RequestParam("telephone") String telephone,
                              @ApiParam("邮箱") @RequestParam("email") String email,
                              @ApiParam("员工类型") @RequestParam("type") Integer type){
        log.info("username:{} password:{} email:{} telephone:{}",username, password, email, telephone);
        User user = new User();
        user.setUsername(username);
        user.setTelephone(telephone);
        user.setPassword(password);
        user.setEmail(email);
        user.setShopName(getShopName());
        user.setType(type);

        if(userService.registeredUser(user)){
            return Result.success("添加员工成功。", user.getUsername());
        }else{
            return Result.error("添加员工失败。");
        }

    }


    public String getShopName(){
        User user = userService.getUserByName(SecurityUtil.getUserName());
        return user.getShopName();
    }

}
