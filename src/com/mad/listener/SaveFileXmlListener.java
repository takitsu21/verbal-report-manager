package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.XmlWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveFileXmlListener extends AbstractApplication implements ActionListener {
    private File f;

    @Override
    public void actionPerformed(ActionEvent e) {
        f = new File("data" + ".xml");
        JFileChooser jfc = new JFileChooser(f);
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jfc.setSelectedFile(f);
        jfc.setFileFilter(new FileNameExtensionFilter(".xml", "xml"));

        jfc.setDialogTitle("Choississez un endroit pour sauvegarder votre fichier: ");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            XmlWriter.save("" + jfc.getSelectedFile());
            if (jfc.getSelectedFile().isDirectory()) {
                System.out.println("You selected the directory: " + jfc.getSelectedFile());
            }
        }
    }
}
