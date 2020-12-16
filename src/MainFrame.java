import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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
    private String csv;
    private Container content;
    //    private List<JScrollPane> tables = new ArrayList<>();
    private JScrollPane oldTable = null;
//    private JComboBox<String> comboBox;
    private Map<String, String> data = new HashMap<>();
    private String dataString = "";


    private void fileListener(ActionEvent event) {
        //JOptionPane.showMessageDialog( frame, "New File invoked" );
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
//         int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            try {
                this.frame.getContentPane().setVisible(false);

                this.displayCsv = new Table();
                if (path.substring(path.length() - 4).equals(".csv")) {

                    displayCsv.TableCSV(path);
                    System.out.println("csv");
                } else {
                    XML2CSV a = new XML2CSV(path);
                    a.converte();
                    data = a.dicoData;
                    System.out.println(data);

                    //ajouté le menu defilant
                    JComboBox<Object> comboBox = new JComboBox<>();
                    for (String key : data.keySet()) {
                        comboBox.addItem(key);
                    }
                    comboBox.addActionListener(new ChooseProgram());
                    frame.add(comboBox);

                    System.out.println(data.entrySet().iterator().next().getKey());
                    dataString = data.get(data.entrySet().iterator().next().getKey());

                    displayCsv.TableXML(path, dataString);
                    System.out.println("xml");
                }

                content = frame.getContentPane();
//                content.removeAll();
                removeOldTable();

                content.add(displayCsv.Jscroll);
                oldTable = displayCsv.Jscroll;
//                tables.add();


                this.frame.getContentPane().setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
            this.repaint();
        }
    }

    public Map<String, String> getData() {
        return data;
    }

    public void removeOldTable() {
        if (oldTable != null) {
            content.remove(oldTable);
            oldTable = null;
        }

    }

    public String getCsv() {
        return csv;
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


    private void save_file_chooser(ActionEvent e) {


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


    public MainFrame() throws IOException, ParserConfigurationException, SAXException {
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
        if (displayCsv.Jscroll != null) {
            content.add(displayCsv.Jscroll);
        }
        content.setLayout(new FlowLayout());


        JButton bout = new JButton("Save xml");
        frame.add(bout, BorderLayout.SOUTH);


        JButton button = new JButton("Save csv");
        button.addActionListener(this::save_file_chooser);
        button.setBounds(30, 40, 20, 30);
        frame.add(button);


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