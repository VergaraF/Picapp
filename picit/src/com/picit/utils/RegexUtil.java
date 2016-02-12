package com.picit.utils;

import java.util.regex.Pattern;

public class RegexUtil {
	/**
	 * A username can only contain numbers and letters and must be between 2 and 16 characters*/
	private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]{2,16}$");
	
	/** A password can have any character but must be between 6 and 32 characters long
	 * */
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(".{6,32}$");
	
	/**
	 * An email can have any letter or -_. letter before the @ 
	 * then any letter then followed by a period 
	 * then the top down domain must be between 2 and 4 characters*/
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9-_.]+@[A-Za-z0-9]+\\.[A-Za-z]{2,4}$");

	/**
	 * Checks if the username matches a regex pattern
	 * 
	 * @param username the username to match the pattern against
	 * 
	 * @return returns true if the username matches the pattern
	 * */
	public static boolean isValidUsername(String username) {
		return USERNAME_PATTERN.matcher(username).find();
	}
	
	/**
	 * Checks if the password matches a regex pattern
	 * 
	 * @param password the password to match the pattern against
	 * 
	 * @return returns true if the password matches the pattern
	 * */
	public static boolean isValidPassword(String username) {
		return PASSWORD_PATTERN.matcher(username).find();
	}
	
	/**
	 * Checks if the email matches a regex pattern
	 * 
	 * @param email the email to match the pattern against
	 * 
	 * @return returns true if the email matches the pattern
	 * */
	public static boolean isValidEmail(String username) {
		return EMAIL_PATTERN.matcher(username).find();
	}
}
