package com.janhen.seckill.exeception;

import com.janhen.seckill.result.CodeMsg;
import com.janhen.seckill.result.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

	private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value = Exception.class)
	public ResultVO<String> exceptionHandler(HttpServletRequest request,
                                             Exception e) {
		e.printStackTrace();
		
		if (e instanceof SeckillException) {
			SeckillException ex = (SeckillException) e;
			return ResultVO.error(ex.getCodeMsg());
		} else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return ResultVO.error(CodeMsg.BIND_ERROR.fillArgs(msg));
		} else {
			// take exception message from stack trace
			return ResultVO.error(CodeMsg.SERVER_ERROR);
		}
	}
}
