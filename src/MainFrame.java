import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class MainFrame extends JPanel {
    private String path; //= "../minutes_info.csv";
    public JFrame frame;
    private final JPanel southPanel;
    private final JPanel northPanel;
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
                    if (comboBox.getItemCount() > 0) {
                        comboBox = new JComboBox<>();
                        isFirstFile = false;
                    }
                    for (String key : Data.dataSet.keySet()) {
                        comboBox.addItem(key);
                    }
                    comboBox.addActionListener(this::comboBoxListener);
                    if (isFirstFile) {
                        northPanel.add(comboBox);

                    }
                }
                if (isFirstFile) {
                    content = frame.getContentPane();
                    content.add(displayCsv.Jscroll,BorderLayout.CENTER);
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


        southPanel = new JPanel();
        northPanel = new JPanel();

        southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        displayCsv = new Table();


        content = frame.getContentPane();


        if (displayCsv.Jscroll != null) {
            content.add(displayCsv.Jscroll,BorderLayout.CENTER);
        }

        JButton bout = new JButton("Save xml");
        southPanel.add(bout);


        JButton button = new JButton("Save csv");
        button.addActionListener(this::saveFileChooser);
        button.setBounds(30, 40, 20, 30);
        southPanel.add(button);
        content.add(northPanel,BorderLayout.NORTH);
        content.add(southPanel,BorderLayout.SOUTH);
    }

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
            System.out.println("aïe something went wrong");
        }
        MainFrame fra = new MainFrame();

        fra.frame.setVisible(true);


    }
}
