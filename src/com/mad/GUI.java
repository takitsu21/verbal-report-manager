package com.mad;

import com.mad.listener.DragDropListener;
import com.mad.listener.OpenFileListener;
import com.mad.listener.SaveFileListener;
import com.mad.util.Data;
import com.mad.util.Table;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class GUI {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        AbstractApplication.setFrame(new JFrame("MAD"));
        AbstractApplication.getFrame().setSize(800, 600);
        AbstractApplication.getFrame().setLocationRelativeTo(null);
        AbstractApplication.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menu = new JMenuBar();
        AbstractApplication.getFrame().setJMenuBar(menu);
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
        AbstractApplication.setSouthPanel(new JPanel());
        AbstractApplication.setNorthPanel(new JPanel());

        AbstractApplication.getSouthPanel().setLayout(new FlowLayout(FlowLayout.RIGHT));
        AbstractApplication.getNorthPanel().setLayout(new FlowLayout(FlowLayout.LEFT));
        AbstractApplication.setDisplayCsv(new Table());

        AbstractApplication.setContent(AbstractApplication.getFrame().getContentPane());

        if (AbstractApplication.getDisplayCsv().Jscroll != null) {
            AbstractApplication.getContent().add(AbstractApplication.getDisplayCsv().Jscroll, BorderLayout.CENTER);
        }

        AbstractApplication.getContent().add(AbstractApplication.getNorthPanel(), BorderLayout.NORTH);
        AbstractApplication.getContent().add(AbstractApplication.getSouthPanel(), BorderLayout.SOUTH);

        AbstractApplication.setDragAndDrop(new JLabel("Drag XML or CSV here.", SwingConstants.CENTER));
        DropTargetListener dtl = new DragDropListener();
        new DropTarget(AbstractApplication.getFrame(), dtl);

        AbstractApplication.getFrame().add(BorderLayout.CENTER, AbstractApplication.getDragAndDrop());

        AbstractApplication.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    File file = new File(AbstractApplication.TMP_PATH);
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Data.doc = dBuilder.parse(file); // ouverture et lecture du fichier XML
                    Data.doc.getDocumentElement().normalize(); // normalise le contenu du fichier, opération très conseillée
                    Data.root = Data.doc.getDocumentElement();
                    AbstractApplication.getXmlEditor().save("./data.xml");
                    if (file.delete()) {
                        System.out.println("Overwriting data.xml");
                    }
                } catch (Exception exc) {
                    System.out.println("Cannot save");
                }

            }
        });

        AbstractApplication.getFrame().setVisible(true);
    }
}

