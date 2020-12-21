package com.mad;

import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XML2CSV;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class MainFrame extends JPanel {
    private String path;
    private JFrame frame;
    private JPanel southPanel;
    private JPanel northPanel;
    private Table displayCsv;
    private Container content;
    private JComboBox<String> comboBox = new JComboBox<>();
    private boolean isFirstFile = true;

    public MainFrame() {
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSouthPanel(JPanel southPanel) {
        this.southPanel = southPanel;
    }

    public void setNorthPanel(JPanel northPanel) {
        this.northPanel = northPanel;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    public void setDisplayCsv(Table displayCsv) {
        this.displayCsv = displayCsv;
    }

    public Table getDisplayCsv() {
        return displayCsv;
    }

    public Container getContent() {
        return content;
    }

    public void setContent(Container content) {
        this.content = content;
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }

    public void setComboBox(JComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    public boolean isFirstFile() {
        return isFirstFile;
    }

    public void setFirstFile(boolean firstFile) {
        isFirstFile = firstFile;
    }


    public void fileListener(ActionEvent event) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            setPath(selectedFile.getAbsolutePath());
            try {
                if (path.endsWith(".csv")) {
                    displayCsv.TableCSV(path);
                } else {
                    XML2CSV xmlConverter = new XML2CSV(path);
                    xmlConverter.convert();
                    Data.dataSet = xmlConverter.dicoData;
                    displayCsv.TableXML(path, Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));

                    if (comboBox.getItemCount() > 0) {
                        comboBox = new JComboBox<>();
                        isFirstFile = false;
                    }
                    for (String key : Data.dataSet.keySet()) {
                        comboBox.addItem(key);
                    }

                    if (isFirstFile) {
                        comboBox.addActionListener(this::comboBoxListener);
                        northPanel.add(comboBox);
                    }
                }
                if (isFirstFile) {
                    content = frame.getContentPane();
                    content.add(displayCsv.Jscroll, BorderLayout.CENTER);
                } else {
                    String[][] newArr = Table.sDataToArray(
                            Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));
                    displayCsv.table.setModel(
                            new DefaultTableModel(Arrays.copyOfRange(newArr, 1, newArr.length), newArr[0]));
//                    content.remove(displayCsv.table);
//                    content.add(displayCsv.Jscroll, BorderLayout.CENTER);
                    System.out.println("else first file");
                }
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void save(String data, String name) {
        byte[] bs = data.getBytes();
        Path path = Paths.get(name);

        try {
            Path writtenFilePath = Files.write(path, bs);
            System.out.println("save " + name);
        } catch (Exception e) {
            System.out.println("Erreur: " + e);
        }
    }


    public void saveFileChooser(ActionEvent e) {
//        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        File userDir;
        if (comboBox.getItemCount() > 0) {
            String formatedFileName = String.join("_",
                    ((String) Objects.requireNonNull(comboBox.getSelectedItem())).split(" "));
            userDir = new File(formatedFileName + ".csv");
        } else {
            return;
        }
        JFileChooser jfc = new JFileChooser(userDir);
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jfc.setSelectedFile(userDir);
        jfc.setFileFilter(new FileNameExtensionFilter(".csv", "csv"));

        jfc.setDialogTitle("Choississez un endroit pour sauvegarder votre fichier: ");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            //System.out.println(jfc.getSelectedFile());
            save(Data.dataSet.get(comboBox.getSelectedItem()), "" + jfc.getSelectedFile());
            if (jfc.getSelectedFile().isDirectory()) {
                System.out.println("You selected the directory: " + jfc.getSelectedFile());
                //save(csv, ""+jfc.getSelectedFile());
            }
        }
    }


    public void comboBoxListener(ActionEvent e) {
        JComboBox combo = (JComboBox) e.getSource();
        String[][] newArr = Table.sDataToArray(
                Data.dataSet.get(Objects.requireNonNull(combo.getSelectedItem()).toString()));
        TableModel tm = new DefaultTableModel(Arrays.copyOfRange(newArr, 1, newArr.length), newArr[0]);
        displayCsv.table.setModel(tm);
    }


}
