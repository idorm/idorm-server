package idorm.idormServer.common.performance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile("local")
@Slf4j
@Component
@RequiredArgsConstructor
public class QueryCountInterceptor implements HandlerInterceptor {

	private final JpaInspector jpaInspector;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		jpaInspector.start();
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws Exception {

		if (isExceptionThrown(ex)) {
			log.info("Exception : {}", ex.getMessage());
			return;
		}
		long duration = jpaInspector.getDuration(System.currentTimeMillis());
		String result = generateSqlQueriesResult(request, duration, jpaInspector.getQueriesResult());
		jpaInspector.clear();
		log.info("\n===========RESULT==========\n{}", result);
	}

	private boolean isExceptionThrown(final Exception ex) {
		return ex != null;
	}

	private String generateSqlQueriesResult(final HttpServletRequest request, final long duration,
		final String queryResult) {
		String url = "URL: " + request.getMethod() + " " + request.getRequestURI() + "\n";
		String time = "TIME: " + duration + "\n";
		return url + time + queryResult;
	}
}
