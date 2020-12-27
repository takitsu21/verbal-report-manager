package com.mad;

import com.mad.listener.TableChangedListener;

import com.mad.util.Data;

import com.mad.util.Table;
import com.mad.util.XmlToCsv;
import com.mad.util.XmlWriter;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.awt.*;


public abstract class AbstractApplication extends JPanel {
    public static final String TMP_PATH = "./xml-editor.tmp.xml";
    protected final TableModelListener modelListener = new TableChangedListener();
    protected static String path;
    protected static JFrame frame;
    protected static JPanel southPanel;
    protected static JPanel northPanel;
    protected static JButton showSelectedLines;
    protected static JButton deleteLines;
    protected static JButton addStudent;
    protected static JButton resetTable;
    protected static JButton showTree;
    protected static JButton addCourse;
    protected static Table displayCsv;
    protected static Container content;
    protected static JComboBox<String> comboBox = new JComboBox<>();
    protected static boolean isFirstFile = true;
    protected static JComboBox<String> searchComboBox;
    protected static JLabel dragAndDrop;
    protected static JTree showHierarchicTree;
    protected static JButton addProgramButton;

    protected static XmlWriter xmlEditor = new XmlWriter();


    public AbstractApplication() {
    }


    public static JButton getAddProgramButton() {
        return addProgramButton;
    }

    public static void setAddProgramButton(JButton addProgramButton) {
        AbstractApplication.addProgramButton = addProgramButton;
    }

    public static JButton getAddCourse() {
        return addCourse;
    }

    public static void setAddCourse(JButton addCourse) {
        AbstractApplication.addCourse = addCourse;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        AbstractApplication.path = path;
    }

    public static JComboBox<String> getSearchComboBox() {
        return searchComboBox;
    }

    public static void setSearchComboBox(JComboBox<String> searchComboBox) {
        AbstractApplication.searchComboBox = searchComboBox;
    }

    public static XmlWriter getXmlEditor() {
        return xmlEditor;
    }

    public static void setXmlEditor(XmlWriter xmlEditor) {
        AbstractApplication.xmlEditor = xmlEditor;
    }

    public static JButton getShowTree() {
        return showTree;
    }

    public static void setShowTree(JButton showTree) {
        AbstractApplication.showTree = showTree;
    }

    public static JTree getShowHierarchicTree() {
        return showHierarchicTree;
    }

    public static void setShowHierarchicTree(JTree showHierarchicTree) {
        AbstractApplication.showHierarchicTree = showHierarchicTree;
    }

    public static JLabel getDragAndDrop() {
        return dragAndDrop;
    }

    public static void setDragAndDrop(JLabel dragAndDrop) {
        AbstractApplication.dragAndDrop = dragAndDrop;
    }

    public static JButton getShowSelectedLines() {
        return showSelectedLines;
    }

    public static void setShowSelectedLines(JButton showSelectedLines) {
        AbstractApplication.showSelectedLines = showSelectedLines;
    }

    public static JButton getResetTable() {
        return resetTable;
    }

    public static void setResetTable(JButton resetTable) {
        AbstractApplication.resetTable = resetTable;
    }

    public static JButton getDeleteLines() {
        return deleteLines;
    }

    public static void setDeleteLines(JButton deleteLines) {
        AbstractApplication.deleteLines = deleteLines;
    }

    public static JButton getAddStudent() {
        return addStudent;
    }

    public static void setAddStudent(JButton addStudent) {
        AbstractApplication.addStudent = addStudent;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void setFrame(JFrame frame) {
        AbstractApplication.frame = frame;
    }

    public static JPanel getSouthPanel() {
        return southPanel;
    }

    public static void setSouthPanel(JPanel southPanel) {
        AbstractApplication.southPanel = southPanel;
    }

    public static JPanel getNorthPanel() {
        return northPanel;
    }

    public static void setNorthPanel(JPanel northPanel) {
        AbstractApplication.northPanel = northPanel;
    }

    public static Table getDisplayCsv() {
        return displayCsv;
    }

    public static void setDisplayCsv(Table displayCsv) {
        AbstractApplication.displayCsv = displayCsv;
    }

    public static Container getContent() {
        return content;
    }

    public static void setContent(Container content) {
        AbstractApplication.content = content;
    }

    public static JComboBox<String> getComboBox() {
        return comboBox;
    }

    public static void setComboBox(JComboBox<String> comboBox) {
        AbstractApplication.comboBox = comboBox;
    }

    public static boolean isIsFirstFile() {
        return isFirstFile;
    }

    public static void setIsFirstFile(boolean isFirstFile) {
        AbstractApplication.isFirstFile = isFirstFile;
    }

    public static void clearJTables() {
        for (Component c : getContent().getComponents()) {
            if (c instanceof JTable || c instanceof JScrollPane) {
                getContent().remove(c);
            }
        }
    }

    public static void refreshTable(){
        XmlWriter.save(TMP_PATH);
        XmlToCsv xmlConverter = new XmlToCsv(TMP_PATH);
        xmlConverter.convert();

//        setDisplayCsv(new Table());
//        getDisplayCsv().TableXML(TMP_PATH, Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));
        Table.table.getModel().addTableModelListener(new TableChangedListener());
        clearJTables();
        getContent().add(getDisplayCsv().Jscroll, BorderLayout.CENTER);
        getComboBox().setSelectedItem(getComboBox().getSelectedItem());
        getFrame().setVisible(true);

    }
}
