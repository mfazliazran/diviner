package com.hacktics.diviner.gui.controllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Timer;

/**
 * 
 * @author Eran Tamari
 *
 */

public class BlinkingButton extends JButton{
	
	private static final long serialVersionUID = 2888643462656531706L;
	private int blinkCounter;
	private Timer blinkTimer;
	private Color defaultColor;
	private Color blinkingColor;
	private static int BLINK_INTERVAL = 100;
	
	//Constructor
	public BlinkingButton(Color i_defaultColor, Color i_blinkingColor , String text) {
		 
		setText(text);
		blinkingColor = i_blinkingColor;
		defaultColor = i_defaultColor;
		setBackground(defaultColor);
		AddBlinkEvent();
	}
	
	public void Blink()	{
		blinkCounter = 0;
		blinkTimer.start();
	}
	
	private void AddBlinkEvent(){
		
		blinkTimer = new Timer(BLINK_INTERVAL , new ActionListener() {
			
			 public void actionPerformed(ActionEvent evt) {
				 if (blinkCounter < 8)
				 {
			    	if (blinkCounter % 2 == 0)
			    	{
			    	setBackground(defaultColor);
			    	}
			    	else
			    	{
			    	setBackground(blinkingColor);
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
	
	public void setDefualtColor(Color color) {
		defaultColor = color;
		setBackground(defaultColor);
	}
	
	public void RestoreDefaultColor() {
		stopTimer();
		setBackground(defaultColor);
	}
	
	/**
	 * If the parameter is selected (color == blinking color)
	 */
	public boolean isSelected() {
		return isFocusOwner();
	}
	
	
}
