
package com.hacktics.diviner.gui;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hacktics.diviner.constants.Titles;
import com.hacktics.diviner.payloads.AttackVectorContainer;
import com.hacktics.diviner.payloads.DelimiterSwapRuleContainer;
import com.hacktics.diviner.payloads.ExploitContainer;
import com.hacktics.diviner.payloads.PayloadContainer;
import com.hacktics.diviner.payloads.PayloadDatabaseLoader;
import com.hacktics.diviner.payloads.PlatformContainer;

public class AddNewAttack extends JDialog{
	 private JTextField txtNameField = null;
	    private JTextField txtDescriptionField = null;
	    private JTextField txtTemplateField = null;
	    
	    private JLabel lblNameField = null;
	    private JLabel lblDescriptionField = null;
	    private JLabel lblTemplateTypeField = null;
	    private boolean isSuccess;
	    private JButton btnAddNewButton = null;
	    private static PayloadManager fatherWindow = null;
	    
	    //****************
	    //* CONSTRUCTORS *
	    //****************

	    /**
	     * Custom Size Constructor.
	     * @param width The width of the frame
	     * @param height The height of the frame
	     */
	    public AddNewAttack(final int width, final int height, PayloadManager father) {
			super(fatherWindow, "Add new", ModalityType.DOCUMENT_MODAL);
			setSize(250, 400);
			setLocation(new Point(Diviner.getWindowWidth() / 2 - 250, Diviner.getWindowHeight() / 2 - 300));
	        this.fatherWindow = father;
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
	        this.setTitle(Titles.FRAME_ADD_NEW_ATTACK_TITLE);
	        this.setResizable(false); //disable window resize
	        this.setLayout(new GridLayout(7, 1)/*new GridBagLayout()*/);

	    	isSuccess = false;	        
	        txtNameField = new JTextField();
	        txtNameField.setSize(50, 25);
	        txtDescriptionField = new  JTextField();
	        txtDescriptionField.setSize(50, 25);
	        txtTemplateField = new JTextField();
	        txtTemplateField.setSize(50, 25);
						
	        lblNameField = new JLabel("Attack Name");
	        lblDescriptionField = new JLabel("Description");
	        lblTemplateTypeField = new JLabel("Template");
			
			
			
			btnAddNewButton = new JButton(Titles.FRAME_ADD_NEW_ATTACK_TITLE);
			btnAddNewButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					AddNewAttackToContainer();
				}
			});
			//JFrame AddNewScreen = new JFrame("Add Content");
		
			
			this.add(lblNameField);
			this.add(txtNameField);
			
			this.add(lblDescriptionField);
			this.add(txtDescriptionField);
			
			this.add(lblTemplateTypeField);
			this.add(txtTemplateField);
			
			this.add(btnAddNewButton);
				
			this.setVisible(true);
			
	    } //end of initialization method

	    protected void AddNewAttackToContainer() {
	    	if (!(txtNameField.getText().equals("") || txtDescriptionField.getText().equals("") || txtTemplateField.getText().equals(""))) {
		        	
		    	String name = txtNameField.getText();
		    	String desc = txtDescriptionField.getText();
		    	String template = txtTemplateField.getText();
		    	
		    	Collection<AttackVectorContainer> AttackVectors = PayloadDatabaseLoader.getAttackVectorList();
				//Make sure no duplicate names of attacks
		    	for (AttackVectorContainer AttackVector : AttackVectors) {
		    		if (AttackVector.getAttackName() == name) {
		    			return;
					}
		    	}
		    	
		    	AttackVectorContainer attack = new AttackVectorContainer(name, name, desc, template, null, null, null, null, null);
				PayloadDatabaseLoader.addNewAttackVector(attack);
				
				isSuccess = true;
		    	fatherWindow.repaintPayloadsPanels(attack.getAttackName());
		    	setVisible(false);
				dispose();
	    	}
	    	else {
				JOptionPane.showMessageDialog(this, "Fields cannot be empty");
			}
		}
	    
	    public String getAttackName() {
	    	return txtNameField.getText();
	    }
	    
	    public boolean isSuccess() {
	    	return isSuccess;
	    }
}
