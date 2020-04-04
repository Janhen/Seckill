package com.janhen.seckill.common.exeception;

import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  public ResultVO<String> exceptionHandler(HttpServletRequest request,
                                           Exception e) {
    // handle JSR303 exception ALSO can handle by using BindingResult as param ....
    if (e instanceof BindException) {
      BindException ex = (BindException) e;
      List<ObjectError> errors = ex.getAllErrors();
      ObjectError error = errors.get(0);
      String msg = error.getDefaultMessage();
      log.info("【绑定异常】绑定错误信息: {}", msg);
      return ResultVO.error(ResultEnum.BIND_ERROR.getCode(), String.format(ResultEnum.BIND_ERROR.getMsg(), msg));
    } else if (e instanceof SeckillException) {
      SeckillException ex = (SeckillException) e;
      log.info("【秒杀异常】信息...", e);
      return ResultVO.error(ex.getCode(), ex.getMessage());
    } else {
      return ResultVO.error(ResultEnum.SERVER_ERROR);
    }
  }
}
