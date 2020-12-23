package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SearchBarListener extends Application implements ActionListener {


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //String searchText = (String) getSearchBar().getSelectedItem();
            String searchText = (String) getSearchBar().getSelectedItem();
            searchText = searchText.trim();
            String[] listText = searchText.split(";");
            String[] etu;
            boolean isListStuds = true;
            if (listText.length == 1) {
                if (listText[0].startsWith("2") && listText[0].length() < 8) {
                    searchInTable(Table.table, listText[0]);
                }
                if (listText[0].startsWith("2") && listText[0].length() == 8) {
                    selectEtu(listText);
                }
                else {
                    searchCourse(listText);
                }

            } else if (listText.length > 1) {
                for (String s : listText) {
                    if ((s.length() != 8) || !(s.startsWith("2"))) {
                        isListStuds = false;
                    }
                }
                if (isListStuds) {
                    selectEtu(listText);
                }
                else {
                    searchCourse(listText);
                }
            }

        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }


    private void searchCourse(String[] names) {
        String[][] tableau_final = new String[Data.dataArray.length][names.length + 1];

        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < Data.dataArray[0].length; j++) {
                String currentCheck = Data.dataArray[0][j];
                String[] splited = currentCheck.split(" - ");   //.map(String::toLowerCase).toArray(String[]::new);
                if (currentCheck.equals(names[i]) || splited[0].equals(names[i]) || splited[splited.length - 1].equals(names[i])) {
                    for (int k = 0; k < Data.dataArray.length; k++) {
                        if(! Data.dataArray[k][j].isEmpty()) {
                            tableau_final[k][i + 1] = Data.dataArray[k][j];
                        }
                    }
                }
            }
        }
        final int finalTabLength = tableau_final.length;
        tableau_final[0][0] = "Statistiques";
        tableau_final[finalTabLength - 1][0] = "Écart-type";
        tableau_final[finalTabLength - 2][0] = "Note moyenne";
        tableau_final[finalTabLength - 3][0] = "Note min";
        tableau_final[finalTabLength - 4][0] = "Note max";

        Table.setNewModelTable(Table.table, tableau_final);
    }


    public static void selectEtu(String[] etu) {
        List<Element> listStudents = Data.getChildren(Data.root, "student");
        List<Element> listCourses = Data.getChildren(Data.root, "course");
        //int id = 1;
        String[][] data = new String[1][1];
        data = new String[etu.length + 1][Data.dataArray[0].length];
        for (int i=0;i<Data.dataArray[0].length;i++){
            data[0][i]=Data.dataArray[0][i];
        }


        for (int j=1; j<etu.length+1;j++) {
            for (Element studs : listStudents) {
                if (etu[j-1].equalsIgnoreCase(XmlToCsv.read(studs, "identifier"))) {

                    List<Element> cours = Data.getChildren(studs, "grade");

                    for(int id=0;id<Data.dataArray.length;id++){
                        if(etu[j - 1].equals(Data.dataArray[id][0])){
                            data[j]=Data.dataArray[id];
                            break;
                        }
                    }


                    for (Element cour : cours) {
                        String coursTest = XmlToCsv.read(cour, "item");
                        int trouver = 0;
                        for (int m = 3; m < data[0].length; m++) {
                            if (coursTest.equals(data[0][m].split(" - ")[0])) {
                                trouver = 1;
                                break;
                            }
                        }
                        if (trouver == 0) {
                            for (int i=0;i<data.length;i++) {
                                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                            }
                            for (Element element : listCourses) {
                                if (coursTest.equals(XmlToCsv.read(element, "identifier"))) {
                                    data[0][data[0].length - 1] = coursTest + " - " + XmlToCsv.read(element, "name");
                                    break;
                                }
                            }

                            data[j][data[0].length - 1] = XmlToCsv.read(cour, "value");
                            System.out.println(data[0][data[0].length - 1]);
                        }
                    }


                    break;

                }
            }
        }
        Table.setNewModelTable(Table.table, data);
    }



    private static String[] searchInTable(JTable table, String searchText) {
        String [] num = null;


        RowSorter<? extends TableModel> rs = table.getRowSorter();
        if (rs == null) {
            table.setAutoCreateRowSorter(true);
            rs = table.getRowSorter();
        }
        TableRowSorter<? extends TableModel> rowSorter = (TableRowSorter<? extends TableModel>) rs;
        if (searchText.length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText)));
            //System.out.println(rowSorter.getModel().getValueAt(0,1));
            num = new String[table.getRowCount()];

            for(int row = 0; row < table.getRowCount(); row++) {
                num[row] = (String) table.getModel().getValueAt(table.convertRowIndexToModel(row), 0);
            }

        }


        return num;
    }
}
