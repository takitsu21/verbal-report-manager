package com.mad.listener;

import com.mad.AbstractApplication;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyWatcher extends AbstractApplication implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {

        char c = e.getKeyChar();
        if(!(Character.isDigit(c))){
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
