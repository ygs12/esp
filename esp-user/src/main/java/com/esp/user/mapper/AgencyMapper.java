package com.esp.user.mapper;

import com.esp.user.common.mapper.UserBaseMapper;
import com.esp.user.domain.Agency;
import com.esp.user.domain.User;
import com.esp.user.model.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName: user-service
 * @Auther: GERRY
 * @Date: 2018/11/17 17:00
 * @Description:
 */
@Mapper
public interface AgencyMapper extends UserBaseMapper<Agency> {
    List<User> selectAgent(@Param("user") User user, @Param("pageParams") PageParams pageParams);

    Long selectAgentCount(@Param("user") User user);
}
