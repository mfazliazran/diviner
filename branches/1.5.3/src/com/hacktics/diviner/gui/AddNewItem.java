package com.hacktics.diviner.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Dialog.ModalityType;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import com.hacktics.diviner.payloads.AttackVectorContainer;
import com.hacktics.diviner.payloads.PlatformContainer;

public class AddNewItem extends JDialog{

	protected static PayloadManager fatherWindow = null;
	protected String SelectedAttack= null;
	Collection<AttackVectorContainer> AttackVectors = null;
	protected AttackVectorContainer attack;
	private PlatformTable platformsTable;
	
	protected AddNewItem(final int width, final int height,Collection<AttackVectorContainer> PayloadAttacks,String Selectedattack, PayloadManager father) {
		super(fatherWindow, "Add new", ModalityType.DOCUMENT_MODAL);
		setSize(300, 600);
		setLocation(new Point(Diviner.getWindowWidth() / 2 - 250, Diviner.getWindowHeight() / 2 - 300));
		this.fatherWindow = father;
		this.SelectedAttack = Selectedattack;
		this.AttackVectors = PayloadAttacks;
		initialize();
	} //end of constructor


	private void initialize() {
		//Get the attack object of the selected attack
		for (AttackVectorContainer AttackVector : AttackVectors) {
			if (AttackVector.getAttackName().equals(SelectedAttack)) {
				attack = AttackVector;
				break;
			}
		}
	}
	protected JScrollPane getPlatformTable() {

		HashMap< String, PlatformContainer> platforms = attack.getPlatforms();
		
		Object[][] lines = new Object[platforms.keySet().size()][4];
		int linesIndex = 0;

		for (PlatformContainer platform : platforms.values()) {

			Object[] line = {new Boolean(true), platform.getPlatformName(), platform.getPlatformVersion(), platform.getPlatformId()};
			lines[linesIndex] = line;
			linesIndex++;

		}
		platformsTable = new PlatformTable(platforms, lines);
		platformsTable.setGridColor(Color.GRAY);
		//		table.setShowGrid(true);
		return new JScrollPane(platformsTable);

	}
	
	protected String getSelectedPlatforms() {
		return platformsTable.getSelectedPlatforms();
	}
}
