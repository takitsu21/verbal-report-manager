package com.mad;

import com.mad.listener.*;
import com.mad.util.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
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

        Application.getContent().add(Application.getNorthPanel(), BorderLayout.NORTH);
        Application.getContent().add(Application.getSouthPanel(), BorderLayout.SOUTH);

        Application.setDragAndDrop(new JLabel("Drag XML or CSV here.", SwingConstants.CENTER));
        DropTargetListener dtl = new DragDropListener();
        new DropTarget(Application.getFrame(), dtl);
        Application.getFrame().add(BorderLayout.CENTER, Application.getDragAndDrop());

        Application.getFrame().setVisible(true);
    }
}
