package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ComboBoxListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<?> combo = (JComboBox<?>) e.getSource();
        String[][] newArr = Table.sDataToArray(
                Data.dataSet.get(Objects.requireNonNull(combo.getSelectedItem()).toString()));

        Table.setNewModelTable(Table.table, newArr);
        Table.table.getModel().removeTableModelListener(new TableChangedListener());
        Table.table.getSelectionModel().removeListSelectionListener(new EnableButtonsRowsListener());
        Table.table.getSelectionModel().addListSelectionListener(new EnableButtonsRowsListener());
        Table.table.getModel().addTableModelListener(new TableChangedListener());
    }
}
