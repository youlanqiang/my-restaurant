package top.youlanqiang.orderproject.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import top.youlanqiang.orderproject.core.controller.UserController;
import top.youlanqiang.orderproject.core.entity.User;
import top.youlanqiang.orderproject.core.entity.enums.EmployeeType;
import top.youlanqiang.orderproject.core.exception.UnmessageException;
import top.youlanqiang.orderproject.core.mapper.UserMapper;
import top.youlanqiang.orderproject.core.service.UserService;
import top.youlanqiang.orderproject.core.util.SecurityUtil;
import com.zhenzi.sms.ZhenziSmsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    PasswordEncoder encoder;

    @Resource
    ZhenziSmsClient zhenziSmsClient;

    @Resource
    UserController userController;

    @Override
    public User checkTelephoneAndCode(String telephone, String code) {
        User user = userMapper.selectUserByTelephone(telephone);
        if(user == null){
            throw new UnmessageException("没有查询到对应用户。");
        }
        if(user.getCode().equals(code)){
            return user;
        }

        throw new UnmessageException("登陆失败，验证码错误。");
    }

    @Override
    public User getUserByTelephone(String telephone) {
        User user = userMapper.selectUserByTelephone(telephone);
        if(user == null){
            throw new UnmessageException("没有查询到对应用户。");
        }
        return user;
    }

    @Override
    public boolean setUserCodeForTelephone(String telephone, String code) {
        User user = userMapper.selectUserByTelephone(telephone);

        if(user == null){
            throw new UnmessageException("没有查询到对应用户。");
        }
        user.setCode(code);
        try {
            zhenziSmsClient.send(telephone, "您的验证码:" + code);

        }catch(Exception e){
            throw new UnmessageException(e.getMessage());
        }
        log.info("code == {}", code);
        return userMapper.updateById(user) == 1;
    }

    @Override
    public boolean registeredUser(User user) {

        if (userMapper.selectUserByName(user.getUsername()) != null) {
            throw new UnmessageException("用户名已存在!");
        }
        if (userMapper.selectUserByTelephone(user.getTelephone()) != null) {
            throw new UnmessageException("手机号已经被注册！");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userMapper.insert(user) == 1;
    }


    @Override
    public boolean updatePassword(String oldPassword, String newPassword) {
        User user = userMapper.selectUserByName(SecurityUtil.getUserName());
        Preconditions.checkNotNull(user, "用户不存在!");
        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            return userMapper.updateById(user) == 1;
        }
        throw new UnmessageException("旧密码不对!");
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByName(String userName) {
        return userMapper.selectUserByName(userName);
    }


    @Override
    public IPage<User> getPageByName(Integer current, Integer size, String name) {
        IPage<User> page = new Page<>(current, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (name != null && !name.trim().equals("")) {
            wrapper.like("username", name);
        }
        wrapper.eq("shop_name", userController.getShopName());
        wrapper.orderByDesc("id");

        page = userMapper.selectPage(page, wrapper);
        return page;
    }

    @Override
    public boolean deleteUser(Integer id) {
        User user = userMapper.selectById(id);
        if(user == null){
            throw new UnmessageException("未发现这个用户。");
        }
        if(user.getType().equals(EmployeeType.manager.getType())){
            throw new UnmessageException("不能删除店长。");
        }
        return userMapper.deleteById(id) == 1;
    }

    @Override
    public boolean updateEmployeePassword(Integer id, String password) {
        User user = userMapper.selectById(id);
        if(user == null){
            throw new UnmessageException("没有发现这个员工.");
        }
        if(user.getType().equals(EmployeeType.manager.getType())){
            throw new UnmessageException("不能修改店长密码。");
        }
        user.setPassword(encoder.encode(password));
        return userMapper.updateById(user) == 1;
    }
}
