package cn.com.liurz.flow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// @EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置路由拦截策略 表单登录规则 注册规则 认证成功逻辑 ，认证失败逻辑。
     * 请求拦截配置
     * @param http
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers("/activitServer/**").anonymous()    // activitServer下的全部放行
               .antMatchers("/activitServer/modeler/data/save").anonymous();   // modeler放行

    }
    //授权认证配置
   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        *//**
     * 基于内存的方式，创建两个用户admin/123456，user/123456
     * *//*
        auth.inMemoryAuthentication()
                .withUser("admin")//用户名
                .password(passwordEncoder().encode("123456"))//密码
                .roles("ADMIN");//角色
        auth.inMemoryAuthentication()
                .withUser("user")//用户名
                .password(passwordEncoder().encode("123456"))//密码
                .roles("USER");//角色
    }*/


    /**
     * 配置文件无需认证路径
     * @param web
     */
    @Override
    public void configure(WebSecurity web) throws Exception{
        // web.ignoring是直接绕开spring security的所有filter，直接跳过验证 ;http.permitAll不会绕开springsecurity验证，相当于是允许该路径通过
        web.ignoring().antMatchers("/activitServer/modeler/data/save").
        antMatchers("/activitServer/**");   // activitServer下的全部放行

    }

    /**
     * 指定加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        // 使用BCrypt加密密码
        return new BCryptPasswordEncoder();
    }
}
