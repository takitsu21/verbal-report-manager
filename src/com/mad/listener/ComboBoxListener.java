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

        Table.setTemporaryTable(newArr);
        Table.setNewModelTable(Table.table, newArr);
        String[] blocs = new String[Data.dataArray[0].length - 3];
        if (blocs.length >= 0) System.arraycopy(Data.dataArray[0], 3, blocs, 0, blocs.length);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(blocs);
        getSearchComboBox().setModel(model);
        Table.table.getModel().removeTableModelListener(new TableChangedListener());
        Table.table.getModel().addTableModelListener(new TableChangedListener());
        //refreshTable();
    }
}
