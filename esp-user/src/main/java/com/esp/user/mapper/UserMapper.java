package com.esp.user.mapper;

import com.esp.user.common.mapper.UserBaseMapper;
import com.esp.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ProjectName: microservice-house
 * @Auther: GERRY
 * @Date: 2018/11/12 20:12
 * @Description:
 */
@Mapper
public interface UserMapper extends UserBaseMapper<User> {
    void updateUser(User user);
}
