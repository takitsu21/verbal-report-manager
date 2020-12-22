package com.mad.listener;

import com.mad.Application;
import com.mad.EditableComboBoxExemple;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenFileListener extends Application implements ActionListener {
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
            setContent(getFrame().getContentPane());
            if (getPath().endsWith(".csv")) {
                if (getComboBox().getItemCount() > 0) {
                    getNorthPanel().remove(getComboBox());
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
                    setIsFirstFile(false);
                }
                for (String key : Data.dataSet.keySet()) {
                    getComboBox().addItem(key);
                }

                if (isIsFirstFile()) {
                    getComboBox().addActionListener(new ComboBoxListener());
                    getNorthPanel().add(getComboBox());
                }
            }
            if (getResetTable() == null && getShowSelectedLines() == null && getSearchBar() == null) {
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
        setValidate(new JButton("Rechercher"));
        Table.table.getSelectionModel().addListSelectionListener(new EnableButtonsRowsListener());
        EditableComboBoxExemple editableComboBox = new EditableComboBoxExemple();

        getShowSelectedLines().setEnabled(false);

        Dimension d = getSearchBar().getPreferredSize();
        getSearchBar().setPreferredSize(new Dimension(150, (int) d.getHeight()));

        getResetTable().addActionListener(new ResetTableListener());
        getShowSelectedLines().addActionListener(new SelectRowsListener());
        getValidate().addActionListener(new SearchBarListener());

        getSouthPanel().add(getResetTable());
        getSouthPanel().add(getShowSelectedLines());
        getNorthPanel().setLayout(new GridLayout(1,8,3,0));
        getNorthPanel().add(new JPanel());
        getNorthPanel().add(new JPanel());
        getNorthPanel().add(new JPanel());
        getNorthPanel().add(new JPanel());

        getNorthPanel().add(EditableComboBoxExemple.searchComboBox);
//        EditableComboBoxExemple.searchComboBox.set;
//        getNorthPanel().add(getValidate());
//        getNorthPanel().add(getSearchBar());

    }

    private static void resetComboBox() {
        setComboBox(new JComboBox<>());
        getComboBox().setName("programs");
    }
}
