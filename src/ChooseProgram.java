import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
        JComboBox combo = (JComboBox) e.getSource();
        String[][] arr = Table.sDataToArray(Data.dataSet.get(combo.getSelectedItem().toString()));
        TableModel tm = new DefaultTableModel(Arrays.copyOfRange(arr, 1, arr.length), arr[0]);
        getDisplayCsv().table.setModel(tm);
    }

}
