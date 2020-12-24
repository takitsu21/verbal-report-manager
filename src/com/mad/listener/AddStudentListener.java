package com.mad.listener;

import com.mad.util.Data;
import com.mad.util.XmlToCsv;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddStudentListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame tmp = new JFrame("Vue hiérarchisé");
        tmp.setSize(600, 300);
        tmp.setLocationRelativeTo(null);
        tmp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        //pnl.setLayout(new BorderLayout());

        JTextField studNum=new JTextField("Student number");
        studNum.setSize(60, 30);
        pnl.add(new JLabel("Student number:"));
        pnl.add(studNum);

        JTextField name=new JTextField("Name");
        name.setSize(60, 30);
        pnl.add(new JLabel("Name:"));
        pnl.add(name);

        JTextField surname=new JTextField("Surname");
        surname.setSize(60, 30);
        pnl.add(new JLabel("Surname:"));
        pnl.add(surname);

        JTextField program=new JTextField("Program");
        surname.setSize(60, 30);
        pnl.add(new JLabel("Program:"));
        pnl.add(program);

        tmp.add(pnl, BorderLayout.NORTH);

        List<Element> listCourses = Data.getChildren(Data.root, "course");

        JCheckBox[] cours= new JCheckBox[listCourses.size()+1];
        JPanel panel = new JPanel();
        JScrollPane scroll = new JScrollPane();
        scroll.setPreferredSize(new Dimension(450, 110));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        cours[0]=new JCheckBox("Select All");
        scroll.add(cours[0]);

        for (int i=0; i<listCourses.size(); i++){
            cours[i]=new JCheckBox(XmlToCsv.read(listCourses.get(i), "identifier") + " - " + XmlToCsv.read(listCourses.get(i), "name"));

            panel.add(cours[i]);
            System.out.println(cours[i].getText());
        }

        scroll.add(panel);

        tmp.add(scroll, BorderLayout.CENTER);

        tmp.setVisible(true);
    }
}
