package com.mad.listener;

import com.mad.Application;
import com.mad.util.Table;
import org.xml.sax.SAXException;

import javax.sound.midi.Soundbank;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLOutput;

public class SearchListener extends Application implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Table.selectEtu(getSearchBar().getText(), getPath());
        } catch (IOException | SAXException | ParserConfigurationException ioException) {
            ioException.printStackTrace();
        }
    }
}
