package com.mad;

import com.mad.util.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EditableComboBoxExemple {
    public static JComboBox<String> searchComboBox = new JComboBox<>();
    public EditableComboBoxExemple() {
        //create a JTable
        JTable table = Table.table;

        //create combo box as a search component
        searchComboBox.setEditable(true);
        searchComboBox.addActionListener(createSearchActionListener(searchComboBox, table));
        System.out.println("ok");


    }

    private static ActionListener createSearchActionListener(JComboBox<String> comboBox, JTable table) {
        return e -> {
            if ("comboBoxChanged".equals(e.getActionCommand())) {
                String searchText = (String) comboBox.getSelectedItem();
                searchText = searchText.trim().toLowerCase();
                //if there are matches then add the search text in combo for later use
                if (Table.searchInTable(table, searchText)) {
                    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
                    //check if search text is not empty and model already doesn't have it.
                    if (!searchText.isEmpty() && model.getIndexOf(searchText) == -1) {
                        model.addElement(searchText);
                    }
                }
            }
        };
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JComboBox Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 500));
        return frame;
    }
}