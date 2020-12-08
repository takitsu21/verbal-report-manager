import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class MainFrame extends JPanel {
    private String path = "./minutes_info.csv";
    public JFrame frame;
    private Table displayCsv;
    private String csv;

    private void fileListener(ActionEvent event)  {
        //JOptionPane.showMessageDialog( frame, "New File invoked" );
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
           path = selectedFile.getAbsolutePath();
           try{this.frame.getContentPane().setVisible(false);
           this.displayCsv = new Table(path);
           Container content = frame.getContentPane();
           content.add(displayCsv.Jscroll);
           this.frame.getContentPane().setVisible(true);

           }
           catch (IOException e) {
               e.printStackTrace();
           }
            this.repaint();
        }
    }

    private void save(String data, String name) {
        byte[] bs = data.getBytes();
        Path path = Paths.get(name);

        try {
            Path writtenFilePath = Files.write(path, bs);
            System.out.println("save "+name);
        }
        catch (Exception e){
            System.out.println("Erreur: "+e);
        }
    }


    private void save_file_chooser(ActionEvent e) {


        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory to save your file: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            //System.out.println(jfc.getSelectedFile());
            save(displayCsv.csv, ""+jfc.getSelectedFile());
            if (jfc.getSelectedFile().isDirectory()) {
                System.out.println("You selected the directory: " + jfc.getSelectedFile());
                //save(csv, ""+jfc.getSelectedFile());
            }
        }
    }





    public MainFrame() throws IOException {
        frame = new JFrame("Jalon 2 ");
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayCsv = new Table(path);
        Container content = frame.getContentPane();
        content.add(displayCsv.Jscroll);
        content.setLayout(new FlowLayout());
        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);
        JMenu file = new JMenu("Fichiers");
        file.setMnemonic('F');
        menu.add(file);
        JMenuItem ouvrir = new JMenuItem("Ouvrir...");
        ouvrir.setMnemonic('O');
        ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_DOWN_MASK));
        ouvrir.addActionListener(this::fileListener);
        file.add(ouvrir);

        JButton bout = new JButton("yo la team");
        frame.add(bout,BorderLayout.SOUTH);

        JMenu help = new JMenu("help");
        menu.add(help);
        JMenuItem about = new JMenuItem("A propos");
        help.add(about);


        JButton button = new JButton("Save");
        button.addActionListener(this::save_file_chooser);
        button.setBounds(30,40,20,30);
        frame.add(button);




    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
            System.out.println("aïe something went wrong");
        }
        MainFrame fra = new MainFrame();

        fra.frame.setVisible(true);


    }
    }