package org.forpdi.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {

	public static List<Long> stringListToLongList(String strList) {
		if (strList == null) {
			return Collections.emptyList();
		}
		String[] split = strList.split(",");
		List<Long> longList = new ArrayList<>(split.length);
		for (String str : split) {
			longList.add(Long.parseLong(str));
		}
		return longList;
	}
}
