package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MasqueListener extends AbstractApplication implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getAfficheEtu().isSelected()) {
            String[][] newTable;
            newTable = new String[Table.getTemporaryTable().length][Table.getTemporaryTable()[0].length - 2];
            if (Table.getTemporaryTable()[Table.getTemporaryTable().length - 1][0].equals("Écart-type")) {

                for (int i = 0; i < Table.getTemporaryTable().length; i++) {
                    System.arraycopy(Table.getTemporaryTable()[i], 3, newTable[i], 1, Table.getTemporaryTable()[i].length - 3);
                }

                newTable[0][0] = "Statistiques";

                newTable[newTable.length - 1][0] = "Écart-type";
                newTable[newTable.length - 2][0] = "Note moyenne";
                newTable[newTable.length - 3][0] = "Note min";
                newTable[newTable.length - 4][0] = "Note max";
            } else {
                for (int i = 0; i < Table.getTemporaryTable().length; i++) {
                    newTable[i] = Arrays.copyOfRange(Table.getTemporaryTable()[i], 3, Table.getTemporaryTable()[i].length);
                }
            }
            Table.setNewModelTable(Table.table, newTable);
        } else {

            Table.setNewModelTable(Table.table, Table.getTemporaryTable());
            Table.table.getModel().removeTableModelListener(new TableChangedListener());
            Table.table.getModel().addTableModelListener(new TableChangedListener());
        }
    }


}
