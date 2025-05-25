package com.fullcycle.admin.catalog.infrastructure.utils;

public final class ChecksumUtils {

	private ChecksumUtils() {}

	public static String generate(final byte[] bytes) {
		int sum = 0;
		for (byte b : bytes) {
			sum += b;
		}
		return Integer.toHexString(sum);
	}
}
