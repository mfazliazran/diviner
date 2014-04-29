package com.hacktics.diviner.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.hacktics.diviner.gui.PayloadManager.buttonTableCellEditor;
import com.hacktics.diviner.gui.PayloadManager.buttonTableCellRenderer;
import com.hacktics.diviner.gui.PayloadManager.labelTableCellEditor;
import com.hacktics.diviner.gui.PayloadManager.labelTableCellRenderer;
import com.hacktics.diviner.gui.scanwizard.WizardTable;
import com.hacktics.diviner.payloads.PlatformContainer;

public class PlatformTable extends JTable{

	private static String[] COLUMNS = {"Is Affected", "Name", "Version", "ID"}; 
	private PlatformModel model;
	public PlatformTable (HashMap< String, PlatformContainer> platforms, Object[][] lines) {
		super(lines, COLUMNS);

		this.setGridColor(Color.GRAY);
		this.setShowGrid(true);
		model = new PlatformModel(lines, COLUMNS, true);
		this.setModel(model);
		removeColumn(this.getColumnModel().getColumn(3));//Dont show id in GUI
	}

	public String getSelectedPlatforms() {
		return this.model.getSelectedPlatforms();
	}

	public class PlatformModel extends WizardTable{

		
		public PlatformModel(Object[][] rowData, String[] columnNames,
				boolean isCellsEditable) {
			super(rowData, columnNames, isCellsEditable);
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return (col == 0);
		}

		//Active means that the first line is true
		private ArrayList<Object[]> getIsFirstColActiveRows() {
			ArrayList<Object[]> activeRows = new ArrayList<Object[]>();
			for (Object[] row : rowData) {

				if ((Boolean)row[0]) {
					activeRows.add(row);
				}
			}
			return activeRows;
		}


		public String getSelectedPlatforms() {
			StringBuilder sb = new StringBuilder();
			String prefix = "";
			for (Object[] line : getIsFirstColActiveRows()) {
				sb.append(prefix);
				prefix = ",";
				sb.append(line[3]);
			}
			return sb.toString();
		}
	}
}