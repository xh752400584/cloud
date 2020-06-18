package com.xh.cloud.mapper;

import com.xh.cloud.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where id=#{id}")
    public User getUserById(Integer id);

    @Delete("delete  from user where id=#{id}")
    public int deleteUserById(Integer id);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user (name) values(#{name})")
    public int insertUser(User user);

    @Update("update user set name=#{name} where id =#{id}")
    public int updateUser(User user);
}
