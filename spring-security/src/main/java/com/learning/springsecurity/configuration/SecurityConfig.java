package com.learning.springsecurity.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author hufei
 * @Date 2023/4/25 12:57
 * @Version 1.0
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final PersistentTokenRepository persistentTokenRepository;

    public SecurityConfig(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          PersistentTokenRepository persistentTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 内存设置用户名、密码、角色
//        auth.inMemoryAuthentication().withUser("demo").password(passwordEncoder().encode("demo")).roles("admin");
        // 数据库设置用户名、密码、角色
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 没有权限时跳转的页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");
        // 登录及登录访问流程控制
        http.formLogin() //自定义自己编写的页面
                .loginPage("/login.html") //登录页面设置
                .loginProcessingUrl("/user/login") //登录访问路径, 这个路径不需要实际的servlet去绑定, 只需要登录页面中提交按钮绑定的action和这里一致即可
                .defaultSuccessUrl("/success.html") //登录成功之后的跳转路径
                .failureUrl("/unauth.html") //登录失败之后的跳转路径
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/user/login").permitAll() //设置哪些路径可直接访问, 无需认证
                // admin角色才有权限访问的路径
                .antMatchers("/demo").hasAuthority("admin")
                // 能匹配上的角色才有权限访问的路径
                .antMatchers("/demo").hasAnyAuthority("admin,guest")
                .antMatchers("/demo").hasAnyAuthority("admin", "guest")
                // admin角色才有权限访问的路径(角色匹配要加上 ROLE_ )
                .antMatchers("/demo").hasRole("admin")
                // 能匹配上的角色才有权限访问的路径(角色匹配要加上 ROLE_ )
                .antMatchers("/demo").hasAnyRole("admin,guest")
                .antMatchers("/demo").hasAnyRole("admin", "guest")
                // 其它请求只要用户能够成功登录即可
                .anyRequest()
                .authenticated()
                .and()
                // 设置记住我, 绑定 persistentRepository, 设置有效时长, 设置用户服务
                .rememberMe().tokenRepository(persistentTokenRepository).tokenValiditySeconds(60).userDetailsService(userDetailsService);
        //关闭csrf防护
        http.csrf().disable();
        //用户注销流程
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login.html").permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/css/**", "/js/**");
    }

}
