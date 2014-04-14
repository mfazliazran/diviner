package com.hacktics.diviner.gui.controllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.payloads.constants.XMLConstants;

public class PayloadRadioButton extends ComparableRadioButton implements PayloadInterface{


	private static final long serialVersionUID = 1L;
	private static final Color defualtColor = Color.BLACK;
	private static final Color recommendedColor = Color.RED;
	private String [] platforms;
	private int blinkCounter;
	private Timer blinkTimer;
	private String textPayload;
	
	public PayloadRadioButton(String title, String text, String[] platforms) {
		super("<html><b>" + title + "</b> ( " + AnalyzerUtils.encodeHTML(text) + " )</html>");
		textPayload = text;
		this.platforms = platforms;
		addBlinkingFeature();
	}

	//Sets the text as recommended for the selected platform
	@Override
	public void checkPlatform(String platform) {


		if (platform.equalsIgnoreCase(XMLConstants.ALL_PLATFORMS)) {
			setRecommended();
			return;
		}

		if (platform.equalsIgnoreCase(XMLConstants.GENERAL_PLATFORM)) {
			unsetRecommended();
			return;
		}
		for (String platformName : platforms) {
			if (platformName.equals(platform)) {
				setRecommended();
				return;
			}
		}

		unsetRecommended();
	}

	@Override
	public void setRecommended() {
		blinkCounter = 0;
		blinkTimer.start();
	}

	@Override
	public void unsetRecommended() {
		setForeground(defualtColor);
		repaint();
	}

	private void addBlinkingFeature() {

		blinkTimer = new Timer(BLINK_INTERVAL , new ActionListener() {


			public void actionPerformed(ActionEvent evt) {
				if (blinkCounter < 9)
				{
					if (blinkCounter % 2 == 0)
					{
						setForeground(recommendedColor);
					}
					else
					{
						setForeground(defualtColor);
					}

					blinkCounter++;
				}	

				else	//stop blinking 
				{
					stopTimer();
				}
			}});

	}

	private void stopTimer(){
		blinkTimer.stop();
	}

	
	public String getPayloadText() {
		return textPayload;
	}


}
