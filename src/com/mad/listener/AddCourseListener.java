package com.mad.listener;

import com.mad.AbstractPopUpCours;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AddCourseListener extends AbstractPopUpCours implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        setTmp(new JFrame("Ajouter un Cours"));
        getTmp().setSize(300, 200);
        getTmp().setLocationRelativeTo(null);
        getTmp().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIdLabel(new JLabel("Identifiant:"));
        setIdField(new JTextField(10));
        setCourseLabel(new JLabel("Nom:        "));
        setCourseNameFiled(new JTextField(10));
        setCoefLabel(new JLabel("coefficient:"));
        setCoefField(new JTextField(10));


        Container contentPane =  getTmp().getContentPane();
        setGrille( new JPanel(new GridLayout(3, 1, 5, 1)));

        setP1(new JPanel());
        getP1().add(getIdLabel());
        getP1().add(getIdField());
        getGrille().add(getP1());

        setP2(new JPanel());
        getP2().add(getCourseLabel());
        getP2().add(getCourseNameFiled());
        getGrille().add(getP2());

       setP3( new JPanel());
        getP3().add(getCoefLabel());
        getP3().add(getCoefField());
        getGrille().add(getP3());

        contentPane.setLayout(new BorderLayout());
        contentPane.add(getGrille(), BorderLayout.CENTER);
        JPanel southPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton validate = new JButton("valider");
        validate.addActionListener(new ValidateCourseListener());
        southPane.add(validate);
        contentPane.add(southPane, BorderLayout.SOUTH);


        getTmp().setVisible(true);


    }


}
