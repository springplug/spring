package com.springplug.web.mvc.exception.handler;

import com.springplug.common.util.string.StringUtils;
import com.springplug.web.mvc.exception.BaseException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String,Object> baseErrorHandler(Exception e) throws Exception {
//		ExceptionResult.recordLog(ErrorCode.error_by_unknown, e.getMessage(), "info", e);
		e.printStackTrace();
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",500);
		m.put("msg","服务器异常");
		return m;
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public Map<String,Object> MethodArgumentTypeMismatchExceptionExceptionHandler(MethodArgumentTypeMismatchException e) throws Exception {
//		ExceptionResult.recordLog(ErrorCode.error_by_unknown, e.getMessage(), "info", e);
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",500);
		m.put("msg","参数错误");
		return m;
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public Map<String,Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) throws Exception {
//		ExceptionResult.recordLog(ErrorCode.error_by_unknown, e.getMessage(), "info", e);
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",500);
		m.put("msg","请求方式不正确");
		return m;
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseBody
	public Map<String,Object> constraintViolationExceptionHandler(ConstraintViolationException e) throws Exception {
//		ExceptionResult.recordLog(ErrorCode.error_by_unknown, e.getMessage(), "info", e);
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",500);
		m.put("msg",e.getConstraintViolations().iterator().next().getMessage());
		return m;
	}

	@ExceptionHandler(value = BindException.class)
	@ResponseBody
	public Map<String,Object> bindErrorHandler(BindException e) throws Exception {
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",400);
		if (null != e.getFieldErrors() && e.getFieldErrors().get(0).getCode().equals("typeMismatch")) {
			m.put("msg", "字段["+e.getFieldErrors().get(0).getField()+"]类型不正确");
		}else {
			m.put("msg", StringUtils.isContainChinese(e.getAllErrors().get(0).getDefaultMessage()) ? e.getAllErrors().get(0).getDefaultMessage() : "参数错误");
		}
		return m;
	}

	@ExceptionHandler(value = BaseException.class)
	@ResponseBody
	public Map<String,Object> businessExceptionHandler(BaseException e) throws Exception {
		// 设置httpStatusCode
//		HttpServletResponse response = ServletUtils.getResponse();
//		response.setStatus(e.getCode());
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",e.getCode());
		m.put("msg",e.getMessage());
		m.put("data",e.getData());
		return m;
	}

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	@ResponseBody
	public Map<String,Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) throws Exception {
		Map<String,Object> m=new HashMap<String, Object>();
		m.put("code",500);
		m.put("msg","参数错误");
		m.put("data",null);
		return m;
	}

}
