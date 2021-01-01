package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.XmlWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class EnregistrerListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractApplication.save(false);
    }
}