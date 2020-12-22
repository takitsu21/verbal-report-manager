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

            String searchText = (String) getSearchBar().getSelectedItem();
            searchText = searchText.trim().toLowerCase();
            System.out.println(searchText);
            selectEtu(searchText, getPath());
            System.out.println(searchText);
//            searchCourse(searchBarText);


        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }



    private void searchCourse(String searchBarText) {
        String[] names = Stream.of(searchBarText.split(";")).
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

        Table.setNewModelTable(Table.table, tableau_final);
    }


    private void selectEtu(String etu, String path) throws IOException, SAXException, ParserConfigurationException {
        XmlToCsv xml = new XmlToCsv(path);
        List<Element> listStudents = Data.getChildren(xml.getRoot(), "student");
        for (Element studs : listStudents) {
            if (etu.equalsIgnoreCase(XmlToCsv.read(studs, "identifier"))) {
                List<Element> cours = Data.getChildren(studs, "grade");
                String[][] data = new String[2][cours.size() + 3];
                data[0][0] = "N° Étudiant";
                data[0][1] = "Nom";
                data[0][2] = "Prénom";
                for (int i = 3; i < cours.size() + 3; i++) {
                    data[0][i] = XmlToCsv.read(cours.get(i - 3), "item");
                }

                data[1][0] = XmlToCsv.read(studs, "identifier");
                data[1][1] = XmlToCsv.read(studs, "name");
                data[1][2] = XmlToCsv.read(studs, "surname");
                int j = 3;
                for (Element c : cours) {
                    data[1][j] = XmlToCsv.read(c, "value");
                    j++;
                }

                Table.setNewModelTable(Table.table, data);
                break;

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
