package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteRowListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = getDisplayCsv().getSelectedRows();
        String[] studentId;
        for (int i = 0; i < selectedRows.length; i++) {
            XmlWriter.deleteStudent(Data.dataArray[selectedRows[i] + 1][0]);
        }
        refreshTable();



    }
}
