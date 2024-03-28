package idorm.idormServer.common.performance;

import java.util.List;

public class QueryCounter {

	private final long time;
	private List<String> queries;
	private int count;

	public QueryCounter(final int count, final long time, final List<String> queries) {
		this.time = time;
		this.queries = queries;
		this.count = count;
	}

	public void addQueryCounting(String sql) {
		queries.add(sql);
		count++;
	}

	public String getResult() {
		String result = String.join("\n", queries);
		return result + NPlusOneWarning.getWarningMessage(queries);
	}

	public long getTime() {
		return time;
	}

	public int getCount() {
		return count;
	}
}
