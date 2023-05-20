package com.learning.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learning.entity.UsersDO;
import org.springframework.stereotype.Repository;

/**
 * @ClassName UsersMapper
 * @Description TODO
 * @Author hufei
 * @Date 2023/5/4 19:30
 * @Version 1.0
 */
@Repository
public interface UsersDao extends BaseMapper<UsersDO> {
}
