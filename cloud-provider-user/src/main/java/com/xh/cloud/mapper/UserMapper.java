package com.xh.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xh.cloud.entity.po.UserPO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
    @Select("select * from user where id=#{id}")
    public UserPO getUserById(Integer id);

    @Delete("delete  from user where id=#{id}")
    public int deleteUserById(Integer id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into user (name) values(#{name})")
    public int insertUser(UserPO user);

    @Update("update user set name=#{name} where id =#{id}")
    public int updateUser(UserPO user);
}
