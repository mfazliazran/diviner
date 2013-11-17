package com.hacktics.diviner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.OptionPaneUI;

import com.hacktics.diviner.analyze.Clairvoyance;
import com.hacktics.diviner.analyze.CodeBehaviour;
import com.hacktics.diviner.gui.controllers.BlinkingButton;
import com.hacktics.diviner.gui.scanwizard.Scenarios;

/**
 * 
 * @author Eran Tamari
 *
 */

public class EntryPointPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8651156700345948436L;
	private int blinkCounter;
	private Timer blinkTimer;
	private TitledBorder entryPointTitleBorder;
	private TitledBorder enrtyPointTitleBorderBlinking;
	private TitledBorder enrtyPointTitleBorderClicked;
	private String pageName;
	private ParameterList paramListPanel;
	private OutputPanel outputPanel;
	private boolean isMagnifyOptionAdded;
	private JPanel mainPanel;
	private JPanel optionspanel;
	private JLabel magnifyIcon;
	private JLabel injectionIcon;
	private JLabel starIcon;
	private JSplitPane splitPane;
	private JLabel eyeIcon;
	private boolean isAuthenticated;
	private EntryPointPanel clone;
	private Statistics stats;
	private boolean isResultsTab;
	private HashMap <String, EntryPointPanel>  affectedMap;
	private static final CompoundBorder ENTRY_POINT_BORDER_WHEN_CLICKED = new CompoundBorder(new EtchedBorder(),new LineBorder(Color.RED, 1 ,true));
	private static final Border ENTRY_POINT_BORDER = new CompoundBorder(new EtchedBorder(),new LineBorder(Color.black , 3 , true));
	private static final Border ENTRY_POINT_BLINKING_BORDER = new CompoundBorder(new EtchedBorder(),new LineBorder(Color.yellow , 3 , true));
	private static final Color ENTRY_POINT_COLOR = Color.LIGHT_GRAY;
	private static final String SOURCE_CODE_TOOLTIP = "Clairvoyance - source code divination";
	private static final String PAYLOAD_MANAGER = "Payload Manager";
	private static final String MAGNIFY = "Magnify";
	private static final String MAGNIFY_DISABLED = "Magnify Disabled";
	private static final String OPEN_REPEATER = "Open Repeater";

	public EntryPointPanel(String pageName, boolean isResultsTab, Statistics stats, boolean isAuthenticated, int divinerRequestId) {

		mainPanel = new JPanel();
		affectedMap = new HashMap<String, EntryPointPanel>();
		this.isResultsTab = isResultsTab;
		this.pageName = (pageName != null) ? pageName : "/";	//If the page name is null - we assume it's the root directory
		this.stats = stats;
		this.setToolTipText(pageName);
		paramListPanel = new ParameterList(stats.getNumOfParams());
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		isMagnifyOptionAdded = false;

		optionspanel = new JPanel(new GridLayout(4, 1));
		optionspanel.setBackground(ENTRY_POINT_COLOR);
		add(optionspanel, BorderLayout.EAST);

		//Result Tab
		if (isResultsTab) {

			//Authentication Analyze can be performed only if all scenarios are executed
			if (Scenarios.isAllScenariosEnabled()) {
				if (isAuthenticated) {
					optionspanel.add(new JLabel(GuiUtils.getGuiUtils().getAuthenticatedIcon()));
				}
				else {
					optionspanel.add(new JLabel(GuiUtils.getGuiUtils().getUnauthenticatedIcon()));
				}
			}

			outputPanel = new OutputPanel(false);

			//Add icons to options bar
			eyeIcon = new JLabel(GuiUtils.getGuiUtils().getEyeIcon());
			eyeIcon.setToolTipText(SOURCE_CODE_TOOLTIP);
			eyeIcon.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					CodeDispalyFrame codeDispalyFrame = new CodeDispalyFrame(EntryPointPanel.this.pageName, CodeBehaviour.getSourceCode(EntryPointPanel.this.pageName));
					codeDispalyFrame.setVisible(true);
				}});
			optionspanel.add(eyeIcon);

			starIcon = new JLabel(GuiUtils.getGuiUtils().getStarIcon());
			starIcon.setToolTipText(PAYLOAD_MANAGER);
			addStarEvent();
			optionspanel.add(starIcon);

			magnifyIcon = new JLabel(GuiUtils.getGuiUtils().getMagnifyDisabledIcon());
			magnifyIcon.setToolTipText(MAGNIFY_DISABLED);
			optionspanel.add(magnifyIcon);

		}
		//Requests tab - show requests from ZAP
		else {
			outputPanel = new OutputPanel(true);
			outputPanel.setStatistics(stats);
			JLabel lblDivinerRequestId = new JLabel(Integer.toString(divinerRequestId), JLabel.CENTER);
			lblDivinerRequestId.setFont(new Font("Serif", Font.ITALIC, 18));
			
			optionspanel.add(lblDivinerRequestId);
			
			starIcon = new JLabel(GuiUtils.getGuiUtils().getStarIcon());
			starIcon.setToolTipText(PAYLOAD_MANAGER);
			addStarEvent();
			
			optionspanel.add(starIcon);

			injectionIcon = new JLabel(GuiUtils.getGuiUtils().getRepeaterIcon());
			injectionIcon.setToolTipText(OPEN_REPEATER);
			
			optionspanel.add(injectionIcon);
			injectionIcon.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					PayloadManager.showZapRepeater(EntryPointPanel.this.stats.getRequestId());
				}});
		}
		initializeGUI();

		addBlinkEvent();
	}


	public void addMagnifyOption() {

		if (isMagnifyOptionAdded == false) {

			isMagnifyOptionAdded = true;
			optionspanel.remove(magnifyIcon);	//Remove the disabled magnify
			magnifyIcon = new JLabel(GuiUtils.getGuiUtils().getMagnifyEnabledIcon());
			magnifyIcon.setToolTipText(MAGNIFY);
			optionspanel.add(magnifyIcon);

			//Open magnify frame when magnify icon is clicked
			magnifyIcon.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					JDialog inspectFrame = new JDialog(Diviner.getMainFrame(), pageName, ModalityType.DOCUMENT_MODAL);
					inspectFrame.setIconImage(GuiUtils.getGuiUtils().getMagnifyIcon());
					JPanel affectedPagesPanel = new JPanel(new GridLayout(affectedMap.values().size() - 1, 1, 20, 20));
					JPanel leftPanel = new JPanel();
					leftPanel.add(affectedPagesPanel);

					for (EntryPointPanel affectedPage : affectedMap.values()) {
						//Put the entry point on the right side
						if (affectedPage.equals(EntryPointPanel.this.clone)) {
							//							rightPanel.add(EntryPointPanel.this.clone);
						}
						//Put any affected page on the left panel
						else {
							affectedPagesPanel.add(affectedPage);
						}
					}
					//					JScrollPane rightPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					JScrollPane leftPane = new JScrollPane(leftPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

					EntryPointPanel.this.clone.outputPanel.setPreferredSize(new Dimension(250,300));
					EntryPointPanel.this.clone.paramListPanel.getScrollPane().setPreferredSize(new Dimension(250,300));
					splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane, EntryPointPanel.this.clone);
					splitPane.setResizeWeight(0.5);
					inspectFrame.add(splitPane);
					inspectFrame.setSize(new Dimension(910,420));
					inspectFrame.setResizable(false);
					inspectFrame.setVisible(true);
				}
			});
		}

	}

	private void addStarEvent() {
		starIcon.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {

				PayloadManager payloadManager = null;

				for (Parameter param : paramListPanel.getParamList()) {
					if (param.isSelected()) {

						//open payload manager for affecting parameters
						if (param.isAffecting()) {
							payloadManager = new PayloadManager(param.getPageName(), param.getParameterID());
						}
						//open payload manager for non-affecting parameters on requests tab
						else {
							if (!isResultsTab) { //Cannot open payload manager for non-affecting parameters on results tab
								payloadManager = new PayloadManager(stats.getRequestId(), param.getParamName(), param.getParamValue());
							}
						}

						break;
					}
				}

			}
		});
	}

	public void changeColor(){
		setBorder(enrtyPointTitleBorderBlinking);
	}

	public void restoreColor(){
		setBorder(entryPointTitleBorder);
	}

	private void stopTimer(){
		blinkTimer.stop();
	}

	public ParameterList getParameterList()
	{
		return paramListPanel;
	}

	private void initializeGUI()
	{
		//set blinking borders
		entryPointTitleBorder = new TitledBorder(ENTRY_POINT_BORDER , pageName);
		entryPointTitleBorder.setTitleJustification(TitledBorder.CENTER);
		enrtyPointTitleBorderBlinking  = new TitledBorder(ENTRY_POINT_BLINKING_BORDER , pageName);
		enrtyPointTitleBorderBlinking.setTitleJustification(TitledBorder.CENTER);
		enrtyPointTitleBorderClicked = new TitledBorder(ENTRY_POINT_BORDER_WHEN_CLICKED , pageName);
		enrtyPointTitleBorderClicked.setTitleJustification(TitledBorder.CENTER);

		//Adding the panels to the EntryPoint
		mainPanel.add(outputPanel);
		mainPanel.add(paramListPanel);
		mainPanel.setBackground(ENTRY_POINT_COLOR);

		//Entry point layout
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT , 0, 0));

		setBackground(ENTRY_POINT_COLOR);
		setBorder(entryPointTitleBorder);

	}

	public OutputPanel getOutputPanel(){
		return outputPanel;
	}

	public Statistics getStats() {
		return stats;
	}

	private void addBlinkEvent(){
		blinkTimer = new Timer(100, new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				if (blinkCounter < 8)
				{
					if (blinkCounter % 2 == 0)
					{
						setBorder(enrtyPointTitleBorderBlinking);
					}
					else
					{
						setBorder(entryPointTitleBorder);
					}

					blinkCounter++;
				}	

				else	//stop blinking 
				{
					stopTimer();
				}
			}});
	}

	public void restoreAllDefaultColors(){
		paramListPanel.restoreDefaultColors();
		outputPanel.restoreDefaultColors();
	}

	public String getName()
	{
		return pageName;
	}


	public void setEffects() {
		outputPanel.addEffects();
	}

	//Each entry point stores a list (hashmap) of entries clones which are affected by it. These will be shown in the Magnify option 
	public void setAffectedPages() {

		this.clone = clone();
		addEntryPoint(this.clone);
		for (Parameter param : paramListPanel.getParamList()) {

			if (param.getAffectedPages() != null) {

				for (ParameterEffect effect : param.getAffectedPages()) {
					addEntryPoint(EntryPointsArray.getClone(effect.getOutputPage()));
				}
			}
		}
	}

	public EntryPointPanel clone() {
		EntryPointPanel clone = new EntryPointPanel(this.getName(), true, new Statistics(stats.getNumOfParams()), this.isAuthenticated, -1);
		for (Parameter param : this.getParameterList().getParamList()) {
			clone.getParameterList().addParameter(param.getParameterID(), param.getRequestId(), param.getPageName(), param.getParamName(), param.getParamValue(), param.isAffecting(), param.getInfo());


		}

		return clone;
	}


	public void actionPerformed(ActionEvent e) 
	{
		Parameter clickedParam = (Parameter)e.getSource();
		ArrayList <BlinkingButton> blinkButtonsArray =  new ArrayList <BlinkingButton>();;
		ArrayList<ParameterEffect> affectedPages = clickedParam.getAffectedPages();

		for (EntryPointPanel entryPoint : affectedMap.values()) {

			//Restore all default colors once a parameter is clicked
			entryPoint.restoreAllDefaultColors(); 
			entryPoint.restoreColor();
		}		

		//Blinking Action when clicked
		if (affectedPages != null) {
			for (ParameterEffect affectedPage : affectedPages)	{

				EntryPointPanel entryPoint = affectedMap.get(affectedPage.getOutputPage());
				entryPoint.changeColor();
				affectedMap.get(clickedParam.getPageName()).changeColor();
				blinkButtonsArray.add((Parameter)e.getSource());
				switch(affectedPage.getType())	{
				case ParameterEffect.DATABASE_REFLECTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.DATABASE_REFLECTION.ordinal()]);
					break;
				case ParameterEffect.OUTPUT_REFLECTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.OUTPUT_REFLECTION.ordinal()]);
					break;
				case ParameterEffect.SESSION_REFLECTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.SESSION_REFLECTION.ordinal()]);
					break;
				case ParameterEffect.DATABASE_EXCEPTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.DATABASE_EXCEPTION.ordinal()]);
					break;
				case ParameterEffect.OUTPUT_EXCEPTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.OUTPUT_EXCEPTION.ordinal()]);
					break;
				case ParameterEffect.SESSION_EXCEPTION:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.SESSION_EXCEPTION.ordinal()]);
					break;
				case ParameterEffect.DIFF:
					blinkButtonsArray.add((entryPoint.getOutputPanel().getOutputTypeList())[OUTPUT_TYPE.DIFF.ordinal()]);
					break;
				}	
			}	

			//Implemented blinking in another loop in order to keep the blinks timing
			for (BlinkingButton blinkingButton : blinkButtonsArray){
				blinkingButton.Blink();


			}
		}
	}


	private void addEntryPoint(EntryPointPanel entryPoint){

		//Don't add entry points that were already added in previous run
		if (! affectedMap.containsKey(entryPoint.getName()))	{

			affectedMap.put(entryPoint.getName(), entryPoint);
		}
	}

	//Makes parameters and output panel visible for clones
	public void showParameters() {

		for (EntryPointPanel affectedPage : affectedMap.values()) {
			for (Parameter param : affectedPage.getParameterList().getParamList()) {
				param.addActionListener(this);

			}	
			setAffettedOutputVisible(affectedPage);
		}

		for (EntryPointPanel affectedPage : affectedMap.values()) {
			affectedPage.setEffects();
		}
	}

	private void setAffettedOutputVisible(EntryPointPanel entryPoint) {

		if (entryPoint != null) {
			for (Parameter param : entryPoint.getParameterList().getParamList())	{
				if (param.getAffectedPages() != null) {
					for (ParameterEffect affectedPage : param.getAffectedPages()) {
						if (affectedMap.get(affectedPage.getOutputPage()) != null) {	//Solved bug- a case when the affected page is also an affecting page, but its effect does not appear in the entry's magnify option
							switch(affectedPage.getType())	{
							case ParameterEffect.DATABASE_REFLECTION:

								affectedMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.DATABASE_REFLECTION);
								break;
							case ParameterEffect.OUTPUT_REFLECTION:
								affectedMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.OUTPUT_REFLECTION);
								break;
							case ParameterEffect.SESSION_REFLECTION:
								affectedMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.SESSION_REFLECTION);
								break;
							case ParameterEffect.DATABASE_EXCEPTION:
								affectedMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.DATABASE_EXCEPTION);
								break;
							case ParameterEffect.OUTPUT_EXCEPTION:
								affectedMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.OUTPUT_EXCEPTION);
								break;
							case ParameterEffect.SESSION_EXCEPTION:
								affectedMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.SESSION_EXCEPTION);
								break;
							case ParameterEffect.DIFF:
								affectedMap.get(affectedPage.getOutputPage()).getOutputPanel().setVisible(OUTPUT_TYPE.DIFF);
								break;
							}
						}	
					}
				}
			}
		}
	}
}
