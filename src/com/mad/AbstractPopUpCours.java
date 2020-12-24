package com.mad;

import javax.swing.*;

public abstract class AbstractPopUpCours extends AbstractApplication {
    protected static JFrame tmp;
    protected static JLabel idLabel;
    protected static JTextField idField;
    protected static JLabel courseLabel;
    protected static JTextField courseNameFiled;
    protected static JLabel coefLabel;
    protected static JTextField coefField;
    protected static JPanel grille;
    protected static JPanel p1;
    protected static JPanel p2;
    protected static JPanel p3;
    protected static JPanel southPane;
    protected static JButton validate;

    public static JFrame getTmp() {
        return tmp;
    }

    public static void setTmp(JFrame tmp) {
        AbstractPopUpCours.tmp = tmp;
    }

    public static JLabel getIdLabel() {
        return idLabel;
    }

    public static String getID() {
        return idField.getText();
    }

    public static void setIdLabel(JLabel idLabel) {
        AbstractPopUpCours.idLabel = idLabel;
    }

    public static JTextField getIdField() {
        return idField;
    }

    public static void setIdField(JTextField idField) {
        AbstractPopUpCours.idField = idField;
    }

    public static JLabel getCourseLabel() {
        return courseLabel;
    }

    public static void setCourseLabel(JLabel courseLabel) {
        AbstractPopUpCours.courseLabel = courseLabel;
    }

    public static JTextField getCourseNameFiled() {
        return courseNameFiled;
    }

    public static String getCourseName() {
        return courseNameFiled.getText();
    }

    public static void setCourseNameFiled(JTextField courseNameFiled) {
        AbstractPopUpCours.courseNameFiled = courseNameFiled;
    }

    public static JLabel getCoefLabel() {
        return coefLabel;
    }


    public static void setCoefLabel(JLabel coefLabel) {
        AbstractPopUpCours.coefLabel = coefLabel;
    }

    public static JTextField getCoefField() {
        return coefField;
    }

    public static String getCoef() {
        return coefField.getText();
    }

    public static void setCoefField(JTextField coefField) {
        AbstractPopUpCours.coefField = coefField;
    }

    public static JPanel getGrille() {
        return grille;
    }

    public static void setGrille(JPanel grille) {
        AbstractPopUpCours.grille = grille;
    }

    public static JPanel getP1() {
        return p1;
    }

    public static void setP1(JPanel p1) {
        AbstractPopUpCours.p1 = p1;
    }

    public static JPanel getP2() {
        return p2;
    }

    public static void setP2(JPanel p2) {
        AbstractPopUpCours.p2 = p2;
    }

    public static JPanel getP3() {
        return p3;
    }

    public static void setP3(JPanel p3) {
        AbstractPopUpCours.p3 = p3;
    }

    public static JPanel getSouthPane() {
        return southPane;
    }

    public static void setSouthPane(JPanel southPane) {
        AbstractPopUpCours.southPane = southPane;
    }

    public static JButton getValidate() {
        return validate;
    }

    public static void setValidate(JButton validate) {
        AbstractPopUpCours.validate = validate;
    }
}
