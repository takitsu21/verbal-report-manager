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
        String text = """
                <style>
                    h1 {
                        font-size: 26;
                    }

                    h2 {
                        font-size: 22;
                    }

                    li {
                        font-size: 18;
                    }

                </style>
                <h1>
                    <u><b>Changer de programme<b></u>
                </h1>
                <li>Une fois votre fichier ouvert il vous suffit de cliquer sur la combobox situé au dessus du tableau affichant les\s
                 programmes disponible et sélectionner le programme voulu.
                </li>
                <h1>
                    <u><b>Selection<b></u>
                </h1>
                <li>Vous pouvez sélectionner plusieurs lignes avec la souris et afficher / supprimer les lignes actuellement en
                    sélection ou même utiliser le raccourci Ctrl + A pour tout sélectionner et exécuter les actions souhaitées avec les
                    boutons mis à disposition.
                </li>
                <h1>
                    <u><b>Recherche<b></u>
                    <ul>
                        <li>Pour utiliser la searchbar vous pouvez coupler les recherches avec le ";"
                            pour ajouter des contraintes à votre recherche vous pouvez utiliser le symbole "&amp;" entre chaque arguments\s

                        </li>
                               <h4> </h4>                          </br><h2 style="color: orange;margin:0 0 0 20px;"><u></br>⚠Attention⚠</u></h2>
                        <li>Pour utiliser une recherche a contrainte il faut mettre la recherche associé à
                             l’etudiant avant celle du cours
                        </li>
                    </ul>
                    </ul>

                </h1>
                <h1>
                    <u><b>Modification<b></u>
                </h1>
                <li>Pour modifier les notes des étudiants vous devez double cliquer sur la cellule voulu et changer la valeur inscrit
                    par
                    une note entre 0 et 20 ou ABI / ABJ (Absence Injustifié / Absence Justifié)
                </li>

                <h1 style="color: orange;">
                    <u><b>⚠Attention⚠<b></u>
                </h1>
                <li>Le C-Z (undo) et C-Y (redo) ne marche pas pour l’ajout de programme soyez sûr de vos actions la face du monde
                    pourrait en être bouleversé
                </li><li>
                Si une modification ne s'affiche pas veuillez utiliser le bouton d'actualisation ou faire F5.
                </li>""";

        pane.setText(text);
        pane.setEditable(false);
        helpFrame.setIconImage(AbstractApplication.getIco());
        StyledDocument doc = (StyledDocument) pane.getDocument();
        Style style = doc.addStyle("StyleName", null);

        StyleConstants.setIcon(style, new ImageIcon("example_recherche.gif"));
        try {
            doc.insertString((doc.getLength() / 2) + 35, "gif recherche", style);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }
        helpFrame.getContentPane().add(new JScrollPane(pane));
        helpFrame.setSize(700, 700);
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setVisible(true);
    }
}
