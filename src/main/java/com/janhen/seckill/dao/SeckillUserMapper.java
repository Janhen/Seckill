package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeckillUserMapper {

    @Select("SELECT * FROM seckill_user WHERE id = #{id}")
    SeckillUser getById(@Param("id") long id);

    @Update("UPDATE seckill_user SET password = #{password} WHERE id = #{id}")
    void updatePasswordById(@Param("password") String dbPass, @Param("id") long id);
}
