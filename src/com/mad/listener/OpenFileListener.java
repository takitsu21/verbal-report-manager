package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EventListener;

public class OpenFileListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            openFile(selectedFile.getPath());
        }
    }

//    public static void openFile(File file) {
//        if(getOriginPath() == null){setOriginPath(file.getPath());}
//        setPath(file.getPath());
//
//
//        try {
//            if (getContent() == null) {
//                setContent(getFrame().getContentPane());
//            }
//
//            if (getPath().endsWith(".csv")) {
//                if (getComboBox().getItemCount() > 0) {
//                    //getNorthPanel().remove(getComboBox());
//                    //getNorthPanel().remove(getShowTree());
//                    resetComboBox();
//                }
//                if(!isFirstFile) {
//                    getNorthPanel().remove(getSearchComboBox());
//                }
//                setIsFirstFile(false);
//                getDisplayCsv().TableCSV(getPath());
//            } else {
//                XmlToCsv xmlConverter = new XmlToCsv(getPath());
//                xmlConverter.convert();
//
//                getDisplayCsv().TableXML(
//                        getPath(), Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));
//
//
//                if (getComboBox().getItemCount() > 0) {
//                    setIsFirstFile(false);
//                    resetComboBox();
//
////                    Table.table.getSelectionModel().addListSelectionListener(new EnableButtonsRowsListener());
////                    getDeleteLines().addActionListener(new DeleteRowListener());
//
//                }
//                if(!isFirstFile) {
//                    getNorthPanel().remove(getSearchComboBox());
//                }
//
//
//
//                /*    setShowHierarchicTree(new JTree());
//                    setShowTree(new JButton("Vue hiérarchisé"));
//                    getShowTree().addActionListener(new HierarchicalListener());
//                    getNorthPanel().add(getShowTree());
//                    setIsFirstFile(false);*/
//                //}
//                for (String key : Data.dataSet.keySet()) {
//                    getComboBox().addItem(key);
//                }
//
//                if (isIsFirstFile()) {
//                    setShowHierarchicTree(new JTree());
//                    setShowTree(new JButton("Vue hiérarchisé"));
//                    getComboBox().addActionListener(new ComboBoxListener());
//                    getShowTree().addActionListener(new HierarchicalListener());
//                    getNorthPanel().add(getShowTree());
//                    getNorthPanel().add(getComboBox());
//
//                }
//            }
//
//
//            if (getResetTable() == null && getShowSelectedLines() == null && getSearchComboBox() == null) {
//                initComponents();
//            }
//            clearJTables();
//
//            getContent().add(getDisplayCsv().Jscroll, BorderLayout.CENTER);
//            System.gc();
//            getFrame().setVisible(true);
//        } catch (Exception exc) {
//            exc.printStackTrace();
//        }
//    }





    public static void initComponents() {
        System.out.println("init");
        String[] blocs = new String[Data.dataArray[0].length - 3];

        if (blocs.length >= 0) System.arraycopy(Data.dataArray[0], 3, blocs, 0, blocs.length);
        setResetTable(new JButton("Remise à zéro du tableau"));
        setShowSelectedLines(new JButton("Afficher ligne selectionné"));
        setDeleteLines(new JButton("Supprimer ligne selectionné"));
        setAddStudent(new JButton("Ajouter un élève"));
        setAddCourse(new JButton("Ajouter Cours"));
        setAddProgramButton(new JButton("Ajouter un programme"));
        setSearchComboBox(new JComboBox<>(blocs));

        getSearchComboBox().setEditable(true);

        Dimension d = getSearchComboBox().getPreferredSize();
        getSearchComboBox().setPreferredSize(new Dimension(250, (int) d.getHeight()));

        getResetTable().addActionListener(new ResetTableListener());
        getShowSelectedLines().addActionListener(new SelectRowsListener());
        getDeleteLines().addActionListener(new DeleteRowListener());
        getAddStudent().addActionListener(new AddStudentListener());
        getSearchComboBox().addActionListener(new SearchBarListener());
        Table.table.getSelectionModel().addListSelectionListener(new EnableButtonsRowsListener());
        getAddProgramButton().addActionListener(new AddProgramListener());
        getAddCourse().addActionListener(new AddCourseListener());


        setAddProgramButton(new JButton("Ajouter un programme"));
//        getSouthPanel().add(getAddProgramButton());
//        getSouthPanel().add(getAddCourse());
//        getSouthPanel().add(getAddStudent());
//
//        getSouthPanel().add(getResetTable());
//        getSouthPanel().add(getShowSelectedLines());
//        getSouthPanel().add(getDeleteLines());
//
//        getNorthPanel().add(getSearchComboBox());

        Table.table.getModel().removeTableModelListener(new TableChangedListener());
        Table.table.getModel().addTableModelListener(new TableChangedListener());
        getShowSelectedLines().setEnabled(false);
        getDeleteLines().setEnabled(false);
        getDragAndDrop().setVisible(false);
        Table.table.setAutoCreateRowSorter(true);
    }

    private static void resetComboBox() {
//        if (!isIsFirstFile()) {
//            getNorthPanel().remove(getComboBox());
//            getNorthPanel().remove(getShowTree());
//            getNorthPanel().remove(getSearchComboBox());
//        }
//        setShowHierarchicTree(new JTree());
//        setShowTree(new JButton("Vue hiérarchisé"));

        setComboBox(new JComboBox<>());
        getComboBox().setName("programs");


    }

    public static void openFile(String fileName) {
        try {
            if (getContent() == null) {
                setContent(getFrame().getContentPane());
            }
            if (getOriginPath() == null) {
                setOriginPath(fileName);
            }
            setPath(fileName);


            if (fileName.endsWith(".csv")) {
                clearNorthPanel();
                clearSouthPanel();
                getSouthPanel().add(getShowSelectedLines());
                getNorthPanel().add(getSearchComboBox());
                getDisplayCsv().TableCSV(fileName);
            } else {
                XmlToCsv xmlConverter = new XmlToCsv(fileName);
                xmlConverter.convert();
                System.out.println(Data.dataSet.entrySet().iterator().next().getKey());

                getDisplayCsv().TableXML(
                        fileName,
                        Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey())
                );
//                refreshPanels(fileName);
            }
            if (!componentsInitialised) {
                initComponents();
            }
            getContent().add(getDisplayCsv().Jscroll, BorderLayout.CENTER);
            System.gc();
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearNorthPanel() {

        for (Component c : getNorthPanel().getComponents()) {
            if (c != null)
                getNorthPanel().remove(c);
                System.out.println("Removing" + c);
        }
    }

    private static void clearSouthPanel() {
        for (Component c : getSouthPanel().getComponents()) {
            if (c != null)
                getNorthPanel().remove(c);
                System.out.println("Removing" + c);
        }
    }

    private static void fillSouthPanel(String fileName) {
        if (fileName.endsWith(".csv")) {
            getSouthPanel().add(getShowSelectedLines());
        } else {
            System.out.println(getAddProgramButton());
            System.out.println(getAddCourse());
            System.out.println(getAddStudent());
            System.out.println(getResetTable());
            System.out.println(getShowSelectedLines());
            System.out.println(getDeleteLines());


            getSouthPanel().add(getAddProgramButton());
            getSouthPanel().add(getAddCourse());
            getSouthPanel().add(getAddStudent());
            getSouthPanel().add(getResetTable());
            getSouthPanel().add(getShowSelectedLines());
            getSouthPanel().add(getDeleteLines());
        }
    }

    private static void fillNorthPanel(String fileName) {
        if (fileName.endsWith(".csv")) {
            getNorthPanel().add(getSearchComboBox());
        } else {
            getNorthPanel().add(getShowTree());
            getNorthPanel().add(getComboBox());
        }
    }

    private static void refreshPanels(String fileName) {
        clearNorthPanel();
        clearSouthPanel();
        fillSouthPanel(fileName);
        fillNorthPanel(fileName);


    }

}
