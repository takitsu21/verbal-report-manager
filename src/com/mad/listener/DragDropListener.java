package com.mad.listener;

import com.mad.AbstractApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

public class DragDropListener extends AbstractApplication implements DropTargetListener {

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void drop(DropTargetDropEvent event) {
        event.acceptDrop(DnDConstants.ACTION_COPY);
        Transferable transferable = event.getTransferable();
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        for (DataFlavor flavor : flavors) {
            try {
                if (flavor.isFlavorJavaFileListType()) {
                    List<File> files = (List) transferable.getTransferData(flavor);

                    for (File file : files) {
                        if (file.getPath().endsWith(".xml") || file.getPath().endsWith(".csv")) {
                            OpenFileListener.openFile(file.getPath());
                            getDragAndDrop().setVisible(false);
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Mauvais type de fichier", "Alerte", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        event.dropComplete(true);

    }

}