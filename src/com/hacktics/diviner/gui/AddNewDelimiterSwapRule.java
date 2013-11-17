/**
 *
 */
package com.hacktics.diviner.gui;



import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Collection;




import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JButton;


import javax.swing.JLabel;

import javax.swing.JTextField;


import com.hacktics.diviner.constants.Titles;
import com.hacktics.diviner.payloads.AttackVectorContainer;
import com.hacktics.diviner.payloads.DelimiterSwapRuleContainer;

import com.hacktics.diviner.payloads.SwapRuleContainer;

/**
 * The PayloadAdvisor class helps the pen-tester construct complex attack
 * payloads using a database of ready-to-use detection & exploitation inputs,
 * as well as a collection of exposure-specific evasion techniques.
 * When possible, the PayloadAdvisor will attempt to recommend payloads
 * that have a higher probability of being executed successfully.
 *
 * @author Shay Chen
 * @since 1.0
 */
public class AddNewDelimiterSwapRule extends BaseJFrame {

	//**********************
	//* FIELD DECLERATIONS *
	//**********************
	/**
	 * Unique class identifier.
	 */
	private static final long serialVersionUID = 8904618772542045407L;
	/**
	 * A variable for storing the current frame status.
	 */
	private static boolean isActive;
	/**
	 * A variable for verifying frame hierarchy.
	 */
	private static boolean isMainWindow = false;
	/**
	 * A variable for storing the current execution status.
	 */
	private static boolean isRunning;
	/**
	 * The main menu control.
	 */
	private JMenuBar mnuMainMenu;

	private JTextField txtTitleField = null;
	private JTextField txtTargetTypeField = null;
	private JTextField txtContextField = null;
	private JTextField txtDescriptionField = null;
	private JTextField txtCreditField = null;
	private JTextField txtPlatformsField = null;
	private JButton    btnRuleField = null;

	private AttackVectorContainer attack;
	private JLabel lblTitleField = null;
	private JLabel lblTargetTypeField = null;
	private JLabel lblContextField = null;
	private JLabel lblRuleField = null;
	private JLabel lblDescriptionField = null;
	private JLabel lblCreditField = null;
	private JLabel lblPlatformsField = null;
	Collection<AttackVectorContainer> AttackVectors = null;
	private String SelectedAttack= null;


	private JButton btnAddNewButton = null;

	private static int DEFAULT_WINDOW_WIDTH = 500; 
	private static int DEFAULT_WINDOW_HEIGHT = 500; 

	private PayloadManager fatherWindow = null;
	private ArrayList<SwapRuleContainer> _swaprule = new ArrayList<SwapRuleContainer>();


	private static AddNewRules AddNewRulesScreen = null;

	//****************
	//* CONSTRUCTORS *
	//****************

	/**
	 * Default Constructor.
	 */
	public AddNewDelimiterSwapRule() {
		super(DEFAULT_WINDOW_WIDTH,DEFAULT_WINDOW_HEIGHT);
		this.initialize();
	} //end of default constructor


	/**
	 * Custom Size Constructor.
	 * @param width The width of the frame
	 * @param height The height of the frame
	 */
	public AddNewDelimiterSwapRule(final int width, final int height,Collection<AttackVectorContainer> PayloadAttacks,String Selectedattack, PayloadManager father) {
		super(width, height);
		this.fatherWindow = father;
		this.SelectedAttack = Selectedattack;
		this.AttackVectors = PayloadAttacks;
		this.initialize();
	} //end of constructor


	//***********
	//* METHODS *
	//***********
	/**
	 * Performs standard frame initialization activities on the JFrame,
	 * such as binding the frame control and events.
	 */
	public final void initialize() {
		this.setTitle(Titles.FRAME_ADD_NEW_PAYLOAD_TITLE);
		this.setResizable(false); //disable window resize
		this.setLayout(new GridLayout(13, 1)/*new GridBagLayout()*/);

		//Populate the menu bar
		JMenu fileMenu = new JMenu(Titles.MENU_FILE_TITLE);
		JMenuItem saveItem = new JMenuItem(Titles.MENUITEM_SAVE_TITLE);
		//############EMPTY EVENT#############
		fileMenu.add(saveItem);
		JMenuItem loadItem = new JMenuItem(Titles.MENUITEM_LOAD_TITLE);
		//############EMPTY EVENT#############
		fileMenu.add(loadItem);
		JMenuItem exitItem = new JMenuItem(Titles.MENUITEM_CLOSE_TITLE);
		//add EVENT: ActionClose (inner class in father class)
		exitItem.addActionListener(new ActionClose());
		fileMenu.add(exitItem);

		JMenu actionMenu = new JMenu(Titles.MENU_ACTION_TITLE);
		JMenuItem startItem = new JMenuItem(Titles.MENUITEM_START_TITLE);
		//add EVENT: ActionStart (implemented in an inner class)
		startItem.addActionListener(new ActionStart());
		actionMenu.add(startItem);
		JMenuItem stopItem = new JMenuItem(Titles.MENUITEM_STOP_TITLE);
		//add EVENT: ActionStop (implemented in an inner class)
		stopItem.addActionListener(new ActionStop());
		actionMenu.add(stopItem);

		JMenu helpMenu = new JMenu(Titles.MENU_HELP_TITLE);
		JMenuItem manualItem = new JMenuItem(Titles.MENUITEM_MANUAL_TITLE);
		//############EMPTY EVENT#############
		helpMenu.add(manualItem);
		JMenuItem aboutItem = new JMenuItem(Titles.MENUITEM_ABOUT_TITLE);
		//add EVENT:  ActionOpenAboutDialog (implemented as an inner class)
		aboutItem.addActionListener(new ActionOpenAboutDialog());
		helpMenu.add(aboutItem);

		//Actual menu creation
		mnuMainMenu = new JMenuBar();
		mnuMainMenu.add(fileMenu);
		mnuMainMenu.add(actionMenu);
		mnuMainMenu.add(helpMenu);

		//Add GUI components to the frame
		setJMenuBar(mnuMainMenu);
		//add(mnuMainMenu);


		txtTitleField = new  JTextField();
		txtTitleField.setSize(50, 25);
		txtTargetTypeField = new JTextField();
		txtTargetTypeField.setSize(50, 25);
		txtContextField = new JTextField();
		txtContextField.setSize(50, 25);
		txtDescriptionField = new JTextField();
		txtDescriptionField.setSize(50, 25);
		txtCreditField = new JTextField();
		txtCreditField.setSize(50, 25);
		txtPlatformsField = new JTextField();
		txtPlatformsField.setSize(50, 25);
		btnRuleField = new JButton("Add New Rule");
		btnRuleField.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				AddNewSetofRules();
			}
		});	

		lblTitleField = new JLabel("Title");
		lblTargetTypeField = new JLabel("Target Type");
		lblRuleField = new JLabel("Rules");
		lblContextField = new JLabel("Context");
		lblDescriptionField = new JLabel("Description");
		lblCreditField =  new JLabel("Credit");
		lblPlatformsField =  new JLabel("Platforms");


		btnAddNewButton = new JButton("Add New Delimiter");
		btnAddNewButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				AddNewDelimiterSwapRuleContainer();
			}
		});
		//JFrame AddNewScreen = new JFrame("Add Content");

		//Get the attack object of the selected attack
		for (AttackVectorContainer AttackVector : AttackVectors) {
			if (AttackVector.getAttackName().equals(SelectedAttack)) {
				attack = AttackVector;
				break;
			}
		}




		this.add(lblTitleField);
		this.add(txtTitleField);

		this.add(lblTargetTypeField);
		this.add(txtTargetTypeField);

		this.add(lblRuleField);
		this.add(btnRuleField);

		this.add(lblContextField);
		this.add(txtContextField);

		this.add(lblDescriptionField);
		this.add(txtDescriptionField);

		this.add(lblCreditField);
		this.add(txtCreditField);

		this.add(lblPlatformsField);
		this.add(txtPlatformsField);

		this.add(btnAddNewButton);


		this.setVisible(true);

	} //end of initialization method


	protected void AddNewSetofRules() {
		// TODO Auto-generated method stub
		AddNewRulesScreen = new AddNewRules(this);
	}


	protected void AddNewDelimiterSwapRuleContainer() {
		// TODO Auto-generated method stub
		if (txtTitleField.getText() != "" && txtTargetTypeField.getText() != "" && txtContextField.getText() != ""
				&& txtDescriptionField.getText() != ""	&&	txtCreditField.getText() != "" && txtPlatformsField.getText() != "") {

			String Title = txtTitleField.getText();
			String Target = txtTargetTypeField.getText();
			ArrayList<SwapRuleContainer>  Rule = null;
			if (this.getSwapRule() != null) {
				Rule = this.getSwapRule();
			}
			String Context = txtContextField.getText();
			String Description = txtDescriptionField.getText();
			String Credit = txtCreditField.getText();
			String Platforms = txtPlatformsField.getText();
			
			String id = Integer.toString(attack.getMaxDelimiterId() + 1);
			DelimiterSwapRuleContainer NewDelimiterToAdd = new DelimiterSwapRuleContainer(id, Title, Target, Context, Rule, Description, Credit, Platforms);
			attack.addDelimiterSwapRule(NewDelimiterToAdd, id);


			this.fatherWindow.repaint();
			setVisible(false);
			dispose();
		}

		else {
			JOptionPane.showMessageDialog(this, "Fields cannot be empty");
		}
	}


	/**
	 * Changes the status of the main frame execution flag.
	 * @param status The state of the class
	 */
	public final void setRunning(final boolean status) {
		isRunning = status;
	}  //end of method


	/**
	 * Returns the status of the main frame execution flag.
	 * @return the current state of the flag
	 */
	public final boolean getRunning() {
		return isRunning;
	} //end of method


	//*******************
	//* COVERED METHODS *
	//*******************

	/* (non-Javadoc)
	 * @see com.hasc.diviner.gui.BaseJFrame#setActive(boolean)
	 */
	@Override
	public final void setActive(final boolean status) {
		isActive = status;
	}  //end of method


	/* (non-Javadoc)
	 * @see com.hasc.diviner.gui.BaseJFrame#setMainWindow()
	 */
	@Override
	public final void setMainWindow(final boolean flag) {
		isMainWindow = flag;
	} //end of method


	/* (non-Javadoc)
	 * @see com.hasc.diviner.gui.BaseJFrame#getActive()
	 */
	@Override
	public final boolean getActive() {
		return isActive;
	} //end of method


	/* (non-Javadoc)
	 * @see com.hasc.diviner.gui.BaseJFrame#getMainWindow()
	 */
	@Override
	public final boolean getMainWindow() {
		return isMainWindow;
	} //end of method


	/* (non-Javadoc)
	 * @see com.hasc.diviner.gui.BaseJFrame#stopProcesses()
	 */
	@Override
	protected final void stopProcesses() {
		// DO Nothing
	} //end of method

	public void setSwapRule(ArrayList<SwapRuleContainer> Swap)
	{
		this._swaprule = Swap;
	}

	public ArrayList<SwapRuleContainer> getSwapRule()
	{
		return this._swaprule;
	}

	//*****************
	//* INNER CLASSES *
	//*****************

	/**
	 * Inner class: event of starting the main frame operation.
	 */
	class ActionStart implements ActionListener {
		/**
		 * Starts the main frame operation.
		 * @param e Action Event
		 */
		public void actionPerformed(final ActionEvent e) {
			setRunning(true);
		}
	} //end of inner class action exit


	/**
	 * Inner class: event of stopping the main frame operation.
	 */
	class ActionStop implements ActionListener {
		/**
		 * Stops the main frame operation.
		 * @param e Action Event
		 */
		public void actionPerformed(final ActionEvent e) {
			setRunning(false);
		}
	} //end of inner class action close


	/**
	 * Inner class: opens the about dialog window.
	 */
	class ActionOpenAboutDialog implements ActionListener {
		/**
		 * Opens the about dialog window.
		 * @param e Action Event
		 */
		public void actionPerformed(final ActionEvent e) {
			//Do Nothing
		}
	} //end of inner class action close

} //end of class
