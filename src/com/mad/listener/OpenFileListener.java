package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XML2CSV;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

public class OpenFileListener extends Application implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            setPath(selectedFile.getAbsolutePath());
            try {
                setContent(getFrame().getContentPane());
                if (getPath().endsWith(".csv")) {
                    if (getComboBox().getItemCount() > 0) {
                        getNorthPanel().remove(getComboBox());
                        setComboBox(new JComboBox<>());
                    }
                    getDisplayCsv().TableCSV(getPath());
                } else {
                    XML2CSV xmlConverter = new XML2CSV(getPath());
                    xmlConverter.convert();
                    Data.dataSet = xmlConverter.dicoData;
                    getDisplayCsv().TableXML(
                            getPath(), Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));

                    if (getComboBox().getItemCount() > 0) {
                        setComboBox(new JComboBox<>());
                        getComboBox().setName("programs");
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
                clearJTables();
                getContent().add(getDisplayCsv().Jscroll, BorderLayout.CENTER);
                getFrame().setVisible(true);

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }


    }
}
