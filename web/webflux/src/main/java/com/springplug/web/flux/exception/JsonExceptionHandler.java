package com.springplug.web.flux.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonExceptionHandler implements ErrorWebExceptionHandler {

	/**
	 * MessageReader
	 */
	private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

	/**
	 * MessageWriter
	 */
	private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

	/**
	 * ViewResolvers
	 */
	private List<ViewResolver> viewResolvers = Collections.emptyList();

	/**
	 * 存储处理异常后的信息
	 */
	private ThreadLocal<Map<String, Object>> exceptionHandlerResult = new ThreadLocal<>();

	/**
	 * 参考AbstractErrorWebExceptionHandler
	 */
	public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
		Assert.notNull(messageReaders, "'messageReaders' must not be null");
		this.messageReaders = messageReaders;
	}

	/**
	 * 参考AbstractErrorWebExceptionHandler
	 */
	public void setViewResolvers(List<ViewResolver> viewResolvers) {
		this.viewResolvers = viewResolvers;
	}

	/**
	 * 参考AbstractErrorWebExceptionHandler
	 */
	public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
		Assert.notNull(messageWriters, "'messageWriters' must not be null");
		this.messageWriters = messageWriters;
	}

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable error) {
		// 按照异常类型进行处理
		HttpStatus httpStatus;
		String body;

		if (error instanceof MethodNotAllowedException) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			body="请求方式错误";
		}else if(error instanceof ResponseStatusException) {
			httpStatus = HttpStatus.NOT_FOUND;
			body = "地址不存在";
		}else{
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			body = "服务器异常";
		}
		// 封装响应体,此body可修改为自己的jsonBody
		Map<String, Object> result = new HashMap<>(2, 1);
		result.put("httpStatus", HttpStatus.OK);

		String msg = "{\"code\":" + httpStatus.value() + ",\"msg\": \"" + body + "\"}";
		result.put("body", msg);
		// 参考AbstractErrorWebExceptionHandler
		if (exchange.getResponse().isCommitted()) {
			return Mono.error(error);
		}
		exceptionHandlerResult.set(result);
		ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
				.switchIfEmpty(Mono.error(error)).flatMap((handler) -> handler.handle(newRequest))
				.flatMap((response) -> write(exchange, response));
	}

	/**
	 * 参考DefaultErrorWebExceptionHandler
	 */
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Map<String, Object> result = exceptionHandlerResult.get();
		return ServerResponse.status((HttpStatus) result.get("httpStatus")).contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(result.get("body")));
	}

	/**
	 * 参考AbstractErrorWebExceptionHandler
	 */
	private Mono<? extends Void> write(ServerWebExchange exchange, ServerResponse response) {
		exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
		return response.writeTo(exchange, new ResponseContext());
	}

	/**
	 * 参考AbstractErrorWebExceptionHandler
	 */
	private class ResponseContext implements ServerResponse.Context {

		@Override
		public List<HttpMessageWriter<?>> messageWriters() {
			return JsonExceptionHandler.this.messageWriters;
		}

		@Override
		public List<ViewResolver> viewResolvers() {
			return JsonExceptionHandler.this.viewResolvers;
		}

	}

}
