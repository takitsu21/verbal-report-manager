package com.mad;

import com.mad.listener.DragDropListener;
import com.mad.listener.EnregistrerListener;
import com.mad.listener.OpenFileListener;
import com.mad.listener.SaveFileListener;
import com.mad.listener.SaveFileXmlListener;
import com.mad.listener.SaveOnExitListener;
import com.mad.util.Table;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class GUI extends AbstractApplication {
    public GUI() {
        super();
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        setFrame(new JFrame("MAD"));
        getFrame().setSize(920, 600);
        getFrame().setLocationRelativeTo((Component)null);
        getFrame().setDefaultCloseOperation(0);
        JMenuBar menu = new JMenuBar();
        getFrame().setJMenuBar(menu);
        JMenu file = new JMenu("Fichiers");
        file.setMnemonic('F');
        menu.add(file);
        JMenuItem ouvrir = new JMenuItem("Ouvrir...");
        ouvrir.setMnemonic('O');
        ouvrir.setAccelerator(KeyStroke.getKeyStroke(79, 128));
        ouvrir.addActionListener(new OpenFileListener());
        file.add(ouvrir);
        JMenuItem enregistrer = new JMenuItem("Enregistrer", 115);
        enregistrer.setAccelerator(KeyStroke.getKeyStroke(83, 128));
        enregistrer.addActionListener(new EnregistrerListener());
        file.add(enregistrer);
        JMenu export = new JMenu("Enregistrer sous");
        export.setMnemonic('E');
        JMenuItem xmlItem = new JMenuItem("xml");
        JMenuItem csvItem = new JMenuItem("csv");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");
        undo.setAccelerator(KeyStroke.getKeyStroke(90, 128));
        redo.setAccelerator(KeyStroke.getKeyStroke(89, 128));
        undo.addActionListener(e -> undo());
        redo.addActionListener(e -> redo());
        file.add(undo);
        file.add(redo);
        csvItem.addActionListener(new SaveFileListener());
        xmlItem.addActionListener(new SaveFileXmlListener());
        file.add(export);
        export.add(xmlItem);
        export.add(csvItem);
        JMenu help = new JMenu("Help");
        help.setMnemonic('H');
        menu.add(help);
        JMenuItem about = new JMenuItem("A propos");
        help.add(about);
        setSouthPanel(new JPanel());
        setNorthPanel(new JPanel());
        getSouthPanel().setLayout(new FlowLayout(0));
        getNorthPanel().setLayout(new FlowLayout(0));
        setDisplayCsv(new Table());
        setContent(getFrame().getContentPane());
        if (getDisplayCsv().Jscroll != null) {
            getContent().add(getDisplayCsv().Jscroll, "Center");
        }

        getContent().add(getNorthPanel(), "North");
        getContent().add(getSouthPanel(), "South");
        setDragAndDrop(new JLabel("Drag XML or CSV here.", 0));
        DropTargetListener dtl = new DragDropListener();

        new DropTarget(getFrame(), dtl);
        getFrame().add(BorderLayout.CENTER, getDragAndDrop());
        getFrame().setIconImage(ImageIO.read(new File("./MAD16x16.png")));
        getFrame().addWindowListener(new SaveOnExitListener());
        getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                (new File(AbstractApplication.getTmpPath())).delete();
                System.out.println("tmp file deleted");
            }
        });
        getFrame().setVisible(true);
    }
}
