package com.janhen.seckill.util;

import com.alibaba.fastjson.JSON;
import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.ResultVO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Slf4j
public class WebUtil {

	/**
	 * render message as response
	 * @param response
	 * @param obj
	 */
	public static void render(HttpServletResponse response, Object obj) {
		response.setContentType("application/json;charset=UTF-8");

		try (OutputStream out = response.getOutputStream()) {
			String str = "";
			if (obj instanceof ResultEnum) {
				ResultEnum resultEnum = (ResultEnum) obj;
				str = JSON.toJSONString(ResultVO.error(resultEnum));
			} else {
				str = JSON.toJSONString(obj);
			}
			
			out.write(str.getBytes("UTF-8"));
			out.flush();
		} catch (Exception e) {
			log.error("【参数返回】解析异常", e);
		}
	}
}
