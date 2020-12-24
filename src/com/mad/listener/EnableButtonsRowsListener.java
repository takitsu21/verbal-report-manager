package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Arrays;

public class EnableButtonsRowsListener extends Application implements ListSelectionListener {
    @Override
    public void valueChanged(ListSelectionEvent e) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        if (getDisplayCsv().getSelectedRows().length > 0) {
                            getShowSelectedLines().setEnabled(true);
                        } else {
                            getShowSelectedLines().setEnabled(false);
                        }


                    }
                }
        );

    }
}
