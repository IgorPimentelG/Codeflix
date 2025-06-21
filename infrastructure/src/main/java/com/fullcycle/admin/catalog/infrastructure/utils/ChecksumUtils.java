package com.fullcycle.admin.catalog.infrastructure.utils;

import com.google.common.hash.Hashing;

public final class ChecksumUtils {

	private ChecksumUtils() {}

	public static String generate(final byte[] bytes) {
		final int hash = Hashing.crc32c().hashBytes(bytes).asInt();
		return Integer.toHexString(hash);
	}
}
