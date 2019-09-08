package com.qjkji.orderproject.core.config;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import javax.annotation.Resource;
import java.util.Properties;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    UserDetailsService userDetailsService;


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                // 测试用资源，需要验证了的用户才能访问
                .antMatchers("/", "/login", "/weixin/**", "/sms-code", "/login-by-sms",
                        "/user/registered", "/kitchen/**",
                        "/favicon.ico",
                        "/js/**", "/img/**", "/css/**",
                        "/captcha.jpg").permitAll()
                // 其他都放行了
                .anyRequest().authenticated();
    }

    /**
     * 密码加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




    @Bean
    public DefaultKaptcha defaultKaptcha(){

        Properties properties = new Properties();
        properties.setProperty("kaptcha.border","no");
        properties.setProperty("kaptcha.textproducer.font.color","black");
        properties.setProperty("kaptcha.textproducer.char.space","5");
        Config config = new Config(properties);

        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
    

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception{


        return super.authenticationManager();
    }




}
