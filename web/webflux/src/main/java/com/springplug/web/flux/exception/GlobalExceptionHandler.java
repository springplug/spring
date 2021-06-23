package com.springplug.web.flux.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String,Object> baseErrorHandler(Exception e){
		e.printStackTrace();
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",500);
		m.put("msg","服务器异常");
	    return m;
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseBody
	public Map<String,Object> illegalArgumentErrorHandler(Exception e){
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",500);
		m.put("msg",e.getMessage());
	    return m;
	}

	@ExceptionHandler(value = WebExchangeBindException.class)
	@ResponseBody
	public Map<String,Object> bindErrorHandler(WebExchangeBindException e){
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",400);
		if (null != e.getFieldErrors() && e.getFieldErrors().get(0).getCode().equals("typeMismatch")) {
			m.put("msg", "字段["+e.getFieldErrors().get(0).getField()+"]类型不正确");
		}else {
			m.put("msg", e.getAllErrors().get(0).getDefaultMessage());
		}
		return m;
	}
}
