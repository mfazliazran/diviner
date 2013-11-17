package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.print.DocFlavor.STRING;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.ParserConfigurationException;

import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.extension.manualrequest.ManualRequestEditorDialog;
import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HttpMessage;
import com.hacktics.diviner.analyze.AnalyzerUtils;
import com.hacktics.diviner.database.DivinerRecordResult;
import com.hacktics.diviner.gui.controllers.AttackItem;
import com.hacktics.diviner.gui.controllers.ComparableRadioButton;
import com.hacktics.diviner.gui.controllers.PayloadButton;
import com.hacktics.diviner.gui.controllers.RadioButtonScrollPane;
import com.hacktics.diviner.gui.controllers.PayloadRadioButton;
import com.hacktics.diviner.payloads.AttackVectorContainer;
import com.hacktics.diviner.payloads.AuthorContainer;
import com.hacktics.diviner.payloads.DelimiterSwapRuleContainer;
import com.hacktics.diviner.payloads.EvasionRuleContainer;
import com.hacktics.diviner.payloads.ExploitContainer;
import com.hacktics.diviner.payloads.PayloadContainer;
import com.hacktics.diviner.payloads.PayloadDatabaseLoader;
import com.hacktics.diviner.payloads.PlatformContainer;
import com.hacktics.diviner.payloads.SwapRuleContainer;
import com.hacktics.diviner.payloads.constants.XMLConstants;
import com.sittinglittleduck.DirBuster.gui.JTableTree.AbstractCellEditor;

/**
 * 
 * @author Eran Tamari
 *
 */

public class PayloadManager extends JDialog implements ActionListener{

	private static final long serialVersionUID = 3206080561719390324L;
	private String paramName;
	private int attackCounter;
	private JPanel payloadsPanel;
	private JPanel AttacksPanel;
	private JPanel bottomPanel;
	private JPanel topPanel;
	private JPanel detectionPanel;
	private JPanel platformsPanel;
	private JPanel evasionPanel;
	private JPanel exploitationPanel;
	private JPanel delimiterPanel;
	private RadioButtonScrollPane attacksListPanel;
	private RadioButtonScrollPane platformsListPanel;
	private RadioButtonScrollPane evasionListPanel;
	private RadioButtonScrollPane exploitationListPanel;
	private RadioButtonScrollPane delimiterListPanel;
	private RadioButtonScrollPane detectionPayloadsPanel;
	private Set <AbstractButton> attacksList;
	private int requestId;
	private int sourceID;
	private int NEWSCREENHEIGHT = 300;
	private int NEWSCREENWIDTH = 300;
	private String sourceURL;
	private int targetID;
	private String targetURL;
	private JTable table;
	private JTextField origValueBox;
	private JTextField payloadBox;
	private JLabel currentAttackLabel;
	private JDialog multipleChoiseDialog;
	private TreeSet<AbstractButton> detectionPayloads;
	private TreeSet<AbstractButton> platformsPayloads;
	private TreeSet<AbstractButton> exploitationPayloads;
	private TreeSet <AbstractButton> delimiterPayloads;
	private String creditsMessage = "";
	private TreeSet<AbstractButton> evasionPayloads;
	private static final String GO = "GO!";
	private static final String [] COLOUMNS = { "Source ID", "Source URL","View Source" };
	private static final String CANCEL = "Cancel";
	private static final String OK = "OK";
	private static final String SOURCE = "source";
	private static final String TARGET = "target";
	private static final String ADDNEW = "Add New";
	private static final String NEW_ATTACK = "Attack";
	private static final String NEW_PLATFORM = "Platform";
	private static final String NEW_DETECTION = "Detection Payload";
	private static final String NEW_EXPLOITATION = "Exploitation Payload";
	private static final String NEW_EVASION = "Evasion Technique";
	private static final String NEW_DELIMITER = "Delimiter Swap Rule";
	private static final String PLATFORM_ACTION = "PLATFORM_ACTION";
	private static final String EXPLOIT_ACTION = "EXPLOIT_ACTION";
	private static final String EVASION_ACTION = "EVASION_ACTION";
	private static final String DELIMITER_ACTION = "DELIMITER_ACTION";
	private static final String DETECTION_ACTION = "DETECTION_ACTION";
	private static final String RESTORE_DEFUALT_VAL = "RESTORE_DEFAULT_VALUE";
	private static final String FILE = "File";
	private static final String SAVE_XML = "Save Payloads";
	private static final String LOAD_XML = "Load Payloads";
	private static final String CREDITS = "Contributors";
	private static final String RIGHT_ARROW = " -> ";
	private static final String TWO_WAY_ARROW = " <-> ";
	private static final String PARAMETER_PREFIX = "Parameter Name: ";
	private static final String SWAP_TOKEN = "SWAP_TOKEN&!@#";
	private static final boolean IS_RADIO_BUTTON = true;
	Collection<AttackVectorContainer> AttackVectors = null;

	private static AddNewPayload payloadScreen = null;
	private static AddNewDelimiterSwapRule delimiterSwapScreen = null;
	private static AddNewPlatform platformScreen = null;
	private static AddNewExploit exploitScreen = null;
	private static AddNewEvasionTechnique evasionScreen = null;
	private static AddNewAttack attackScreen = null;

	public static final int PM_HEIGHT = 820;
	public static final int PM_WEDTH = 1100;

	public PayloadManager(String pageName, long paramId) {
		super(Diviner.getMainFrame(),"Payload Manager", ModalityType.DOCUMENT_MODAL);
		setIconImage(GuiUtils.getGuiUtils().getPayloadManagerIcon());
		ArrayList<DivinerRecordResult> resultsList = AnalyzerUtils.getUniqueResultsPerPageAndParam(pageName, paramId);
		this.paramName = resultsList.get(0).getName();
		this.AttackVectors = PayloadDatabaseLoader.getAttackVectorList();
		// More than one source request - show multiple source dialog
		if (resultsList.size() > 1) {
			showMultipleSourceDialog(resultsList);
		}
		else {
			showPayloadManager(resultsList.get(0).getInputID(), resultsList.get(0).getName(), resultsList.get(0).getValue());
		}
	}

	//Open payload manager for non-affecting parameters
	public PayloadManager(int sourceId, String paramName, String paramValue) {
		super(Diviner.getMainFrame(),"Payload Manager", ModalityType.DOCUMENT_MODAL);
		this.paramName = paramName;
		this.AttackVectors = PayloadDatabaseLoader.getAttackVectorList();
		setIconImage(GuiUtils.getGuiUtils().getPayloadManagerIcon());
		showPayloadManager(sourceId, paramName, paramValue);
	}

	private void showPayloadManager(int sourceId, String paramName, String paramValue) {
		setSize(PM_WEDTH, PM_HEIGHT);
		setLayout(new BorderLayout(30, 30));
		createMenu();
		setCredits();
		JPanel mainPanel = new JPanel(new BorderLayout());
		payloadsPanel = new JPanel();
		AttacksPanel = new JPanel();
		bottomPanel = new JPanel();
		topPanel = new JPanel();

		this.requestId = sourceId;

		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(payloadsPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		mainPanel.add(AttacksPanel, BorderLayout.EAST);

		mainPanel.setBorder(new LineBorder(Color.BLACK));
		setAttacksPanel();
		currentAttackLabel = new JLabel(attacksListPanel.getFirstSelected(), JLabel.CENTER);
		setPayloadsPanel();
		setTopPanel();
		setBottomPanel(paramName, paramValue);


		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setMinimumSize(this.getMinimumSize());
		setLocationRelativeTo(this);
		setVisible(true);
	}

	//Set the top panel
	private void setTopPanel() {
		topPanel.setLayout(new BorderLayout());
		currentAttackLabel.setFont(new Font("Serif", Font.ITALIC, 15));
		topPanel.add(currentAttackLabel, BorderLayout.SOUTH);
		//		topPanel.add(new JLabel(GuiUtils.getGuiUtils().getLogoIcon()), BorderLayout.CENTER);
	}


	private void showMultipleSourceDialog (ArrayList<DivinerRecordResult> resultsList) {
		int gridLines = resultsList.size();

		Object[][] lines = new Object[gridLines][3];
		int linesIndex = 0;

		for (DivinerRecordResult recordResult : resultsList) {

			sourceID = recordResult.getInputID();
			sourceURL = recordResult.getInPage();

			Object[] line = {sourceID, sourceURL, sourceID};
			lines[linesIndex] = line;
			linesIndex++;

		}
		table = new JTable(lines, COLOUMNS);
		table.setGridColor(Color.GRAY);
		table.setShowGrid(true);

		table.getColumn(COLOUMNS[0]).setCellRenderer(new labelTableCellRenderer());
		table.getColumn(COLOUMNS[0]).setCellEditor(new labelTableCellEditor());
		table.getColumn(COLOUMNS[1]).setCellRenderer(new labelTableCellRenderer());
		table.getColumn(COLOUMNS[1]).setCellEditor(new labelTableCellEditor());
		table.getColumn(COLOUMNS[2]).setCellRenderer(new buttonTableCellRenderer());
		table.getColumn(COLOUMNS[2]).setCellEditor(new buttonTableCellEditor());

		multipleChoiseDialog = new JDialog(this,"Choose a Result", ModalityType.DOCUMENT_MODAL);

		table.getColumnModel().getColumn(1).setPreferredWidth(350);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(50);

		JScrollPane tableScroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JLabel heading = new JLabel("<html><b>The chosen parameter was reflected in more than one request.<br/> Please choose the desired request:</b></html>");

		JPanel tablePanel = new JPanel(new BorderLayout());



		JPanel bottomPanel = new JPanel();
		JButton btnOk= new JButton(OK);
		btnOk.addActionListener(this);
		JButton btnCanel = new JButton(CANCEL);
		btnCanel.addActionListener(this);
		bottomPanel.add(btnOk);
		bottomPanel.add(btnCanel);

		tablePanel.add(heading, BorderLayout.NORTH);
		tablePanel.add(tableScroll, BorderLayout.CENTER);
		tablePanel.add(bottomPanel, BorderLayout.SOUTH);

		multipleChoiseDialog.add(tablePanel);
		multipleChoiseDialog.setSize(900, 500);
		multipleChoiseDialog.setVisible(true);	

	}

	private void setPayloadsPanel() {

		payloadsPanel.setLayout(new BorderLayout());
		JPanel techniquePayloadsPanel = new JPanel(new GridLayout(2, 1));
		
		JLabel detectionLabel = new JLabel("<html><u>Detection Payloads</u></html>");
		JLabel expolitationLabel = new JLabel("<html><u>Exploitation Payloads</u></html>");
		JLabel delimiterLabel = new JLabel("<html><u>Delimiter Swap Rules</u></html>");
		JLabel evasionLabel = new JLabel("<html><u>Evasion Rules</u></html>");

		detectionLabel.setFont(new Font("Serif", Font.ITALIC, 18));
		expolitationLabel.setFont(new Font("Serif", Font.ITALIC, 18));
		delimiterLabel.setFont(new Font("Serif", Font.ITALIC, 18));
		evasionLabel.setFont(new Font("Serif", Font.ITALIC, 18));



		detectionPayloads = new TreeSet<AbstractButton>();
		exploitationPayloads = new TreeSet<AbstractButton>();
		delimiterPayloads = new TreeSet<AbstractButton>();
		evasionPayloads = new TreeSet<AbstractButton>();
		platformsPayloads = new TreeSet<AbstractButton>();

		//////////////////////////////////Add New payloads buttons///////////////

		//Create the Add New Platform option 
		JButton btnNewPlatform = new JButton(GuiUtils.getGuiUtils().getAddPayloadIcon());
		btnNewPlatform.addActionListener(this);
		btnNewPlatform.setActionCommand(ADDNEW + NEW_PLATFORM);
		btnNewPlatform.setToolTipText(ADDNEW + " " + NEW_PLATFORM);


		//Create the Add New Detection option 
		JButton btnNewDetection = new JButton(GuiUtils.getGuiUtils().getAddPayloadIcon());
		btnNewDetection.addActionListener(this);
		btnNewDetection.setActionCommand(ADDNEW + NEW_DETECTION);
		btnNewDetection.setToolTipText(ADDNEW + " " + NEW_DETECTION);

		//Create the Add New Exploitation option 
		JButton btnNewExploit = new JButton(GuiUtils.getGuiUtils().getAddPayloadIcon());
		btnNewExploit.addActionListener(this);
		btnNewExploit.setActionCommand(ADDNEW + NEW_EXPLOITATION);
		btnNewDetection.setToolTipText(ADDNEW + " " + NEW_EXPLOITATION);


		//Create the Add New Evasion option 
		JButton btnNewEvasion = new JButton(GuiUtils.getGuiUtils().getAddPayloadIcon());
		btnNewEvasion.addActionListener(this);
		btnNewEvasion.setActionCommand(ADDNEW + NEW_EVASION);
		btnNewDetection.setToolTipText(ADDNEW + " " + NEW_EVASION);

		//////////////////////////////////End of Add New payloads buttons///////////////

		platformsPanel = new JPanel(new BorderLayout());
		platformsListPanel = new RadioButtonScrollPane("Platforms", platformsPayloads, false, false);
		platformsPanel.add(platformsListPanel, BorderLayout.CENTER);
		platformsPanel.add(btnNewPlatform, BorderLayout.WEST);



		//Detection Panel
		detectionPanel = new JPanel(new BorderLayout());
		detectionPayloadsPanel = new RadioButtonScrollPane("Detection Payloads", detectionPayloads, true, false);
		detectionPanel.add(detectionPayloadsPanel, BorderLayout.CENTER);
		detectionPanel.add(btnNewDetection, BorderLayout.WEST);

		techniquePayloadsPanel.add(detectionPanel, BorderLayout.CENTER);


		//Exploitation Panel
		exploitationPanel = new JPanel(new BorderLayout());
		exploitationListPanel = new RadioButtonScrollPane("Exploitation Payloads", exploitationPayloads, true, false);
		exploitationPanel.add(exploitationListPanel, BorderLayout.CENTER);
		exploitationPanel.add(btnNewExploit, BorderLayout.WEST);

		techniquePayloadsPanel.add(exploitationPanel, BorderLayout.CENTER);

		//Delimiter Panel
		delimiterPanel = new JPanel(new BorderLayout());
		delimiterListPanel = new RadioButtonScrollPane("Delimiter Swap Rules", delimiterPayloads, false, true);
		delimiterPanel.add(delimiterListPanel);
		//		payloadsPanel.add(delimiterPanel);

		//Evasion Panel
		evasionPanel = new JPanel(new BorderLayout());
		evasionListPanel = new RadioButtonScrollPane("Evasion Rules", evasionPayloads, false, true);
		evasionPanel.add(evasionListPanel, BorderLayout.CENTER);
		evasionPanel.add(btnNewEvasion, BorderLayout.WEST);

	
		payloadsPanel.add(platformsPanel, BorderLayout.NORTH);
		payloadsPanel.add(techniquePayloadsPanel, BorderLayout.CENTER);
		payloadsPanel.add(evasionPanel, BorderLayout.SOUTH);
		//Initially show the first attack in list with all its payloads
		repaintPayloadsPanels(attacksListPanel.getFirstSelected());
	}

	private void setBottomPanel(String paramName, String paramValue) {
		bottomPanel.setLayout(new BorderLayout());

		JLabel paramNameLabel = new JLabel("<html><i><b>" + PARAMETER_PREFIX + paramName + "</b></i></html>", JLabel.CENTER);
		paramNameLabel.setFont(new Font("Serif", Font.ITALIC, 18));

		JPanel bottomTextPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		JButton btnDefValue = new JButton("Restore Original Value", GuiUtils.getGuiUtils().getRestoreIcon());
		btnDefValue.setToolTipText("Restore Original Value");
		btnDefValue.addActionListener(this);
		btnDefValue.setActionCommand(RESTORE_DEFUALT_VAL);

		origValueBox = new JTextField(paramValue);
		origValueBox.setEditable(false);
		origValueBox.setEnabled(false);

		JLabel payloadsLabel = new JLabel("<html><b>Current Value:&nbsp;&nbsp;</b></html>");
		payloadsLabel.setFont(new Font("Serif", Font.ITALIC, 15));
		payloadsLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel origValueLabel = new JLabel("<html><b>Original Value: </b></html>");
		origValueLabel.setFont(new Font("Serif", Font.ITALIC, 15));
		origValueLabel.setHorizontalAlignment(SwingConstants.CENTER);

		payloadBox = new JTextField();

		restoreDefaultPayload();

		JButton btnGo = new JButton("Send To Repeater", GuiUtils.getGuiUtils().getRepeaterIcon());
		btnGo.setToolTipText("Open Repeater");
		btnGo.setActionCommand(GO);
		btnGo.addActionListener(this);


		//Original value panel
		JPanel panelOriginalValue = new JPanel(new BorderLayout());
		panelOriginalValue.add(origValueLabel, BorderLayout.WEST);
		panelOriginalValue.add(origValueBox, BorderLayout.CENTER);

		//Current value panel
		JPanel panelPayloadValue = new JPanel(new BorderLayout());
		panelPayloadValue.add(payloadsLabel, BorderLayout.WEST);
		panelPayloadValue.add(payloadBox, BorderLayout.CENTER);

		bottomTextPanel.add(panelOriginalValue);
		bottomTextPanel.add(panelPayloadValue);


		JPanel panelButtons = new JPanel(new GridLayout(2, 1));
		panelButtons.add(btnDefValue);
		panelButtons.add(btnGo);

		bottomPanel.add(panelButtons, BorderLayout.EAST);
		bottomPanel.add(paramNameLabel, BorderLayout.NORTH);
		bottomPanel.add(bottomTextPanel, BorderLayout.CENTER);

	}

	private void restoreDefaultPayload() {
		payloadBox.setText(origValueBox.getText());
		payloadBox.repaint();
	}

	private void setPayload(String payload) { 
		payloadBox.setText(payload);
		payloadBox.repaint();
	}

	//Create the payload manager menu
	private void createMenu() {
		JMenuBar menu = new JMenuBar();
		JMenu menuFile = new JMenu(FILE);
		JMenuItem loadMenuItem = new JMenuItem(LOAD_XML);
		JMenuItem saveMenuItem = new JMenuItem(SAVE_XML);
		JMenuItem creditsItem = new JMenuItem(CREDITS);

		menuFile.add(loadMenuItem);
		menuFile.add(saveMenuItem);
		menuFile.add(creditsItem);

		loadMenuItem.addActionListener(this);
		saveMenuItem.addActionListener(this);
		creditsItem.addActionListener(this);

		menu.add(menuFile);
		setJMenuBar(menu);

	}
	private void appendPayload(String payload) { 

		int currentPos = payloadBox.getCaretPosition();
		String initialStr = payloadBox.getText().substring(0, currentPos);
		String endStr = "";

		if (currentPos <= payloadBox.getText().length()) {
			endStr = payloadBox.getText().substring(currentPos);
		}

		payloadBox.setText(initialStr + payload + endStr);
		payloadBox.repaint();

	}
	private String urlDecode(String encodedStr) {
		String result = "";
		try{
			result = URLDecoder.decode(encodedStr, "UTF-8");
		}
		catch (Exception e) { e.printStackTrace(); }

		return result;
	}

	private void setCredits() {
		creditsMessage = "The following people helped in the making of this payload database: \n";

		for (AuthorContainer author : PayloadDatabaseLoader.getAuthors()) {
			creditsMessage += author.getAuthorName() + "\n";
		}
	}

	public void setAttacksPanel() {

		AttacksPanel.setLayout(new BorderLayout());

		JButton btnAddNewAttack = new JButton(GuiUtils.getGuiUtils().getAddPayloadIcon());
		btnAddNewAttack.setActionCommand(ADDNEW + NEW_ATTACK);
		btnAddNewAttack.addActionListener(this);
		attacksList = new TreeSet<AbstractButton>();

		for (AttackVectorContainer attack : PayloadDatabaseLoader.getAttackVectorList()) {
			AttackItem attackRadioButton = new AttackItem(attack.getAttackName());
			attackRadioButton.addActionListener(this);
			attacksList.add(attackRadioButton);			
		}


		attacksListPanel = new RadioButtonScrollPane("Attacks", attacksList, true, false);
		AttacksPanel.add(attacksListPanel, BorderLayout.EAST);
		AttacksPanel.add(btnAddNewAttack, BorderLayout.SOUTH);


	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case GO:
			showZapRepeater(this.requestId, paramName, payloadBox.getText());
			break;

		case SOURCE:
			showZapRepeater(this.sourceID);
			break;

		case TARGET:
			showZapRepeater(this.targetID);
			break;

		case RESTORE_DEFUALT_VAL:
			restoreDefaultPayload();
			break;

		case CANCEL:
			multipleChoiseDialog.dispose();
			this.dispose();
			break;

		case SAVE_XML:
			try {
				XMLSavePayloadManger.PayloadMangertoXML(this);

			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case LOAD_XML:
			try {
				JFileChooser LoadFileDialog = new JFileChooser();
				int iReturnedValue = LoadFileDialog.showDialog(LoadFileDialog,"Load");

				if (iReturnedValue == JFileChooser.APPROVE_OPTION) {
					PayloadDatabaseLoader.getPayloadDatabase(LoadFileDialog.getSelectedFile().toString());
					PayloadManager.this.AttackVectors = PayloadDatabaseLoader.getAttackVectorList();

					AttacksPanel.removeAll();
					setAttacksPanel();
					AttacksPanel.repaint();
					AttacksPanel.revalidate();
					
					PayloadManager.this.repaintPayloadsPanels(currentAttackLabel.getText());

				}
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(PayloadManager.this, "Could not load XML file");
				e1.printStackTrace();
			}
			break;

		case CREDITS:
			JOptionPane.showMessageDialog(this, creditsMessage, CREDITS, JOptionPane.INFORMATION_MESSAGE);
			break;
		case OK:

			if (table.getSelectedRow() != -1) {
				int sourceId = (Integer) table.getValueAt(table.getSelectedRow(), 0);
				String paramValue = AnalyzerUtils.getParamValueByName(AnalyzerUtils.readZapHistory(sourceId).getHttpMessage(), paramName);

				this.dispose();

				showPayloadManager(sourceId, paramName, paramValue);
			}
			else {
				GuiUtils.getGuiUtils().showErrorDialog(this, "Choose a source request from the table");
			}
			break;
		default:

			repaintGui(e);
			break;
		}		

	}


	//Handle click events to make interactive GUI
	private void repaintGui(ActionEvent event) {

		String eventName = event.getActionCommand();
		//A platform was picked by the user
		if (eventName.startsWith(PLATFORM_ACTION)) {
			//Set recommended according to the selected platform
			detectionPayloadsPanel.setRecommended(eventName.replace(PLATFORM_ACTION, ""), IS_RADIO_BUTTON);
			exploitationListPanel.setRecommended(eventName.replace(PLATFORM_ACTION, ""), IS_RADIO_BUTTON);
			evasionListPanel.setRecommended(eventName.replace(PLATFORM_ACTION, ""), !IS_RADIO_BUTTON);
			delimiterListPanel.setRecommended(eventName.replace(PLATFORM_ACTION, ""), !IS_RADIO_BUTTON);
		}

		//Handle the Add new buttons
		else if (eventName.startsWith(ADDNEW)){


			switch (eventName.substring(ADDNEW.length())) {

			case NEW_DELIMITER:
				delimiterSwapScreen = new AddNewDelimiterSwapRule(300, 500, AttackVectors, attacksListPanel.getSelected().getText(), this);
				break;
			case NEW_DETECTION:
				payloadScreen = new AddNewPayload(300, 500,AttackVectors, attacksListPanel.getSelected().getText(), this);	
				break;
			case NEW_EVASION:
				evasionScreen = new AddNewEvasionTechnique(300, 500, AttackVectors, attacksListPanel.getSelected().getText(), this);
				break;
			case NEW_EXPLOITATION:
				exploitScreen = new AddNewExploit(300, 500, AttackVectors, attacksListPanel.getSelected().getText(), this);
				break; 
			case NEW_PLATFORM:
				platformScreen = new AddNewPlatform(300, 300, AttackVectors, attacksListPanel.getSelected().getText(), this);
				break;
			case NEW_ATTACK:
				//pop up Add_New_Attack window
				attackScreen = new AddNewAttack(300, 500, this);
				//Add the new attack
				if (attackScreen.isSuccess()) {
					AttackItem attackRadioButton = new AttackItem(attackScreen.getAttackName());
					attackRadioButton.addActionListener(this);
					attacksListPanel.addNewAttack(attackRadioButton);
					AttacksPanel.repaint();
					AttacksPanel.revalidate();
				}
				break;
			}

		}

		//Detection payload chosen
		else if (eventName.startsWith(DETECTION_ACTION)) {
			appendPayload(eventName.replace(DETECTION_ACTION, ""));
		}

		//Exploitation payload chosen
		else if (eventName.startsWith(EXPLOIT_ACTION)) {
			appendPayload(eventName.replace(EXPLOIT_ACTION, ""));
		}
		else if (eventName.startsWith(DELIMITER_ACTION)) {
			String sourceChar = ((PayloadButton) event.getSource()).getSourceChar();
			String targetChar = ((PayloadButton) event.getSource()).getTargetChar();
			String tempValue = payloadBox.getText().replace(sourceChar, SWAP_TOKEN);
			String tempValue2 = tempValue.replace(targetChar, sourceChar);
			setPayload(tempValue2.replace(SWAP_TOKEN, targetChar));
		}
		else if (eventName.startsWith(EVASION_ACTION)) {
			String sourceChar = ((PayloadButton) event.getSource()).getSourceChar();
			String targetChar = ((PayloadButton) event.getSource()).getTargetChar();
			setPayload(payloadBox.getText().replace(sourceChar, targetChar));

		}

		//An attack was picked by the user
		else {	
			repaintPayloadsPanels(eventName);
		}	

	}


	public void repaintPayloadsPanels(String attackName) {
		for (AttackVectorContainer attack : PayloadDatabaseLoader.getAttackVectorList()) {
			if (attack.getAttackName().equals(attackName)) {

				detectionPayloads = getDetectionPayloads(attack);
				evasionPayloads = getEvasionPayloads(attack);
				delimiterPayloads = getdelimiterPayloads(attack);
				exploitationPayloads = getExploitationPayloads(attack);
				platformsPayloads = getPlatforms(attack);

				currentAttackLabel.setText(attackName);
				currentAttackLabel.repaint();
				break;
			}

		}

		platformsListPanel.setNewView(platformsPayloads);
		detectionPayloadsPanel.setNewView(detectionPayloads);
		evasionListPanel.setNewView(evasionPayloads);
		delimiterListPanel.setNewView(delimiterPayloads);
		exploitationListPanel.setNewView(exploitationPayloads);

	}

	//Returns platforms list
	private TreeSet<AbstractButton> getPlatforms(AttackVectorContainer attack) {

		TreeSet<AbstractButton> result = new TreeSet<AbstractButton>();
		HashMap<String, PlatformContainer> platforms = attack.getPlatforms();
		if (platforms != null) {
			for (PlatformContainer platform : platforms.values()) {
				ComparableRadioButton radioButton = new ComparableRadioButton(platform.getPlatformName());
				radioButton.addActionListener(this);
				radioButton.setActionCommand(PLATFORM_ACTION + radioButton.getText());
				result.add(radioButton);
			}
		}
		return result;
	}

	//Returns Detection payloads list
	private TreeSet<AbstractButton> getDetectionPayloads(AttackVectorContainer attack) {

		TreeSet<AbstractButton> result = new TreeSet<AbstractButton>();
		HashMap<String, PayloadContainer> detectionPayloads = attack.getDetectionPayloads();
		if (detectionPayloads != null) {
			for (PayloadContainer payload : detectionPayloads.values()) {
				String [] platforms = convertIdToPlatformName(attack, payload.getPlatforms().split(","));
				PayloadRadioButton payloadButton = new PayloadRadioButton(payload.getTitle(), urlDecode(payload.getPayloadValue()), platforms);
				payloadButton.addActionListener(this);
				payloadButton.setActionCommand(DETECTION_ACTION + payloadButton.getPayloadText());
				result.add(payloadButton);
			}
		}
		return result;
	}

	
	private String[] convertIdToPlatformName(AttackVectorContainer attack, String [] platformIds) {
		String [] result = new String [platformIds.length];
		int index = 0;

		if (platformIds.length > 0) {
			//All platforms
			if (platformIds[0].equalsIgnoreCase(XMLConstants.ALL_PLATFORMS)) {
				result = new String [attack.getPlatforms().size()];

				for (PlatformContainer platform : attack.getPlatforms().values()) {
					result[index] = platform.getPlatformName();
					index++;

				}
			}

			//General platform
			else if (platformIds[0].equalsIgnoreCase(XMLConstants.GENERAL_PLATFORM)){
				result[0] = "";
			}
			else {
				//Few Platforms
				for (String id : platformIds) {
					result[index]= attack.getPlatform(id).getPlatformName();
					index++;
				}
			}
		}


		return result;
	}

	//Returns Evasion payloads list
	private TreeSet<AbstractButton>  getEvasionPayloads(AttackVectorContainer attack) {

		TreeSet <AbstractButton> result = new TreeSet<AbstractButton>();
		JButton btnNewEvasionPayload= new JButton(ADDNEW + NEW_EVASION);
		btnNewEvasionPayload.addActionListener(this);
		btnNewEvasionPayload.setActionCommand(btnNewEvasionPayload.getText());
		//result.add(btnNewEvasionPayload);

		HashMap<String, EvasionRuleContainer> evasions = attack.getEvasionRules();
		if (evasions != null) {
			for (EvasionRuleContainer payload : evasions.values()) {
				for (SwapRuleContainer rule : payload.getSwapRuleCollection()) {
					String sourceChar = rule.getSourceCharacter();
					String targetChar = rule.getTargetCharacter();
					String text = sourceChar + RIGHT_ARROW + targetChar;
					String [] platforms = convertIdToPlatformName(attack, payload.getPlatforms().split(","));


					PayloadButton payloadButton = new PayloadButton(text, platforms, sourceChar, targetChar);
					payloadButton.addActionListener(this);
					payloadButton.setActionCommand(EVASION_ACTION + payloadButton.getText());
					result.add(payloadButton);
				}

			}
		}

		return result;
	}

	//Returns Exploitation payloads list
	private TreeSet<AbstractButton> getExploitationPayloads(AttackVectorContainer attack) {

		TreeSet<AbstractButton> result = new TreeSet<AbstractButton>();
		HashMap<String, ExploitContainer> exploits = attack.getExploitationPayloads();
		if (exploits != null) {
			for (ExploitContainer payload : exploits.values()) {
				String [] platforms = convertIdToPlatformName(attack, payload.getPlatforms().split(","));
				PayloadRadioButton payloadButton = new PayloadRadioButton(payload.getTitle(), urlDecode(payload.getExploitValue()), platforms);
				payloadButton.addActionListener(this);
				payloadButton.setActionCommand(EXPLOIT_ACTION + payloadButton.getPayloadText());
				result.add(payloadButton);
			}
		}
		return result;
	}


	//Returns delimiters list
	private TreeSet<AbstractButton> getdelimiterPayloads(AttackVectorContainer attack) {

		TreeSet <AbstractButton> result = new TreeSet<AbstractButton>();
		JButton btnNewDelimiterPayload= new JButton(ADDNEW + NEW_DELIMITER);
		btnNewDelimiterPayload.addActionListener(this);
		btnNewDelimiterPayload.setActionCommand(btnNewDelimiterPayload.getText());
		//		result.add(btnNewDelimiterPayload);

		HashMap<String, DelimiterSwapRuleContainer> delimiters = attack.getDelimiterSwapRules();
		if (delimiters != null) {
			for (DelimiterSwapRuleContainer payload : delimiters.values()) {
				for (SwapRuleContainer rule : payload.getSwapRuleCollection()) {
					String sourceChar = rule.getSourceCharacter();
					String targetChar = rule.getTargetCharacter();
					String text = sourceChar + TWO_WAY_ARROW + targetChar;
					String [] platforms = convertIdToPlatformName(attack, payload.getPlatforms().split(","));

					PayloadButton payloadButton = new PayloadButton(text, platforms, sourceChar, targetChar);
					payloadButton.addActionListener(this);
					payloadButton.setActionCommand(DELIMITER_ACTION + payloadButton.getText());
					result.add(payloadButton);
				}

			}
		}
		return result;
	}

	public static void showZapRepeater(int requestId) {
		ExtensionHistory extHist = (ExtensionHistory) Control.getSingleton().getExtensionLoader().getExtension("ExtensionHistory");
		if (extHist != null) {
			ManualRequestEditorDialog dialog = extHist.getResendDialog();
			dialog.setMessage(AnalyzerUtils.readZapHistory(requestId).getHttpMessage());
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
	}

	public void showZapRepeater(int requestId, String paramName, String paramValue) {
		ExtensionHistory extHist = (ExtensionHistory) Control.getSingleton().getExtensionLoader().getExtension("ExtensionHistory");
		if (extHist != null) {
			ManualRequestEditorDialog dialog = extHist.getResendDialog();
			HttpMessage msg = AnalyzerUtils.readZapHistory(requestId).getHttpMessage().cloneRequest();

			//Find the type of the param (GET/POST)
			for (HtmlParameter param : AnalyzerUtils.getMsgParams(msg)) {
				if (param.getName().equals(paramName)) {

					switch (param.getType()) {
					case form:
						AnalyzerUtils.setParameterPostRequest(msg, paramName, paramValue);
						break;

					case url: 
						AnalyzerUtils.setParameterGetRequest(msg, paramName, paramValue);
						break;

					case cookie:
						break;
					}

					break;

				}
			}
			dialog.setMessage(msg);
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}


	}

	public class buttonTableCellRenderer implements TableCellRenderer
	{

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JButton button = new JButton(GuiUtils.getGuiUtils().getMagnifyEnabledIcon());
			button.setBackground(Color.LIGHT_GRAY);
			return button;
		}
	}

	public class buttonTableCellEditor extends AbstractCellEditor implements TableCellEditor
	{
		private JButton button;
		private int value;
		public buttonTableCellEditor()
		{
			button = new JButton(GuiUtils.getGuiUtils().getMagnifyEnabledIcon());
			button.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					showZapRepeater(value);
				}
			});
		}
		public Object getCellEditorValue() {
			return null;
		}
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			this.value = (Integer) value;
			return button;
		}
	}

	public class labelTableCellRenderer implements TableCellRenderer
	{

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = new JLabel(value.toString(), JLabel.CENTER);
			label.setFont(new Font("Serif", Font.BOLD, 13));
			if (table.isRowSelected(row)) {
				label.setOpaque(true);
				label.setBackground(Color.YELLOW);
			}
			return label;
		}
	}
	public class labelTableCellEditor extends AbstractCellEditor implements TableCellEditor
	{

		@Override
		public Component getTableCellEditorComponent(JTable arg0, Object arg1,
				boolean arg2, int row, int column) {

			return null;
		}

	}

}
