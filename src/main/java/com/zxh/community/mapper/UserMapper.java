package com.zxh.community.mapper;

import com.zxh.community.module.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zxh
 * @create 2021-12-11-14:18
 */
@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified) values(#{name},#{account_id},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}