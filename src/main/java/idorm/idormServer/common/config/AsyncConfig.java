package idorm.idormServer.common.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import idorm.idormServer.common.exception.CustomAsyncUncaughtExceptionHandler;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

	private final int CORE_POOL_SIZE = 10;
	private final int MAX_POOL_SIZE = 50;
	private final int QUEUE_CAPACITY = 100;
	private final int KEEP_ALIVE_SECONDS = 60;

	@Override
	@Bean(name = "asyncExecutor")
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
		executor.setThreadNamePrefix("Async-");
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new CustomAsyncUncaughtExceptionHandler();
	}
}
