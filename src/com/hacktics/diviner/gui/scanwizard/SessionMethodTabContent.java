package com.hacktics.diviner.gui.scanwizard;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.parosproxy.paros.db.RecordHistory;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;

import com.hacktics.diviner.analyze.RequestSender;
import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.gui.GuiUtils;
import com.hacktics.diviner.gui.ProgressBarDialog;

/**
 * 
 * @author Eran Tamari
 *
 */

public class SessionMethodTabContent extends GenericTabContent{

	HttpMessage publicSessionRequest;

	private ButtonGroup group;
	private String selected;
	private JProgressBar progressbar;
	private Progress progressThread;
	private boolean isActive = false;
	private Dialog parent;
	private static final String HEADLINE =  "How to Generate A New Session Identifier (Public):";
	private static final String DESCRIPTION =  "<html><b>You can choose how to locate a page that generates a new session identifier.<br>Note that the automated selection process may take a while.</b></html>";
	private static final String SET_COOKIE_HEADER = "Set-Cookie";
	private static final String COOKIE_HEADER = "Cookie: ";
	private JPanel mainPanel;
	public enum SessionMethodSelection { AUTOMATIC ("Choose for me"), MANUAL("Let me choose"), LOGIN("Use the Login request (not recommended)");
	public String value;

	private SessionMethodSelection(String value)	{
		this.value = value;
	}

	}


	public SessionMethodTabContent(String backButtonCommand, JPanel panel, ActionListener eventHandler)	{
		super(backButtonCommand, HEADLINE, new JLabel(DESCRIPTION),  panel,  eventHandler);

		parent = (Dialog) eventHandler;
		this.mainPanel = panel;
		this.mainPanel.setLayout(new BorderLayout());

		JPanel optionsPanel = new JPanel(new GridLayout(5, 2, 20 , 20));
		optionsPanel.setLayout(new GridLayout(5, 2, 20 , 20));

		JRadioButton buttonAutomatic = new JRadioButton("<html><i>" + SessionMethodSelection.AUTOMATIC.value + "</i></html>");
		buttonAutomatic.setActionCommand(SessionMethodSelection.AUTOMATIC.name());

		JRadioButton buttonManual = new JRadioButton("<html><i>" + SessionMethodSelection.MANUAL.value + "</i></html>");
		buttonManual.setActionCommand(SessionMethodSelection.MANUAL.name());

		JRadioButton buttonLogin = new JRadioButton("<html><i>" + SessionMethodSelection.LOGIN.value + "</i></html>");
		buttonLogin.setActionCommand(SessionMethodSelection.LOGIN.name());

		buttonAutomatic.setSelected(true);

		group = new ButtonGroup();

		group.add(buttonAutomatic);
		group.add(buttonManual);
		group.add(buttonLogin);


		optionsPanel.add(buttonAutomatic);
		optionsPanel.add(buttonManual);
		optionsPanel.add(buttonLogin);

		progressbar = new JProgressBar();
		mainPanel.add(optionsPanel, BorderLayout.CENTER);
		mainPanel.add(progressbar, BorderLayout.SOUTH);



	}	

	public String getSelection()
	{
		selected = group.getSelection().getActionCommand(); 
		return selected;
	}

	public void getSessionRequestAutomatically()
	{
		HttpMessage result = null;
		progressThread = new Progress();
		progressThread.start();

		try{
			for (RecordHistory request : AnalyzerUtils.getHistoryOfSelectedHostToAnalyze()) {
				result = null;
				if (AnalyzerUtils.isEqualToLogin(request.getHttpMessage())) // Don't use the Login request to create a public session
				{
					continue;
				}
				result = getRequestWithSetCookie(request);

				if (result != null)
				{
					String uri = request.getHttpMessage().getRequestHeader().getURI().toString();
					String method = request.getHttpMessage().getRequestHeader().getMethod().toString();	
					String options[] = {"OK", "NEXT"}; 
					String requestText =  "<html><b>" + method + "  " + uri + "</b></html>";
					int confirm = JOptionPane.showOptionDialog(this, "The following request will be used to create a public session,\n" +
							"press NEXT to use a different request\n" + requestText, "Confirm Public Session", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, GuiUtils.getGuiUtils().getQuestionIcon(), options , null);
					if (confirm == 0) {
						progressbar.setIndeterminate(false);
						break;
					}

				}
			}
			progressbar.setIndeterminate(false);
		}
		catch (Exception e){e.printStackTrace();}
		publicSessionRequest = result;
	}

	public HttpMessage getAutomaticPublicSession() {
		return publicSessionRequest;
	}
	//Check if the response contains Set-Cookie header
	private HttpMessage getRequestWithSetCookie(RecordHistory record) throws IOException
	{
		HttpMessage result = null;
		HttpMessage msg = record.getHttpMessage().cloneRequest();
		removeCookieHeader(msg); //Remove cookie from request
		msg.setRequestHeader(new HttpRequestHeader(msg.getRequestHeader().toString()));
		RequestSender.send(msg);
		if (msg.getResponseHeader().toString().contains(SET_COOKIE_HEADER)) //Response contains Set-Cookie header
		{
			result = msg.cloneRequest();
		}
		return result;
	}


	//Removes the Cookie header from the request
	private void removeCookieHeader(HttpMessage msg) throws HttpMalformedHeaderException
	{
		String cookie = COOKIE_HEADER + msg.getCookieParamsAsString();
		String [] headersWithoutCookie = msg.getRequestHeader().toString().split(Pattern.quote(cookie));
		if (headersWithoutCookie.length > 1)//cookie found in request header and needs to be removed
		{
			msg.setRequestHeader(new HttpRequestHeader(headersWithoutCookie[0] + headersWithoutCookie[1]));
		}
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
