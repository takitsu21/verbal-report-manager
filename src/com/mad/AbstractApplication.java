package com.mad;

import com.mad.util.Action;
import com.mad.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Stack;

public abstract class AbstractApplication {
    public static final String TMP_PATH = "./xml-editor.tmp.xml";
    private static final Stack<Action> commandStack = new Stack<>();
    public static String ORIGIN_PATH;
    protected static String path;
    protected static JFrame frame;
    protected static JPanel southPanel;
    protected static JPanel northPanel;
    protected static JButton showSelectedLines;
    protected static JButton deleteLines;
    protected static JButton addStudent;
    protected static JButton showTree;
    protected static JButton addCourse;
    protected static JButton addProgramButton;
    protected static Table displayCsv;
    protected static Container content;
    protected static JComboBox<String> comboBox;
    protected static JComboBox<String> searchComboBox;
    protected static JLabel dragAndDrop;
    protected static JTree showHierarchicTree;
    protected static boolean componentsInitialised = false;
    protected static String infoSearchComboBox;
    protected static JCheckBox afficheEtu;
    protected static JButton refresh;
    protected static String savedAsName;
    protected static Timestamp lastModificationAt;
    protected static Timestamp lastTmpModificationAt;
    protected static BufferedImage ico;
    private static int undoRedoPointer = -1;


    public AbstractApplication() {
    }

    public static Timestamp getLastModificationAt() {
        return lastModificationAt;
    }

    public static void setLastModificationAt(Timestamp lastModificationAt) {
        AbstractApplication.lastModificationAt = lastModificationAt;
    }

    public static Timestamp getLastTmpModificationAt() {
        return lastTmpModificationAt;
    }

    public static void setLastTmpModificationAt(Timestamp lastTmpModificationAt) {
        AbstractApplication.lastTmpModificationAt = lastTmpModificationAt;
    }

    public static String getTmpPath() {
        return TMP_PATH;
    }

    public static String getSavedAsName() {
        return savedAsName;
    }

    public static void setSavedAsName(String savedAsName) {
        AbstractApplication.savedAsName = savedAsName;
    }

    public static JButton getAddProgramButton() {
        return addProgramButton;
    }

    public static void setAddProgramButton(JButton addProgramButton) {
        AbstractApplication.addProgramButton = addProgramButton;
    }

    public static JCheckBox getAfficheEtu() {
        return afficheEtu;
    }

    public static void setAfficheEtu(JCheckBox afficheEtu) {
        AbstractApplication.afficheEtu = afficheEtu;
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

    public static String getInfoSearchComboBox() {
        return infoSearchComboBox;
    }

    public static void setInfoSearchComboBox(String infoSearchComboBox) {
        AbstractApplication.infoSearchComboBox = infoSearchComboBox;
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

    public static String getOriginPath() {
        return ORIGIN_PATH;
    }

    public static void setOriginPath(String originPath) {
        ORIGIN_PATH = originPath;
    }

    public static JButton getRefresh() {
        return refresh;
    }

    public static void setRefresh(JButton refresh) {
        AbstractApplication.refresh = refresh;
    }

    public static BufferedImage getIco() {
        return ico;
    }

    public static void setIco(BufferedImage ico) {
        AbstractApplication.ico = ico;
    }

    public static void clearJTables() {
        for (Component c : getContent().getComponents()) {
            if (c instanceof JTable || c instanceof JScrollPane) {
                getContent().remove(c);
            }
        }
    }

    public static void refreshTable() {
        if (path.endsWith(".xml")) {
            XmlWriter.save(TMP_PATH);
            XmlToCsv xmlConverter = new XmlToCsv(TMP_PATH);
            xmlConverter.convert();
            getComboBox().setSelectedItem(getComboBox().getSelectedItem());
            if (getAfficheEtu().isSelected()) {
                getAfficheEtu().setSelected(false);
            }
        } else if (path.endsWith(".csv")) {
            save(false);
            Table.setTemporaryTable(Data.dataArray);
            Table.setNewModelTable(Table.table, Data.dataArray);
        }

    }

    public static boolean save(boolean saveAs) {
        String path = AbstractApplication.getOriginPath();
        Timestamp currentSaveTimestamp = new Timestamp(System.currentTimeMillis());
        if (path.endsWith(".csv")) {
            try {
                PrintWriter pr = new PrintWriter(path);
                for (String[] l : Data.dataArray) {
                    StringBuilder acc = new StringBuilder();
                    for (String m : l) {
                        acc.append("\"").append(m).append("\",");
                    }
                    pr.println(acc);
                }
                return true;
            } catch (FileNotFoundException fileNotFoundException) {
                JOptionPane.showMessageDialog(AbstractApplication.getFrame(), "Erreur FATAL");
            }
        }
        if (path.endsWith(".xml")) {
            if (!saveAs) {
                if (XmlWriter.save(path)) {
                    setLastModificationAt(currentSaveTimestamp);
                    setLastTmpModificationAt(currentSaveTimestamp);
                    return true;
                }

            } else {
                if (saveAs()) {
                    setLastModificationAt(currentSaveTimestamp);
                    setLastTmpModificationAt(currentSaveTimestamp);
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean saveAs() {
        File f = new File(ORIGIN_PATH);
        JFileChooser jfc = new JFileChooser(f) {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setIconImage(getIco());
                return dialog;
            }
        };
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jfc.setSelectedFile(f);
        jfc.setFileFilter(new FileNameExtensionFilter(".xml", "xml"));
        jfc.setDialogTitle("Choississez un endroit pour sauvegarder votre fichier: ");
        jfc.setFileSelectionMode(0);
        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == 0) {
            if (jfc.getSelectedFile().getPath().endsWith(".xml")) {
                if (XmlWriter.save(jfc.getSelectedFile().getPath())) {
                    setSavedAsName(jfc.getSelectedFile().getPath());
                    return true;
                }
            } else if (XmlWriter.save(jfc.getSelectedFile().getPath() + ".xml")) {
                setSavedAsName(jfc.getSelectedFile().getPath() + ".xml");
                return true;
            }
        }

        return false;
    }

    public static boolean isSaved() {
        try {
            if (getLastTmpModificationAt() == null) {
                return true;
            } else {
                return getLastModificationAt().compareTo(getLastTmpModificationAt()) == 0;
            }
        } catch (Exception e) {
            return true;
        }
    }

    public static void undo() {
        Action command = commandStack.get(undoRedoPointer);
        command.unExecute();
        --undoRedoPointer;
    }

    public static void redo() {
        if (undoRedoPointer != commandStack.size() - 1) {
            ++undoRedoPointer;
            Action command = commandStack.get(undoRedoPointer);
            command.execute();
        }
    }

    protected void insertAction(Runnable runner, String type, Object arg, XmlMethodType xmt, boolean refreshTable) {
        this.deleteElementsAfterPointer(undoRedoPointer);
        Action command = new XmlUndoRedo(runner, type, arg, xmt, refreshTable);
        command.execute();
        commandStack.push(command);
        ++undoRedoPointer;
    }

    protected void insertAction(Runnable runner, String type, XmlMethodType xmt, boolean refreshTable, Object... args) {
        this.deleteElementsAfterPointer(undoRedoPointer);
        Action command = new XmlUndoRedo(runner, type, xmt, refreshTable, args);
        command.execute();
        commandStack.push(command);
        ++undoRedoPointer;
    }

    private void deleteElementsAfterPointer(int undoRedoPointer) {
        if (commandStack.size() >= 1) {
            for (int i = commandStack.size() - 1; i > undoRedoPointer; --i) {
                commandStack.remove(i);
            }
        }
    }
}
