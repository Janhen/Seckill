package com.janhen.seckill.dao;

import com.janhen.seckill.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
  String TABLE_NAME = " user ";

  @Select({"SELECT * FROM ", TABLE_NAME, " WHERE id = #{id}"})
  User getById(@Param("id") int id);

  @Insert({"INSERT INTO ", TABLE_NAME, " (id, name) VALUES(#{id}, #{name})"})
  int insert(User user);
}
