package com.mad.util;

import com.mad.util.exceptions.StudentNotFoundException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;


public class XmlWriter {
    public static Node getStudent(String studentId) {
        NodeList nodeList = Data.root.getElementsByTagName("student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodeList.item(i);
                String currentStudentId = element.getElementsByTagName("identifier").item(0).getTextContent();
                if (studentId.equalsIgnoreCase(currentStudentId)) {
                    return node;
                }
            }
        }
        throw new StudentNotFoundException(String.format("%s N'a pas été trouvé", studentId));
    }

    public static boolean deleteStudent(String studentId) {
        try {
            Data.root.removeChild(getStudent(studentId));
            return true;
        } catch (StudentNotFoundException e) {
            return false;
        }
    }

    public static boolean addStudent(Node newStudent) {
        try {
            Data.root.appendChild(newStudent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean addProgram(Node program) {
        try {
            Data.root.appendChild(program);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean modifyCourse(String studentId, String courseId, String val) {
        Element student = (Element) getStudent(studentId);
        List<Element> grades = Data.getChildren(student, "grade");
        for (Element e : grades) {
            if (XmlToCsv.read(e, "item").equalsIgnoreCase(courseId)) {
//                student.removeChild(e);
                List<Element> values = Data.getChildren(e, "value");
                for (Element v : values) {
                    v.setTextContent(val);
                }
//                student.appendChild(e);
                System.out.println("student modified");
                return true;
            }
        }
        return false;
    }

    public static boolean deleteCourse(String studentId, String courseId) {
        Element student = (Element) getStudent(studentId);
        List<Element> grades = Data.getChildren(student, "grade");
        for (Element e : grades) {
            if (XmlToCsv.read(e, "item").equalsIgnoreCase(courseId)) {
                Data.root.removeChild(student);
                student.removeChild(e);
                Data.root.appendChild(student);
                return true;
            }
        }
        return false;
    }

    public static boolean addCourse(String studentId, String courseId, String note) {
        Element student = (Element) getStudent(studentId);
        List<Element> courses = Data.getChildren(Data.root, "course");
        for (Element e : courses) {
            if (XmlToCsv.read(e, "identifier").equalsIgnoreCase(courseId)) {
                Node composante = Data.doc.createElement("item");
                Node value = Data.doc.createElement("value");
                Node grade = Data.doc.createElement("grade");
                composante.appendChild(Data.doc.createTextNode(courseId));
                value.appendChild(Data.doc.createTextNode(note));
                grade.appendChild(composante);
                grade.appendChild(value);

                student.appendChild(grade);
                return true;
            }
        }
        return false;
    }

    public boolean addCourseGeneral(String CourseName, String courseId, String coef) {
        ///Element student = (Element) getStudent(studentId);
        try {
            Node newCourse = Data.doc.createElement("course");
            Node identifier = Data.doc.createElement("identifier");
            Node name = Data.doc.createElement("name");
            Node coefNode = Data.doc.createElement("credits");

            identifier.appendChild(Data.doc.createTextNode(courseId));
            newCourse.appendChild(identifier);
            name.appendChild(Data.doc.createTextNode(CourseName));
            newCourse.appendChild(name);
            coefNode.appendChild(Data.doc.createTextNode(coef));
            newCourse.appendChild(coefNode);
            Data.root.appendChild(newCourse);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private static void breakLine(Node node) {
        node.appendChild(Data.doc.createTextNode("\n"));
    }

    public static Node generateStudentNode(String[][] dataset) {
        /*
        new String[][]{
                    {"identifier", "11111111"}, {"name", "dylannTest"},
                    {"surname", "didi"}, {"program", "SLTEST"},
                    {"grade", "SL3333", "9.4123"},
                    {"grade", "SL1111", "11.112"}
            }
           tableau de ce type attendu lors de l'insertion
           de l'étudiant
         */
        Node student = Data.doc.createElement("student");
        breakLine(student);
        for (String[] s : dataset) {
            Node item;
            String nodeType = s[0];

            if (nodeType != null) {
                switch (nodeType) {
                    case "identifier":
                    case "name":
                    case "surname":
                    case "program":
                        item = Data.doc.createElement(s[0]);
                        item.appendChild(Data.doc.createTextNode(s[1]));
                        student.appendChild(item);
                        breakLine(student);
                        break;
                    case "grade":
                        item = Data.doc.createElement(s[0]);
                        Node composante = Data.doc.createElement("item");
                        Node value = Data.doc.createElement("value");
                        composante.appendChild(Data.doc.createTextNode(s[1]));
                        value.appendChild(Data.doc.createTextNode(s[2]));
                        item.appendChild(composante);
                        item.appendChild(value);
                        student.appendChild(item);
                        breakLine(student);
                        break;
                }
            }
        }
        return student;
    }

    public static Node generateProgram(String[][] dataset) {
        /*
        new String[][]{
                    {"identifier", "SLL info"}, {"name", "L5 INFO"},
                    {"option", "1", "OPTION 4", "SL8666", "SL8666"},
                    {"composite", "2", "OPTION 4", "SL8666", "SL8666"},
                    {"item", "1"},
                    {"item", "2"},
                    {"option", "3", "OPTION 4", "SL8666", "SL8666"},
                    {"item", "3"}
            }
            tableau de ce type attendu lors de l'insertion
            de l'ajout du programme
         */
        Node program = Data.doc.createElement("program");
        breakLine(program);
        for (String[] s : dataset) {
            Node item;
            String nodeType = s[0];
            switch (nodeType) {
                case "identifier":
                case "name":
                case "item":
                    item = Data.doc.createElement(s[0]);
                    item.appendChild(Data.doc.createTextNode(s[1]));
                    program.appendChild(item);
                    breakLine(program);
                    break;
                case "option":
                case "composite":
                    Node nodeIterator = Data.doc.createElement(nodeType);

                    Node identifier = Data.doc.createElement("identifier");
                    identifier.appendChild(Data.doc.createTextNode(s[1]));
                    Node name = Data.doc.createElement("name");
                    name.appendChild(Data.doc.createTextNode(s[2]));
                    nodeIterator.appendChild(identifier);
                    nodeIterator.appendChild(name);

                    for (int i = 3; i < s.length; i++) {
                        Node innerItem = Data.doc.createElement("item");
                        innerItem.appendChild(Data.doc.createTextNode(s[i]));
                        nodeIterator.appendChild(innerItem);
//                        breakLine(program);
                    }
                    program.appendChild(nodeIterator);
                    break;
            }
        }
        return program;
    }

    public static boolean save(String dst) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(dst));
            Source input = new DOMSource(Data.root);

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(input, output);
            System.out.printf("Saved at %s\n", dst);
            return true;
        } catch (TransformerException e) {
            return false;
        }
    }
}
