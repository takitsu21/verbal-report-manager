package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;
import com.mad.util.XmlWriter;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


public class TableChangedListener extends AbstractApplication implements TableModelListener {
    public TableChangedListener() {
        //System.out.println("TableChangedListener added");
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            int col = Table.table.getSelectedColumn();
            String courseId = String.valueOf(Table.table.getModel().getColumnName(col)).split(" ")[0];
            String newVal = (String) Table.table.getModel().getValueAt(e.getFirstRow(), e.getColumn());
            String numEtu = (String) Table.table.getModel().getValueAt(e.getFirstRow(), 0);
            updateCell(newVal, numEtu, courseId);
            XmlWriter.save(AbstractApplication.TMP_PATH);
            XmlToCsv xmlConverter = new XmlToCsv(AbstractApplication.TMP_PATH);
            xmlConverter.convert();
            Table.table.getModel().removeTableModelListener(this);
            Table.table.getModel().addTableModelListener(this);
        }
    }

    private void updateCell(String newVal, String numEtu, String courseId) {
        if (newVal.isEmpty()) {
            XmlWriter.deleteCourse(numEtu, courseId);
        } else {
            try {
                if (Double.parseDouble(newVal) <= 20.0 && Double.parseDouble(newVal) >= 0.0) {
                    if (!XmlWriter.modifyCourse(numEtu, courseId, newVal)) {
                        XmlWriter.addCourse(numEtu, courseId, newVal);
                    }
                }
            } catch (NumberFormatException exc) {
                if (newVal.equalsIgnoreCase("abi") || newVal.equalsIgnoreCase("abj")) {
                    if (XmlWriter.modifyCourse(numEtu, courseId, newVal.toUpperCase())) {
                        XmlWriter.addCourse(numEtu, courseId, newVal.toUpperCase());
                    }
                }
            }
        }
    }
}
