package com.mad.listener;
import com.mad.AbstractPopUpCours;
import com.mad.util.XmlWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ValidateCourseListener extends AbstractPopUpCours implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String id = getID();
        String name = getCourseName();
        String coef = getCoef();
//        System.out.println(id);
//        System.out.println(name);
//        System.out.println(coef);
        if (getXmlEditor().addCourseGeneral(name,id,coef)){
            getTmp().dispose();
        }




    }
}
