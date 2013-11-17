package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.hacktics.diviner.database.CodeLine;

/**
 * 
 * @author Eran Tamari
 *
 */

public class CodeDispalyFrame extends JDialog implements ActionListener{

	
	private static final long serialVersionUID = 4767631352874992754L;
	private static final String ASPNET = "ASP.NET";
	private static final String JSP = "JSP";
	private static final String ASP = "ASP";
	private static final String DEEP_ANALYSIS = "Complete Analysis";
	private static final String SHOW_PATH = "Show Decision Path";

	public CodeDispalyFrame(String pageName, ArrayList<CodeLine> codeLines)  {
		super(Diviner.getMainFrame(),"Clairvoyance - source code divination", ModalityType.DOCUMENT_MODAL);
		setSize(1000,600);
		setLayout(new BorderLayout());
		setIconImage(GuiUtils.getGuiUtils().getClairvoyanceIcon());

		JButton jspButton = new JButton(JSP);
		JButton ASPButton = new JButton(ASP);
		JButton CSharpButton = new JButton(ASPNET);

		ASPButton.setEnabled(false);
		CSharpButton.setEnabled(false);
		
		jspButton.addActionListener(this);
		ASPButton.addActionListener(this);
		CSharpButton.addActionListener(this);

		JPanel topPanel = new JPanel();
		topPanel.add(jspButton);
		topPanel.add(ASPButton);
		topPanel.add(CSharpButton);
		topPanel.setPreferredSize(new Dimension(100,100));
		add(topPanel, BorderLayout.NORTH);

		JPanel sidePanel = new JPanel(new GridLayout(2, 1, 5, 5));
		JButton deepAnalysisButton = new JButton(DEEP_ANALYSIS);
		JButton showPathButton = new JButton(SHOW_PATH);
		sidePanel.add(deepAnalysisButton);
		sidePanel.add(showPathButton);
		add(sidePanel, BorderLayout.EAST);

		deepAnalysisButton.setEnabled(false);
		showPathButton.setEnabled(false);
		
		CodePanel codePanel = new CodePanel(pageName, codeLines);
		add(codePanel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {

		case JSP:
			System.out.println("bla");
			break;

		case ASP:
			System.out.println("bla");
			break;

		case ASPNET:
			System.out.println("bla");
			break;
		}
	}






}
