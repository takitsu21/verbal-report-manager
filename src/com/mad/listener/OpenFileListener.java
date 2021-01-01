package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;

public class OpenFileListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jfc.setFileFilter(new FileNameExtensionFilter(".csv; .xml", "csv", "xml"));

        jfc.setDialogTitle("Choisissez le fichier à ouvrir");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            openFile(selectedFile.getPath());
        }
    }

    public static void initComponents() throws IOException {

        String[] blocs = new String[Data.dataArray[0].length - 3];

        if (blocs.length >= 0) System.arraycopy(Data.dataArray[0], 3, blocs, 0, blocs.length);
        setShowSelectedLines(new JButton("Afficher ligne selectionné"));
        setDeleteLines(new JButton("Supprimer ligne selectionné"));
        setAddStudent(new JButton("Ajouter un élève"));
        setAddCourse(new JButton("Ajouter Cours"));
        setAddProgramButton(new JButton("Ajouter un programme"));
        setSearchComboBox(new JComboBox<>(blocs));
        setShowTree(new JButton("Vue hiérarchisé"));
        setShowHierarchicTree(new JTree());
        BufferedImage buttonIcon = ImageIO.read(new File("./refresh.png"));
        setRefresh(new JButton(new ImageIcon(buttonIcon)));
        getRefresh().addActionListener(new RefreshListener());


        getSearchComboBox().setEditable(true);

        Dimension d = getSearchComboBox().getPreferredSize();
        getSearchComboBox().setPreferredSize(new Dimension(250, (int) d.getHeight()));

        getShowTree().addActionListener(new HierarchicalListener());
        getShowSelectedLines().addActionListener(new SelectRowsListener());
        getDeleteLines().addActionListener(new DeleteRowListener());
        getAddStudent().addActionListener(new AddStudentListener());
        getSearchComboBox().addActionListener(new SearchBarListener());
        Table.table.getSelectionModel().addListSelectionListener(new EnableButtonsRowsListener());
        getAddProgramButton().addActionListener(new AddProgramListener());
        getAddCourse().addActionListener(new AddCourseListener());


        Table.table.getModel().removeTableModelListener(new TableChangedListener());
        Table.table.getModel().addTableModelListener(new TableChangedListener());
        getShowSelectedLines().setEnabled(false);
        getDeleteLines().setEnabled(false);
        getDragAndDrop().setVisible(false);
        Table.table.setAutoCreateRowSorter(true);
    }

    public static void openFile(String fileName) {
        try {
            if (getOriginPath() == null) {
                setOriginPath(fileName);
            }
            setPath(fileName);
            if (! (fileName.endsWith(".xml")) && !(fileName.endsWith(".csv")) ){
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Mauvais type de fichier","Alerte", JOptionPane.WARNING_MESSAGE);
            }
            else{
            if (fileName.endsWith(".csv")) {
                getDisplayCsv().TableCSV(fileName);
            }

            else {
                XmlToCsv xmlConverter = new XmlToCsv(fileName);
                xmlConverter.convert();

                getDisplayCsv().TableXML(
                        fileName,
                        Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey())
                );
                setComboBox(new JComboBox<>());
                for (String key : Data.dataSet.keySet()) {
                    getComboBox().addItem(key);
                }
            }
            if (!componentsInitialised) {
                initComponents();
            }
            refreshPanels(fileName);
            clearJTables();
            getContent().add(getDisplayCsv().Jscroll, BorderLayout.CENTER);
            setLastModificationAt(new Timestamp(System.currentTimeMillis()));
            frame.setVisible(true);
            System.gc();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearNorthPanel() {
        for (Component c : getNorthPanel().getComponents()) {
            getNorthPanel().remove(c);
        }
    }

    private static void clearSouthPanel() {
        for (Component c : getSouthPanel().getComponents()) {
            getSouthPanel().remove(c);
        }
    }

    private static void fillSouthPanel(String fileName) {
        if (fileName.endsWith(".csv")) {
            getSouthPanel().add(getShowSelectedLines());
        } else {
            getSouthPanel().add(getAddProgramButton());
            getSouthPanel().add(getAddCourse());
            getSouthPanel().add(getAddStudent());
            getSouthPanel().add(getShowSelectedLines());
            getSouthPanel().add(getDeleteLines());
        }
    }

    private static void fillNorthPanel(String fileName) {
        if (fileName.endsWith(".csv")) {
            getNorthPanel().add(getSearchComboBox());
            getNorthPanel().add(getRefresh());
        } else {
            getComboBox().addActionListener(new ComboBoxListener());
            getNorthPanel().add(getSearchComboBox());
            getNorthPanel().add(getShowTree());
            getNorthPanel().add(getComboBox());
            getNorthPanel().add(getRefresh());
        }
    }

    private static void refreshPanels(String fileName) {
        clearNorthPanel();
        clearSouthPanel();
        fillSouthPanel(fileName);
        fillNorthPanel(fileName);
        getFrame().revalidate();
        getFrame().repaint();
    }
}
