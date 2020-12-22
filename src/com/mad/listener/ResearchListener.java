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

public class ResearchListener extends Application implements ActionListener {
    public void actionPerformed(ActionEvent e) {

        String[] names={"SLUIN501","SLUIN602 - Compilation"};
        Data d= new Data();
        String[][] tableau=d.dataArray;

        String[][] tableau_final=new String[tableau.length][names.length+1];

        for(int i=0; i<names.length; i++) {
            for (int j = 0; j < tableau[0].length; j++) {
                if (tableau[0][j].equals(names[i]) || tableau[0][j].split(" - ")[0].equals(names[i]) || tableau[0][j].split(" - ")[tableau[0][j].split(" - ").length-1].equals(names[i])) {
                    for (int k = 0; k < tableau.length; k++) {
                        tableau_final[k][i+1] = tableau[k][j];
                    }
                }
            }
        }
        tableau_final[0][0]="";
        tableau_final[tableau_final.length-1][0]="Écart-type";
        tableau_final[tableau_final.length-2][0]="Note moyenne";
        tableau_final[tableau_final.length-3][0]="Note min";
        tableau_final[tableau_final.length-4][0]="Note max";

        TableModel tm = new DefaultTableModel(Arrays.copyOfRange(tableau_final, 1, tableau.length), tableau_final[0]);
        getDisplayCsv().table.setModel(tm);
    }
}
