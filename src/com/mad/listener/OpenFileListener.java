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

public class OpenFileListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            openFile(selectedFile);
        }
    }

    public static void openFile(File file) {
        setPath(file.getPath());


        try {
            if (getContent() == null) {
                setContent(getFrame().getContentPane());
            }

            if (getPath().endsWith(".csv")) {
                if (getComboBox().getItemCount() > 0) {
                    getNorthPanel().remove(getComboBox());
                    getNorthPanel().remove(getShowTree());
                    resetComboBox();
                }
                getDisplayCsv().TableCSV(getPath());
            } else {
                XmlToCsv xmlConverter = new XmlToCsv(getPath());
                xmlConverter.convert();
                getDisplayCsv().TableXML(
                        getPath(), Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));

                if (getComboBox().getItemCount() > 0) {
                    resetComboBox();
                    setShowHierarchicTree(new JTree());
                    setShowTree(new JButton("Vue hiérarchisé"));
                    getShowTree().addActionListener(new HierarchicalListener());
                    getNorthPanel().add(getShowTree());
                    setIsFirstFile(false);
                }
                for (String key : Data.dataSet.keySet()) {
                    getComboBox().addItem(key);
                }

                if (isIsFirstFile()) {
                    getComboBox().addActionListener(new ComboBoxListener());
                    getNorthPanel().add(getComboBox());
                    setShowHierarchicTree(new JTree());
                    setShowTree(new JButton("Vue hiérarchisé"));
                    getShowTree().addActionListener(new HierarchicalListener());
                    getNorthPanel().add(getShowTree());
                }

                Table.table.getModel().addTableModelListener(new TableChangedListener());

            }
            if (getResetTable() == null && getShowSelectedLines() == null && getSearchComboBox() == null) {
                initComponents();
            }
            clearJTables();
            getContent().add(getDisplayCsv().Jscroll, BorderLayout.CENTER);

//
            getFrame().setVisible(true);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private static void initComponents() {
        setResetTable(new JButton("Remise à zéro du tableau"));
        setShowSelectedLines(new JButton("Afficher ligne selectionné"));
        setDeleteLines(new JButton("Supprimer ligne selectionné"));
        setAddStudent(new JButton("Ajouter un élève"));


        String[] blocs = new String[Data.dataArray[0].length - 3];

        if (blocs.length >= 0) System.arraycopy(Data.dataArray[0], 3, blocs, 0, blocs.length);

        setSearchComboBox(new JComboBox<>(blocs));
        getSearchComboBox().setEditable(true);
        getSearchComboBox().addActionListener(new SearchBarListener());


        Dimension d = getSearchComboBox().getPreferredSize();
        getSearchComboBox().setPreferredSize(new Dimension(250, (int) d.getHeight()));

        getResetTable().addActionListener(new ResetTableListener());
        getShowSelectedLines().addActionListener(new SelectRowsListener());
        getDeleteLines().addActionListener(new DeleteRowListener());
        getAddStudent().addActionListener(new AddStudentListener());

        Table.table.getSelectionModel().addListSelectionListener(new EnableButtonsRowsListener());


        getSouthPanel().add(getAddStudent());

        setAddCourse(new JButton("Ajouter Cours"));
        getAddCourse().addActionListener(new AddCourseListener());
        getSouthPanel().add(getAddCourse());

        setAddProgramButton(new JButton("Ajouter un programme"));
        getSouthPanel().add(getAddProgramButton());
        getAddProgramButton().addActionListener(new AddProgramListener());

        getSouthPanel().add(getResetTable());
        getSouthPanel().add(getShowSelectedLines());
        getSouthPanel().add(getDeleteLines());
//        getNorthPanel().setLayout(new GridLayout(1, 8, 3, 0));
//        getNorthPanel().add(new JPanel());
//        getNorthPanel().add(new JPanel());
//        getNorthPanel().add(new JPanel());
//        getNorthPanel().add(new JPanel());





        getNorthPanel().add(getSearchComboBox());
        Table.table.getModel().addTableModelListener(new TableChangedListener());
        getShowSelectedLines().setEnabled(false);
        getDeleteLines().setEnabled(false);
        getDragAndDrop().setVisible(false);
        Table.table.setAutoCreateRowSorter(true);
    }

    private static void resetComboBox() {
        setComboBox(new JComboBox<>());
        getComboBox().setName("programs");
        getNorthPanel().remove(getShowTree());

    }
}
