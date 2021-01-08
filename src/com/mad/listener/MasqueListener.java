package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MasqueListener extends AbstractApplication implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getAfficheEtu().isSelected()) {
            String[][] newTable;

            try {
                newTable = new String[Table.getTemporaryTable().length][];
                for (int i = 0; i < Table.getTemporaryTable().length; i++) {

                    newTable[i] = Arrays.copyOfRange(Table.getTemporaryTable()[i], 3, Table.getTemporaryTable().length);
                }
            } catch (NullPointerException exc) {
                System.out.println(exc.getMessage());
                newTable = new String[Data.dataArray.length][];
                for (int i = 0; i < Data.dataArray.length; i++) {

                    newTable[i] = Arrays.copyOfRange(Data.dataArray[i], 3, Data.dataArray.length);
                }
            }
            Table.setNewModelTable(Table.table, newTable);
        } else {
            Table.setNewModelTable(Table.table, Data.dataArray);
        }
    }


}
