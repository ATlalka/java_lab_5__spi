package main;

import api.DataSet;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class MyTableModel implements TableModel {

    private DataSet dataSet = new DataSet();

    public MyTableModel(String [][] data, String []headers){
        dataSet.setData(data);
        dataSet.setHeader(headers);
    }

    @Override
    public int getRowCount() {
        return dataSet.getData().length;
    }

    @Override
    public int getColumnCount() {
        return dataSet.getData()[0].length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return dataSet.getHeader()[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataSet.getData()[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
