//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mad.util;

import com.mad.AbstractApplication;
import com.mad.util.exceptions.StudentNotFoundException;
import java.io.File;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlWriter {
    public XmlWriter() {
    }

    public static Node getStudent(String studentId) {
        Node node = null;
        NodeList nodeList = Data.root.getElementsByTagName("student");

        for(int i = 0; i < nodeList.getLength(); ++i) {
            node = nodeList.item(i);
            if (node.getNodeType() == 1) {
                Element element = (Element)nodeList.item(i);
                String currentStudentId = element.getElementsByTagName("identifier").item(0).getTextContent();
                if (studentId.equalsIgnoreCase(currentStudentId)) {
                    break;
                }
            }
        }

        return node;
    }

    public static Node getCourse(String courseId) {
        List<Element> courses = Data.getChildren(Data.root, "course");
        Node ret = null;
        Iterator var3 = courses.iterator();

        while(var3.hasNext()) {
            Element course = (Element)var3.next();
            if (Data.read(course, "identifier").equalsIgnoreCase(courseId)) {
                ret = course;
                break;
            }
        }

        return ret;
    }

    public static boolean deleteStudent(String studentId) {
        return deleteStudent(getStudent(studentId));
    }

    public static boolean deleteStudent(Node student) {
        try {
            Data.root.removeChild(student);
            return true;
        } catch (StudentNotFoundException var2) {
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
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCourse(Node course) {
        try {
            Data.root.removeChild(course);
            return true;
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static boolean addNode(Node n) {
        try {
            n = Data.doc.importNode(n, true);
            n.getParentNode().appendChild(n);
            return true;
        } catch (Exception var4) {
            try {
                Data.root.appendChild(n);
                return true;
            } catch (Exception var3) {
                return false;
            }
        }
    }

    public static boolean addProgram(Node program) {
        try {
            Data.root.appendChild(program);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean modifyCourse(String studentId, String courseId, String val) {
        Element student = (Element)getStudent(studentId);
        List<Element> grades = Data.getChildren(student, "grade");
        Iterator var5 = grades.iterator();

        Element e;
        do {
            if (!var5.hasNext()) {
                return false;
            }

            e = (Element)var5.next();
        } while(!Data.read(e, "item").equalsIgnoreCase(courseId));

        List<Element> values = Data.getChildren(e, "value");
        Iterator var8 = values.iterator();

        while(var8.hasNext()) {
            Element v = (Element)var8.next();
            v.setTextContent(val);
        }

        System.out.println("student modified");
        return true;
    }

    public static boolean deleteCourse(String studentId, String courseId) {
        Element student = (Element)getStudent(studentId);
        List<Element> grades = Data.getChildren(student, "grade");
        Iterator var4 = grades.iterator();

        Element e;
        do {
            if (!var4.hasNext()) {
                return false;
            }

            e = (Element)var4.next();
        } while(!Data.read(e, "item").equalsIgnoreCase(courseId));

        Data.root.removeChild(student);
        student.removeChild(e);
        Data.root.appendChild(student);
        return true;
    }

    public static boolean addCourse(String studentId, String courseId, String note) {
        Element student = (Element)getStudent(studentId);
        List<Element> courses = Data.getChildren(Data.root, "course");
        Iterator var5 = courses.iterator();

        Element e;
        do {
            if (!var5.hasNext()) {
                return false;
            }

            e = (Element)var5.next();
        } while(!Data.read(e, "identifier").equalsIgnoreCase(courseId));

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

    public static boolean addCourseGeneral(String courseName, String courseId, String coef) {
        try {
            Data.root.appendChild(addNewCourseNode(courseName, courseId, coef));
            return true;
        } catch (Exception var4) {
            return false;
        }
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
        } catch (Exception var7) {
            return null;
        }
    }

    public static Node getCourseDoc(Node n) {
        List<Element> courses = Data.getChildren(Data.root, "course");
        Node ret = null;
        Iterator var3 = courses.iterator();

        while(var3.hasNext()) {
            Element course = (Element)var3.next();
            if (Data.read(course, "identifier").equalsIgnoreCase(Data.read((Element)n, "identifier"))) {
                ret = course;
                break;
            }
        }

        return ret;
    }

    public static Node getProgramDoc(Node n) {
        List<Element> courses = Data.getChildren(Data.root, "program");
        Node ret = null;
        Iterator var3 = courses.iterator();

        while(var3.hasNext()) {
            Element course = (Element)var3.next();
            if (Data.read(course, "identifier").equalsIgnoreCase(Data.read((Element)n, "identifier"))) {
                ret = course;
                break;
            }
        }

        return ret;
    }

    public static boolean removeNode(Node n) {
        try {
            n.getParentNode().removeChild(n);
            return true;
        } catch (Exception var4) {
            try {
                Data.root.removeChild(n);
                return true;
            } catch (Exception var3) {
                return false;
            }
        }
    }

    private static void breakLine(Node node) {
        node.appendChild(Data.doc.createTextNode("\n"));
    }

    public static Node generateStudentNode(String[][] dataset) {
        Node student = Data.doc.createElement("student");
        breakLine(student);
        String[][] var2 = dataset;
        int var3 = dataset.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String[] s = var2[var4];
            String nodeType = s[0];
            if (nodeType != null) {
                byte var9 = -1;
                switch(nodeType.hashCode()) {
                    case -1852993317:
                        if (nodeType.equals("surname")) {
                            var9 = 1;
                        }
                        break;
                    case -1618432855:
                        if (nodeType.equals("identifier")) {
                            var9 = 0;
                        }
                        break;
                    case -309387644:
                        if (nodeType.equals("program")) {
                            var9 = 3;
                        }
                        break;
                    case 3373707:
                        if (nodeType.equals("name")) {
                            var9 = 2;
                        }
                        break;
                    case 98615255:
                        if (nodeType.equals("grade")) {
                            var9 = 4;
                        }
                }

                Element item;
                switch(var9) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        item = Data.doc.createElement(s[0]);
                        item.appendChild(Data.doc.createTextNode(s[1]));
                        student.appendChild(item);
                        breakLine(student);
                        break;
                    case 4:
                        item = Data.doc.createElement(s[0]);
                        Node composante = Data.doc.createElement("item");
                        Node value = Data.doc.createElement("value");
                        composante.appendChild(Data.doc.createTextNode(s[1]));
                        value.appendChild(Data.doc.createTextNode(s[2]));
                        item.appendChild(composante);
                        item.appendChild(value);
                        student.appendChild(item);
                        breakLine(student);
                }
            }
        }

        return student;
    }

    public static Node generateProgram(String[][] dataset) {
        Node program = Data.doc.createElement("program");
        breakLine(program);
        String[][] var2 = dataset;
        int var3 = dataset.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String[] s = var2[var4];
            String nodeType = s[0];
            byte var9 = -1;
            switch(nodeType.hashCode()) {
                case -1618432855:
                    if (nodeType.equals("identifier")) {
                        var9 = 0;
                    }
                    break;
                case -1399754105:
                    if (nodeType.equals("composite")) {
                        var9 = 4;
                    }
                    break;
                case -1010136971:
                    if (nodeType.equals("option")) {
                        var9 = 3;
                    }
                    break;
                case 3242771:
                    if (nodeType.equals("item")) {
                        var9 = 2;
                    }
                    break;
                case 3373707:
                    if (nodeType.equals("name")) {
                        var9 = 1;
                    }
            }

            switch(var9) {
                case 0:
                case 1:
                case 2:
                    Node item = Data.doc.createElement(s[0]);
                    item.appendChild(Data.doc.createTextNode(s[1]));
                    program.appendChild(item);
                    breakLine(program);
                    break;
                case 3:
                case 4:
                    Node nodeIterator = Data.doc.createElement(nodeType);
                    Node identifier = Data.doc.createElement("identifier");
                    identifier.appendChild(Data.doc.createTextNode(s[1]));
                    Node name = Data.doc.createElement("name");
                    name.appendChild(Data.doc.createTextNode(s[2]));
                    nodeIterator.appendChild(identifier);
                    nodeIterator.appendChild(name);

                    for(int i = 3; i < s.length; ++i) {
                        Node innerItem = Data.doc.createElement("item");
                        innerItem.appendChild(Data.doc.createTextNode(s[i]));
                        nodeIterator.appendChild(innerItem);
                    }

                    program.appendChild(nodeIterator);
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
            System.out.printf("Saved at %s\n", dst);
            AbstractApplication.setLastTmpModificationAt(new Timestamp(System.currentTimeMillis()));
            return true;
        } catch (TransformerException var4) {
            return false;
        }
    }
}
