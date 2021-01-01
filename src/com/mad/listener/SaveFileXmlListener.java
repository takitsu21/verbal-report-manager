package com.mad.listener;

import com.mad.AbstractApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveFileXmlListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractApplication.save(true);
    }
}