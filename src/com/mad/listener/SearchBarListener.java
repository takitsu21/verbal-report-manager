package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;
import com.mad.util.XmlWriter;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SearchBarListener extends AbstractApplication implements ActionListener {


    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            String searchText;
            if(getInfoSearchComboBox()!=null){
                searchText=getInfoSearchComboBox();
                setInfoSearchComboBox(null);
            }
            else {
                searchText = (String) getSearchComboBox().getSelectedItem();

                try {
                    searchText = searchText.trim();
                } catch (NullPointerException exc) {
                    return;
                }
            }

            String[] listText = searchText.split(";");


            String[] ligne = {};
            for (String s : listText) {

                String[] etu = searchInTable(Table.table, s);

                int oldLength = ligne.length;
                ligne = Arrays.copyOf(ligne, (ligne.length + etu.length));
                System.arraycopy(etu, 0, ligne, oldLength, etu.length);

            }



            for(int i=0;i<ligne.length;i++){

                for(int j=0; j<ligne.length;j++){
                    if(i!=j && ligne[i].equals(ligne[j])){
                        ligne[j]=null;
                    }
                }

            }




            String[][] data=new String[ligne.length+1][];
            if (ligne.length == 0) {
                data=searchCourse(listText);
                //System.out.println("1");
            } else if (getPath().endsWith(".xml")) {
                //System.out.println("xml");
                data=selectEtu(ligne);
            }else if (getPath().endsWith(".csv")){
                //System.out.println(Arrays.toString(ligne));
                //data=searchInCsv(ligne);
                data[0]=Data.dataArray[0];
                for (int i=0; i< ligne.length;i++){
                    for (int j=1;j<Data.dataArray.length;j++){

                        if(ligne[i] != null && ligne[i].equals(Data.dataArray[j][0])){

                            data[i+1]=Data.dataArray[j];
                            break;
                        }
                    }
                }

            }
            Table.setNewModelTable(Table.table, data);


        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }


    private String[][] searchCourse(String[] names) {
        String[][] tableau_final = new String[Data.dataArray.length][names.length + 1];

        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < Data.dataArray[0].length; j++) {
                String currentCheck = Data.dataArray[0][j];
                String[] splited = currentCheck.split(" - "); //.map(String::toLowerCase).toArray(String[]::new);
                if (currentCheck.equalsIgnoreCase(names[i]) || splited[0].equalsIgnoreCase(names[i]) || splited[splited.length - 1].equalsIgnoreCase(names[i])) {
                    for (int k = 0; k < Data.dataArray.length; k++) {
                        if (!Data.dataArray[k][j].isEmpty()) {
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
                if (etu[j - 1].equalsIgnoreCase(XmlToCsv.read(studs, "identifier"))) {

                    List<Element> cours = Data.getChildren(studs, "grade");

                    for (int id = 0; id < Data.dataArray.length; id++) {
                        if (etu[j - 1].equals(Data.dataArray[id][0])) {
                            data[j] = Data.dataArray[id];
                            break;
                        }
                    }


                    for (Element cour : cours) {
                        String coursTest = XmlToCsv.read(cour, "item");
                        int trouver = 0;
                        for (int m = 3; m < data[0].length; m++) {
                            if (coursTest!=null && coursTest.equals(data[0][m].split(" - ")[0])) {
                                trouver = 1;
                                break;
                            }
                        }
                        if (trouver == 0) {
                            for (int i = 0; i < data.length; i++) {
                                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                            }
                            for (Element element : listCourses) {
                                if (coursTest!=null && coursTest.equals(XmlToCsv.read(element, "identifier"))) {
                                    data[0][data[0].length - 1] = coursTest + " - " + XmlToCsv.read(element, "name");
                                    break;
                                }
                            }

                            data[j][data[0].length - 1] = XmlToCsv.read(cour, "value");
                        }
                    }


                    break;

                }
            }
        }
        return data;
    }


    private static String[] searchInTable(JTable table, String searchText) {
        String[] num = new String[0];
        RowSorter<? extends TableModel> rs = table.getRowSorter();
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
        String[][] data=new String[etu.length][];
        for (int i=0; i< etu.length;i++){
            for (int j=0;j<Data.dataArray.length;j++){
                if(etu[i].equals(Data.dataArray[j][0])){
                    System.out.println(Arrays.toString(Data.dataArray[j]));
                    data[i]=Data.dataArray[j];
                    break;
                }
            }
        }
        return data;
    }
}
