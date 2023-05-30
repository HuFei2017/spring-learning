package com.learning.springsecurity.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.springsecurity.dao.UsersDao;
import com.learning.springsecurity.entity.UsersDO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CustomUserDetailsService
 * @Description TODO
 * @Author hufei
 * @Date 2023/4/28 14:46
 * @Version 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UsersDao usersDao;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder,
                                    UsersDao usersDao) {
        this.passwordEncoder = passwordEncoder;
        this.usersDao = usersDao;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        QueryWrapper<UsersDO> wrapper = new QueryWrapper<>();
        wrapper.eq("username", userName);
        UsersDO usersDO = usersDao.selectOne(wrapper);
        if (null == usersDO) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<GrantedAuthority> authList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_admin");
        return new User(usersDO.getUsername(), passwordEncoder.encode(usersDO.getPassword()), authList);
    }
}
