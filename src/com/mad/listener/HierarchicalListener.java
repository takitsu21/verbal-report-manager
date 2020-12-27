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
        Element currentProgram = null;
        for (Element elem : program) {
            if (XmlToCsv.read(elem, "identifier").equals(getComboBox().getSelectedItem())) {
                currentProgram = elem;
                break;
            }
        }
        DefaultMutableTreeNode programTree = new DefaultMutableTreeNode(String.format("%s - %s",
                comboBox.getSelectedItem(), XmlToCsv.read(currentProgram, "name")));
        List<Element> courses = Data.getChildren(Data.root, "course");
        List<Element> options = Data.getChildren(currentProgram, "option");
        List<Element> composites = Data.getChildren(currentProgram, "composite");


        DefaultMutableTreeNode coursesTree = new DefaultMutableTreeNode("Cours " +
                XmlToCsv.read(currentProgram, "identifier"));
        for (Element course : Data.getChildren(currentProgram, "item")) {
            coursesTree.add(new DefaultMutableTreeNode(formatCourseName(courses,
                    course.getTextContent())));
        }
        programTree.add(coursesTree);
        addToProgramTree(programTree, options, courses);
        addToProgramTree(programTree, composites, courses);

        JScrollPane scrollPane = new JScrollPane();
        setShowHierarchicTree(new JTree(programTree));
        scrollPane.setViewportView(getShowHierarchicTree());
        tmp.add(scrollPane);

        tmp.setVisible(true);
    }

    private void addToProgramTree(DefaultMutableTreeNode programTree, List<Element> values, List<Element> courses) {
        for (Element val : values) {
            DefaultMutableTreeNode currentValTree = new DefaultMutableTreeNode(XmlToCsv.read(val, "name"));
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
                    courseCode, XmlToCsv.read(courseName, "name"));
        }
        return String.format("%s", courseCode);
    }
}
