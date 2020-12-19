import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        private JList listOfFiles;

        public TestPane() {
            setLayout(new BorderLayout());
            listOfFiles = new JList();
            add(new JScrollPane(listOfFiles));

            JPanel top = new JPanel();
            top.add(new JLabel("Pick a directory"));
            JButton pick = new JButton("Pick");
            pick.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = fc.showOpenDialog(fc);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File directory = fc.getSelectedFile();
                        File[] filesInDir = directory.getAbsoluteFile().listFiles();
                        addFilesToList(filesInDir);
                    }
                }

                protected void addFilesToList(File[] filesInDir) {
                    DefaultListModel<File> model = new DefaultListModel<>();
                    for (File file : filesInDir) {
                        model.addElement(file);
                    }
                    listOfFiles.setModel(model);
                }
            });
            top.add(pick);

            add(top, BorderLayout.NORTH);

            JPanel bottom = new JPanel();
            JButton analyze = new JButton("Analyze");
            bottom.add(analyze);

            add(bottom, BorderLayout.SOUTH);
        }

    }

}