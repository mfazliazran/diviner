package com.hacktics.diviner.gui;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.hacktics.diviner.database.CodeLine;

/**
 * 
 * @author Eran Tamari
 *
 */

public class CodePanel extends JScrollPane{

	private static final long serialVersionUID = 6800473343244681520L;

	public CodePanel(String pageName, ArrayList<CodeLine> codeLines) {
	
		int rows;
		if (codeLines.size() <= 10) {
		rows = 10;	
		}
		else {
			rows = codeLines.size();
		}
		JPanel codePanel = new JPanel(new GridLayout(rows, 1, 0, 0));		
		
		for (CodeLine sourceLine : codeLines) {
			codePanel.add(new CodeSnippet(sourceLine.getSourceCode(), sourceLine.getProbability()));
		}
		
		DivinerTitleBorder titleBorder = new DivinerTitleBorder(pageName);
		setBorder(titleBorder);
		setViewportView(codePanel);
	}

	
	

	
	
}
