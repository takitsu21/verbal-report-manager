package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class SelectRowsListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = getDisplayCsv().getSelectedRows();
        if (selectedRows.length > 0) {
            String[] stud = new String[selectedRows.length];
            //newModel[0] = Data.dataArray[0];
            int acc = 0;
            for (int row : selectedRows) {
                stud[acc++] = Data.dataArray[row + 1][0];
            }
            //getSearchComboBox().getEditor().setItem(String.join(";",stud));
            setInfoSearchComboBox(String.join(";",stud));

            new SearchBarListener().actionPerformed(e);
            //Table.table.getModel().removeTableModelListener(new TableChangedListener());
            //Table.setNewModelTable(Table.table, newModel);
            //Table.table.getModel().addTableModelListener(new TableChangedListener());
        }
    }
}
