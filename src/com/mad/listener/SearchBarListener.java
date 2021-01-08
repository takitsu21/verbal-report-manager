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

    private static String[][] searchCourse(String[] names) {
        String[][] tableau_final;
        boolean affichuerStat = true;
        if (Table.getTemporaryTable().length < Data.dataArray.length) {
            tableau_final = new String[Table.getTemporaryTable().length][names.length + 3];
            affichuerStat = false;
        } else {
            tableau_final = new String[Data.dataArray.length][names.length + 3];

        }
        if (getAfficheEtu().isSelected()) {
            tableau_final[0][0] = "Statistiques";
        }


        String[][] tableauStat = new String[names.length][4];
        for (int i = 0; i < names.length; i++) {


            for (int j = 0; j < Table.getTemporaryTable()[0].length; j++) {
                String currentCheck = Table.getTemporaryTable()[0][j];
                String[] splited = currentCheck.split(" - ");
                if (currentCheck.equalsIgnoreCase(names[i]) || splited[0].equalsIgnoreCase(names[i])
                        || splited[splited.length - 1].equalsIgnoreCase(names[i])) {

                    for (int k = 0; k < Table.getTemporaryTable().length; k++) {
                        tableau_final[k][0] = Table.getTemporaryTable()[k][0];
                        tableau_final[k][1] = Table.getTemporaryTable()[k][1];
                        tableau_final[k][2] = Table.getTemporaryTable()[k][2];
                        if (Table.getTemporaryTable()[k][j] != null && !Table.getTemporaryTable()[k][j].equals("")) {
                            tableau_final[k][i + 3] = Table.getTemporaryTable()[k][j];
                        }
                    }
                    if (affichuerStat) {
                        tableauStat[i][0] = Data.dataArray[Data.dataArray.length - 1][j];
                        tableauStat[i][1] = Data.dataArray[Data.dataArray.length - 2][j];
                        tableauStat[i][2] = Data.dataArray[Data.dataArray.length - 3][j];
                        tableauStat[i][3] = Data.dataArray[Data.dataArray.length - 4][j];
                    }
                }
            }
        }

        for (int i = 0; i < tableau_final.length; i++) {
            boolean enleverLigne = true;
            for (int j = 3; j < tableau_final[i].length; j++) {

                if (tableau_final[i][j] != null) {
                    enleverLigne = false;
                    break;
                }


            }
            if (enleverLigne) {

                List<String[]> list = new ArrayList<>(Arrays.asList(tableau_final));

                list.remove(i);

                tableau_final = list.toArray(new String[0][0]);
                i = i - 1;
            }

        }


        if (affichuerStat) {

            final int finalTabLength = tableau_final.length;

            tableau_final[finalTabLength - 1][0] = "Écart-type";
            tableau_final[finalTabLength - 2][0] = "Note moyenne";
            tableau_final[finalTabLength - 3][0] = "Note min";
            tableau_final[finalTabLength - 4][0] = "Note max";
            for (int i = 0; i < tableauStat.length; i++) {
                for (int j = 0; j < 4; j++) {
                    tableau_final[finalTabLength - 1 - j][i + 3] = tableauStat[i][j];
                }
            }
        }

        return tableau_final;
    }

    public static String[][] selectEtu(String[] etu) {
        List<Element> listStudents = Data.getChildren(Data.root, "student");
        List<Element> listCourses = Data.getChildren(Data.root, "course");
        String[][] data = new String[etu.length + 1][Table.getTemporaryTable()[0].length];

        for (int i = 0; i < Table.getTemporaryTable()[0].length; i++) {
            data[0][i] = Table.getTemporaryTable()[0][i];
        }
        for (int j = 1; j < etu.length + 1; j++) {
            for (Element studs : listStudents) {
                if (Table.getTemporaryTable()[1][0] != null && etu[j - 1] != null && etu[j - 1].equalsIgnoreCase(Data.read(studs, "identifier"))) {

                    List<Element> cours = Data.getChildren(studs, "grade");

                    for (int id = 0; id < Table.getTemporaryTable().length; id++) {
                        if (Table.getTemporaryTable()[id] != null && etu[j - 1].equals(Table.getTemporaryTable()[id][0])) {
                            data[j] = Table.getTemporaryTable()[id];
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

    @Override
    public void actionPerformed(ActionEvent e) {


        if (!isDoubleCalled) {
            search();
            isDoubleCalled = true;
        } else {
            isDoubleCalled = false;
        }
    }

    public void search() {
        try {
            getAfficheEtu().setSelected(false);
            Table.setTemporaryTable(Data.dataArray);
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
            String[] listSearchText = searchText.split("[ ]?&[ ]?");


            for (int index = 0; index < listSearchText.length; index++) {
                String[] listText = listSearchText[index].split("[ ]?;[ ]?");
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
                        data[0] = Table.getTemporaryTable()[0];
                        for (int i = 0; i < listNumStud.length; i++) {
                            for (int j = 1; j < Table.getTemporaryTable().length; j++) {

                                if (listNumStud[i] != null && listNumStud[i].equals(Table.getTemporaryTable()[j][0])) {

                                    data[i + 1] = Table.getTemporaryTable()[j];
                                    break;
                                }
                            }
                        }

                    }

                    HashSet<String[]> set2 = new HashSet<>();
                    for (int i = 1; i < data.length; i++) {
                        if (data[i][0] != null) {
                            set2.add(data[i]);
                        }
                    }
                    String[][] t = set2.toArray(new String[0][]);
                    String[][] dataFinal = new String[t.length + 1][];

                    dataFinal[0] = data[0];
                    System.arraycopy(t, 0, dataFinal, 1, t.length);

                    String[][] toSort = Arrays.copyOfRange(dataFinal, 1, dataFinal.length);
                    List<String[]> listData = new ArrayList<>();
                    listData.add(dataFinal[0]);
                    Arrays.sort(toSort, (o1, o2) -> {
                        if (o1 != null && o2 != null && o1[1] != null && o2[1] != null) {
                            return CharSequence.compare(o1[1], o2[1]);
                        }
                        return 0;
                    });
                    listData.addAll(Arrays.asList(toSort));
                    strArr = listData.toArray(new String[0][0]);
                }


                if (strArr[0][0] == null) {

                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Recherche incorrect", "Erreur", JOptionPane.WARNING_MESSAGE);
                    refreshTable();
                } else {
                    Table.setTemporaryTable(strArr);
                }
            }

            Table.setNewModelTable(Table.table, Table.getTemporaryTable());

        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
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
}
