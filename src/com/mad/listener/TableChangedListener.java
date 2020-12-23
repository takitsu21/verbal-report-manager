package com.mad.listener;

import com.mad.Application;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;
import com.mad.util.XmlWriter;
import org.xml.sax.SAXException;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class TableChangedListener extends Application implements TableModelListener {

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            int col = Table.table.getSelectedColumn();
            String courseId = String.valueOf(Table.table.getModel().getColumnName(col)).split(" ")[0];
            String newVal = (String) Table.table.getModel().getValueAt(e.getFirstRow(), e.getColumn());
            String numEtu = (String) Table.table.getModel().getValueAt(e.getFirstRow(), 0);
            System.out.println(Table.table.getModel().getValueAt(e.getFirstRow(), 0));
            System.out.println(courseId);
            System.out.println(newVal);
            System.out.println(newVal.isEmpty());
//            if (newVal.isEmpty()) {
//                getXmlEditor().deleteCourse(numEtu, courseId);
//            } else {
//
//            }
            if (newVal.isEmpty()) {
                getXmlEditor().deleteCourse(numEtu, courseId);
            }
            else {
                if (!getXmlEditor().modifyCourse(numEtu, courseId, newVal)) {
                    System.out.println(getXmlEditor().addCourse(numEtu, courseId, newVal));
                }
            }


            getXmlEditor().save("./xml-editor.tmp.xml");

            XmlToCsv xmlConverter = new XmlToCsv(getPath());
            xmlConverter.convert();
            try {
                getDisplayCsv().TableXML(
                        getPath(), Data.dataSet.get(Data.dataSet.entrySet().iterator().next().getKey()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (SAXException saxException) {
                saxException.printStackTrace();
            } catch (ParserConfigurationException parserConfigurationException) {
                parserConfigurationException.printStackTrace();
            }
            OpenFileListener.openFile(new File("./xml-editor.tmp.xml"));
//            try{
//                File file = new File("./xml-editor.tmp.xml");
//                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//                Data.doc = dBuilder.parse(file); // ouverture et lecture du fichier XML
//                Data.doc.getDocumentElement().normalize(); // normalise le contenu du fichier, opération très conseillée
//                Data.root = Data.doc.getDocumentElement();
////                getXmlEditor().save("./data.xml");
//                file.delete();
//                System.out.println("Overwriting data.xml");
//            } catch (Exception exc) {
//
//            }

//            System.out.println("Cell " + e.getFirstRow() + ", " + e.getColumn() + " changed."
//                    + " The new value: " + ));
        }
    }
}
