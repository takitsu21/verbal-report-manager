package com.mad;

import com.mad.util.Table;

import javax.swing.*;
import java.awt.*;


public abstract class Application extends JPanel {
    private static String path;
    private static JFrame frame;
    private static JPanel southPanel;
    private static JPanel northPanel;
    private static Table displayCsv;
    private static Container content;
    private static JComboBox<String> comboBox = new JComboBox<>();
    private static boolean isFirstFile = true;

    public Application() {
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

    public void clearJTables() {
        for (Component c : getContent().getComponents()) {
            if (c instanceof JTable || c instanceof JScrollPane) {
                getContent().remove(c);
            }
        }
    }
}
