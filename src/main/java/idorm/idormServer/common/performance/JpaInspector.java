package idorm.idormServer.common.performance;

import java.util.ArrayList;
import java.util.Objects;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Profile("local")
@Slf4j
@Component
public class JpaInspector implements StatementInspector {

	private static final ThreadLocal<QueryCounter> queryCounters = new ThreadLocal<>();

	void start() {
		queryCounters.set(new QueryCounter(0, System.currentTimeMillis(), new ArrayList<>()));
	}

	void clear() {
		queryCounters.remove();
	}

	@Override
	public String inspect(final String sql) {
		log.info("sql = " + sql);
		QueryCounter queryCounter = queryCounters.get();
		if (Objects.nonNull(queryCounter)) {
			queryCounter.addQueryCounting(sql);
		}
		return sql;
	}

	public String getQueriesResult() {
		QueryCounter queryCounter = queryCounters.get();
		return "COUNT : " + queryCounter.getCount() + "\n" + queryCounter.getResult();
	}

	public long getDuration(final long currentTimeMillis) {
		QueryCounter queryCounter = queryCounters.get();
		return currentTimeMillis - queryCounter.getTime();
	}
}
