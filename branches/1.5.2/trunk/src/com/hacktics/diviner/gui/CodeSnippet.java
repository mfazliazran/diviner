package com.hacktics.diviner.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

/**
 * 
 * @author Eran Tamari
 *
 */

public class CodeSnippet extends JPanel{

	private static final long serialVersionUID = -8977473580506458694L;
	private String code;
	private static Color HIGH_PROB = Color.GREEN;
	private static Color MIDDLE_PLUS_PROB = Color.ORANGE;
	private static Color MIDDLE_MINUS_PROB = new Color(139, 125, 107);
	private static Color LOW_PROB = Color.RED;
	private JButton probButton;
	private static final Border SNIPPET_BORDER = new CompoundBorder(new EtchedBorder(),new LineBorder(Color.black , 1 , false));
	private static final int EXCELLENT_CHANCE = 95;
	private static final int VERY_GOOD_CHANCE = 90;
	private static final int GOOD_CHANCE = 80;
	private static final int ALMOST_GOOD_CHANCE = 70;
	private static final int FINE_CHANCE = 60;
	
	private static final int NOT_GOOD_CHANCE = 40;
	private static final int BAD_CHANCE = 20;
	
	public CodeSnippet(String code, int probability) {
		this.code = code;
		probButton = new JButton(Integer.toString(probability) + "%");
		Color probColor;
		if (probability >= VERY_GOOD_CHANCE) {
			if (probability >= EXCELLENT_CHANCE) {
				probColor = HIGH_PROB.darker();	
			}
			else {
				probColor = HIGH_PROB.brighter();
			}
		}
		
		else if (probability >= ALMOST_GOOD_CHANCE) {
			if (probability >= GOOD_CHANCE) {
				probColor =  MIDDLE_PLUS_PROB.brighter();
			}
			else {
				probColor =  MIDDLE_PLUS_PROB.darker();
			}
			
		}
		else if (probability >= NOT_GOOD_CHANCE) {
			if (probability >= FINE_CHANCE) {
				probColor = MIDDLE_MINUS_PROB.brighter();
			}
			else {
				probColor = MIDDLE_MINUS_PROB.darker();
			}
		}
		else {
			if (probability >= BAD_CHANCE) {
				probColor = LOW_PROB.brighter();	
			}
			else {
				probColor = LOW_PROB.darker();	
			}
		}
		probButton.setBackground(probColor);
		

		JLabel codeText = new JLabel(this.code);
		codeText.setFont(new Font("David", Font.PLAIN, 15));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(probButton);
		this.add(codeText);
		this.setBorder(SNIPPET_BORDER);
	}

	public JButton getProbButton() {
		return probButton;
	}

	public void setProbButton(JButton probButton) {
		this.probButton = probButton;
	}

}
