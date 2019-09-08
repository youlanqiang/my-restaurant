package com.qjkji.orderproject.core.controller;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.qjkji.orderproject.core.entity.Result;
import com.qjkji.orderproject.core.entity.User;
import com.qjkji.orderproject.core.entity.enums.EmployeeType;
import com.qjkji.orderproject.core.exception.UnmessageException;
import com.qjkji.orderproject.core.service.UserService;
import com.qjkji.orderproject.core.sms.SmsAuthentication;
import com.qjkji.orderproject.core.util.ConstantVal;
import com.qjkji.orderproject.core.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


@Controller
@CrossOrigin
@Api(tags = "路由")
@Slf4j
public class IndexController {

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    DefaultKaptcha defaultKaptcha;

    @Resource
    UserService userService;

    @GetMapping("/captcha.jpg")
    @ResponseBody
    public void applyCheckCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = defaultKaptcha.createText();
        //生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        //保存到session
        request.getSession().setAttribute(ConstantVal.CHECK_CODE, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

    @GetMapping(value = "/sms-code")
    @ResponseBody
    public Result getSmsCode(@RequestParam("telephone") String telephone){
        String code = String
                .valueOf(new Random().nextInt(899999) + 100000);
        if(userService.setUserCodeForTelephone(telephone, code)){
            return Result.success("发送成功。");
        }
        return Result.error("发送失败。");
    }


    @PostMapping(value="/login-by-sms")
    @ResponseBody
    public Result loginBySms(HttpServletRequest request,
                             @RequestParam("telephone") String telephone,
                             @RequestParam("code") String code){
        try{
            User user = userService.checkTelephoneAndCode(telephone, code);
            //手机验证码登陆
            Authentication authenticate = new SmsAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
        }catch(UnmessageException e){
            return Result.error(e.getMessage());
        } catch (Exception e){
            return Result.error("登陆失败!");
        }
        return Result.success("登陆成功！");
    }


    @PostMapping(value = "/login")
    @ResponseBody
    public Result login(HttpServletRequest request,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam("kaptcha") String kaptcha) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try{
            String s = request.getSession().getAttribute(ConstantVal.CHECK_CODE).toString();

            if (kaptcha.trim().isEmpty() || !s.equals(kaptcha)) {
                return Result.error("验证码错误。");
            }

            //使用SpringSecurity拦截登陆请求 进行认证和授权

            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("登陆失败!");
        }

        return Result.success("登陆成功！");
    }

    @GetMapping("/")
    public String index(){
        String userName= SecurityUtil.getUserName();

        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }

        User user = userService.getUserByName(userName);
        if(user.getType().equals(EmployeeType.chef.getType())){
            return "kitchenManager";
        }
        return "frontManager";
    }

    @GetMapping("/front")
    @ApiOperation("前台管理")
    public String front(){
        String userName= SecurityUtil.getUserName();

        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }
        User user = userService.getUserByName(userName);
        if(user.getType().equals(EmployeeType.chef.getType())){
            return "kitchenManager";
        }
        return "frontManager";
    }

    @GetMapping("/userManager")
    @ApiOperation("用户管理")
    public String userManager(){
        String userName= SecurityUtil.getUserName();
        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }
        User user = userService.getUserByName(userName);
        if(user.getType().equals(EmployeeType.chef.getType())){
            return "kitchenManager";
        }
        return "userManager";
    }

    @GetMapping("/food")
    @ApiOperation("菜品管理")
    public String food(){
        String userName= SecurityUtil.getUserName();

        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }
        User user = userService.getUserByName(userName);
        if(user.getType().equals(EmployeeType.chef.getType())){
            return "kitchenManagers";
        }
        return "foodManager";
    }

    @GetMapping("/reservation")
    @ApiOperation("预约管理")
    public String reservation(){
        String userName= SecurityUtil.getUserName();


        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }
        User user = userService.getUserByName(userName);
        if(user.getType().equals(EmployeeType.chef.getType())){
            return "kitchenManager";
        }
        return "reservationManager";
    }

    @GetMapping("/order")
    @ApiOperation("订单管理")
    public String order(){
        String userName= SecurityUtil.getUserName();


        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }
        User user = userService.getUserByName(userName);
        if(user.getType().equals(EmployeeType.chef.getType())){
            return "kitchenManager";
        }
        return "orderManager";
    }

    @GetMapping("/statistics")
    @ApiOperation("统计管理")
    public String statistics(){
        String userName= SecurityUtil.getUserName();
        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }
        User user = userService.getUserByName(userName);
        if(user.getType().equals(EmployeeType.chef.getType())){
            return "kitchenManager";
        }
        return "statisticsManager";
    }


    @GetMapping("/myinfo")
    @ApiOperation("我的信息")
    public String myinfo(){
        String userName= SecurityUtil.getUserName();


        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }
        User user = userService.getUserByName(userName);
        if(user.getType().equals(EmployeeType.chef.getType())){
            return "kitchenManager";
        }
        return "myinfo";
    }

    @GetMapping("/mykitchen")
    @ApiOperation("后厨管理")
    public String kitchen(){
        String userName= SecurityUtil.getUserName();

        if(userName == null || userName.trim().isEmpty() || userName.equals("anonymousUser")){
            return "login";
        }
        return "kitchenManager";
    }
}
