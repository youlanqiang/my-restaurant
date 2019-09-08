package com.qjkji.orderproject.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
@NoArgsConstructor
@TableName("user")
@ToString
public class User implements UserDetails {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    @JSONField(serialize = false)
    private String password;

    /**
     * 默认电话
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * 店名
     */
    private String shopName;


    /**
     * 类型
     */
    private Integer type;

    /**
     * 权限
     */
    @TableField(exist = false)
    private List<GrantedAuthority> authorities;



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
