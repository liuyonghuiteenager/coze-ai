package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getAccount, account);
        User user = userDao.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("账号不存在");
        }
        // 根据数据库 status 控制是否启用
        boolean enabled = user.getStatus() == 1;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(
                user.getAccount(),
                user.getPassword(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                AuthorityUtils.NO_AUTHORITIES  // 后续可扩展角色权限
        );
    }
}
