package com.mad;

import com.mad.util.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GUI {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
            System.out.println("aïe something went wrong");
        }
        MainFrame application = new MainFrame();
        application.setFrame(new JFrame("MAD"));
        application.getFrame().setSize(800, 600);
        application.getFrame().setLocationRelativeTo(null);
        application.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menu = new JMenuBar();
        application.getFrame().setJMenuBar(menu);
        JMenu file = new JMenu("Fichiers");
        file.setMnemonic('F');
        menu.add(file);
        JMenuItem ouvrir = new JMenuItem("Ouvrir...");
        ouvrir.setMnemonic('O');
        ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        ouvrir.addActionListener(application::fileListener);
        file.add(ouvrir);
        JMenu help = new JMenu("help");
        menu.add(help);
        JMenuItem about = new JMenuItem("A propos");
        help.add(about);
        application.setSouthPanel(new JPanel());
        application.setNorthPanel(new JPanel());

        application.getSouthPanel().setLayout(new FlowLayout(FlowLayout.RIGHT));
        application.getNorthPanel().setLayout(new FlowLayout(FlowLayout.LEFT));
        application.setDisplayCsv(new Table());

        application.setContent(application.getFrame().getContentPane());

        if (application.getDisplayCsv().Jscroll != null) {
            application.getContent().add(application.getDisplayCsv().Jscroll, BorderLayout.CENTER);
        }

        JButton saveXmlFile = new JButton("Save xml");
        application.getSouthPanel().add(saveXmlFile);

        JButton saveCsvFile = new JButton("Save csv");
        saveCsvFile.addActionListener(application::saveFileChooser);
        saveCsvFile.setBounds(30, 40, 20, 30);
        application.getSouthPanel().add(saveCsvFile);

        application.getContent().add(application.getNorthPanel(), BorderLayout.NORTH);
        application.getContent().add(application.getSouthPanel(), BorderLayout.SOUTH);

        application.getFrame().setVisible(true);
    }
}
