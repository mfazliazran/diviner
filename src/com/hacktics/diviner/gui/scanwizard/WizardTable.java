package com.hacktics.diviner.gui.scanwizard;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Eran Tamari
 *
 */
public class WizardTable extends AbstractTableModel{

	private String [] columnNames;
	protected ArrayList<Object[]> rowData;
	private boolean isCellsEditable;
	public WizardTable(ArrayList<Object[]> rowData, String[] columnNames, boolean isCellsEditable) {
		super();
		this.columnNames = columnNames;
		this.rowData = rowData;
		this.isCellsEditable = isCellsEditable;
	
	}

	public WizardTable(Object[][] rowData, String[] columnNames, boolean isCellsEditable) {
		super();
		this.columnNames = columnNames;
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		//Convert to array
		for (Object[] line : rowData) {
			result.add(line);
		}
		this.rowData = result;
		this.isCellsEditable = isCellsEditable;
	
	}
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	
	@Override
	public int getRowCount() {
		return rowData.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return rowData.get(rowIndex)[columnIndex];
	}
	
	/*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
            return isCellsEditable;
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        
        rowData.get(row)[col] = value;

       
    }
    
    public void addNewRow(Object[] row) {
    	rowData.add(row);
    	fireTableRowsInserted(rowData.size(), rowData.size());
    }
	
    //Active means that the last line is true
    public ArrayList<Object[]> getIsLastColActiveRows() {
    	ArrayList<Object[]> activeRows = new ArrayList<Object[]>();
    	for (Object[] row : rowData) {

    		if ((Boolean)row[2]) {
    			activeRows.add(row);
    		}
    	}
    	return activeRows;
    }
    

}
