package com.janhen.seckill.service;

import com.janhen.seckill.dao.UserMapper;
import com.janhen.seckill.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  @Autowired
  private UserMapper userMapper;

  public User getById(int id) {
    return userMapper.getById(id);
  }
}
