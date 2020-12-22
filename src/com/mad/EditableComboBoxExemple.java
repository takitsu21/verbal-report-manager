package com.mad;

import com.mad.listener.SearchBarListener;
import com.mad.util.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EditableComboBoxExemple extends Application{
    public static JComboBox<String> searchComboBox = new JComboBox<>();
    public EditableComboBoxExemple() {
        JTable table = Table.table;

        searchComboBox.setEditable(true);
        searchComboBox.addActionListener(new SearchBarListener());
        Application.setSearchBar(searchComboBox);
    }



}