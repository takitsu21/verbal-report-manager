package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.XmlToCsv;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddProgramListener extends AbstractApplication implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame programFrame = new JFrame("Ajouter un programme");
        programFrame.setSize(800, 400);
        programFrame.setLocationRelativeTo(null);
        programFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        programFrame.setVisible(true);
        programFrame.setLayout(new GridLayout(2, 2));

        List<Element> courses = Data.getChildren(Data.root, "course");
        String[] stringCourses = generateCheckboxValues(courses);

        programFrame.getContentPane().add(new CheckBoxGroup("COURS", stringCourses));
        programFrame.getContentPane().add(new CheckBoxGroup("OPTIONS", stringCourses));
        programFrame.getContentPane().add(new CheckBoxGroup("COMPOSANTES", stringCourses));
        Container name = new Container();
        name.add(new JLabel("test"));
//        name.add(new JTextField());
        programFrame.getContentPane().add(name);




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

    public class CheckBoxGroup extends JPanel {

        private JCheckBox all;
        private List<JCheckBox> checkBoxes;

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


class CheckboxAction extends AbstractAction {
    public CheckboxAction(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox cbLog = (JCheckBox) e.getSource();
        System.out.println(cbLog.getText());
    }
}