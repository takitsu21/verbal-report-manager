import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class ChooseProgram extends MainFrame implements ActionListener {

    public ChooseProgram() throws IOException, ParserConfigurationException, SAXException {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.get)
        JComboBox combo = (JComboBox)e.getSource();
        System.out.println(combo.getName());
        System.out.println(getData().get(combo.getName()));
        String[][] arr = Table.sDataToArray(getData().get(combo.getName()));
//        frame.getContentPane().add();
        JTable table = new JTable(Arrays.copyOfRange(arr, 1, arr.length), arr[0]);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane Jscroll = new JScrollPane(table);
        removeOldTable();
        frame.getContentPane().add(Jscroll);
        frame.getContentPane().setVisible(true);
        repaint();
    }

    public void chooseProgram(HashMap<String, String> xmlData) {

    }
}
