package com.mad;

import com.mad.util.Table;

import javax.swing.*;
import java.awt.*;


public abstract class Application extends JPanel {
    private static String path;
    private static JFrame frame;
    private static JPanel southPanel;
    private static JPanel northPanel;
    private static JButton showSelectedLines;
    private static JButton resetTable;
    private static JButton validate;
    private static Table displayCsv;
    private static Container content;
    private static JComboBox<String> comboBox = new JComboBox<>();
    private static boolean isFirstFile = true;
    private static JTextField searchBar;
    private static JLabel dragAndDrop;

    public Application() {
    }

    public static JLabel getDragAndDrop() {
        return dragAndDrop;
    }

    public static void setDragAndDrop(JLabel dragAndDrop) {
        Application.dragAndDrop = dragAndDrop;
    }

    public static JButton getValidate() {
        return validate;
    }

    public static void setValidate(JButton validate) {
        Application.validate = validate;
    }

    public static JButton getShowSelectedLines() {
        return showSelectedLines;
    }

    public static void setShowSelectedLines(JButton showSelectedLines) {
        Application.showSelectedLines = showSelectedLines;
    }

    public static JButton getResetTable() {
        return resetTable;
    }

    public static void setResetTable(JButton resetTable) {
        Application.resetTable = resetTable;
    }

    public static JTextField getSearchBar() {
        return searchBar;
    }

    public static void setSearchBar(JTextField searchBar) {
        Application.searchBar = searchBar;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        Application.path = path;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void setFrame(JFrame frame) {
        Application.frame = frame;
    }

    public static JPanel getSouthPanel() {
        return southPanel;
    }

    public static void setSouthPanel(JPanel southPanel) {
        Application.southPanel = southPanel;
    }

    public static JPanel getNorthPanel() {
        return northPanel;
    }

    public static void setNorthPanel(JPanel northPanel) {
        Application.northPanel = northPanel;
    }

    public static Table getDisplayCsv() {
        return displayCsv;
    }

    public static void setDisplayCsv(Table displayCsv) {
        Application.displayCsv = displayCsv;
    }

    public static Container getContent() {
        return content;
    }

    public static void setContent(Container content) {
        Application.content = content;
    }

    public static JComboBox<String> getComboBox() {
        return comboBox;
    }

    public static void setComboBox(JComboBox<String> comboBox) {
        Application.comboBox = comboBox;
    }

    public static boolean isIsFirstFile() {
        return isFirstFile;
    }

    public static void setIsFirstFile(boolean isFirstFile) {
        Application.isFirstFile = isFirstFile;
    }

    public static void clearJTables() {
        for (Component c : getContent().getComponents()) {
            if (c instanceof JTable || c instanceof JScrollPane) {
                getContent().remove(c);
            }
        }
    }
}
