package com.janhen.seckill.util;

import com.alibaba.fastjson.JSON;
import com.janhen.seckill.result.CodeMsg;
import com.janhen.seckill.result.ResultVO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

@Slf4j
public class WebUtil {

	/** 前后端分离格式 */
	public static void render(HttpServletResponse response, Object obj) {
		response.setContentType("application/json;charset=UTF-8");

		try (OutputStream out = response.getOutputStream()) {
			String str = "";
			if (obj instanceof CodeMsg) {
				CodeMsg codeMsg = (CodeMsg) obj;
				str = JSON.toJSONString(ResultVO.error(codeMsg));
			} else {
				str = JSON.toJSONString(obj);
			}
			
			out.write(str.getBytes("UTF-8"));
			out.flush();
		} catch (Exception e) {
			log.error("【参数返回】解析异常", e);
		}
	}

	/**
	 * 返回数据格式
	 * @param response
	 * @param obj
	 */
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


	/** json ==>> obj */
	public static <T> T stringToBean(String str, Class<T> clazz) {
		if (str == null || str.length() == 0 || clazz == null) {
			return null;
		}

		// int 和 long 基本类型进行特殊处理, 无法使用 json 工具转换
		if (clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(str);
		} else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(str);
		} else if (clazz == String.class) {
			return (T) str;
		} else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}

	/**  obj ==>> json  */
	public static <T> String beanToString(T value) {
		if (value == null) {
			return null;
		}

		Class<?> clazz = value.getClass();

		if (clazz == int.class || clazz == Integer.class) {
			return String.valueOf(value);
		} else if (clazz == long.class || clazz == Long.class) {
			return String.valueOf(value);
		} else if (clazz == String.class) {
			return (String) value;
		} else {
			return JSON.toJSONString(value);
		}
	}
	
	
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
