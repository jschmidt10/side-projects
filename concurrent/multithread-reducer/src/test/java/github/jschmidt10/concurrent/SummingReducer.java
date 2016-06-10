package github.jschmidt10.concurrent;

public class SummingReducer extends MultithreadReducer<String, Long> {
	@Override
	public Long reduce(String key, Long accumulator, Long value) {
		return accumulator + value;
	}
}
