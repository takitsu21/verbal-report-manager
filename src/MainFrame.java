import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class MainFrame extends JPanel {
    private String path; //= "../minutes_info.csv";
    public JFrame frame;
    private Table displayCsv;
    private Container content;
    private JComboBox<String> comboBox = new JComboBox<>();
    private Map<String, String> data;
    private boolean isFirstFile = true;


    private void fileListener(ActionEvent event) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);


        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            try {
                displayCsv = new Table();
                if (path.endsWith(".csv")) {

                    displayCsv.TableCSV(path);
                    System.out.println("csv");
                } else {
                    XML2CSV a = new XML2CSV(path);
                    a.converte();

                    Data.dataSet = a.dicoData;
                    displayCsv.TableXML(path, Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));

                    System.out.println(comboBox.getItemCount());
                    JPanel panel_north = new JPanel();
                    if (comboBox.getItemCount() > 0) {
                        comboBox = new JComboBox<>();
                        isFirstFile = false;
                    }
                    for (String key : Data.dataSet.keySet()) {
                        comboBox.addItem(key);
                    }
                    comboBox.addActionListener(this::comboBoxListener);
                    if (isFirstFile) {
                        panel_north.add(comboBox);
                        content.add(panel_north, BorderLayout.NORTH);
                    }
                }
                if (isFirstFile) {
                    content = frame.getContentPane();
                    content.add(displayCsv.Jscroll);
                    content.setVisible(true);
                } else {
                    System.out.println(Data.dataSet.entrySet().iterator().next().getKey());
                    String[][] newArr = Table.sDataToArray(
                            Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));
                    displayCsv.table.setModel(
                            new DefaultTableModel(Arrays.copyOfRange(newArr, 1, newArr.length), newArr[0]));
                }

                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

//    public void removeOldTable() {
//        if (!Data.oldScroll.isEmpty()) {
//            for (JScrollPane t : Data.oldScroll) {
//                content.remove(t);
//                content.revalidate();
//
//                Data.oldScroll.remove(t);
//                System.out.println("scrollpane");
////            }
//            }
//        }
//
//        if (!Data.oldTable.isEmpty()) {
//            for (JTable t : Data.oldTable) {
//                content.remove(t);
//                content.revalidate();
//
//                Data.oldTable.remove(Data.oldTable);
//                System.out.println("tablepane");
//            }
//        }
//        repaint();
//    }

    public Table getDisplayCsv() {
        return displayCsv;
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


    private void saveFileChooser(ActionEvent e) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory to save your file: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            //System.out.println(jfc.getSelectedFile());
            save(displayCsv.getCsv(), "" + jfc.getSelectedFile());
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
        repaint();
    }


    public MainFrame() {
        frame = new JFrame("Jalon 2 ");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);
        JMenu file = new JMenu("Fichiers");
        file.setMnemonic('F');
        menu.add(file);
        JMenuItem ouvrir = new JMenuItem("Ouvrir...");
        ouvrir.setMnemonic('O');
        ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        ouvrir.addActionListener(this::fileListener);
        file.add(ouvrir);


        JMenu help = new JMenu("help");
        menu.add(help);
        JMenuItem about = new JMenuItem("A propos");
        help.add(about);
        displayCsv = new Table();


        content = frame.getContentPane();
        JPanel panel_south = new JPanel();
        panel_south.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton saveXmlButton = new JButton("Save xml");
        panel_south.add(saveXmlButton, BorderLayout.SOUTH);
        JButton saveCsvButton = new JButton("Save csv");
        saveCsvButton.addActionListener(this::saveFileChooser);
        saveCsvButton.setBounds(30, 40, 20, 30);
        panel_south.add(saveCsvButton);
        content.add(panel_south);


    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        try {
            UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
            System.out.println("aïe something went wrong");
        }
        MainFrame fra = new MainFrame();

        fra.frame.setVisible(true);


    }
}