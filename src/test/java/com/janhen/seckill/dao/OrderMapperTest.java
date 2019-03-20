package com.janhen.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMapperTest {

    @Autowired
    OrderMapper orderMapper;

    @Test
    public void selectSeckillOrderByUserIdAndGoodsId() {
    }

    @Test
    public void selectSeckillCountByUserIdAndGoodsId() {
    }

    @Test
    public void insertOrderInfo() {
    }

    @Test
    public void insertSeckillOrder() {
    }

    @Test
    public void selectOrderInfoById() {
    }
}