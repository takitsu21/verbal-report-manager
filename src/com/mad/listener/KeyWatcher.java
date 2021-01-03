package com.mad.listener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyWatcher implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {

        char c = e.getKeyChar();
        if (!(Character.isDigit(c))) {
            Toolkit.getDefaultToolkit().beep();
            e.consume();
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
