package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.XmlWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;

public class EnregistrerListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String path = getOriginPath();
        System.out.println(path);
        if(path.endsWith(".csv")){
            System.out.println(Arrays.deepToString( Data.dataArray));
            try {
                PrintWriter pr = new PrintWriter(path);
                for(String[] l : Data.dataArray){
                    StringBuilder acc = new StringBuilder();
                    for(String m : l){
                        acc.append("\"").append(m).append("\",");
                    }
                    System.out.println(acc);
                    pr.println(acc);
                }
            } catch (FileNotFoundException fileNotFoundException) {
                JOptionPane.showMessageDialog(getFrame(),"Erreur FATAL");
            }



        }
        if(path.endsWith(".xml")){
            XmlWriter.save(path);
        }



    }
}
