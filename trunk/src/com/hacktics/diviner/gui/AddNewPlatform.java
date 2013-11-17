/**
 *
 */
package com.hacktics.diviner.gui;




import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Collection;


import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JButton;

import javax.swing.JLabel;

import javax.swing.JTextField;



import com.hacktics.diviner.constants.Titles;
import com.hacktics.diviner.payloads.AttackVectorContainer;

import com.hacktics.diviner.payloads.PlatformContainer;

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
public class AddNewPlatform extends JDialog {

	//**********************
	//* FIELD DECLERATIONS *
	//**********************
	/**
	 * Unique class identifier.
	 */
	private static final long serialVersionUID = 8904618772542045407L;

	private JTextField txtAliasField = null;
	private JTextField txtNameField = null;
	private JTextField txtValueField = null;

	private JLabel lblAliasField = null;
	private JLabel lblNameField = null;
	private JLabel lblValueField = null;

	private JButton btnAddNewButton = null;
	private static PayloadManager fatherWindow;
	Collection<AttackVectorContainer> AttackVectors = null;
	private String SelectedAttack= null;
	private AttackVectorContainer attack;

	//****************
	//* CONSTRUCTORS *
	//****************


	/**
	 * Custom Size Constructor.
	 * @param width The width of the frame
	 * @param height The height of the frame
	 */
	public AddNewPlatform(final int width, final int height, Collection<AttackVectorContainer> PayloadAttacks ,String Selectedattack, PayloadManager father) {
		super(fatherWindow, "Add new", ModalityType.DOCUMENT_MODAL);
		setSize(250, 400);
		setLocation(new Point(Diviner.getWindowWidth() / 2 - 250, Diviner.getWindowHeight() / 2 - 300));
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
		this.setLayout(new GridLayout(7, 1)/*new GridBagLayout()*/);

		txtAliasField = new  JTextField();
		txtAliasField.setSize(50, 25);
		txtNameField = new JTextField();
		txtNameField.setSize(50, 25);
		txtValueField = new JTextField();
		txtValueField.setSize(50, 25);

		lblAliasField = new JLabel("Platform Alias");
		lblNameField = new JLabel("Platform Name");
		lblValueField = new JLabel("Platform Version");

		//Get the attack object of the selected attack
		for (AttackVectorContainer AttackVector : AttackVectors) {
			if (AttackVector.getAttackName().equals(SelectedAttack)) {
				attack = AttackVector;
				break;
			}
		}
		
		
		
		btnAddNewButton = new JButton("Add New Platform");
		btnAddNewButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				AddNewPlatformToContainer();
			}
		});

		//JFrame AddNewScreen = new JFrame("Add Content");

		this.add(lblAliasField);
		this.add(txtAliasField);
		this.add(lblNameField);
		this.add(txtNameField);
		this.add(lblValueField);
		this.add(txtValueField);
		this.add(btnAddNewButton);

		this.setVisible(true);

	} //end of initialization method


	protected void AddNewPlatformToContainer() {
		// TODO Auto-generated method stub
		if (txtAliasField.getText() != "" &&  txtNameField.getText() != "" && txtValueField.getText() != "") {

			String PlatformAlias = txtAliasField.getText();
			String PlatformID = Integer.toString(attack.getMaxPlatformId() + 1);
			String PlatformName = txtNameField.getText();
			String PlatformVersion = txtValueField.getText();
			PlatformContainer NewPlatformToAdd = new PlatformContainer(PlatformAlias, PlatformID, PlatformName, PlatformVersion);


			attack.addPlatform(NewPlatformToAdd,PlatformID);
		
			
			fatherWindow.repaintPayloadsPanels(SelectedAttack);
			setVisible(false);
			dispose();

		}

		else {
			JOptionPane.showMessageDialog(this, "Fields cannot be empty");
		}
	}




} //end of class
