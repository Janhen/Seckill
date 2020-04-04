package com.janhen.seckill.util;

public class PatternContants {
  /** 4位代码正则表达式 */
  public static final String CODE_PATTERN_4 = "^[0-9a-zA-Z]{4}$";

  /** 30位代码正则表达式 */
  public static final String CODE_PATTERN_30 = "^[0-9a-zA-Z]{1,30}$";

  /** BigDecimal 格式校验 [12,4] */
  public static final String DECIMAL_PATTERN_12_4 = "^\\d{0,8}(\\.){0,1}\\d{0,4}$";

  /** 手机号正则表达式 */
  public static final String PHONE_PATTERN = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
}
