package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.XmlMethodType;
import com.mad.util.XmlWriter;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AddCourseListener extends AbstractApplication implements ActionListener {
    private JFrame tmp;
    private JTextField idField;
    private JTextField courseNameFiled;
    private JTextField coefField;

    public JTextField getIdField() {
        return idField;
    }

    public JTextField getCourseNameFiled() {
        return courseNameFiled;
    }

    public JTextField getCoefField() {
        return coefField;
    }

    @Override

    public void actionPerformed(ActionEvent e) {
        tmp = new JFrame("Ajouter un Cours");
        tmp.setSize(300, 200);
        tmp.setLocationRelativeTo(null);
        tmp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel idLabel = new JLabel("Identifiant:");
        idField = new JTextField(10);
        JLabel courseLabel = new JLabel("Nom:         ");
        courseNameFiled = new JTextField(10);
        JLabel coefLabel = new JLabel("coefficient:");
        coefField = new JTextField(10);
        coefField.addKeyListener(new KeyWatcher());
        tmp.setIconImage(getIco());


        Container contentPane = tmp.getContentPane();
        JPanel Grille = new JPanel(new GridLayout(3, 1, 5, 1));

        JPanel P1 = new JPanel();
        P1.add(idLabel);
        P1.add(idField);
        Grille.add(P1);

        JPanel P2 = new JPanel();
        P2.add(courseLabel);
        P2.add(courseNameFiled);
        Grille.add(P2);

        JPanel P3 = new JPanel();
        P3.add(coefLabel);
        P3.add(coefField);
        Grille.add(P3);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(Grille, BorderLayout.CENTER);
        JPanel southPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton validate = new JButton("valider");
        validate.addActionListener(new ValidateCourseListener());
        southPane.add(validate);
        contentPane.add(southPane, BorderLayout.SOUTH);

        tmp.setVisible(true);
    }


    public class ValidateCourseListener implements ActionListener {
        private void err() {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(tmp, "        Valeur incrorrect\nSelectionnez une valeur < 30", "Erreur", JOptionPane.WARNING_MESSAGE);
            getCoefField().setText("");

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = getIdField().getText();
                String name = getCourseNameFiled().getText();
                String coef = getCoefField().getText();
                if (Integer.parseInt(coef) > 30 || Integer.parseInt(coef) < 0) {
                    err();
                } else {

                    Node newCourse = XmlWriter.addNewCourseNode(name, id, coef);
                    insertAction(() -> XmlWriter.addNode(newCourse),
                            "course", newCourse, XmlMethodType.ADD, true);
                    tmp.dispose();

                }
            } catch (NumberFormatException ex) {
                err();
            }
        }
    }


}

