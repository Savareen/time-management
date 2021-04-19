package com.igorzaitcev.management.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataLoader {
	public static <T> List<T> loadData(Function<String, T> mapper, String fileName) {
		return FileUtils.getStream(fileName).map(mapper).collect(Collectors.toList());
	}
}
