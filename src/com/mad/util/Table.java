package com.mad.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Table {
    public static JTable table;
    private static String[][] temporaryTable;
    public JScrollPane Jscroll;
    private StringBuilder csv = new StringBuilder();

    public Table() {
    }

    public static String[][] sDataToArray(String data) {
        String[] ligne = data.split("\"\n\"");
        String[][] tableau = new String[ligne.length][];

        for (int i = 0; i < ligne.length; ++i) {
            tableau[i] = ligne[i].split("\",\"");
        }

        tableau[0][0] = tableau[0][0].replace("\"", "");
        tableau[tableau.length - 1][tableau[0].length - 1] = tableau[tableau.length - 1][tableau[0].length - 1].replace("\"", "");
        Data.setDataArray(tableau);
        return tableau;
    }

    public static String[][] getTemporaryTable() {
        return temporaryTable;
    }

    public static void setTemporaryTable(String[][] newTableData) {
        temporaryTable = newTableData;
    }

    public static void setNewModelTable(JTable table, String[][] newTableData) {
        TableModel tm = new DefaultTableModel(Arrays.copyOfRange(newTableData, 1, newTableData.length), newTableData[0]);
        table.setModel(tm);
    }

    public static int[] getSelectedRows() {
        return table.getSelectedRows();
    }

    public void setCsv(String csv) {
        this.csv = new StringBuilder(csv);
    }

    public void TableXML(String path, String data) {
        XmlToCsv xmlConverter = new XmlToCsv(path);
        xmlConverter.convert();
        this.setCsv(data);
        String[][] tableau = sDataToArray(data);
        TableModel tm = new DefaultTableModel(Arrays.copyOfRange(tableau, 1, tableau.length), tableau[0]);
        table = new JTable(tm);
        table.setAutoResizeMode(0);
        this.Jscroll = new JScrollPane(table);
    }

    public void TableCSV(String path) throws IOException {
        FileReader fr = new FileReader(path);
        FileReader fr_count = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        BufferedReader br_count = new BufferedReader(fr_count);
        String ln = br_count.readLine();

        int nbline;
        for (nbline = 1; ln != null; ln = br_count.readLine()) {
            ++nbline;
        }

        br_count.close();
        String line = br.readLine();
        this.csv.append(line).append('\n');
        String[] column = line.split("\",\"");
        column[0] = column[0].replace("\"", "");
        column[column.length - 1] = column[column.length - 1].replace("\"", "");
        String[][] data = new String[nbline - 2][column.length];
        int i = 0;
        line = br.readLine();
        String[] temp = line.split("\",\"");

        while (line != null) {
            this.csv.append(line).append('\n');
            System.arraycopy(temp, 0, data[i], 0, temp.length);
            data[i][temp.length - 1] = data[i][temp.length - 1].replace("\"", "");
            data[i][0] = data[i][0].replace("\"", "");
            line = br.readLine();
            if (line != null) {
                temp = line.split("\",\"");
            }

            if (i < nbline - 1) {
                ++i;
            }
        }

        String[][] tableau = new String[data.length + 1][];
        tableau[0] = column;
        System.arraycopy(data, 0, tableau, 1, data.length);
        Data.setDataArray(tableau);
        table = new JTable(data, column);
        table.setAutoResizeMode(0);
        this.Jscroll = new JScrollPane(table);
    }
}
