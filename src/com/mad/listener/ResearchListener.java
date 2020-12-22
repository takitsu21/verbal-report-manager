package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;
import com.mad.util.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class ResearchListener extends Application implements ActionListener {
    public void actionPerformed(ActionEvent e) {

        String[] names = Stream.of(getSearchBar().getText().split(";")).
                map(String::toLowerCase).toArray(String[]::new);
        System.out.println(Arrays.toString(names));

        String[][] tableau_final = new String[Data.dataArray.length][names.length + 1];

        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < Data.dataArray[0].length; j++) {
                String currentCheck = Data.dataArray[0][j];
                String[] splited = Stream.of(currentCheck.split(" - ")).
                        map(String::toLowerCase).toArray(String[]::new);
                if (currentCheck.equals(names[i]) || splited[0].equals(names[i]) || splited[splited.length - 1].
                        equals(names[i])) {
                    for (int k = 0; k < Data.dataArray.length; k++) {
                        tableau_final[k][i + 1] = Data.dataArray[k][j];
                    }
                }
            }
        }
        final int finalTabLength = tableau_final.length;
        tableau_final[0][0] = "";
        tableau_final[finalTabLength - 1][0] = "Écart-type";
        tableau_final[finalTabLength - 2][0] = "Note moyenne";
        tableau_final[finalTabLength - 3][0] = "Note min";
        tableau_final[finalTabLength - 4][0] = "Note max";

        Table.setNewModelTable(getDisplayCsv().table, tableau_final);
    }
}
