package com.mad.listener;

import com.mad.util.Data;
import com.mad.util.XmlToCsv;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddStudentListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame("Ajouter un étudiant");
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(2, 2));

        JPanel pnl = new JPanel();
        //pnl.setLayout(new BorderLayout());

        JTextField studNum=new JTextField("Student number");
        studNum.setSize(60, 30);
        pnl.add(new JLabel("Student number:"));
        pnl.add(studNum);

        JTextField name=new JTextField("Name");
        name.setSize(60, 30);
        pnl.add(new JLabel("Name:"));
        pnl.add(name);

        JTextField surname=new JTextField("Surname");
        surname.setSize(60, 30);
        pnl.add(new JLabel("Surname:"));
        pnl.add(surname);

        JTextField program=new JTextField("Program");
        surname.setSize(60, 30);
        pnl.add(new JLabel("Program:"));
        pnl.add(program);

        frame.add(pnl, BorderLayout.NORTH);

        List<Element> listCourses = Data.getChildren(Data.root, "course");

        String[] cours= generateCheckboxValues(listCourses);

        /*container.add(new CheckBoxGroup("COURS", cours), BorderLayout.SOUTH);
        container.add(new CheckBoxGroup("OPTIONS", cours), BorderLayout.SOUTH);
        container.add(new CheckBoxGroup("COMPOSANTES", cours), BorderLayout.SOUTH);*/

        frame.getContentPane().add(new CheckBoxGroup("COURS", cours), BorderLayout.CENTER);
        JLabel l = new JLabel("Ce cours peut être ajouté mais ne fait pas parti du programme courant");
        l.setForeground(Color.decode("0xf27e11"));
        frame.getContentPane().add(l,BorderLayout.SOUTH);

        Container container = new Container();
        container.add(new JLabel("test"));
//        name.add(new JTextField());
        frame.getContentPane().add(container);




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
                for (int index = 0; index < options.length ; index++) {

                    JCheckBox cb = new JCheckBox(options[index]);
                    cb.setOpaque(false);
                    //cb.setBackground(Color.GRAY);
                    cb.setForeground(Color.decode("0xf27e11"));
                    for (int i=0; i < Data.dataArray[0].length; i++){
                        if(options[index].equals(Data.dataArray[0][i])){
                            System.out.println(options[index]);
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
