package idorm.idormServer.common.exception;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		log.error("Method name : {}, param Count : {}\n\nException Cause -{}", method.getName(), params.length,
			ex.getMessage());
	}
}