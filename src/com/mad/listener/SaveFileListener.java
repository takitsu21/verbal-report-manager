package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

public class SaveFileListener extends Application implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        File userDir;
        if (getComboBox().getItemCount() > 0) {
            String formatedFileName = String.join("_",
                    ((String) Objects.requireNonNull(getComboBox().getSelectedItem())).split(" "));
            userDir = new File(formatedFileName + ".csv");
        } else {
            return;
        }
        JFileChooser jfc = new JFileChooser(userDir);
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jfc.setSelectedFile(userDir);
        jfc.setFileFilter(new FileNameExtensionFilter(".csv", "csv"));

        jfc.setDialogTitle("Choississez un endroit pour sauvegarder votre fichier: ");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            Data.save(Data.dataSet.get(getComboBox().getSelectedItem()), "" + jfc.getSelectedFile());
            if (jfc.getSelectedFile().isDirectory()) {
                System.out.println("You selected the directory: " + jfc.getSelectedFile());
            }
        }
    }
}