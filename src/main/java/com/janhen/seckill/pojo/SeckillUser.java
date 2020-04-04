package com.janhen.seckill.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SeckillUser {
  private Long id;
  private String nickname;
  private String password;
  private String salt;
  private String head;
  private Date registerDate;
  private Date lastLoginDate;
  private Integer loginCount;
}
