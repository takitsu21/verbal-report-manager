//package com.mad.listener;
//
//import com.mad.Application;
//import com.mad.util.Data;
//import com.mad.util.Table;
//import com.mad.util.XmlToCsv;
//import com.mad.util.XmlWriter;
//import org.xml.sax.SAXException;
//
//import javax.swing.*;
//import javax.xml.parsers.ParserConfigurationException;
//import java.awt.event.FocusAdapter;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
//import java.io.IOException;
//import java.util.Arrays;
//
//public class MyFocusListener extends FocusAdapter {
//    @Override
//    public void focusGained(FocusEvent e) {
//        XmlWriter xmlEditor = new XmlWriter();
//
//        int row = Table.table.getSelectedRow();
//        int col = Table.table.getSelectedColumn();
//        String courseId = String.valueOf(Table.table.getModel().getColumnName(col)).split(" ")[0];
//        String numEtu = Data.dataArray[row+1][0];
//        System.out.printf("Num etu : %s - %s\n", numEtu, courseId);
//        String str = "";
//        str += "Selected Cell: " + Table.table.getSelectedRow() + ", " + Table.table.getSelectedColumn();
//        System.out.println(str);
//        Object value = Table.table.getValueAt(row, col);
//        System.out.println();
//        System.out.println(String.valueOf(value));
//        System.out.println(Arrays.toString(Data.dataArray[row]));
//        System.out.println(courseId);
//        System.out.println(xmlEditor.modifyCourse(numEtu, courseId, String.valueOf(value)));
//        xmlEditor.save("./test.xml");
//        try {
//            XmlToCsv xml = new XmlToCsv("./test.xml");
//            xml.convert();
//        } catch (ParserConfigurationException parserConfigurationException) {
//            parserConfigurationException.printStackTrace();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        } catch (SAXException saxException) {
//            saxException.printStackTrace();
//        }
//    }
//
//    @Override
//    public void focusLost(FocusEvent e) {
//        XmlWriter xmlEditor = new XmlWriter();
//
//        int row = Table.table.getSelectedRow();
//        int col = Table.table.getSelectedColumn();
//        String courseId = String.valueOf(Table.table.getModel().getColumnName(col)).split(" ")[0];
//        String numEtu = Data.dataArray[row+1][0];
//        System.out.printf("Num etu : %s - %s\n", numEtu, courseId);
//        String str = "";
//        str += "Selected Cell: " + Table.table.getSelectedRow() + ", " + Table.table.getSelectedColumn();
//        System.out.println(str);
//        Object value = Table.table.getValueAt(row, col);
//        System.out.println();
//        System.out.println(String.valueOf(value));
//        System.out.println(Arrays.toString(Data.dataArray[row]));
//        System.out.println(courseId);
//        System.out.println(xmlEditor.modifyCourse(numEtu, courseId, String.valueOf(value)));
//        xmlEditor.save("./test.xml");
//        try {
//            XmlToCsv xml = new XmlToCsv("./test.xml");
//            xml.convert();
//        } catch (ParserConfigurationException parserConfigurationException) {
//            parserConfigurationException.printStackTrace();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        } catch (SAXException saxException) {
//            saxException.printStackTrace();
//        }
//
//    }
//    //    @Override
////    public void focusGained(FocusEvent e) {
////        JTextField src = (JTextField)e.getSource();
////        System.out.println(src.getText());
////    }
////
////    @Override
////    public void focusLost(FocusEvent e) {
////        JTextField src = (JTextField)e.getSource();
////        System.out.println(src.getText());
////    }
//
//}
