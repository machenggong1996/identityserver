package com.beyondsoft.identityserver.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ManageMapper {
    @Insert("insert into users (username,password,enabled) values ( #{username},#{password},1)")
    void insertUser(@Param("username") String username, @Param("password") String password);

    @Insert("insert into authorities (username,authority) values ( #{username},#{authority})")
    void insertAuthority(@Param("username") String username, @Param("authority") String authority);

    @Update("update users set password = #{password} where username = #{username}")
    int updateUserPassword(@Param("username") String userName, @Param("password") String password);

    @Select("select password from users where username = #{username}")
    String getPasswordByUserName(@Param("username") String userName);

    @Select("SELECT authority FROM authorities WHERE username = #{username} ")
    List<String> listUserRolesByUserName(@Param("username") String userName);

    @Delete("delete from users where username = #{userName}")
    int deleteUserByUserName(@Param("userName") String userName);

    @Delete("delete from authorities where username = #{userName}")
    int deleteAuthoritiesByUserName(@Param("userName") String userName);

    @Select("SELECT count(1) FROM users WHERE username = #{username}")
    Integer getByUserName(@Param("username") String userName);
}
