package com.mad;

import com.mad.listener.SearchBarListener;
import com.mad.util.Data;
import com.mad.util.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EditableComboBoxExemple extends Application{
    public static JComboBox<String> searchComboBox;
    public EditableComboBoxExemple() {
        JTable table = Table.table;
        String blocs[]= new String[Data.dataArray[0].length-3];

        for(int i=0; i<blocs.length; i++){
            blocs[i]=Data.dataArray[0][i+3];

        }

        searchComboBox = new JComboBox<>(blocs);
        searchComboBox.setEditable(true);
        searchComboBox.addActionListener(new SearchBarListener());
        Application.setSearchBar(searchComboBox);
    }



}