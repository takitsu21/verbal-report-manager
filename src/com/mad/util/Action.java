package com.mad.util;

import com.mad.AbstractApplication;

public abstract class Action extends AbstractApplication {
    Memento memento;

    public abstract void execute();

    public abstract void unExecute();
}
