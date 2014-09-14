package com.hacktics.diviner.gui.scanwizard;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.parosproxy.paros.network.HttpMessage;

import com.hacktics.diviner.analyze.AnalyzeController;
import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.analyze.Plugins;
import com.hacktics.diviner.diffutil.Response_Manager;
import com.hacktics.diviner.gui.DivinerTitleBorder;
import com.hacktics.diviner.gui.GuiUtils;
import com.hacktics.diviner.gui.scanwizard.SessionMethodTabContent.SessionMethodSelection;
import com.hacktics.diviner.zapDB.ZapHistoryDB;

/**
 * 
 * @author Eran Tamari
 *
 */

public class ScanWizard extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabsEnum currentTab;
	private JPanel pluginPanel;
	private JPanel panelStep4;
	private JPanel panelTabLogin;
	private JPanel panelTabSession;	
	private JPanel diffPanel;
	private JTabbedPane tabs;
	private AnalyzeController controller;
	private Scenarios scenarios;
	private JScrollPane selectedUrlsScroll;
	private JPanel mainLoginPanel;
	private LoginAndSessionTabContent loginTab;
	private LoginAndSessionTabContent sessionTab;
	private PluginTabContent pluginConent;
	private ReplayTabContent replayTab;
	private DiffTabContent diffTab; 
	private SessionMethodTabContent sessionMethodTab;
	private ScopeTabContent scopeTab;
	private boolean allowChooseSessionTab = false;
	private static final int CustomHistoryDefaultValue = 5;
	private static final String OK = "OK";
	private static final String CANCEL = "Cancel";
	private static final String BACK = "Back";
	private DomainTabContent domainTab;
	private static int limitHistoryRequests;
	private static HttpMessage loginRequest;
	private static HttpMessage publicSessionRequest;
	private HashMap<Integer, Response_Manager> diffsMap;
	private static final boolean COMPLETED_PROCESS = true;
	private static JCheckBox UserLockCheckBox;
	private static boolean wizardSuccess = false;
	private static final String USER_LOCK = "<html><b><u>The application Supports User Lock mechanism (the login page will not be analyzed)</u></b></html>";
	private static ScanWizard scanWizardSingelton;
	private static String payloadsList;

	public ScanWizard(Frame parent) {

		super(parent,"Scan Wizard", ModalityType.DOCUMENT_MODAL);	//The wizard is the top frame
		scanWizardSingelton = this;
		initTabs();
		setMinimumSize(new Dimension(800,600));
		setPreferredSize(new Dimension(800,600));
		setLocation(100, 100);
		setLayout(new BorderLayout());
		add(tabs);
		setResizable(true);
		pack();
		ZapHistoryDB.setHistoryFromZapDB();
		setVisible(true);
	}

	private void initTabs()	{
		currentTab = TabsEnum.DOMAIN_TAB_INDEX;
		tabs = new JTabbedPane(SwingConstants.LEFT);
		setDomainPanel();
		setPluginsPanel();
		setScenariosPanel();
		setScopeLSPanel();
		setLoginPanel();
		setSessionMethodPanel();
		setSessionPanel();
		setReplayPanel();
		setDiffPanel();
		tabs.setEnabledAt(TabsEnum.PLUGIN_TAB_INDEX.index, false);
		tabs.setEnabledAt(TabsEnum.SCENARIO_TAB_INDEX.index, false);
		tabs.setEnabledAt(TabsEnum.URLS_TAB_INDEX.index, false);
		tabs.setEnabledAt(TabsEnum.LOGIN_TAB_INDEX.index, false);
		tabs.setEnabledAt(TabsEnum.SESSION_TAB_INDEX.index, false);
		tabs.setEnabledAt(TabsEnum.MANUAL_SESSION_TAB_INDEX.index, false);
		tabs.setEnabledAt(TabsEnum.REPLAY_TAB_INDEX.index, false);
		tabs.setEnabledAt(TabsEnum.DIFF_TAB_INDEX.index, false);
	}

	private void setDomainPanel() {

		JPanel panel = new JPanel(new BorderLayout());
		domainTab = new DomainTabContent(CANCEL, panel, this);
		tabs.addTab("Domain", domainTab);

	}

	private void setPluginsPanel() {
		pluginPanel = new JPanel();
		pluginConent = new PluginTabContent(BACK, pluginPanel, this);
		tabs.addTab("Plugins" , pluginConent);
	}

	private void setScenariosPanel() {
		scenarios = new Scenarios();
		
		JPanel Containerpanel = new JPanel(new GridLayout(4 , 1 , 5 , 2));
		
		JPanel scenariosPanel = new JPanel(new GridLayout(3 , 3 , 5 , 4));
		JPanel historyPanel = new JPanel(new GridLayout(3 , 3 , 5 , 4));
		JPanel multiThreadPanel = new JPanel(new GridLayout(1 , 1 , 0 , 0));
		JPanel verifyPanel = new JPanel(new GridLayout(1 , 1 , 0 , 0));
		
		for (JCheckBox scenario : scenarios.getAllScenarios())
		{
			scenariosPanel.add(scenario);
		}
		
		for (JCheckBox histMode : scenarios.getAllHistModes())
		{
			historyPanel.add(histMode);
		}
		
		multiThreadPanel.setBorder(new DivinerTitleBorder(scenarios.getMultithreadTitle()));
		historyPanel.setBorder(new DivinerTitleBorder(scenarios.getHistoryTitle()));
		verifyPanel.setBorder(new DivinerTitleBorder(scenarios.getVerifyTitle()));
		scenariosPanel.setBorder(new DivinerTitleBorder(scenarios.getScenariosTitle()));

		verifyPanel.add(scenarios.getVerifyBox());
		multiThreadPanel.add(scenarios.getMultithread());
		
		Containerpanel.add(scenariosPanel);
		Containerpanel.add(historyPanel);
		Containerpanel.add(multiThreadPanel);
		Containerpanel.add(verifyPanel);
		
		ScenraioTabContent scenarioTab = new ScenraioTabContent(BACK, Containerpanel, this);
		tabs.addTab("Scenarios" , scenarioTab);

	}

	private void setScopeLSPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panelStep4 = new JPanel(new BorderLayout());
		panel.add(panelStep4, BorderLayout.CENTER);
		scopeTab = new ScopeTabContent(BACK, panel, this);
		tabs.addTab("URLs" , scopeTab);
	}

	private void setLoginPanel() {
		UserLockCheckBox = new JCheckBox(USER_LOCK);
		mainLoginPanel = new JPanel(new BorderLayout());

		mainLoginPanel.add(UserLockCheckBox, BorderLayout.NORTH);

		loginTab = new LoginAndSessionTabContent(BACK, mainLoginPanel, this);
		tabs.addTab("Login" , loginTab);
	}

	private void setSessionMethodPanel() {
		JPanel panelTabSessionMethod = new JPanel();

		sessionMethodTab = new SessionMethodTabContent(BACK, panelTabSessionMethod, this);
		tabs.addTab("Public Session" , sessionMethodTab);
	}


	private void setSessionPanel() {
		panelTabSession = new JPanel(new BorderLayout(20, 20));

		//TODO:Why is this here?
		controller = new AnalyzeController(scenarios);
		controller.getFrame().addWindowListener(new FrameListener());

		sessionTab = new LoginAndSessionTabContent(BACK, panelTabSession, this);
		sessionTab.setPublicSessionHeadLine();
		tabs.addTab("Choose Request" , sessionTab);
	}

	//Replayable params
	private void setReplayPanel() {
		JPanel replayPanel = new JPanel(new GridLayout(1,0));

		replayTab = new ReplayTabContent(BACK, replayPanel, this);
		tabs.addTab("Replay Params" , replayTab);
	}

	private void setDiffPanel() {

		diffPanel = new JPanel(new BorderLayout());
		diffTab = new DiffTabContent(BACK, diffPanel, this);
		tabs.addTab("Compare", diffTab);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()){
		case OK:
			handleOKButton();
			break;

		case BACK:

			//Need to go 2 tabs back
			if (currentTab == TabsEnum.REPLAY_TAB_INDEX && 
			SessionMethodSelection.valueOf(sessionMethodTab.getSelection()) != SessionMethodSelection.MANUAL) {

				previousTab();
			}
			previousTab();
			break;

		case CANCEL:
			closeDialog();
			break;
		}
	}


	private void handleOKButton() {
		switch (currentTab)
		{
		//The user selected a host to analyze
		case DOMAIN_TAB_INDEX:
			handleDomainTabOK();

			if (!domainTab.isSuccessful())	//No domain is selected - show error and don't increase tab
			{
				GuiUtils.getGuiUtils().showErrorDialog(this, "Cannot select empty domain");
				return;
			}
			break;

		case PLUGIN_TAB_INDEX:

			//If unsuccessful - do not increase tab
			if (! setSelectedPlugins()) {
				return;				
			}
			ExceptionsConfigDialog.setConcatenatedPayloads();
			ExceptionsConfigDialog.setErrorRegex();
			break;

			//The user selected URLs in the domain to analyze
		case URLS_TAB_INDEX:
			//			handleScopeTabOK();
			scopeTab.setSelectedUrlsInScope();
			if (!scopeTab.isSuccessful()) {
				GuiUtils.getGuiUtils().showErrorDialog(this, "Must select URLS to scan");
				return;
			}
			else {
				ArrayList<String> urlsList = scopeTab.getScopeUrlList();
				mainLoginPanel.removeAll();
				panelTabSession.removeAll();
				loginTab.setBottomPanel();
				sessionTab.setBottomPanel();
				loginTab.updateMainPanel(urlsList);
				sessionTab.updateMainPanel(urlsList);
			}
			break;

		case LOGIN_TAB_INDEX:
			loginRequest = loginTab.getSelectedSessionRequest();
			break;

		case SCENARIO_TAB_INDEX:
			if (scenarios.getEnabledScenariosCount() == 0 || scenarios.getEnabledHistoryModeCount() == 0 )
			{
				GuiUtils.getGuiUtils().showErrorDialog(this, "You must choose at least one scenario and one history mode");
				return;	//Don't increase tab
			}

			else if (scenarios.isCustomHistoryEnabled()) { //Custom limit history was set
				String limit = (String)JOptionPane.showInputDialog(this, "Set the limit of history requests", "Limit History", JOptionPane.QUESTION_MESSAGE, null, null, CustomHistoryDefaultValue);
				try {
					limitHistoryRequests = Integer.parseInt(limit);
				}
				catch (Exception e){ //Invalid limit value
					GuiUtils.getGuiUtils().showErrorDialog(this, "History limit is invalid. Only numbers are acceptible.");
					return;
				}
			}
			else { //Custom history limit was not set
				limitHistoryRequests = AnalyzerUtils.NO_LIMIT;
			}
			break;

		case MANUAL_SESSION_TAB_INDEX:
			publicSessionRequest = sessionTab.getSelectedSessionRequest();
			break;

		case REPLAY_TAB_INDEX:
			replayTab.setSelectedCsrfTokens();
			
			break;

		case DIFF_TAB_INDEX:
			diffTab.setSelectedDiffs();
			break;
		}
		//Session tab is handled in another function since it's more complicated
		if (currentTab == TabsEnum.SESSION_TAB_INDEX) {
			handleSessionMethodTabOK();
		} 
		else {
			increaseTab();
		}
	}

	//Domain tab OK button
	private void handleDomainTabOK()
	{
		JScrollPane urlScroll = domainTab.handleDomainTabOK();
		//		refreshTabs(); //If the user chose a different host - need to update

		if (urlScroll != null)
		{
			//If user is in step 1 again and chose another host
			if (panelStep4.getComponentCount() > 0)
			{
				panelStep4.removeAll();
			}
			panelStep4.add(urlScroll , BorderLayout.CENTER);

			urlScroll.repaint();
			panelStep4.validate();
			panelStep4.repaint();
			this.validate();
			this.repaint();

			scopeTab.setScopeURLs(domainTab.getSelectedHostsUrls());
		}
	}


	private void handleSessionMethodTabOK()
	{
		switch (SessionMethodSelection.valueOf(sessionMethodTab.getSelection()))
		{

		case AUTOMATIC:
			//Start 2 threads - one for finding the request and one for the progress bar
			TaskAutomaticPublicSession task = new TaskAutomaticPublicSession();
			task.execute();

			break;

		case MANUAL:
			allowChooseSessionTab = true;
			nextTab();
			break;

		case LOGIN:
			publicSessionRequest = loginRequest.cloneRequest();
			allowChooseSessionTab = false;
			nextTab();
			nextTab();
			break;

		}

	}

	//Handles the increase tab event
	private void increaseTab()
	{
		switch (currentTab)
		{
		case URLS_TAB_INDEX:
			break;

		case LOGIN_TAB_INDEX:
			//			panelTabSession.add(selectedUrlsScroll, BorderLayout.CENTER);
			//			sessionTab.setBottomPanel();
			break;

		case MANUAL_SESSION_TAB_INDEX:

			break;
			
		case REPLAY_TAB_INDEX:
			if (! Plugins.isDiffEnabled()) {	//When Diff plugin is disabled - no need to open Diff tab
				launchProgressBar();
			}
			else {							//Diff tab is enabled - run DIFF progress bar in a thread
				TaskContentDiffsTab task = new TaskContentDiffsTab();
				task.execute();
			}
			break;
		case DIFF_TAB_INDEX:
			launchProgressBar();
		}
		
		// Alex: Fix tab not found exception - r29
		if (currentTab!=TabsEnum.DIFF_TAB_INDEX) {
			nextTab();
		}

	}

	private void launchProgressBar() {
		this.setVisible(false);
		controller.createAndShowGUI();
	}
	
	private void nextTab()
	{
		currentTab = currentTab.next();
		tabs.setSelectedIndex(currentTab.index);
		tabs.setEnabledAt(currentTab.index - 1 , false);
		tabs.setEnabledAt(currentTab.index , true);
	}

	private void previousTab() {
		currentTab = currentTab.previous();
		tabs.setSelectedIndex(currentTab.index);
		tabs.setEnabledAt(currentTab.index, true);
		tabs.setEnabledAt(currentTab.index + 1, false);
	}

	public static HttpMessage getLoginRequest()
	{
		if (loginRequest == null)
			return null;

		return loginRequest.cloneRequest();
	}

	public static HttpMessage getPublicSessionRequest()
	{
		if (publicSessionRequest == null)
			return null;

		return publicSessionRequest.cloneRequest();
	}

	public static boolean isUserLock() {
		return UserLockCheckBox.isSelected();
	}

	private void closeDialog()
	{
		dispose();
	}

	public static int getHistoryLimit() {
		return limitHistoryRequests;
	}


	//Handles closing event when the wizard is successful
	class FrameListener extends WindowAdapter
	{
		@Override
		public void windowClosed(WindowEvent e)
		{
			closeDialog();
		}
	}

	//This class creates is a separate thread in order to show a progress bar in the sessionMethod  tab
	class TaskAutomaticPublicSession extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			sessionMethodTab.getSessionRequestAutomatically();
			return null;
		}
		@Override
		public void done() {
			publicSessionRequest = sessionMethodTab.getAutomaticPublicSession();
			//Found a public session request
			if (publicSessionRequest != null) 	
			{
				allowChooseSessionTab = false;
			}
			//No response contained Set-Cookie
			else{		
				allowChooseSessionTab = true;
				GuiUtils.getGuiUtils().showErrorDialog(new JDialog(), "Could not find public session page, please choose the page manually");

			}
			if (!allowChooseSessionTab)		//User does not choose manual public session - increase tabX2
			{
				nextTab();
			}
			nextTab();

		}

	}

	//This class creates is a separate thread in order to show a progress bar in the diffTab 
	class TaskContentDiffsTab extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			//Do Diffs process and shows Diffs in GUI
			diffPanel.add(diffTab.showDiffs(), BorderLayout.CENTER);
			ScanWizard.getSingelton().repaint();
			return null;
		}
		@Override
		public void done() {
		}

	}

	public static String getPayloadsList() {
		return payloadsList;
	}

	public static ScanWizard getSingelton() {
		return scanWizardSingelton;
	}

	/**
	 * 
	 * @return true if successful, false if not
	 */
	private boolean setSelectedPlugins() {
		
		//Missing plugin selection
		if (! (pluginConent.isAppendMode() || pluginConent.isReplaceMode()) || !(pluginConent.isExceptionPluginEnabled() || pluginConent.isReflectionPluginEnabled() || pluginConent.isContentDiffPluginEnabled()) || !(pluginConent.isSimpleRandom() || pluginConent.isErrorRandom()) || pluginConent.isValidRandom()) {
			GuiUtils.getGuiUtils().showErrorDialog(this, "Missing pluging selection");
			return false;
		}

		//Get selected plugins from GUI
		//plugins
		Plugins.setDiffEnabled(pluginConent.isContentDiffPluginEnabled());
		Plugins.setReflectionEnabled(pluginConent.isReflectionPluginEnabled());
		Plugins.setExceptionEnabled(pluginConent.isExceptionPluginEnabled());

		//Token append/replace
		Plugins.setAppendMode(pluginConent.isAppendMode());
		Plugins.setReplaceMode(pluginConent.isReplaceMode());
		
		//Token Type
		Plugins.setRandomTokenEnabled(pluginConent.isSimpleRandom());
		Plugins.setValidTokenEnabled(pluginConent.isValidRandom());
		Plugins.setinValidTokenEnabled(pluginConent.isErrorRandom());

		return true;
	}

	public static String getAnalyzedDomain() {
		return DomainTabContent.getSelectedDomain();
	}
	
		
	private enum TabsEnum{DOMAIN_TAB_INDEX (0), PLUGIN_TAB_INDEX (1),SCENARIO_TAB_INDEX (2), URLS_TAB_INDEX (3), LOGIN_TAB_INDEX (4), SESSION_TAB_INDEX (5), MANUAL_SESSION_TAB_INDEX (6), REPLAY_TAB_INDEX (7), DIFF_TAB_INDEX(8); 
		private int index;
		private TabsEnum(int index)
		{
			this.index = index;
	
		}
		public TabsEnum next()
		{
			return TabsEnum.values()[(this.index + 1)];
	
		}
		public TabsEnum previous()
		{
			return TabsEnum.values()[(this.index - 1)];
		}
	}
}
