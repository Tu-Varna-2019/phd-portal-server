package com.tuvarna.phd.utils;

import java.util.Random;

public class Generator {
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public static String generateRandomString(int length) {
		return new Random().ints(length, 0, CHARACTERS.length())
				.mapToObj(CHARACTERS::charAt)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();
	};

}
