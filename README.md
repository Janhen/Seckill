上线: 
[地址](https://seckill.janh80.com/user/to_login)
```aidl
账号: 
18852860001
18852860002
18852860003
18852860005
18852860004

密码: 123456
```

说明:   
对任意秒杀，接口请求限制为 15s 2次;  



# seckill
技术：Mybatis,SpringBoot, Redis, RabbitMQ, Thymeleaf  
描述：商城中特定商品的秒杀，指定时间开启和关闭，通过 JMeter 进行测试高并发下的性能，并进行优化。    

- 前后台两次加密保障用户数据安全，使用 Redis 实现分布式 Session 管理用户信息，构建用户中心；
- 借助 Redis Cache，页面静态化配合 CDN 加速实现各级缓存，有效加快用户访问速度，减少响应时间；
- 通过验证码、动态 URL 配合限流算法来减少并发量，有效减少服务器处理开销；
- 使用 RabbitMQ 处理写 DB 请求，从而异步化获得更好的响应延迟、流量削锋减轻 DB 负载；
- Redis 预减库存，内存标记进一步优化处理逻辑，减轻 Server，Redis 负载；
- 通过模板方法设计通用 Redis Key，封装高复用的响应实体，提供两种秒杀策略；


## 通用部分
**访问拦截：**
![](pic/accessInteceptor.jpg)

**分布式 Session:**  
在拦截器中从 Redis 中获取放入到 ThreadLocal  
经过参数拦截器注入到方法参数中，简化获取用户信息  


**限流策略:**  
对指定用户某个访问接口的控制  


## 业务部分
**登录**
![](pic/login.jpg)

**获取秒杀路径:**   
![](pic/seckill-path.jpg)

**执行秒杀：**  
![](pic/seckill.jpg)

