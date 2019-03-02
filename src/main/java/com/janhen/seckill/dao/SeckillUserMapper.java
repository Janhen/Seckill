package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeckillUserMapper {

    @Select("select * from seckill_user where id = #{id}")
    SeckillUser getById(@Param("id") long id);

    @Update("update seckill_user set password = #{password} where id = #{id}")
    void updatePasswordById(@Param("password") String dbPass, @Param("id") long id);


}
