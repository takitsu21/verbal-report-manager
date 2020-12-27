package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.XmlWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteRowListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = getDisplayCsv().getSelectedRows();
        for (int selectedRow : selectedRows) {
            XmlWriter.deleteStudent(Data.dataArray[selectedRow + 1][0]);
        }
        refreshTable();
    }
}
