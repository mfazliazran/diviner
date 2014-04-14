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
import com.hacktics.diviner.payloads.AttackVectorContainer;
import com.hacktics.diviner.payloads.PayloadContainer;
import com.hacktics.diviner.payloads.PayloadDatabaseLoader;

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
public class AddNewPayload extends AddNewItem {




	Collection<AttackVectorContainer> AttackVectors = null;

	private JTextField txtTitleField = null;
	private JTextField txtTargetTypeField = null;
	private JTextField txtContextField = null;
	private JTextField txtValueField = null;
	private JTextField txtDescriptionField = null;
	private JTextField txtCreditField = null;
	private JTextField txtPlatformsField = null;


	private JLabel lblTitleField = null;
	private JLabel lblTargetTypeField = null;
	private JLabel lblContextField = null;
	private JLabel lblValueField = null;
	private JLabel lblDescriptionField = null;
	private JLabel lblCreditField = null;
	private JLabel lblPlatformsField = null;




	private JButton btnAddNewButton = null;

	private static int DEFAULT_WINDOW_WIDTH = 500; 
	private static int DEFAULT_WINDOW_HEIGHT = 500; 

	private JPanel panelTop;
	private JPanel panelCenter;

	//****************
	//* CONSTRUCTORS *
	//****************

	/**
	 * Custom Size Constructor.
	 * @param width The width of the frame
	 * @param height The height of the frame
	 */
	public AddNewPayload(final int width, final int height,Collection<AttackVectorContainer> PayloadAttacks,String Selectedattack, PayloadManager father) {
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
		panelTop = new JPanel(new GridLayout(12, 1));


		txtTitleField = new  JTextField();
		txtTitleField.setSize(50, 25);
		txtTargetTypeField = new JTextField();
		txtTargetTypeField.setSize(50, 25);
		txtValueField = new JTextField();
		txtValueField.setSize(50, 25);
		txtContextField = new JTextField();
		txtContextField.setSize(50, 25);
		txtDescriptionField = new JTextField();
		txtDescriptionField.setSize(50, 25);
		txtCreditField = new JTextField();
		txtCreditField.setSize(50, 25);


		lblTitleField = new JLabel("Title");
		lblTargetTypeField = new JLabel("Target Type");
		lblValueField = new JLabel("Value");
		lblContextField = new JLabel("Context");
		lblDescriptionField = new JLabel("Description");
		lblCreditField =  new JLabel("Credit");
		lblPlatformsField = new JLabel("Platforms");


		btnAddNewButton = new JButton("Add New Detection Payload");
		btnAddNewButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				AddNewPayloadToContainer();
			}
		});

		//JFrame AddNewScreen = new JFrame("Add Content");



		panelTop.add(lblTitleField);
		panelTop.add(txtTitleField);

		panelTop.add(lblTargetTypeField);
		panelTop.add(txtTargetTypeField);

		panelTop.add(lblValueField);
		panelTop.add(txtValueField);

		panelTop.add(lblContextField);
		panelTop.add(txtContextField);

		panelTop.add(lblDescriptionField);
		panelTop.add(txtDescriptionField);

		panelTop.add(lblCreditField);
		panelTop.add(txtCreditField);

		panelCenter.add(lblPlatformsField, BorderLayout.NORTH);
		panelCenter.add(super.getPlatformTable(), BorderLayout.CENTER);

		panelCenter.add(btnAddNewButton, BorderLayout.SOUTH);
		
		this.add(panelTop);
		this.add(panelCenter);
		this.setVisible(true);
	} //end of initialization method


	protected void AddNewPayloadToContainer() {
		
		String Platforms = super.getSelectedPlatforms();
		
		if (!(txtTitleField.getText().equals("")  || txtTargetTypeField.getText().equals("") || txtContextField.getText().equals("")
				&& txtValueField.getText().equals("") || txtDescriptionField.getText().equals("") ||	txtCreditField.getText().equals("") || Platforms.equals("") )) {

			String Title = txtTitleField.getText();
			String TargetType = txtTargetTypeField.getText();
			String Context = txtContextField.getText();
			String Value = txtValueField.getText();
			String Authors = txtCreditField.getText();
			String Description = txtDescriptionField.getText();
			
			PayloadContainer NewPayloadToAdd= null;
			Collection<AttackVectorContainer> AttackVectors = PayloadDatabaseLoader.getAttackVectorList();

			String id = Integer.toString(attack.getMaxDetectionId() + 1);
			NewPayloadToAdd = new PayloadContainer(id, Title, TargetType, Context, Value, Description, Authors, Platforms);
			attack.addDetectionPayload(NewPayloadToAdd, id);

			fatherWindow.repaintPayloadsPanels(SelectedAttack);
			setVisible(false);
			dispose();
		}
		else {
			JOptionPane.showMessageDialog(this, "Fields cannot be empty");
		}
	}










} //end of class
