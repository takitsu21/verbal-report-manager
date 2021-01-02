package com.mad;

import com.mad.listener.*;
import com.mad.util.Table;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GUI extends AbstractApplication {
    public GUI() {
        super();
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch (Exception ign) {
            ign.printStackTrace();
        }
        setFrame(new JFrame("MAD"));
        getFrame().setSize(920, 600);
        getFrame().setLocationRelativeTo(null);
        getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JMenuBar menu = new JMenuBar();
        getFrame().setJMenuBar(menu);
        JMenu file = new JMenu("Fichiers");
        file.setMnemonic('F');
        menu.add(file);

        JMenuItem ouvrir = new JMenuItem("Ouvrir...");
        ouvrir.setMnemonic('O');
        ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        ouvrir.addActionListener(new OpenFileListener());

        file.add(ouvrir);

        JMenuItem enregistrer = new JMenuItem("Enregistrer", 's');
        enregistrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        enregistrer.addActionListener(new EnregistrerListener());
        file.add(enregistrer);

        JMenu export = new JMenu("Enregistrer sous");
        export.setMnemonic('E');
        JMenuItem xmlItem = new JMenuItem("xml");
        JMenuItem csvItem = new JMenuItem("csv");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");

//        undo.setMnemonic('Z');
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
//        redo.setMnemonic('Y');
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
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

        getSouthPanel().setLayout(new FlowLayout(FlowLayout.LEFT));
        getNorthPanel().setLayout(new FlowLayout(FlowLayout.LEFT));
        setDisplayCsv(new Table());

        setContent(getFrame().getContentPane());

        if (getDisplayCsv().Jscroll != null) {
            getContent().add(getDisplayCsv().Jscroll, BorderLayout.CENTER);
        }

        getContent().add(getNorthPanel(), BorderLayout.NORTH);
        getContent().add(getSouthPanel(), BorderLayout.SOUTH);

        setDragAndDrop(new JLabel("Drag XML or CSV here.", SwingConstants.CENTER));
        DropTargetListener dtl = new DragDropListener();
        new DropTarget(getFrame(), dtl);

        getFrame().add(BorderLayout.CENTER, getDragAndDrop());
        getFrame().setIconImage(ImageIO.read(new File("./MAD16x16.png")));

        getFrame().addWindowListener(new SaveOnExitListener());
        getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new File(getTmpPath()).delete();
                System.out.println("tmp file deleted");
            }
        });

        getFrame().setVisible(true);
    }
}

