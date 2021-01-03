package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class SearchBarListener extends AbstractApplication implements ActionListener {
    private static boolean isDoubleCalled = false;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (! isDoubleCalled){
        search();
        isDoubleCalled = true;}
        else{isDoubleCalled = false;}
    }

    public void search() {
        try {
            Table.setNewModelTable(Table.table, Data.dataArray);
            String searchText;
            if (getInfoSearchComboBox() != null) {
                searchText = getInfoSearchComboBox();
                setInfoSearchComboBox(null);
            } else {
                searchText = (String) getSearchComboBox().getSelectedItem();

                try {
                    searchText = searchText.trim();
                } catch (NullPointerException exc) {
                    return;
                }
            }

            String[] listText = searchText.split("[ ]?;[ ]?");


            String[] ligne = {};
            for (String s : listText) {

                String[] etu = searchInTable(Table.table, s);

                int oldLength = ligne.length;
                ligne = Arrays.copyOf(ligne, (ligne.length + etu.length));
                System.arraycopy(etu, 0, ligne, oldLength, etu.length);

            }


            HashSet<String> set = new HashSet<>();
            for (String s : ligne) {
                if (!s.equals("Note max") && !s.equals("Note min") && !s.equals("Note moyenne") && !s.equals("Écart-type")) {
                    set.add(s);
                }
            }

            String[] listNumStud = set.toArray(new String[0]);
            String[][] strArr;

            if (listNumStud.length == 0) {

                strArr = searchCourse(listText);
            } else {
                String[][] data = new String[listNumStud.length + 1][];
                if (getPath().endsWith(".xml")) {
                    data = selectEtu(listNumStud);
                }
                if (getPath().endsWith(".csv")) {
                    data[0] = Data.dataArray[0];
                    for (int i = 0; i < listNumStud.length; i++) {
                        for (int j = 1; j < Data.dataArray.length; j++) {

                            if (listNumStud[i] != null && listNumStud[i].equals(Data.dataArray[j][0])) {

                                data[i + 1] = Data.dataArray[j];
                                break;
                            }
                        }
                    }

                }

                String[][] toSort = Arrays.copyOfRange(data, 1, data.length);
                List<String[]> listData = new ArrayList<>();
                listData.add(data[0]);
                Arrays.sort(toSort, (o1, o2) -> {
                    if (o1[1] != null && o2[1] != null) {
                        return CharSequence.compare(o1[1], o2[1]);
                    }
                    return 0;
                });
                listData.addAll(Arrays.asList(toSort));
                strArr = listData.toArray(new String[0][0]);
            }



            if (strArr[0][1] == null) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Recherche incorrect", "Erreur", JOptionPane.WARNING_MESSAGE);
                refreshTable();
            } else {
                Table.setNewModelTable(Table.table, strArr);
            }


        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }


    private static String[][] searchCourse(String[] names) {
        String[][] tableau_final = new String[Data.dataArray.length][names.length + 1];
        int[] decalage = new int[names.length];
        String[][] tableauStat = new String[names.length][4];
        for (int i = 0; i < names.length; i++) {
            decalage[i] = 0;
            for (int j = 0; j < Data.dataArray[0].length; j++) {
                String currentCheck = Data.dataArray[0][j];
                String[] splited = currentCheck.split(" - "); //.map(String::toLowerCase).toArray(String[]::new);
                if (currentCheck.equalsIgnoreCase(names[i]) || splited[0].equalsIgnoreCase(names[i]) || splited[splited.length - 1].equalsIgnoreCase(names[i])) {
                    for (int k = 0; k < Data.dataArray.length - 4; k++) {
                        if (!Data.dataArray[k][j].equals("")) {
                            tableau_final[k - decalage[i]][i + 1] = Data.dataArray[k][j];
                        } else {
                            decalage[i] += 1;
                        }
                    }
                    tableauStat[i][0] = Data.dataArray[Data.dataArray.length - 1][j];
                    tableauStat[i][1] = Data.dataArray[Data.dataArray.length - 2][j];
                    tableauStat[i][2] = Data.dataArray[Data.dataArray.length - 3][j];
                    tableauStat[i][3] = Data.dataArray[Data.dataArray.length - 4][j];
                }
            }
        }

        int decalageMin = Integer.MAX_VALUE;

        for (int k : decalage) {
            if (k < decalageMin)
                decalageMin = k;
        }
        final int finalTabLength = tableau_final.length;
        tableau_final[0][0] = "Statistiques";
        tableau_final[finalTabLength - 1 - decalageMin][0] = "Écart-type";
        tableau_final[finalTabLength - 2 - decalageMin][0] = "Note moyenne";
        tableau_final[finalTabLength - 3 - decalageMin][0] = "Note min";
        tableau_final[finalTabLength - 4 - decalageMin][0] = "Note max";
        for (int i = 0; i < tableauStat.length; i++) {
            for (int j = 0; j < 4; j++) {
                tableau_final[finalTabLength - 1 - decalageMin - j][i + 1] = tableauStat[i][j];
            }
        }
        return tableau_final;
    }


    public static String[][] selectEtu(String[] etu) {
        List<Element> listStudents = Data.getChildren(Data.root, "student");
        List<Element> listCourses = Data.getChildren(Data.root, "course");
        String[][] data = new String[etu.length + 1][Data.dataArray[0].length];

        for (int i = 0; i < Data.dataArray[0].length; i++) {
            data[0][i] = Data.dataArray[0][i];
        }
        for (int j = 1; j < etu.length + 1; j++) {
            for (Element studs : listStudents) {
                if (etu[j - 1] != null && etu[j - 1].equalsIgnoreCase(Data.read(studs, "identifier"))) {

                    List<Element> cours = Data.getChildren(studs, "grade");

                    for (int id = 0; id < Data.dataArray.length; id++) {
                        if (etu[j - 1].equals(Data.dataArray[id][0])) {
                            data[j] = Data.dataArray[id];
                            break;
                        }
                    }

                    for (Element cour : cours) {
                        String coursTest = Data.read(cour, "item");
                        int trouver = 0;
                        for (int m = 3; m < data[0].length; m++) {
                            if (coursTest != null && coursTest.equals(data[0][m].split(" - ")[0])) {
                                trouver = 1;
                                break;
                            }
                        }
                        if (trouver == 0) {
                            for (int i = 0; i < data.length; i++) {
                                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                            }
                            for (Element element : listCourses) {
                                if (coursTest != null && coursTest.equals(Data.read(element, "identifier"))) {
                                    data[0][data[0].length - 1] = coursTest + " - " + Data.read(element, "surname");
                                    break;
                                }
                            }

                            data[j][data[0].length - 1] = Data.read(cour, "value");
                        }
                    }
                    break;
                }
            }
        }
        return data;
    }


    private String[] searchInTable(JTable table, String searchText) {
        String[] num = new String[0];
        RowSorter<? extends TableModel> rs = Table.table.getRowSorter();
        if (rs == null) {
            table.setAutoCreateRowSorter(true);
            rs = table.getRowSorter();
        }
        TableRowSorter<? extends TableModel> rowSorter = (TableRowSorter<? extends TableModel>) rs;
        if (searchText.length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.addRowSorterListener(new RowSorterListener() {
                @Override
                public void sorterChanged(RowSorterEvent e) {
                    table.getModel().addTableModelListener(new TableChangedListener());
                }
            });
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText)));

            num = new String[table.getRowCount()];

            for (int row = 0; row < table.getRowCount(); row++) {

                num[row] = (String) table.getModel().getValueAt(table.convertRowIndexToModel(row), 0);
            }
        }
        return num;
    }

    private static String[][] searchInCsv(String[] etu) {
        String[][] data = new String[etu.length][];
        for (int i = 0; i < etu.length; i++) {
            for (int j = 0; j < Data.dataArray.length; j++) {
                if (etu[i].equals(Data.dataArray[j][0])) {
                    //System.out.println(Arrays.toString(Data.dataArray[j]));
                    data[i] = Data.dataArray[j];
                    break;
                }
            }
        }
        return data;
    }
}
