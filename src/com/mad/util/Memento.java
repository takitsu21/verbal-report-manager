package com.mad.util;

public class Memento {
    Runnable object;

    public Runnable getState()
    {
        return object;
    }

    public void setState(Runnable o)
    {
        this.object = o;
    }
}
