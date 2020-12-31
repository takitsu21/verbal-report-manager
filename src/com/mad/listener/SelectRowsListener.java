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

        int[] selectedRows = Table.getSelectedRows();
        if (selectedRows.length > 0) {
            String[] stud = new String[selectedRows.length];
            int acc = 0;
            System.out.println(Arrays.toString(selectedRows));
            for (int row : selectedRows) {
                stud[acc++] = Table.getTemporaryTable()!=null ? Table.getTemporaryTable()[row + 1][0] : Data.dataArray[row + 1][0];
            }
            setInfoSearchComboBox(String.join(";", stud));
            new SearchBarListener().actionPerformed(e);
        }
    }
}
