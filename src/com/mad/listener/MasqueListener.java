package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MasqueListener extends AbstractApplication implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getAfficheEtu().isSelected()) {
            String[][] newTable = new String[Data.dataArray.length][];
            for (int i = 0; i < Data.dataArray.length; i++) {
                newTable[i] = Arrays.copyOfRange(Data.dataArray[i], 3, Data.dataArray.length);
            }
            Table.setNewModelTable(Table.table, newTable);
            Table.table.getModel().addTableModelListener(new TableChangedListener());
        } else {
            Table.setNewModelTable(Table.table, Data.dataArray);
        }
    }
}
