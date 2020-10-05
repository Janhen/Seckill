package com.janhen.seckill.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MD5UtilTest {

  @Test
  public void md5() {
    System.out.println(MD5Util.inputPassToDBPass("ddd", "1a2b3c"));
  }
}
