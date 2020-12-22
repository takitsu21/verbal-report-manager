package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;

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
                if (getPath().endsWith(".csv")) {
                    getDisplayCsv().TableCSV(getPath());
                } else {
                    XmlToCsv xmlConverter = new XmlToCsv(getPath());
                    xmlConverter.convert();
                    Data.dataSet = xmlConverter.dicoData;
                    getDisplayCsv().TableXML(getPath(), Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));

                    if (getComboBox().getItemCount() > 0) {
                        setComboBox(new JComboBox<>());
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
                if (isIsFirstFile()) {
                    setContent(getFrame().getContentPane());
                    getContent().add(getDisplayCsv().Jscroll, BorderLayout.CENTER);
                } else {
                    String[][] newArr = Table.sDataToArray(
                            Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));
                    getDisplayCsv().table.setModel(
                            new DefaultTableModel(Arrays.copyOfRange(newArr, 1, newArr.length), newArr[0]));
                    System.out.println("else first file");
                }
                getFrame().setVisible(true);

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}
