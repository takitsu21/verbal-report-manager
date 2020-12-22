package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;
import com.mad.util.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;

public class ComboBoxListener extends Application implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<?> combo = (JComboBox<?>) e.getSource();
        String[][] newArr = Table.sDataToArray(
                Data.dataSet.get(Objects.requireNonNull(combo.getSelectedItem()).toString()));
        Table.setNewModelTable(getDisplayCsv().table, newArr);
    }
}
