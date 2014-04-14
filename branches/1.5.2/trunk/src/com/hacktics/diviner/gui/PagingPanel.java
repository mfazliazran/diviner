package com.hacktics.diviner.gui;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hacktics.diviner.analyze.AnalyzerUtils;

public class PagingPanel extends JPanel{

	private boolean isScanResultsPaging = true;
	private int firstItemIndex;
	private int lastItemIndex;
	private int currPage;
	private JLabel lblHeader = null;
	private static String SCAN_HEADER = "";
	private static String REQUESTS_HEADER = "Requests Page";
	
	public PagingPanel(boolean isScanResultsPaging, JButton left, JButton right, int displayedNumber) {
		setLayout(new FlowLayout());
		left.setToolTipText("Previous " + displayedNumber);
		right.setToolTipText("Next " + displayedNumber);
		currPage = 1;
		if (isScanResultsPaging) {
			lblHeader = new JLabel(SCAN_HEADER);
		}
		else {
			lblHeader = new JLabel("<html>" + REQUESTS_HEADER + " 0 of 0 </html>");

		}
		
		lblHeader.setFont(new Font("Serif", Font.BOLD, 14));
		
		add(left);
		add(right);
		add(lblHeader);
	}
	
	public void pageRight() {
		if (AnalyzerUtils.isPagingSuccessful()) {
			currPage ++;
			updatePageCount();
		}
	}
	
	public void pageLeft() {
		if (AnalyzerUtils.isPagingSuccessful() && currPage > 1) {
			currPage --;
			updatePageCount();	
		}
	}
	
	public void updatePageCount() {
		lblHeader.setText("Displaying " + REQUESTS_HEADER + " " + currPage + " of " + AnalyzerUtils.getTotalNumOfPages());
		lblHeader.repaint();
	}
	
	public void resetCurrentPage() {
		currPage = 1;
	}
}
