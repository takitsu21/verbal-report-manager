

package com.mad.util;

import com.mad.listener.TableChangedListener;
import org.w3c.dom.Node;

import java.util.Arrays;

public class XmlUndoRedo extends Action {
    private final String type;
    private final XmlMethodType xmt;
    private final Runnable runner;
    boolean refreshTable;
    private Object arg;
    private Object[] args;

    public XmlUndoRedo(Runnable runner, String type, Object arg, XmlMethodType xmt, boolean refreshTable) {
        this.type = type;
        this.arg = arg;
        this.xmt = xmt;
        this.runner = runner;
        this.refreshTable = refreshTable;
        this.memento = this.memento == null ? new Memento() : this.memento;
    }

    public XmlUndoRedo(Runnable runner, String type, XmlMethodType xmt, boolean refreshTable, Object... args) {
        this.type = type;
        this.args = args;
        this.xmt = xmt;
        this.runner = runner;
        this.refreshTable = refreshTable;
        this.memento = this.memento == null ? new Memento() : this.memento;
    }

    public void execute() {
        this.memento.setState(this.runner);
        this.memento.getState().run();
        if (this.refreshTable) {
            refreshTable();
        }

    }

    public void unExecute() {
        this.memento.setState(this.getExactMethodType());
        this.memento.getState().run();
        refreshTable();
    }

    private Runnable getExactMethodType() {
        Runnable redoRunner = null;
        Node n;
        switch (this.xmt) {
            case ADD:
                if (this.type.equalsIgnoreCase("student")) {
                    redoRunner = () -> {
                        XmlWriter.deleteStudent((String) this.arg);
                    };
                } else if (this.type.equalsIgnoreCase("course")) {
                    n = XmlWriter.getCourseDoc((Node) this.arg);
                    redoRunner = () -> {
                        XmlWriter.removeNode(n);
                    };
                } else if (this.type.equalsIgnoreCase("program")) {
                    n = XmlWriter.getProgramDoc((Node) this.arg);
                    redoRunner = () -> {
                        XmlWriter.removeNode(n);
                    };
                }
                break;
            case DELETE:
                if (this.type.equalsIgnoreCase("student")) {
                    n = Data.doc.importNode((Node) this.arg, true);
                    redoRunner = () -> {
                        XmlWriter.addNode(n);
                    };
                }
                break;
            case MODIFY:
                if (this.type.equalsIgnoreCase("course")) {
                    System.out.println(Arrays.toString(this.args));
                    redoRunner = () -> {
                        TableChangedListener.updateCell((String) this.args[0], (String) this.args[1], (String) this.args[2]);
                    };
                }
                break;
            default:
                return null;
        }

        return redoRunner;
    }
}
