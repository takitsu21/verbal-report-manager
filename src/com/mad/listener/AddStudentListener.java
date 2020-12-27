package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.XmlToCsv;
import com.mad.util.XmlWriter;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddStudentListener extends AbstractApplication implements ActionListener {
    private JTextField studNum;
    private JTextField name;
    private JTextField surname;
    private JComboBox program;

    public JTextField getStudNumfield() {
        return studNum;
    }

    public JTextField getNamefield() {
        return name;
    }

    public JTextField getSurnamefield() {
        return surname;
    }

    public JComboBox getProgramfield() {
        return program;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame tmp = new JFrame("Ajouter un étudiant");
        tmp.setSize(800, 400);
        tmp.setLocationRelativeTo(null);
        tmp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tmp.setVisible(true);
        tmp.setLayout(new BorderLayout());

        JPanel pnl = new JPanel();
        JPanel stdNum = new JPanel();
        JPanel namePanel = new JPanel();
        JPanel sureNamePane = new JPanel();
        JPanel prog = new JPanel();
        //pnl.setLayout(new BorderLayout());

        studNum = new JTextField(10);
        studNum.setSize(60, 30);
        stdNum.add(new JLabel("Student number:"));
        stdNum.add(studNum);
        pnl.add(stdNum);

        name = new JTextField(10);
        name.setSize(60, 30);
        namePanel.add(new JLabel("Name:"));
        namePanel.add(name);
        pnl.add(namePanel);

        surname = new JTextField(10);
        surname.setSize(60, 30);
        sureNamePane.add(new JLabel("Surname:"));
        sureNamePane.add(surname);
        pnl.add(sureNamePane);

        /*program = getComboBox();
        surname.setSize(60, 30);
        prog.add(new JLabel("Program:"));
        prog.add(program);
        pnl.add(prog);*/

        tmp.add(pnl, BorderLayout.NORTH);

        List<Element> listCourses = Data.getChildren(Data.root, "course");

        String[] cours = generateCheckboxValues(listCourses);

        /*container.add(new CheckBoxGroup("COURS", cours), BorderLayout.SOUTH);
        container.add(new CheckBoxGroup("OPTIONS", cours), BorderLayout.SOUTH);
        container.add(new CheckBoxGroup("COMPOSANTES", cours), BorderLayout.SOUTH);*/

        CheckBoxGroup cbg = new CheckBoxGroup("COURS", cours);
        tmp.add(cbg, BorderLayout.CENTER);

        JPanel sud = new JPanel(new GridLayout(1, 2));
        JLabel l = new JLabel("Ce cours peut être ajouté mais ne fait pas parti du programme courant");
        l.setForeground(Color.decode("0xf27e11"));
        sud.add(l, BorderLayout.SOUTH);
        JPanel bouton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ajouter = new JButton("Ajouter");
        ajouter.addActionListener(new CheckboxAction(cbg, tmp));
        bouton.add(ajouter, BorderLayout.SOUTH);
        sud.add(bouton);
        tmp.add(sud, BorderLayout.SOUTH);


        //       Container container = new Container();
//        container.add(new JLabel("test"));
////        name.add(new JTextField());
//        tmp.getContentPane().add(container);
//

        tmp.setVisible(true);


    }

    private String[] generateCheckboxValues(List<Element> courses) {
        String[] values = new String[courses.size()];
        int acc = 0;
        for (Element course : courses) {
            String courseName = XmlToCsv.read(course, "name");
            String courseId = XmlToCsv.read(course, "identifier");
            values[acc++] = String.format("%s - %s", courseId, courseName);
        }
        return values;
    }


    public static class CheckBoxGroup extends JPanel {

        private final JCheckBox all;
        private final List<JCheckBox> checkBoxes;

        public CheckBoxGroup(String labelChoice, String... options) {
            checkBoxes = new ArrayList<>(25);
            setLayout(new BorderLayout());
            JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
            all = new JCheckBox("Tout sélectionner...");
            all.addActionListener(e -> {
                for (JCheckBox cb : checkBoxes) {
                    cb.setSelected(all.isSelected());
                }
            });
            header.add(new Label(labelChoice));
            header.add(all);
            add(header, BorderLayout.NORTH);


            JPanel content = new ScrollablePane(new GridBagLayout());
            content.setBackground(UIManager.getColor("List.background"));
            if (options.length > 0) {

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                gbc.weightx = 1;
                for (String option : options) {

                    JCheckBox cb = new JCheckBox(option);
                    cb.setOpaque(false);
                    //cb.setBackground(Color.GRAY);
                    cb.setForeground(Color.decode("0xf27e11"));
                    for (int i = 0; i < Data.dataArray[0].length; i++) {
                        if (option.equals(Data.dataArray[0][i])) {

                            //cb.setBackground(Color.WHITE);
                            cb.setForeground(Color.BLACK);

                        }
                    }

                    checkBoxes.add(cb);
                    content.add(cb, gbc);
                }

                //JCheckBox cb = new JCheckBox(options[options.length - 1]);
                //cb.setOpaque(false);
                //checkBoxes.add(cb);
                //gbc.weighty = 1;
                //content.add(cb, gbc);

            }

            add(new JScrollPane(content));


        }

        public List<JCheckBox> getCheckBoxs() {
            return checkBoxes;

        }

        public static class ScrollablePane extends JPanel implements Scrollable {

            public ScrollablePane(LayoutManager layout) {
                super(layout);
            }

//            public ScrollablePane() {
//            }

            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(100, 100);
            }

            @Override
            public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
                return 32;
            }

            @Override
            public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
                return 32;
            }

            @Override
            public boolean getScrollableTracksViewportWidth() {
                boolean track = false;
                Container parent = getParent();
                if (parent instanceof JViewport) {
                    JViewport vp = (JViewport) parent;
                    track = vp.getWidth() > getPreferredSize().width;
                }
                return track;
            }

            @Override
            public boolean getScrollableTracksViewportHeight() {
                boolean track = false;
                Container parent = getParent();
                if (parent instanceof JViewport) {
                    JViewport vp = (JViewport) parent;
                    track = vp.getHeight() > getPreferredSize().height;
                }
                return track;
            }

        }

    }

    class CheckboxAction extends AbstractApplication implements ActionListener {
        private final List<JCheckBox> checkBoxes;
        JFrame tmp;
//        private final String studNum;
//        private final String name;
//        private final String surname;
        // private final String prog;

        public CheckboxAction(AddStudentListener.CheckBoxGroup checkBoxes, JFrame tmp) {
            this.checkBoxes = checkBoxes.getCheckBoxs();
            this.tmp = tmp;
//            this.studNum = studNum.getText();
//            this.name = name.getText();
//            this.surname = surname.getText();
//            this.prog = prog.getText();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String numEntry = getStudNumfield().getText();
            String nameEntry = getNamefield().getText();
            String surnameEntry = getSurnamefield().getText();
            String programmeEntry = Data.dataSet.entrySet().iterator().next().getKey();//(String) getProgramfield().getSelectedItem();


            String[][] student = new String[checkBoxes.size() + 4][3];
            for (int i = 4; i < checkBoxes.size() + 4; i++) {
                if (checkBoxes.get(i - 4).isSelected()) {
                    student[i][0] = "grade";
                    student[i][1] = checkBoxes.get(i - 4).getText().split(" - ")[0];
                    student[i][2] = "0.0";
                }
            }
            student[0][0] = "identifier";
            student[0][1] = numEntry;
            student[1][0] = "name";
            student[1][1] = nameEntry;
            student[2][0] = "surname";
            student[2][1] = surnameEntry;
            student[3][0] = "program";
            student[3][1] = programmeEntry;

            if (XmlWriter.addStudent(XmlWriter.generateStudentNode(student))) {
                refreshTable();
                tmp.dispose();
            }

        }
    }


}


