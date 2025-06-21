package com.fullcycle.admin.catalog.infrastructure.utils;

public final class SQLUtils {

	private SQLUtils() {}

	public static String upper(final String term) {
		if (term == null) {
			return null;
		}
		return term.toUpperCase();
	}

	public static String like(String term) {
		if (term == null) {
			return null;
		}
		return "%" + term.toUpperCase() + "%";
	}
}
