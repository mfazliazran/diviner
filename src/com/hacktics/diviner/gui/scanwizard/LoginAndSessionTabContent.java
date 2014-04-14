package com.hacktics.diviner.gui.scanwizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.httpclient.URIException;
import org.parosproxy.paros.network.HttpBody;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.network.HttpRequestBody;

import com.hacktics.diviner.zapDB.ZapHistoryDB;

/**
 * 
 * @author Eran Tamari
 *
 */

public class LoginAndSessionTabContent extends GenericTabContent implements ActionListener{


	private static final long serialVersionUID = 1L;
	private HttpMessage sessionRequest;
	private boolean isSucccessful;
	private ArrayList<JRadioButton> scopeURLs;
	private static final String LOGIN_HEADLINE =  "Select the Login page:";
	private static final String PUBLIC_SESSION_HEADLINE =  "Select the Public Session page:";
	private static final String LOGIN_DESCRIPTION =  "<html><b>The login page will be used to authenticate sessions during the analysis processes.</b></html>";
	private static final String PUBLIC_DESCRIPTION =  "<html><b>The session generation / initialization page will be used to generate / replace session identifiers during the analyzing process.</b></html>";
	private static final String GET_METHOD = "GET";
	private static final String POST_METHOD = "POST";
	private JPanel bottomPanel;
	private JTextField parametersText;
	private JRadioButton btnPost;
	private JRadioButton btnGet;
	private HttpMessage selectedMsg;
	private JPanel mainPanel;
	private JScrollPane urlScroll;
	
	public LoginAndSessionTabContent(String backButtonCommand, JPanel panel, ActionListener eventHandler) {
		super(backButtonCommand, LOGIN_HEADLINE, new JLabel(LOGIN_DESCRIPTION),  panel,  eventHandler);
		mainPanel = panel;
		setBottomPanel();
		scopeURLs = new ArrayList<JRadioButton>();
		urlScroll = new JScrollPane();
		

	}

	public void setBottomPanel() {
		bottomPanel = new JPanel(new FlowLayout());
		
		
		JLabel lblParameter = new JLabel("Parameters to submit: ");
		parametersText = new JTextField();
		parametersText.setPreferredSize(new Dimension(300, 30));

		JLabel lblMethod = new JLabel("Method: ");
		btnPost = new JRadioButton(POST_METHOD);
		btnGet =  new JRadioButton(GET_METHOD);

		ButtonGroup groupMethods = new ButtonGroup();
		groupMethods.add(btnPost);
		groupMethods.add(btnGet);

		bottomPanel.add(lblParameter);
		bottomPanel.add(parametersText);
		bottomPanel.add(lblMethod);
		bottomPanel.add(btnGet);
		bottomPanel.add(btnPost);

		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	private void setParametersText(String parameters) {
		parametersText.setText(parameters);
		parametersText.repaint();
	}

	public void updateMainPanel(ArrayList<String> scopeURLs) {
		this.scopeURLs.clear();
		JPanel urlPanel = new JPanel(new GridLayout(scopeURLs.size(),1));
		ButtonGroup group = new ButtonGroup();
		for (String url : scopeURLs) {
			JRadioButton urlRadioButton = new JRadioButton(url);
			urlRadioButton.addActionListener(this);
			this.scopeURLs.add(urlRadioButton);
			urlPanel.add(urlRadioButton);
			group.add(urlRadioButton);
		}
		if (scopeURLs.size() > 0) {
			this.scopeURLs.get(0).setSelected(true);
		}

		urlScroll.setViewportView(urlPanel);
		urlScroll.repaint();
		mainPanel.add(urlScroll, BorderLayout.CENTER);
		initialize();
	}

	public void setPublicSessionHeadLine() {
		setHeadline(PUBLIC_SESSION_HEADLINE);
		getDescription().setText(PUBLIC_DESCRIPTION);
	}

	private HttpMessage getSessionRequest(String httpMethod, String parameters)
	{
		if (httpMethod == POST_METHOD)
		{
			sessionRequest.getRequestHeader().setMethod(POST_METHOD);
			HttpRequestBody body = new HttpRequestBody(parameters);
			sessionRequest.setRequestBody(body);
		}

		else	//Use Get method
		{
			try
			{
				sessionRequest.getRequestHeader().setMethod(GET_METHOD);
				sessionRequest.getRequestHeader().getURI().setQuery(parameters);
			}
			catch (URIException e)
			{
				e.printStackTrace();
			}
		}

		isSucccessful = SUCCESSFUL;
		return sessionRequest;
	}



	public Boolean isSuccessful() {
		return isSucccessful;
	}



	@Override
	public void actionPerformed(ActionEvent e) {

		String selectedLogin = (((JRadioButton)e.getSource()).getText());
		showRequestParamsAndMethod(selectedLogin);
		
	}

	public HttpMessage getSelectedSessionRequest() {
		return selectedMsg;
	}
	

	private void showRequestParamsAndMethod(String selectedLogin) {
		sessionRequest = ZapHistoryDB.getRequestFromZapDBByURL(selectedLogin);
		String loginDefaultMethod = sessionRequest.getRequestHeader().getMethod();
		String logindefaultParams= "";
		try	//Get parameters from original login msg and present them in the dialog
		{
			logindefaultParams = (loginDefaultMethod.equals(POST_METHOD) ? sessionRequest.getRequestBody().toString() : sessionRequest.getRequestHeader().getURI().getQuery());
			setParametersText(logindefaultParams);
			if (loginDefaultMethod.equals(POST_METHOD)) {
				btnPost.setSelected(true);
			}
			else {
				btnGet.setSelected(true);
			}

		}

		catch(URIException ex){ ex.printStackTrace(); }

		String httpMethod = btnGet.isSelected() ? GET_METHOD : POST_METHOD;
		selectedMsg = getSessionRequest(httpMethod, logindefaultParams);
	}
	
	//Set the first selected item with its params and method
	private void initialize() {
		for (JRadioButton url : scopeURLs) {
			if (url.isSelected()) {
				showRequestParamsAndMethod(url.getText());
				break;
			}
			
		}
	}
}

