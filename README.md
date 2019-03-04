# seckill
技术：SpringBoot、MyBatis、Redis、RabbitMQ、Thymeleaf、Lombok

功能：
- 密码两次MD5加密、JSR303 参数校验
- 分布式Session

高并发优化:
- 页面优化: 页面(html)缓存、页面静态化缓存到客户端
- 安全优化: 秒杀地址隐藏、验证码验证、接口限流
- 接口优化: 验证码防刷、Redis预减库存、内存标记减少Redis访问、RabbitMQ异步下单





获取秒杀路径:   

![](pic/seckill-path.jpg)



执行秒杀：  

![](pic/seckill.jpg)







