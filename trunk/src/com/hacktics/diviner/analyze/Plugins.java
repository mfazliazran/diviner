package com.hacktics.diviner.analyze;

public class Plugins {

	//Plugins
	private static boolean isReflectionEnabled = false;
	private static boolean isDiffEnabled = false;
	private static boolean isExceptionEnabled = false;
	
	//Token Generation Mode
	private static boolean isValidTokenEnabled = false;
	private static boolean isinValidTokenEnabled = false;
	private static boolean isRandomTokenEnabled = false;

	//Token Append/Replace
	private static boolean isReplaceMode = false;
	private static boolean isAppendMode = false;
	
	public static boolean isReflectionEnabled() {
		return isReflectionEnabled;
	}
	public static void setReflectionEnabled(boolean isReflectionEnabled) {
		Plugins.isReflectionEnabled = isReflectionEnabled;
	}
	public static boolean isDiffEnabled() {
		return isDiffEnabled;
	}
	public static void setDiffEnabled(boolean isDiffEnabled) {
		Plugins.isDiffEnabled = isDiffEnabled;
	}
	public static boolean isExceptionEnabled() {
		return isExceptionEnabled;
	}
	public static void setExceptionEnabled(boolean isExceptionEnabled) {
		Plugins.isExceptionEnabled = isExceptionEnabled;
	}
	public static boolean isReplaceMode() {
		return isReplaceMode;
	}
	public static void setReplaceMode(boolean isReplaceMode) {
		Plugins.isReplaceMode = isReplaceMode;
	}
	public static boolean isAppendMode() {
		return isAppendMode;
	}
	public static void setAppendMode(boolean isAppendMode) {
		Plugins.isAppendMode = isAppendMode;
	}
	public static boolean isValidTokenEnabled() {
		return isValidTokenEnabled;
	}
	public static void setValidTokenEnabled(boolean isValidTokenEnabled) {
		Plugins.isValidTokenEnabled = isValidTokenEnabled;
	}
	public static boolean isInvalidTokenEnabled() {
		return isinValidTokenEnabled;
	}
	public static void setinValidTokenEnabled(boolean isinValidTokenEnabled) {
		Plugins.isinValidTokenEnabled = isinValidTokenEnabled;
	}
	public static boolean isRandomTokenEnabled() {
		return isRandomTokenEnabled;
	}
	public static void setRandomTokenEnabled(boolean isRandomTokenEnabled) {
		Plugins.isRandomTokenEnabled = isRandomTokenEnabled;
	}
	
	
	
}
