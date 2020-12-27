package com.mad.listener;

import com.mad.AbstractApplication;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EnableButtonsRowsListener extends AbstractApplication implements ListSelectionListener {
    @Override
    public void valueChanged(ListSelectionEvent e) {
        SwingUtilities.invokeLater(
                () -> {
                    if (getDisplayCsv().getSelectedRows().length > 0) {
                        getShowSelectedLines().setEnabled(true);
                        getDeleteLines().setEnabled(true);
                    } else {
                        getShowSelectedLines().setEnabled(false);
                        getDeleteLines().setEnabled(false);
                    }
                }
        );

    }
}
