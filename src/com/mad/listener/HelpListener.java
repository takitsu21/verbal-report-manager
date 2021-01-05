package com.mad.listener;

import com.mad.AbstractApplication;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame helpFrame = new JFrame("Help");
        helpFrame.setSize(200, 200);
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextPane pane = new JTextPane();
        pane.setEditorKit(new HTMLEditorKit());
        String text = "<style>\n" +
                "    h1 {\n" +
                "        font-size: 26;\n" +
                "    }\n" +
                "\n" +
                "    h2 {\n" +
                "        font-size: 22;\n" +
                "    }\n" +
                "\n" +
                "    li {\n" +
                "        font-size: 18;\n" +
                "    }\n" +
                "\n" +
                "</style>\n" +
                "<h1>\n" +
                "    <u><b>Changer de programme<b></u>\n" +
                "</h1>\n" +
                "<li>Une fois votre fichier ouvert il vous suffit de cliquer sur la combobox situé au dessus du tableau affichant les \n" +
                " programmes disponible et sélectionner le programme voulu.\n" +
                "</li>\n" +
                "<h1>\n" +
                "    <u><b>Selection<b></u>\n" +
                "</h1>\n" +
                "<li>Vous pouvez sélectionner plusieurs lignes avec la souris et afficher / supprimer les lignes actuellement en\n" +
                "    sélection ou même utiliser le raccourci Ctrl + A pour tout sélectionner et exécuter les actions souhaitées avec les\n" +
                "    boutons mis à disposition.\n" +
                "</li>\n" +
                "<h1>\n" +
                "    <u><b>Recherche<b></u>\n" +
                "    <ul>\n" +
                "        <li>Pour utiliser la searchbar vous pouvez coupler les recherches avec le \";\"\n" +
                "            pour ajouter des contraintes à votre recherche vous pouvez utiliser le symbole \"&amp;\" entre chaque arguments \n\n" +
                "        </li>\n" +
                "               <h4> </h4>                  "+
                "        </br><h2 style=\"color: orange;margin:0 0 0 20px;\"><u></br>⚠Attention⚠</u></h2>\n" +
                "        <li>Pour utiliser une recherche a contrainte il faut mettre la recherche associé à\n" +
                "             l’etudiant avant celle du cours\n" +
                "        </li>\n" +
                "    </ul>\n" +
                "    </ul>\n" +
                "\n" +
                "</h1>\n" +
                "<h1>\n" +
                "    <u><b>Modification<b></u>\n" +
                "</h1>\n" +
                "<li>Pour modifier les notes des étudiants vous devez double cliquer sur la cellule voulu et changer la valeur inscrit\n" +
                "    par\n" +
                "    une note entre 0 et 20 ou ABI / ABJ (Absence Injustifié / Absence Justifié)\n" +
                "</li>\n" +
                "\n" +
                "<h1 style=\"color: orange;\">\n" +
                "    <u><b>⚠Attention⚠<b></u>\n" +
                "</h1>\n" +
                "<li>Le C-Z (undo) et C-Y (redo) ne marche pas pour l’ajout de programme soyez sûr de vos actions la face du monde\n" +
                "    pourrait en être bouleversé\n" +
                "</li>";

        pane.setText(text);
        pane.setEditable(false);
        helpFrame.setIconImage(AbstractApplication.getIco());
        StyledDocument doc = (StyledDocument) pane.getDocument();
        Style style = doc.addStyle("StyleName", null);

        StyleConstants.setIcon(style, new ImageIcon("example_recherche.gif"));
        try {
            doc.insertString((doc.getLength() / 2) + 82, "gif recherche", style);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }
        helpFrame.getContentPane().add(new JScrollPane(pane));
        helpFrame.setSize(700, 700);
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setVisible(true);
    }
}
