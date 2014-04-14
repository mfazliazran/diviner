package com.hacktics.diviner.gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.extension.manualrequest.ManualRequestEditorDialog;
import org.zaproxy.zap.extension.diviner.DivinerExtension;

import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.gui.scanwizard.ScanWizard;
import com.hacktics.diviner.payloads.PayloadDatabaseLoader;
import com.hacktics.diviner.zapDB.ZapHistoryDB;
import com.hacktics.diviner.constants.DivinerGuiConstants;
import com.hacktics.diviner.constants.Resources;

/**
 * @author Eran Tamari
 *
 */
public final class Diviner extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9108675712706972414L;
	private static final String Version = "1.5.3-Beta";
	private JTabbedPane tabsMain;
	private JComboBox<String> domainsList;
	private JPanel scanResultsPanel; 
	private JPanel rightPanel;
	private Advisor advisorPanel;
	private AdvisorTree advisorTreePanel;
	private JPanel requestsPanel;	//Contains the top panel with combo + requestPanelCenter that contains actual requests
	private JPanel requestPanelCenter;
	private TaskList leadsPanel;
	private JPanel mainTabsPanel;
	private JScrollPane resultsScrollPane;
	private JScrollPane requestsScrollPane;
	private EntryPointsArray entryPointsArray;
	private JMenuBar menuBar;
	private PagingPanel pagingPanel;
	private static JLabel WELCOME_LABEL;	
	private static ScanWizard scanWizard;
	private static int RequestsFirstDisplayed = 0;
	private static JFrame singelton;
	private static PAGING_STATE paging;
	private static final int WINDOW_WIDTH = 1450; 
	private static final int WINDOW_HEIGHT = 850; 
	private static final int MAIN_PANEL_WIDTH = (WINDOW_WIDTH /3)  * 2 + 50 ; 
	private static final String ABOUT = "About";
	private static final String OPTIONS = "Options";
	private static final String UPDATE = "Update Domains";
	private static final String GLOSSARY = "Glossary";
	private static final String EXIT = "Exit";
	private static final String LICENSE = "License";
	private static final String HELP = "Help";
	private static final String ANALYZE = "Analyze";
	private static final String GENERATE_REPORT = "Generate HTML Report";
	private static final String FILE = "Log File";
	private static final String OPEN_DIALOG_TITLE = "Diviner - Web Penetration Tool";
	private static final String FILTER = "FILTER";
	private static final String SHOW_ALL = "Show All";

	 /** The Constant log. */
	private static final Logger log = Logger.getLogger(Diviner.class);


	private static final double REQUESTS_IN_BLOCK = 6.0;
	private static final double RESULTS_IN_BLOCK = 6.0;
	private static final String REQUESTS_RIGHT = "REQUESTS_RIGHT";
	private static final String REQUESTS_LEFT = "REQUESTS_LEFT";
	private static final String AUTHORS = "Diviner " + Version + "\nLead Developers: Shay Chen, Eran Tamari and Alex Mor\nAdditional Contribution: Michal Goldstein, Liran Shienbox\nTsachi Itschak and Lior Suliman\nImages and artwork include content from the following collections:\nhttp://www.fatcow.com/free-icons";

	private static File logFile;
	enum TABS{ Results, Requests }

	/**
	 * Constructor of the Diviner.
	 * Sets layouts and creates GUI 
	 * 	 
	 */
	public Diviner() {
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT); //sets size of JFrame and centers it
		initialize();
		setLookAndFeel();
		WELCOME_LABEL = new JLabel(DivinerGuiConstants.WELCOME_TEXT);

		//Create necessary instances
		tabsMain = new JTabbedPane();
		mainTabsPanel = new JPanel(new BorderLayout());
		scanResultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		requestsPanel = new JPanel(new BorderLayout());
		leadsPanel = new TaskList();
		advisorPanel = new Advisor();
		advisorTreePanel = new AdvisorTree(advisorPanel);
		entryPointsArray = new EntryPointsArray(advisorPanel, leadsPanel, advisorTreePanel);


		//Create right main panel
		rightPanel = new JPanel(new GridLayout(3, 1));
		rightPanel.add(leadsPanel);
		rightPanel.add(advisorTreePanel);
		rightPanel.add(advisorPanel);
		//Create GUI
		windowLayout();
		createMainPanel();
		createLeadsPanel();
		createAdvisorPanel();
		createMenu();
		setRequestsPanel();

		add(rightPanel, BorderLayout.EAST);

		singelton = this;
		setVisible(true);//make frame visible
		//Load XML file for payload manager
		try{
			//changed to payload db filename (for com.hacktics.payloaddb usage), was PAYLOAD_DATABASE_BASIC
			PayloadDatabaseLoader.getPayloadDatabase(Resources.PAYLOAD_DATABASE_FILENAME);
		}
		catch(Exception e) { e.printStackTrace(); }
	}


	//Sets the NIMBUS look and feel - just like ZAP's l&f
	private void setLookAndFeel(){

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}

	private void createMainPanel() {

		WELCOME_LABEL.setFont(new Font("Serif", Font.ITALIC, 20));
		scanResultsPanel.add(WELCOME_LABEL);

		//Results Panel
		JPanel mainResultsPanel = new JPanel(new BorderLayout());

		resultsScrollPane =  new JScrollPane(scanResultsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultsScrollPane.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH,WINDOW_HEIGHT)); //Needed for scroll bars

		//Currently paging is disabled on Results Tab
		//		JButton btnResultsLeft = new JButton(GuiUtils.getGuiUtils().getLeftIcon());
		//		JButton btnResultsRight = new JButton(GuiUtils.getGuiUtils().getRightIcon());
		//		btnResultsLeft.addActionListener(this);
		//		btnResultsRight.addActionListener(this);
		//		mainResultsPanel.add(new PagingPanel(true, btnResultsLeft, btnResultsRight, 12), BorderLayout.NORTH);
		mainResultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

		//Zap Requests panel
		requestsPanel.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH, WINDOW_HEIGHT));

		tabsMain.addTab("Results", mainResultsPanel);
		tabsMain.addTab("Requests", requestsPanel);

		tabsMain.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				JTabbedPane pane = (JTabbedPane)arg0.getSource();
				// Get current tab
				if (pane.getSelectedIndex() == TABS.Requests.ordinal()) {
					updateDomiansList();
				}
			}});

		mainTabsPanel.add(tabsMain, BorderLayout.CENTER);
		add(mainTabsPanel, BorderLayout.CENTER);
	}

	/**
	 * Handles the click event on the main menu
	 */
	public void actionPerformed(ActionEvent e) {

		//Menu Handlers
		switch (e.getActionCommand()){

		case LICENSE:
			showLicenseDialog();
			break;
		case EXIT:
			Diviner.getMainFrame().dispose();
			break;

		case UPDATE:
			updateDomiansList();
			domainsList.repaint();
			break;

		case GLOSSARY:
			showHelpPanel();
			break;

		case GENERATE_REPORT:
//TODO:
			/////phantom JS
			try{
				Runtime.getRuntime().exec("C:\\Users\\eran.tamari\\Downloads\\phantomjs-1.8.1-windows\\phantomjs C:\\Users\\eran.tamari\\Downloads\\phantomjs-1.8.1-windows\\examples\\rasterize.js http://www.google.com c:\\clock6.png");
			}
			catch (Exception ex) { ex.printStackTrace(); }

			File HTMLReport;
			JFileChooser fileChooserReport = new JFileChooser();
			//HTML files only
			fileChooserReport.setMultiSelectionEnabled(false);
			fileChooserReport.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML Documents", "htm", "html");
			fileChooserReport.setFileFilter(filter);




			if (fileChooserReport.showDialog(this, "Save Report") == JFileChooser.APPROVE_OPTION) {
				HTMLReport = fileChooserReport.getSelectedFile();
				ReportGenerator.generateReport(HTMLReport);
			}
			
			break;
		case ABOUT:
			showAboutDialog();
			break;

		case ANALYZE:
			removeWelcomeText();
			showScanWizardDialog();
			break;

		case FILE:
			JFileChooser fileChooserLog = new JFileChooser();
			if (fileChooserLog.showDialog(this, "Save") == JFileChooser.APPROVE_OPTION) {
				logFile = fileChooserLog.getSelectedFile();
				System.out.println(logFile.getName());
			}
			break;

		case FILTER:
			String regex = (String) JOptionPane.showInputDialog(this, "Enter a Regular Expression","Filter", JOptionPane.INFORMATION_MESSAGE ,GuiUtils.getGuiUtils().getQuestionIcon(), null ,"");
			//User pressed cancel on Regex Dialog
			if (regex == null) {
				break;
			}
			EntryPointsArray.addRequestFilter(regex);
			showZapRequests();
			break;

		case SHOW_ALL:
			EntryPointsArray.cleanFilters();
			AnalyzerUtils.restartStartIndex();
			paging = PAGING_STATE.START;
			showZapRequests();
			break;

		case REQUESTS_RIGHT:
			paging = PAGING_STATE.RIGHT;
			showZapRequests();
			break;

		case REQUESTS_LEFT:
			paging = PAGING_STATE.LEFT;
			AnalyzerUtils.setPreviousIndex();
			showZapRequests();
			break;
		}
	}

	private void removeWelcomeText() {
		scanResultsPanel.remove(WELCOME_LABEL);
		scanResultsPanel.repaint();
	}

	private void showZapRequests() {
		ImportRequests importRequestsFromZap = new ImportRequests();
		importRequestsFromZap.setProgressDialog(new ProgressBarDialog(this, "Import Requests", "Loading Requests From Zap"));
		importRequestsFromZap.execute();
	}
	/**
	 * Sets some default layouts of the window
	 */
	private void windowLayout() {

		this.setTitle("Diviner");
		this.setResizable(true);//disable window resize
		this.setLayout(new BorderLayout());
	}

	private void createLeadsPanel() {

		leadsPanel.setPreferredSize(new Dimension(WINDOW_WIDTH / 3,WINDOW_HEIGHT / 3));
		leadsPanel.setMinimumSize(new Dimension(WINDOW_WIDTH / 3,WINDOW_HEIGHT / 3));

	}

	private void showHelpPanel() {

		new HelpPanel(this).setVisible(true);
	}


	private void createAdvisorPanel() {
		advisorPanel.setPreferredSize(new Dimension(WINDOW_WIDTH / 3,WINDOW_HEIGHT * (2 / 3)));
		advisorPanel.setMinimumSize(new Dimension(WINDOW_WIDTH / 3,WINDOW_HEIGHT * (2 / 3)));
	}

	private void createMenu(){

		menuBar = new JMenuBar();

		JMenu menuOptions = new JMenu(OPTIONS);
		JMenu menuHelp = new JMenu(HELP);

		JMenuItem menuItemAbout = new JMenuItem(ABOUT);
		JMenuItem menuItemExit = new JMenuItem(EXIT);
		JMenuItem menuItemDomain = new JMenuItem(ANALYZE);
		JMenuItem menuItemLicense = new JMenuItem(LICENSE);
		JMenuItem menuItemFile = new JMenuItem(FILE);
		JMenuItem menuItemGlossary = new JMenuItem(GLOSSARY);
		JMenuItem menuItemReport = new JMenuItem(GENERATE_REPORT);

		menuOptions.add(menuItemDomain);
		menuOptions.add(menuItemFile);
		menuOptions.add(menuItemReport);
		menuOptions.addSeparator();
		menuOptions.add(menuItemExit);

		menuHelp.add(menuItemAbout);
		menuHelp.add(menuItemGlossary);
		menuHelp.add(menuItemLicense);

		menuItemFile.addActionListener(this);
		menuItemAbout.addActionListener(this);
		menuItemDomain.addActionListener(this);
		menuItemLicense.addActionListener(this);
		menuItemReport.addActionListener(this);
		menuItemGlossary.addActionListener(this);
		menuItemExit.addActionListener(this);

		menuBar.add(menuOptions);
		menuBar.add(menuHelp);

		setJMenuBar(menuBar);
	}

	private void initialize() {
		//set application icon
		
		try {

			this.setIconImage(GuiUtils.getGuiUtils().getDivinerIcon());
		
		
		

		//add EVENT: window closing button
		this.addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) 
					{
						dispose();
					}
				}
			);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}//end of method initialize

	private void showAboutDialog() {
		ImageIcon icon = GuiUtils.getGuiUtils().getLogoIcon();
		JOptionPane.showMessageDialog(this,
				AUTHORS,
				OPEN_DIALOG_TITLE,
				JOptionPane.INFORMATION_MESSAGE,icon);
	}

	private void showLicenseDialog() {
		LicenseDialog license = new LicenseDialog(this, true);
		license.setVisible(true);
	}

	//Show the scan wizard
	private void showScanWizardDialog() {
		scanWizard = new ScanWizard(this);
		scanWizard.addWindowListener(new FrameListener());
	}

	//Presents the results on the main panel
	public void showScanResults() {

		ArrayList<EntryPointPanel> entries = entryPointsArray.getScanResults();
		for (EntryPointPanel page : entries)
		{
			scanResultsPanel.add(page);
		}
		repaintResultsPanel(entries.size());
	}

	private void setRequestsPanel() {

		domainsList = new JComboBox<String>();
		JPanel topBarRequestPanel = new JPanel(new BorderLayout(100, 0));
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 4));

		JButton btnUpdate = new JButton(UPDATE);
		btnUpdate.addActionListener(this);
		JButton btnFilterRequests = new JButton(FILTER);
		btnFilterRequests.addActionListener(this);
		JButton btnShowAll = new JButton(SHOW_ALL);
		btnShowAll.addActionListener(this);

		buttonsPanel.add(btnUpdate);
		buttonsPanel.add(btnFilterRequests);
		buttonsPanel.add(btnShowAll);

		//Requests paging panel
		JPanel requestsPagingPanel = new JPanel(new BorderLayout());
		JButton btnRequestsLeft = new JButton(GuiUtils.getGuiUtils().getLeftIcon());
		JButton btnRequestsRight = new JButton(GuiUtils.getGuiUtils().getRightIcon());
		btnRequestsLeft.addActionListener(this);
		btnRequestsRight.addActionListener(this);
		btnRequestsLeft.setActionCommand(REQUESTS_LEFT);
		btnRequestsRight.setActionCommand(REQUESTS_RIGHT);
		pagingPanel = new PagingPanel(false, btnRequestsLeft, btnRequestsRight, AnalyzerUtils.getPagesPerBlock());
		requestsPagingPanel.add(pagingPanel, BorderLayout.CENTER);

		topBarRequestPanel.add(requestsPagingPanel, BorderLayout.NORTH);
		topBarRequestPanel.add(domainsList, BorderLayout.CENTER);
		topBarRequestPanel.add(buttonsPanel, BorderLayout.EAST);
		requestsPanel.add(topBarRequestPanel, BorderLayout.NORTH);
		requestPanelCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));
		requestsScrollPane = new JScrollPane(requestPanelCenter);
		requestsPanel.add(requestsScrollPane, BorderLayout.CENTER);
	}

	public static File getLogFile() {
		return logFile;
	}

	//Update domains list every time the requests tab is selected
	void updateDomiansList() {
		ArrayList<String> domainsArray = GuiUtils.getGuiUtils().getDomainsFromZap();
		String [] str = new String[domainsArray.size()];
		domainsArray.toArray(str);
		domainsList.removeAllItems();
		for (String domain : domainsArray) {
			domainsList.addItem(domain);
		}
	}

	//Handles the closing event of the Scan Wizard
	class FrameListener extends WindowAdapter {
		public void windowClosed(WindowEvent e)
		{
			showScanResults();
		}
	}

	//This class creates is a separate thread in order to show a progress bar in the sessionMethod  tab
	class ImportRequests extends SwingWorker<Void, Void> {

		private ProgressBarDialog progressDialog;

		public void setProgressDialog(ProgressBarDialog progressDialog) {
			this.progressDialog = progressDialog;
		}

		@Override
		protected Void doInBackground() throws Exception {
			//Show the requests in the request panel
			if (domainsList.getSelectedItem() != null) {

				progressDialog.start();
				ZapHistoryDB.setHistoryFromZapDB();


				ArrayList<EntryPointPanel> requestEntryPointsList = entryPointsArray.getZapRequests((String) domainsList.getSelectedItem());

				//Clean previous requests
				if (requestEntryPointsList.size() > 0) {
					requestPanelCenter.removeAll();
				}
				for (EntryPointPanel request : requestEntryPointsList) {
					requestPanelCenter.add(request);
				}
				repaintRequestsPanel(requestEntryPointsList.size());
			}
			return null;
		}

		@Override
		public void done() {

			progressDialog.stop();
			switch (paging) {
			case LEFT:
				pagingPanel.pageLeft();
				break;
			case RIGHT:
				pagingPanel.pageRight();
				break;
			case START:
				pagingPanel.resetCurrentPage();
				pagingPanel.updatePageCount();
				break;
			}
		}

	}

	//Make sure the requests scroll bar is in the right size every time we add requests
	private void  repaintRequestsPanel(int requestsToPaint) {

		double heightFactor = requestsToPaint / REQUESTS_IN_BLOCK;
		if (heightFactor > 1) {
			requestPanelCenter.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH,(int)(WINDOW_HEIGHT * heightFactor)));
		}
		else {
			requestPanelCenter.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH, WINDOW_HEIGHT - 400));
		}
		requestsScrollPane.revalidate();
		requestsScrollPane.repaint();
	}	

	//Make sure the results scroll bar is in the right size every time we add results
	private void  repaintResultsPanel(int resultsToPaint) {

		double heightFactor = resultsToPaint / RESULTS_IN_BLOCK;
		if (heightFactor > 1) {
			scanResultsPanel.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH,(int)(WINDOW_HEIGHT * heightFactor)));
		}
		else {
			scanResultsPanel.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH, WINDOW_HEIGHT - 400));
		}
		resultsScrollPane.revalidate();
	}	

	public static int getWindowWidth() {
		return WINDOW_WIDTH;
	}

	public static int getWindowHeight() {
		return WINDOW_HEIGHT;
	}


	public static JFrame getMainFrame() {
		return singelton;
	}

	public enum PAGING_STATE {
		RIGHT, LEFT, START
	}
}