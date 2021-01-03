package com.mad.listener;

import com.mad.AbstractApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SaveOnExitListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        if (AbstractApplication.isSaved()) {
            AbstractApplication.getFrame().dispose();
            return;
        }

        JButton saveAs = new JButton("Enregistrer sous et quitter");
        JButton save = new JButton("Enregistrer et quitter");
        JButton quitWithoutSave = new JButton("Quitter sans enregistrer");

        Toolkit.getDefaultToolkit().beep();
        save.addActionListener(e1 -> {
            if (AbstractApplication.save(false)) {
                SwingUtilities.invokeLater(() -> AbstractApplication.getFrame().dispose());
            }
        });

        saveAs.addActionListener(e12 -> {
            if (AbstractApplication.save(true)) {
                SwingUtilities.invokeLater(() -> AbstractApplication.getFrame().dispose());
            }
        });
        quitWithoutSave.addActionListener(e13 -> {
            SwingUtilities.invokeLater(() -> AbstractApplication.getFrame().dispose());
        });
        JOptionPane.showOptionDialog(AbstractApplication.getFrame(),
                "Vous n'avez pas enregistrer votre travail actuel",
                "Travail non enregistrer",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new JButton[]{saveAs, save, quitWithoutSave},
                "test");
    }
}


