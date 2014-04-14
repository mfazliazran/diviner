package com.hacktics.diviner.gui.controllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.Timer;

import com.hacktics.diviner.payloads.constants.XMLConstants;

public class PayloadButton extends JButton implements PayloadInterface, Comparable<AbstractButton>{

	private static final long serialVersionUID = 7520391257328783040L;
	private String []platforms;
	private static final Color defualtColor = Color.BLACK;
	private static final Color recommendedColor = Color.RED;
	private int blinkCounter;
	private Timer blinkTimer;
	private String sourceChar;
	private String targetChar;
	
	public PayloadButton(String text, String [] platforms, String sourceChar, String targetChar) {
		super(text);
		this.platforms = platforms;
		addBlinkingFeature();
		this.sourceChar = sourceChar;
		this.targetChar = targetChar;
	}
	

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
	
	
	@Override
	public void unsetRecommended() {
		setForeground(defualtColor);
		repaint();
	}


	public String getSourceChar() {
		return sourceChar;
	}


	public void setSourceChar(String source) {
		this.sourceChar = source;
	}


	public String getTargetChar() {
		return targetChar;
	}


	public void setTargetChar(String target) {
		this.targetChar = target;
	}


	@Override
	public int compareTo(AbstractButton o) {
		return this.getText().compareTo(o.getText());
	}


}
