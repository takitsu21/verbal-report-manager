package com.mad.listener;

import com.mad.AbstractApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        refreshTable();

    }
}
