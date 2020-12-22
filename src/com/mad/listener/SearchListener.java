package com.mad.listener;

import com.mad.Application;

import javax.sound.midi.Soundbank;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;

public class SearchListener extends Application implements ActionListener {
    private String str;
    public SearchListener(String text) {
       this.str = text;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(str);
    }
}
