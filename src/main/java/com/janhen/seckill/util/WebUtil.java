package com.janhen.seckill.util;

import com.alibaba.fastjson.JSON;
import com.janhen.seckill.common.ResultEnum;
import com.janhen.seckill.common.ResultVO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

@Slf4j
public class WebUtil {

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

	public static void renderWriter(HttpServletResponse response, Object obj) {
		// 重置，并数据协商
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(JSONUtil.beanToString(obj));
			// out.write(JSONUtil.beanToString(obj));
			out.flush();
		} catch (Exception e) {
			log.error("【参数返回】", e);
		} finally {
			out.close();
		}
	}
}
