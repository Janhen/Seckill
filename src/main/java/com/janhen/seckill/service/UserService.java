package com.janhen.seckill.service;

import com.janhen.seckill.dao.UserMapper;
import com.janhen.seckill.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.janhen.seckill.dao.UserMapper;
import com.janhen.seckill.pojo.User;


@Service
public class UserService {
	
	@Autowired
    UserMapper userMapper;
	
	public User getById(int id) {
		return userMapper.getById(id);
	}
	

	@Transactional
	public boolean tx() {
		User user1 = new User(11, "111111");
		userMapper.insert(user1);
		
		User user2 = new User(22, "222222");
		userMapper.insert(user2);
		
		return true;
	}
	
	

}
