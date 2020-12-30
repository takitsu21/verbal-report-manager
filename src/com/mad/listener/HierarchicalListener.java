package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.XmlToCsv;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HierarchicalListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame tmp = new JFrame("Vue hiérarchisé");
        tmp.setSize(600, 300);
        tmp.setLocationRelativeTo(null);
        tmp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        List<Element> program = Data.getChildren(Data.root, "program");
        DefaultMutableTreeNode programs = new DefaultMutableTreeNode("Programmes");
        for (Element elem : program) {
            DefaultMutableTreeNode programTree = new DefaultMutableTreeNode(String.format("%s - %s",
                    Data.read(elem, "identifier"), Data.read(elem, "name")));
            List<Element> courses = Data.getChildren(Data.root, "course");
            List<Element> options = Data.getChildren(elem, "option");
            List<Element> composites = Data.getChildren(elem, "composite");


            for (Element course : Data.getChildren(elem, "item")) {
                programTree.add(new DefaultMutableTreeNode(formatCourseName(courses,
                        course.getTextContent())));
            }
            addToProgramTree(programTree, options, courses);
            addToProgramTree(programTree, composites, courses);

            programs.add(programTree);
        }
        JScrollPane scrollPane = new JScrollPane();
        setShowHierarchicTree(new JTree(programs));
        scrollPane.setViewportView(getShowHierarchicTree());
        tmp.add(scrollPane);

        tmp.setVisible(true);
    }

    private void addToProgramTree(DefaultMutableTreeNode programTree, List<Element> values, List<Element> courses) {
        for (Element val : values) {
            DefaultMutableTreeNode currentValTree = new DefaultMutableTreeNode(Data.read(val, "name"));
            List<Element> valChildrens = Data.getChildren(val, "item");
            for (Element valC : valChildrens) {
                currentValTree.add(new DefaultMutableTreeNode(formatCourseName(courses, valC.getTextContent())));
            }
            programTree.add(currentValTree);
        }
    }

    private String formatCourseName(List<Element> courses, String courseCode) {
        Element courseName = XmlToCsv.findCourseByCode(courses, courseCode);
        if (courseName != null) {
            return String.format("%s - %s",
                    courseCode, Data.read(courseName, "name"));
        }
        return String.format("%s", courseCode);
    }
}
