/**
 *
 */
package com.hacktics.diviner.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javax.swing.JButton;


import javax.swing.JLabel;
import javax.swing.JTextField;




import com.hacktics.diviner.constants.Titles;

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
public class AddNewRules extends BaseJFrame {

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
    
    private JTextField txtIDField = null;
    private JTextField txtSourceField = null;
    private JTextField txtTargetField = null;
  	
    private JLabel lblIDField = null;
    private JLabel lblSourceField = null;
    private JLabel lblTargetField = null;
    
    private JButton btnAddNewButton = null;
    
	private static int DEFAULT_WINDOW_WIDTH = 500; 
	private static int DEFAULT_WINDOW_HEIGHT = 500; 
	
	private AddNewDelimiterSwapRule fatherWindow = null;
	private AddNewEvasionTechnique EfatherWindow = null;
	
	
	DelimiterSwapRuleContainer SelectedDelimiter = null;
	private static ArrayList<SwapRuleContainer> SwapRules = new ArrayList<SwapRuleContainer>();
		    
    //****************
    //* CONSTRUCTORS *
    //****************

    /**
     * Default Constructor.
     */
    public AddNewRules() {
        super(DEFAULT_WINDOW_WIDTH,DEFAULT_WINDOW_HEIGHT);
        this.initialize();
    } //end of default constructor


    /**
     * Custom Size Constructor.
     * @param width The width of the frame
     * @param height The height of the frame
     */
    public AddNewRules(AddNewDelimiterSwapRule father) {
    	super(DEFAULT_WINDOW_WIDTH,DEFAULT_WINDOW_HEIGHT);
        this.fatherWindow = father;
        this.initialize();
    } //end of constructor
    public AddNewRules(AddNewEvasionTechnique father) {
    	super(DEFAULT_WINDOW_WIDTH,DEFAULT_WINDOW_HEIGHT);
        this.EfatherWindow = father;
        this.initialize();
    }


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
        this.setLayout(new GridLayout(7, 1)/*new GridBagLayout()*/);

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
		
		
		txtIDField = new JTextField();
		txtIDField.setSize(50, 25);
		txtSourceField = new JTextField();
		txtSourceField.setSize(50, 25);
		txtTargetField = new JTextField();
		txtTargetField.setSize(50, 25);
		
		lblIDField = new JLabel("Rule ID");
		lblSourceField = new JLabel("Source");
		lblTargetField = new JLabel("Target");
		
		btnAddNewButton = new JButton("Add New Rule");
		btnAddNewButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				AddNewPlatformToContainer();
			}
		});
		
		//JFrame AddNewScreen = new JFrame("Add Content");
	
		this.add(lblIDField);
		this.add(txtIDField);
		
		this.add(lblSourceField);
		this.add(txtSourceField);
		
		this.add(lblTargetField);
		this.add(txtTargetField);
		
		this.add(btnAddNewButton);
			
		this.setVisible(true);
		
    } //end of initialization method


    protected void AddNewPlatformToContainer() {
		// TODO Auto-generated method stub
    	String Source = txtSourceField.getText();
    	String Target = txtTargetField.getText();
    	SwapRuleContainer NewRuleToAdd = new SwapRuleContainer(Source, Target);
    	SwapRules.add(NewRuleToAdd);
    	if (fatherWindow != null) {
    		fatherWindow.setSwapRule(SwapRules);	
		}
    	else {
			EfatherWindow.setRule(SwapRules);
		}
    	setVisible(false);
    	dispose();
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
