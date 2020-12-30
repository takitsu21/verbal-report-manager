package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectRowsListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = getDisplayCsv().getSelectedRows();
        if (selectedRows.length > 0) {
            String[] stud = new String[selectedRows.length];
            int acc = 0;
            for (int row : selectedRows) {
                stud[acc++] = Data.dataArray[row + 1][0];
            }
            setInfoSearchComboBox(String.join(";", stud));
            new SearchBarListener().actionPerformed(e);
        }
    }
}
