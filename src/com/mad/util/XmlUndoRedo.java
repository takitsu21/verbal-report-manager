package com.mad.util;


import org.w3c.dom.Node;

public class XmlUndoRedo extends Action {
    private final String type;
    private final Object arg;
    private final XmlMethodType xmt;
    private final Runnable runner;
    boolean refreshTable;

    public XmlUndoRedo(
            Runnable runner, String type, Object arg, XmlMethodType xmt, boolean refreshTable) {
        this.type = type;
        this.arg = arg;
        this.xmt = xmt;
        this.runner = runner;
        this.refreshTable = refreshTable;
        this.memento = memento == null ? new Memento() : memento;
    }


    @Override
    public void execute() {
        memento.setState(runner);
        memento.getState().run();
        if (refreshTable) {
            refreshTable();
        }
    }

    @Override
    public void unExecute() {
        memento.setState(getExactMethodType());
        memento.getState().run();
        refreshTable();
    }

    private Runnable getExactMethodType() {
        Runnable redoRunner = null;
        switch (xmt) {
            case ADD:
                if (type.equalsIgnoreCase("student")) {
                    redoRunner = () -> XmlWriter.deleteStudent((String) arg);
                }
                break;
            case DELETE:
                if (type.equalsIgnoreCase("student")) {
                    Node firstDocImportedNode = Data.doc.importNode((Node) arg, true);
                    redoRunner = () -> XmlWriter.addStudent(firstDocImportedNode);
                }
                break;
            case MODIFY:
                if (type.equalsIgnoreCase("course")) {
//                    redoRunner = () -> XmlWriter.mod
                }
                break;
            default:
                return null;
        }
        return redoRunner;
    }

}


