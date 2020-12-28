package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectRowsListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = getDisplayCsv().getSelectedRows();
        if (selectedRows.length > 0) {
            String[][] newModel = new String[selectedRows.length + 1][];
            newModel[0] = Data.dataArray[0];
            int acc = 1;
            for (int row : selectedRows) {
                newModel[acc++] = Data.dataArray[row + 1];
            }
            Table.table.getModel().removeTableModelListener(new TableChangedListener());
            Table.setNewModelTable(Table.table, newModel);
            Table.table.getModel().addTableModelListener(new TableChangedListener());
        }
    }
}
