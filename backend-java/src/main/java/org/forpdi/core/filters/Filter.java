package org.forpdi.core.filters;

import java.util.List;

public interface Filter<T> {
	public List<T> depurate(List<T> list);
}
