package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectRowsListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = Table.getSelectedRows();
        if (selectedRows.length > 0) {
            String[][] newModel = new String[selectedRows.length + 1][];
            newModel[0] = Data.dataArray[0];
            int acc = 1;
            for (int row : selectedRows) {
                newModel[acc++] = Table.getTemporaryTable() != null ? Table.getTemporaryTable()[row + 1] : Data.dataArray[row + 1];
            }
            Table.setTemporaryTable(newModel);
            Table.setNewModelTable(Table.table, newModel);
            Table.table.getModel().removeTableModelListener(new TableChangedListener());
            Table.table.getModel().addTableModelListener(new TableChangedListener());
        }
    }
}
