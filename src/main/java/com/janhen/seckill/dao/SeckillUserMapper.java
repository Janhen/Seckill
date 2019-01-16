package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeckillUserMapper {

    @Select("select * from miaosha_user where id = #{id}")
    public SeckillUser getById(@Param("id") long id);

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void updatePasswordById(@Param("password") String dbPass, @Param("id") long id);


}
