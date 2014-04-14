package com.hacktics.diviner.gui.scanwizard;

public class ExceptionConstants {

	private static final Object [] [] payloads = {{ ";" }, { "'" }};
	private static final Object [] [] responseRegex = {{"error"}, {"exception"}, {"unhandled"}, {"JasperException"}};
	
	public static Object[][] getPayloads() {
		return payloads;
	}
	public static Object[][] getResponseRegex() {
		return responseRegex;
	}
	
	

}
