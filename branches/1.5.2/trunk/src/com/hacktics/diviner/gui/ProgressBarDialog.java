package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * 
 * @author Eran Tamari
 *
 */

public class ProgressBarDialog extends JDialog{

	private static final long serialVersionUID = 6188495015257091359L;
	private JProgressBar progressbar;
	private Progress progressThread;
	private boolean isActive = false;
	public ProgressBarDialog(Frame parent, String title, String text) {
		super(parent, title, ModalityType.DOCUMENT_MODAL);
		init(title, text);

	}
	public ProgressBarDialog(Dialog parent, String title, String text) {
		super(parent, title, ModalityType.DOCUMENT_MODAL);
		init(title, text);
	}

	private void init(String title, String text) {
		progressbar = new JProgressBar();
		setLayout(new BorderLayout());
		setTitle(title);
		JLabel progressText = new JLabel(text, JLabel.CENTER);
		progressText.setFont(new Font("Serif", Font.ITALIC, 18));
		add(progressText, BorderLayout.NORTH);
		add(progressbar, BorderLayout.SOUTH);
		setSize(350, 150);
		setLocationRelativeTo(this);
		progressThread = new Progress();
	}
	private class Progress extends Thread {

		public void run() {
			progressbar.setIndeterminate(true);
			setVisible(true);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ignore) {}
		}
	}

	public void start() {
		if (! progressThread.isAlive() && (! isActive)) {
			isActive = true;
			progressThread = new Progress();
			progressThread.start();
		}
	}

	public void stop() {
		if (progressThread.isAlive() && (isActive)) {
			isActive = false;
			progressbar.setIndeterminate(false);
			setVisible(false);

		}
	}
}
