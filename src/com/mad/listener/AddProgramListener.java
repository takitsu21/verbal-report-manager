package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlToCsv;
import com.mad.util.XmlWriter;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddProgramListener extends AbstractApplication implements ActionListener {
    private JFrame programFrame;

    @Override
    public void actionPerformed(ActionEvent e) {
        programFrame = new JFrame("Ajouter un programme");
        programFrame.setSize(185, 393);
        programFrame.setLocationRelativeTo(null);
        programFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        programFrame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 20, 5));
        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel idPanel = new JPanel(new FlowLayout());
        JPanel optionsPanel = new JPanel(new FlowLayout());
        JPanel composantesPanel = new JPanel(new FlowLayout());
        JPanel nextPanel = new JPanel(new FlowLayout());


        JTextField textField = new JTextField();
//        textField.setBounds(128, 28, 86, 20);
        textField.setColumns(15);

        JLabel programName = new JLabel("Nom du programme");
//        programName.setBounds(65, 31, 46, 14);

        namePanel.add(programName);
        namePanel.add(textField);

        JLabel programId = new JLabel("Id du programme");
        programId.setBounds(65, 31, 46, 14);

        JTextField programIdField = new JTextField();
        programIdField.setBounds(128, 65, 86, 20);
        programIdField.setColumns(15);

        idPanel.add(programId);
        idPanel.add(programIdField);


        JLabel nbOptions = new JLabel("Nombre d'options du programme");
        nbOptions.setBounds(65, 68, 46, 14);

        JTextField nbOptionsField = new JTextField();
        nbOptionsField.setBounds(128, 65, 86, 20);
        nbOptionsField.setColumns(15);

        optionsPanel.add(nbOptions);
        optionsPanel.add(nbOptionsField);

        JLabel nbComposantes = new JLabel("Nombre de composantes");
        nbComposantes.setBounds(65, 115, 46, 14);

        JTextField nbComposantesField = new JTextField();
        nbComposantesField.setBounds(128, 112, 247, 17);
        nbComposantesField.setColumns(15);

        composantesPanel.add(nbComposantes);
        composantesPanel.add(nbComposantesField);

        JButton next = new JButton("Suivant");
        next.addActionListener(e14 -> {
            List<Element> courses = Data.getChildren(Data.root, "course");
            String[] stringCourses = generateCheckboxValues(courses);
            getProgramFrame().getContentPane().remove(panel);
            programFrame.setSize(500, 600);
            int nbOptions1 = Integer.parseInt(nbOptionsField.getText());
            int nbComposantes1 = Integer.parseInt(nbComposantesField.getText());
            CheckBoxGroup[] checkBoxGroupComposantes = new CheckBoxGroup[nbComposantes1];
            CheckBoxGroup[] checkBoxGroupOptions = new CheckBoxGroup[nbOptions1];
            CheckBoxGroup checkBoxGroupsCourse = new CheckBoxGroup("COURS", stringCourses);

            JPanel nextPane = new JPanel();
            JPanel tmp = new JPanel();

            nextPane.setLayout(new GridLayout(2, 1));

            nextPane.add(checkBoxGroupsCourse);
            JButton next12 = new JButton("Suivant");
            next12.addActionListener(e13 -> {
                JPanel tmp13 = new JPanel();
                getProgramFrame().getContentPane().remove(nextPane);
                refreshWindow();
                JPanel nextPane12 = new JPanel();

                nextPane12.setLayout(new GridLayout(nbOptions1 + 1, 1));

                for (int i = 0; i < nbOptions1; i++) {
                    checkBoxGroupOptions[i] = new CheckBoxGroup("OPTIONS", stringCourses);
                    nextPane12.add(checkBoxGroupOptions[i]);
                }
                JButton next1 = new JButton("Suivant");
                next1.addActionListener(e12 -> {
                    JPanel tmp12 = new JPanel();
                    getProgramFrame().getContentPane().remove(nextPane12);
                    refreshWindow();

                    JPanel nextPane1 = new JPanel();

                    nextPane1.setLayout(new GridLayout(nbComposantes1 + 1, 1));

                    for (int i = 0; i < nbComposantes1; i++) {
                        checkBoxGroupComposantes[i] = new CheckBoxGroup("COMPOSANTES",
                                stringCourses);
                        nextPane1.add(checkBoxGroupComposantes[i]);
                    }
                    JButton finished = new JButton("Terminer");
                    finished.addActionListener(e1 -> {
                        List<List<String>> programs = new ArrayList<>();
                        List<String> tmp1 = new ArrayList<>();
                        tmp1.add("identifier");
                        tmp1.add(programIdField.getText());
                        programs.add(tmp1);
                        tmp1 = new ArrayList<>();
                        tmp1.add("name");
                        tmp1.add(textField.getText());
                        programs.add(tmp1);
                        for (CheckBoxGroup cb : checkBoxGroupComposantes) {
                            tmp1 = new ArrayList<>();
                            tmp1.add("composite");
                            tmp1.add(cb.checkBoxGroupeId.getText());
                            tmp1.add(cb.checkBoxGroupeName.getText());
                            for (JCheckBox currentCb : cb.checkBoxes) {
                                if (currentCb.isSelected()) {
                                    tmp1.add(currentCb.getText().split(" ")[0]);
                                }
                            }
                            programs.add(tmp1);
                        }

                        for (CheckBoxGroup cb : checkBoxGroupOptions) {
                            tmp1 = new ArrayList<>();
                            tmp1.add("option");
                            tmp1.add(cb.checkBoxGroupeId.getText());
                            tmp1.add(cb.checkBoxGroupeName.getText());
                            for (JCheckBox currentCb : cb.checkBoxes) {
                                if (currentCb.isSelected()) {
                                    tmp1.add(currentCb.getText().split(" ")[0]);

                                }
                            }
                            programs.add(tmp1);
                        }

                        for (JCheckBox currentCb : checkBoxGroupsCourse.checkBoxes) {
                            if (currentCb.isSelected()) {
                                tmp1 = new ArrayList<>();
                                tmp1.add("item");
                                tmp1.add(currentCb.getText().split(" ")[0]);
                                programs.add(tmp1);
                            }
                        }
                        String[][] t = new String[programs.size()][];
                        String[] blankArray = new String[0];
                        for (int i = 0; i < programs.size(); i++) {
                            t[i] = programs.get(i).toArray(blankArray);
                        }
                        if (XmlWriter.addProgram(XmlWriter.generateProgram(t))) {
                            XmlWriter.save(TMP_PATH);
                            Table.table.getModel().removeTableModelListener(modelListener);
                            XmlToCsv z = new XmlToCsv(TMP_PATH);
                            z.convert();
                            getComboBox().addItem(programIdField.getText());
                            Table.table.getModel().addTableModelListener(modelListener);

                            getNorthPanel().repaint();
                            getNorthPanel().revalidate();
                            OpenFileListener.openFile(new File(TMP_PATH));

                            SwingUtilities.invokeLater(() -> programFrame.dispose());
                        }
                    });
                    tmp12.add(finished);

                    nextPane1.add(tmp12);
                    programFrame.getContentPane().add(nextPane1);
                    refreshWindow();
                });
                tmp13.add(next1);
                nextPane12.add(tmp13);
                programFrame.getContentPane().add(nextPane12);
                refreshWindow();
            });
            tmp.add(next12);
            nextPane.add(tmp);

            programFrame.getContentPane().add(nextPane);
            refreshWindow();
        });
        nextPanel.add(next);

        panel.add(namePanel);
        panel.add(idPanel);
        panel.add(optionsPanel);
        panel.add(composantesPanel);
        panel.add(nextPanel);

        programFrame.getContentPane().add(panel);

    }

    public JFrame getProgramFrame() {
        return programFrame;
    }

    private void refreshWindow() {
        programFrame.revalidate();
        programFrame.repaint();
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
        private final JTextField checkBoxGroupeName = new JTextField();
        private final JTextField checkBoxGroupeId = new JTextField();

        public CheckBoxGroup(String labelChoice, String... options) {
            checkBoxes = new ArrayList<>(25);
            setLayout(new BorderLayout());
            JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
            all = new JCheckBox("Tout sélectionner...");
            all.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (JCheckBox cb : checkBoxes) {
                        cb.setSelected(all.isSelected());
                    }
                }
            });
            checkBoxGroupeName.setColumns(10);
            checkBoxGroupeId.setColumns(10);
            header.add(new Label(labelChoice));

            header.add(all);
            header.add(new Label("Nom:"));
            header.add(checkBoxGroupeName, BorderLayout.EAST);
            header.add(new Label("Id:"));
            header.add(checkBoxGroupeId, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);


            JPanel content = new ScrollablePane(new GridBagLayout());
            content.setBackground(UIManager.getColor("List.background"));
            if (options.length > 0) {

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                gbc.weightx = 1;
                for (int index = 0; index < options.length - 1; index++) {
                    JCheckBox cb = new JCheckBox(options[index]);
                    cb.setOpaque(false);
                    checkBoxes.add(cb);
                    content.add(cb, gbc);
                }

                JCheckBox cb = new JCheckBox(options[options.length - 1]);
                cb.setOpaque(false);
                checkBoxes.add(cb);
                gbc.weighty = 1;
                content.add(cb, gbc);

            }

            add(new JScrollPane(content));
        }

        public class ScrollablePane extends JPanel implements Scrollable {

            public ScrollablePane(LayoutManager layout) {
                super(layout);
            }

            public ScrollablePane() {
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
}