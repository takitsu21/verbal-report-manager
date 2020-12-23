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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SearchBarListener extends Application implements ActionListener {


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //String searchText = (String) getSearchBar().getSelectedItem();
            String searchText = (String) getSearchBar().getSelectedItem();
            searchText = searchText.trim();

            if (searchText.length() > 0) {
                selectEtu(searchText.split(";"), getPath());
                //searchCourse(searchBarText);
                //searchInTable(Table.table, searchText);
            }

        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }



    private void searchCourse(String searchBarText) {
        String[] names = searchBarText.split(";");


        String[][] tableau_final = new String[Data.dataArray.length][names.length + 1];

        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < Data.dataArray[0].length; j++) {
                String currentCheck = Data.dataArray[0][j];
                String[] splited = currentCheck.split(" - ");   //.map(String::toLowerCase).toArray(String[]::new);
                if (currentCheck.equals(names[i]) || splited[0].equals(names[i]) || splited[splited.length - 1].equals(names[i])) {
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

        Table.setNewModelTable(Table.table, tableau_final);
    }

    public static void selectEtu(String[] etu, String path) {
        List<Element> listStudents = Data.getChildren(Data.root, "student");
        List<Element> listCourses = Data.getChildren(Data.root,"course");
        int id = 1;
        String[][] data = new String[1][1];
        for (String e : etu) {
//            System.out.println(e);
            for (Element studs : listStudents) {
                if (e.equalsIgnoreCase(XmlToCsv.read(studs, "identifier"))) {
                    List<Element> cours = Data.getChildren(studs, "grade");
                    if (id == 1) {
                        data = new String[etu.length + 1][cours.size() + 3];
                    }

                    data[0][0] = "N° Étudiant";
                    data[0][1] = "Nom";
                    data[0][2] = "Prénom";
                    for (int i = 3; i < cours.size() + 3; i++) {
                        String item = XmlToCsv.read(cours.get(i - 3), "item");
                        String courseName =XmlToCsv.read(XmlToCsv.findCourseByCode(listCourses,XmlToCsv.read(cours.get(i - 3), "item")),"name") ;
                        data[0][i] = String.format("%s - %s",item,courseName);//XmlToCsv.read(cours.get(i - 3), "item");
                    }

                    data[id][0] = XmlToCsv.read(studs, "identifier");
                    data[id][1] = XmlToCsv.read(studs, "name");
                    data[id][2] = XmlToCsv.read(studs, "surname");
                    int j = 3;
                    for (Element c : cours) {
                        data[id][j] = XmlToCsv.read(c, "value");
                        j++;
                    }

                    if (id == etu.length) {
                        Table.setNewModelTable(Table.table, data);
                    }
                    id++;

                    break;

                }
            }
        }
    }


    private static boolean searchInTable(JTable table, String searchText) {
        if (searchText == null) {
            return false;
        }
        int beforeFilterRowCount = table.getRowCount();
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
        }
        int afterFilterRowCount = table.getRowCount();
        return afterFilterRowCount!=0 && afterFilterRowCount != beforeFilterRowCount;
    }
}
