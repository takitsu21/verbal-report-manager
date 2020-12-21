package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;
import com.mad.util.Table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ResetTableListener extends Application implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Table.setNewModelTable(getDisplayCsv().table, Data.dataArray);
    }
}
