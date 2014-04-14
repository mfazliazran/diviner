package com.hacktics.diviner.gui.controllers;

public interface PayloadInterface {

	public static int BLINK_INTERVAL = 100;

	//See if the item's platforms match the selected platform
	public void checkPlatform(String platform);
	

	public void unsetRecommended();
	public void setRecommended();
}
