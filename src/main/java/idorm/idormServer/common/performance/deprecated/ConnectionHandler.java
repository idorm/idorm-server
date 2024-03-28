package idorm.idormServer.common.performance.deprecated;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ConnectionHandler implements InvocationHandler {

	private final Object target;
	private final QueryCounter counter;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		countPrepareStatement(method);
		logQueryCount(method);
		return method.invoke(target, args);
	}

	private void logQueryCount(Method method) {
		if (method.getName().equals("close")) {
			warnTooManyQuery();
			log.info("\n===================== QUERY COUNT = {} =====================", counter.getCount());
		}
	}

	private void countPrepareStatement(Method method) {
		if (method.getName().equals("prepareStatement")) {
			counter.increase();
		}
	}

	private void warnTooManyQuery() {
		if (counter.isWarn()) {
			log.warn("\n============================ TOO MANY QUERY ============================");
		}
	}
}