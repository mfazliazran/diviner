/**
 *
 */
package com.hacktics.diviner.gui;




import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JButton;

import javax.swing.JLabel;

import javax.swing.JTextField;


import com.hacktics.diviner.constants.Titles;
import com.hacktics.diviner.payloads.AdvancedRuleContainer;
import com.hacktics.diviner.payloads.AttackVectorContainer;

import com.hacktics.diviner.payloads.EvasionRuleContainer;

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
public class AddNewEvasionTechnique extends AddNewItem {

	//**********************
	//* FIELD DECLERATIONS *
	//**********************


	private JTextField txtTitleField = null;
	private JTextField txtTargetTypeField = null;
	private JTextField txtContextField = null;
	private JTextField txtDescriptionField = null;
	private JTextField txtCreditField = null;
	private JTextField txtPlatformsField = null;
	private JTextField txtSourceField = null;
	private JTextField txtTargetField = null;
	private JButton    btnRuleField = null;
	private JButton    btnAdvancedRuleField = null;

	private JLabel lblTitleField = null;
	private JLabel lblTargetTypeField = null;
	private JLabel lblContextField = null;
	private JLabel lblRuleField = null;
	private JLabel lblAdvancedRuleField = null;
	private JLabel lblDescriptionField = null;
	private JLabel lblCreditField = null;
	private JLabel lblPlatformsField = null;
	private JLabel lblSourceField = null;
	private JLabel lblTargetField = null;
	Collection<AttackVectorContainer> AttackVectors = null;


	private JButton btnAddNewButton = null;

	private static int DEFAULT_WINDOW_WIDTH = 500; 
	private static int DEFAULT_WINDOW_HEIGHT = 500; 

	private ArrayList<SwapRuleContainer> _rule = new ArrayList<SwapRuleContainer>();
	private ArrayList<AdvancedRuleContainer> _advancedrule = new ArrayList<AdvancedRuleContainer>();
	private JPanel panelTop;
	private JPanel panelCenter;


	private static AddNewRules AddNewRulesScreen = null;

	//****************
	//* CONSTRUCTORS *
	//****************


	/**
	 * Custom Size Constructor.
	 * @param width The width of the frame
	 * @param height The height of the frame
	 */
	public AddNewEvasionTechnique(final int width, final int height,Collection<AttackVectorContainer> PayloadAttacks,String Selectedattack, PayloadManager father) {
		super(width,height, PayloadAttacks, Selectedattack, father);
		initialize();
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
		this.setLayout(new GridLayout(2, 1));

		panelCenter = new JPanel(new BorderLayout());
		panelTop = new JPanel(new GridLayout(10, 1));

		txtTitleField = new  JTextField();
		//		txtTargetTypeField = new JTextField();
		txtContextField = new JTextField();
		txtDescriptionField = new JTextField();
		txtCreditField = new JTextField();
		txtTargetField = new JTextField();
		txtSourceField = new JTextField();
		btnRuleField = new JButton("Add New Rule");
		//		btnAdvancedRuleField = new JButton("Add New Advanced Rule");
		btnRuleField.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				AddNewSetofRules();
			}
		});

//		btnAdvancedRuleField.addActionListener(new ActionListener(){
//
//			public void actionPerformed(ActionEvent e) {
//				AddNewSetofAdvancedRules();
//			}
//		});	

		//TODO:impelement advanced rule
//		btnAdvancedRuleField.setEnabled(false);

		lblTitleField = new JLabel("Title");
		//		lblTargetTypeField = new JLabel("Target Type");
		lblRuleField = new JLabel("Rules");
		//		lblAdvancedRuleField= new JLabel("Advanced Rules");
		//		lblContextField = new JLabel("Context");
		lblDescriptionField = new JLabel("Description");
		lblSourceField = new JLabel("Source");
		lblTargetField = new JLabel("Target");

		lblCreditField =  new JLabel("Credit");
		lblPlatformsField =  new JLabel("Platforms");


		btnAddNewButton = new JButton("Add New Evasion Technique");
		btnAddNewButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				AddNewDelimiterSwapRuleContainer();
			}
		});
		//JFrame AddNewScreen = new JFrame("Add Content");

		panelTop.add(lblTitleField);
		panelTop.add(txtTitleField);

		//		panelTop.add(lblTargetTypeField);
		//		panelTop.add(txtTargetTypeField);

		//		panelTop.add(lblRuleField);
		//		panelTop.add(btnRuleField);

		//		panelTop.add(lblAdvancedRuleField);
		//		panelTop.add(btnAdvancedRuleField);

		//		panelTop.add(lblContextField);
		//		panelTop.add(txtContextField);


		panelTop.add(lblDescriptionField);
		panelTop.add(txtDescriptionField);

		panelTop.add(lblSourceField);
		panelTop.add(txtSourceField);

		panelTop.add(lblTargetField);
		panelTop.add(txtTargetField);

		panelTop.add(lblCreditField);
		panelTop.add(txtCreditField);



		panelCenter.add(lblPlatformsField, BorderLayout.NORTH);
		panelCenter.add(super.getPlatformTable(), BorderLayout.CENTER);

		panelCenter.add(btnAddNewButton, BorderLayout.SOUTH);

		this.add(panelTop);
		this.add(panelCenter);
		this.setVisible(true);

	} //end of initialization method


	protected void AddNewSetofAdvancedRules() {
		// TODO Auto-generated method stub

	}


	protected void AddNewSetofRules() {
		// TODO Auto-generated method stub
		AddNewRulesScreen = new AddNewRules(this);
	}


	protected void AddNewDelimiterSwapRuleContainer() {
		String Platforms = super.getSelectedPlatforms();


		if (!(txtTitleField.getText().equals("") ||  txtSourceField.getText().equals("") || txtTargetField.getText().equals("") ||/*txtTargetTypeField.getText().equals("") ||  txtContextField.getText().equals("")
				||*/ txtDescriptionField.getText().equals("") || txtCreditField.getText().equals("") || Platforms.equals(""))) {

			String Title = txtTitleField.getText();
			//			String Target = txtTargetTypeField.getText();
			String Context = txtContextField.getText();
			String Description = txtDescriptionField.getText();
			String Credit = txtCreditField.getText();
			String Source = txtSourceField.getText();
			String Target = txtTargetField.getText();


			ArrayList<SwapRuleContainer>  Rule = new ArrayList<>();

			Rule.add(new SwapRuleContainer(Source, Target));

			ArrayList<AdvancedRuleContainer>  AdvancedRule = null;
			if (this.getAdvancedRule() != null) {
				AdvancedRule = this.getAdvancedRule();
			}

			String id = Integer.toString(attack.getMaxEvasionId() + 1);
			EvasionRuleContainer NewEvasionToAdd = new EvasionRuleContainer(id, Title, "", Context, Rule, AdvancedRule, Description, Credit, Platforms);
			attack.addEvasionRule(NewEvasionToAdd, id);

			fatherWindow.repaintPayloadsPanels(SelectedAttack);
			setVisible(false);
			dispose();
		}
	
		else {
			JOptionPane.showMessageDialog(this, "Fields cannot be empty");
		}
	}



//*******************
//* COVERED METHODS *
//*******************


public void setRule(ArrayList<SwapRuleContainer> rule)
{
	this._rule = rule;
}

public ArrayList<SwapRuleContainer> getRule()
{
	return this._rule;
}
public void setAdvancedRule(ArrayList<AdvancedRuleContainer> advancedrule)
{
	this._advancedrule = advancedrule;
}

public ArrayList<AdvancedRuleContainer> getAdvancedRule()
{
	return this._advancedrule;
}


} //end of class
