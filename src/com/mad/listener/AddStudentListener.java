package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.XmlMethodType;
import com.mad.util.XmlWriter;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddStudentListener extends AbstractApplication implements ActionListener {
    protected JTextField studNum;
    protected JTextField nameField;
    protected JTextField surname;
    protected JComboBox<String> program;
    protected CheckBoxGroup cbg;
    protected JFrame addStudentFrame;
    protected String[] cours;

    public void setStudNum(JTextField studNum) {
        this.studNum = studNum;
    }

    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
    }

    public void setSurname(JTextField surname) {
        this.surname = surname;
    }

    public JComboBox<String> getProgram() {
        return program;
    }

    public void setProgram(JComboBox<String> program) {
        this.program = program;
    }

    public CheckBoxGroup getCbg() {
        return cbg;
    }

    public void setCbg(CheckBoxGroup cbg) {
        this.cbg = cbg;
    }

    public void setAddStudentFrame(JFrame addStudentFrame) {
        this.addStudentFrame = addStudentFrame;
    }

    public String[] getCours() {
        return cours;
    }

    public void setCours(String[] cours) {
        this.cours = cours;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setAddStudentFrame(new JFrame("Ajouter un étudiant"));
        addStudentFrame.setSize(860, 400);
        addStudentFrame.setLocationRelativeTo(null);
        addStudentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addStudentFrame.setVisible(true);
        addStudentFrame.setLayout(new BorderLayout());

        JPanel pnl = new JPanel();
        JPanel stdNum = new JPanel();
        JPanel namePanel = new JPanel();
        JPanel sureNamePane = new JPanel();
        JPanel prog = new JPanel();

        setStudNum(new JTextField(10));
        studNum.setSize(60, 30);
        stdNum.add(new JLabel("Student number:"));
        stdNum.add(studNum);
        pnl.add(stdNum);

        setNameField(new JTextField(10));
        nameField.setSize(60, 30);
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);
        pnl.add(namePanel);

        setSurname(new JTextField(10));
        surname.setSize(60, 30);
        sureNamePane.add(new JLabel("Surname:"));
        sureNamePane.add(surname);
        pnl.add(sureNamePane);

        List<Element> listCourses = Data.getChildren(Data.root, "course");
        cours = generateCheckboxValues(listCourses);

        setCbg(new CheckBoxGroup("COURS", cours));

        program = new JComboBox<>();
        for (int i = 0; i < getComboBox().getItemCount(); i++) {
            program.addItem(getComboBox().getItemAt(i));
        }
        program.addActionListener(new ListCheckBox());

        prog.add(new JLabel("Program:"));
        prog.add(program);
        pnl.add(prog);

        addStudentFrame.add(pnl, BorderLayout.NORTH);
        addStudentFrame.add(getCbg(), BorderLayout.CENTER);

        JPanel sud = new JPanel(new GridLayout(1, 2));

        JLabel legend = new JLabel("Ce cours peut être ajouté mais ne fait pas parti du programme");
        legend.setForeground(Color.decode("0xf27e11"));
        sud.add(legend, BorderLayout.SOUTH);

        JPanel bouton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ajouter = new JButton("Ajouter");
        ajouter.addActionListener(new CheckboxAction());
        bouton.add(ajouter, BorderLayout.SOUTH);
        sud.add(bouton);
        addStudentFrame.add(sud, BorderLayout.SOUTH);
        addStudentFrame.setVisible(true);
    }

    private String[] generateCheckboxValues(List<Element> courses) {
        String[] values = new String[courses.size()];
        int acc = 0;
        for (Element course : courses) {
            String courseName = Data.read(course, "name");
            String courseId = Data.read(course, "identifier");
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
                    cb.setForeground(Color.decode("0xf27e11"));
                    for (int i = 0; i < Data.dataArray[0].length; i++) {
                        if (option.equals(Data.dataArray[0][i])) {
                            cb.setForeground(Color.BLACK);
                        }
                    }
                    checkBoxes.add(cb);
                    content.add(cb, gbc);
                }
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

    class CheckboxAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String numEntry = studNum.getText();
            String nameEntry = nameField.getText();
            String surnameEntry = surname.getText();
            String programmeEntry = (String) program.getSelectedItem();


            String[][] student = new String[cbg.getCheckBoxs().size() + 4][3];

            for (int i = 0; i < cbg.getCheckBoxs().size(); i++) {
                if (cbg.getCheckBoxs().get(i).isSelected()) {
                    student[i][0] = "grade";
                    System.out.println(cbg.getCheckBoxs().get(i).getText());
                    student[i][1] = cbg.getCheckBoxs().get(i).getText().split(" - ")[0];
                    student[i][2] = "0.0";
                }
            }
            student[0][0] = "identifier";
            student[0][1] = numEntry;
            student[1][0] = "surname";
            student[1][1] = surnameEntry;
            student[2][0] = "name";
            student[2][1] = nameEntry;
            student[3][0] = "program";
            student[3][1] = programmeEntry;
            insertAction(
                    () -> XmlWriter.addStudent(XmlWriter.generateStudentNode(student)),
                    "student", numEntry, XmlMethodType.ADD, true);

            addStudentFrame.dispose();

        }
    }

    public class ListCheckBox implements ActionListener {
        public ListCheckBox() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getComboBox().setSelectedItem(program.getSelectedItem());
            new ComboBoxListener().actionPerformed(e);
            addStudentFrame.remove(getCbg());
            setCbg(new CheckBoxGroup("COURS", cours));
            addStudentFrame.add(getCbg(), BorderLayout.CENTER);
            addStudentFrame.revalidate();
            addStudentFrame.repaint();
        }
    }
}


