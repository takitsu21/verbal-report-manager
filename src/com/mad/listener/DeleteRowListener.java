package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlMethodType;
import com.mad.util.XmlWriter;
import org.w3c.dom.Node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteRowListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = Table.getSelectedRows();
        for (int selectedRow : selectedRows) {
            String numEtu = Data.dataArray[selectedRow + 1][0];
            Node studentNode = XmlWriter.getStudent(numEtu);
            insertAction(
                    () -> XmlWriter.deleteStudent(numEtu),
                    "student", studentNode, XmlMethodType.DELETE, false);
        }
        refreshTable();
        Table.setTemporaryTable(Data.dataArray);
        Table.setNewModelTable(Table.table, Data.dataArray);
    }
}
