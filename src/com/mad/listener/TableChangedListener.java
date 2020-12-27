package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.io.File;

public class TableChangedListener extends AbstractApplication implements TableModelListener {

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {

            int col = Table.table.getSelectedColumn();
            String courseId = String.valueOf(Table.table.getModel().getColumnName(col)).split(" ")[0];
            String newVal = (String) Table.table.getModel().getValueAt(e.getFirstRow(), e.getColumn());
            String numEtu = (String) Table.table.getModel().getValueAt(e.getFirstRow(), 0);
            updateCell(newVal, numEtu, courseId);
            getXmlEditor().save(TMP_PATH);
            Table.table.getModel().removeTableModelListener(new TableChangedListener());
            OpenFileListener.openFile(new File(TMP_PATH));
        }
    }

    private void updateCell(String newVal, String numEtu, String courseId) {
        if (newVal.isEmpty()) {
            getXmlEditor().deleteCourse(numEtu, courseId);
        } else {
            try {
                if (Double.parseDouble(newVal) <= 20.0 && Double.parseDouble(newVal) >= 0.0) {
                    if (!getXmlEditor().modifyCourse(numEtu, courseId, newVal)) {
                        getXmlEditor().addCourse(numEtu, courseId, newVal);
                    }
                }
            } catch (NumberFormatException exc) {
                if (newVal.equalsIgnoreCase("abi") || newVal.equalsIgnoreCase("abj")) {
                    if (!getXmlEditor().modifyCourse(numEtu, courseId, newVal.toUpperCase())) {
                        getXmlEditor().addCourse(numEtu, courseId, newVal.toUpperCase());
                    }
                }
            }
        }
    }
}
