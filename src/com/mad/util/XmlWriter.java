package com.mad.util;

import com.mad.AbstractApplication;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

public class XmlWriter {
    public XmlWriter() {
    }

    public static Node getStudent(String studentId) {
        Node node = null;
        NodeList nodeList = Data.root.getElementsByTagName("student");

        for (int i = 0; i < nodeList.getLength(); ++i) {
            node = nodeList.item(i);
            if (node.getNodeType() == 1) {
                Element element = (Element) nodeList.item(i);
                String currentStudentId = element.getElementsByTagName("identifier").item(0).getTextContent();
                if (studentId.equalsIgnoreCase(currentStudentId)) {
                    break;
                }
            }
        }

        return node;
    }

    public static boolean deleteStudent(String studentId) {
        return deleteStudent(getStudent(studentId));
    }

    public static boolean deleteStudent(Node student) {
        try {
            Data.root.removeChild(student);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean addStudent(String studentId) {
        return addStudent(getStudent(studentId));
    }

    public static boolean addStudent(Node newStudent) {
        try {
            Data.root.appendChild(newStudent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addNode(Node n) {
        try {
            n = Data.doc.importNode(n, true);
            n.getParentNode().appendChild(n);
            return true;
        } catch (Exception e) {
            try {
                Data.root.appendChild(n);
                return true;
            } catch (Exception exc) {
                return false;
            }
        }
    }

    public static boolean removeNode(Node n) {
        try {
            n.getParentNode().removeChild(n);
            return true;
        } catch (Exception e) {
            try {
                Data.root.removeChild(n);
                return true;
            } catch (Exception exc) {
                return false;
            }
        }
    }

    public static boolean modifyCourse(String studentId, String courseId, String val) {
        Element student = (Element) getStudent(studentId);
        List<Element> grades = Data.getChildren(student, "grade");
        for (Element e : grades) {
            if (Data.read(e, "item").equalsIgnoreCase(courseId)) {
                List<Element> values = Data.getChildren(e, "value");
                for (Element v : values) {
                    v.setTextContent(val);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean deleteCourse(String studentId, String courseId) {
        Element student = (Element) getStudent(studentId);
        List<Element> grades = Data.getChildren(student, "grade");
        for (Element e : grades) {
            if (Data.read(e, "item").equalsIgnoreCase(courseId)) {
                student.removeChild(e);
                return true;
            }
        }
        return false;
    }

    public static boolean addCourse(String studentId, String courseId, String note) {
        Element student = (Element) getStudent(studentId);
        List<Element> courses = Data.getChildren(Data.root, "course");
        for (Element e : courses) {
            if (Data.read(e, "identifier").equalsIgnoreCase(courseId)) {
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

    public static Node addNewCourseNode(String courseName, String courseId, String coef) {
        try {
            Node newCourse = Data.doc.createElement("course");
            Node identifier = Data.doc.createElement("identifier");
            Node name = Data.doc.createElement("name");
            Node coefNode = Data.doc.createElement("credits");
            identifier.appendChild(Data.doc.createTextNode(courseId));
            newCourse.appendChild(identifier);
            name.appendChild(Data.doc.createTextNode(courseName));
            newCourse.appendChild(name);
            coefNode.appendChild(Data.doc.createTextNode(coef));
            newCourse.appendChild(coefNode);
            return newCourse;
        } catch (Exception e) {
            return null;
        }
    }

    public static Node getProgramDoc(Node n) {
        List<Element> programs = Data.getChildren(Data.root, "program");
        Node ret = null;
        Iterator<Element> iterator = programs.iterator();

        while (iterator.hasNext()) {
            Element program = iterator.next();
            if (Data.read(program, "identifier").equalsIgnoreCase(Data.read((Element) n, "identifier"))) {
                ret = program;
                break;
            }
        }
        return ret;
    }

    public static Node getCourseDoc(Node n) {
        List<Element> courses = Data.getChildren(Data.root, "course");
        Node ret = null;
        Iterator<Element> iterator = courses.iterator();

        while (iterator.hasNext()) {
            Element course = iterator.next();
            if (Data.read(course, "identifier").equalsIgnoreCase(Data.read((Element) n, "identifier"))) {
                ret = course;
                break;
            }
        }

        return ret;
    }


    private static void breakLine(Node node) {
        node.appendChild(Data.doc.createTextNode("\n"));
    }

    public static Node generateStudentNode(String[][] dataset) {
        Node student = Data.doc.createElement("student");
        breakLine(student);
        for (String[] s : dataset) {
            Node item;
            String nodeType = s[0];

            if (nodeType != null) {
                switch (nodeType) {
                    case "identifier":
                    case "surname":
                    case "name":
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
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(input, output);
            AbstractApplication.setLastTmpModificationAt(new Timestamp(System.currentTimeMillis()));
            return true;
        } catch (TransformerException e) {
            return false;
        }
    }
}
