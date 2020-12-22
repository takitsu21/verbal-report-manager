package com.mad;

import com.mad.listener.*;
import com.mad.util.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GUI {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        Application.setFrame(new JFrame("MAD"));
        Application.getFrame().setSize(800, 600);
        Application.getFrame().setLocationRelativeTo(null);
        Application.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menu = new JMenuBar();
        Application.getFrame().setJMenuBar(menu);
        JMenu file = new JMenu("Fichiers");
        file.setMnemonic('F');
        menu.add(file);
        JMenu export = new JMenu("Exporter");
        export.setMnemonic('E');
        JMenuItem xmlItem = new JMenuItem("xml");
        JMenuItem csvItem = new JMenuItem("csv");
        csvItem.addActionListener(new SaveFileListener());
        file.add(export);
        export.add(xmlItem);
        export.add(csvItem);
        JMenuItem ouvrir = new JMenuItem("Ouvrir...");
        ouvrir.setMnemonic('O');
        ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        ouvrir.addActionListener(new OpenFileListener());
        file.add(ouvrir);
        JMenu help = new JMenu("Help");
        help.setMnemonic('H');
        menu.add(help);
        JMenuItem about = new JMenuItem("A propos");
        help.add(about);
        Application.setSouthPanel(new JPanel());
        Application.setNorthPanel(new JPanel());

        Application.getSouthPanel().setLayout(new FlowLayout(FlowLayout.RIGHT));
        Application.getNorthPanel().setLayout(new FlowLayout(FlowLayout.LEFT));
        Application.setDisplayCsv(new Table());

        Application.setContent(Application.getFrame().getContentPane());

        if (Application.getDisplayCsv().Jscroll != null) {
            Application.getContent().add(Application.getDisplayCsv().Jscroll, BorderLayout.CENTER);
        }

//        JButton saveXmlFile = new JButton("Save xml");
//        Application.getSouthPanel().add(saveXmlFile);
//
        /*JTextField recherche = new JTextField();
        Dimension d = recherche.getPreferredSize();
        recherche.setPreferredSize(new Dimension(100, (int) d.getHeight()));
        Application.setSearchBar(recherche);
        JButton validate = new JButton("Rechercher");
        validate.addActionListener(new SearchBarListener());
        Application.getNorthPanel().add(recherche);
        Application.getNorthPanel().add(validate);*/






        JButton test = new JButton("Test");
        test.addActionListener(new SelectRowsListener());
        test.setBounds(30, 40, 20, 30);
        Application.getSouthPanel().add(test);

        JButton resetTable = new JButton("Reset table");
        resetTable.addActionListener(new ResetTableListener());
        Application.getSouthPanel().add(resetTable);

       Application.getContent().add(Application.getNorthPanel(), BorderLayout.NORTH);
       Application.getContent().add(Application.getSouthPanel(), BorderLayout.SOUTH);

        Application.getFrame().setVisible(true);
    }
}

