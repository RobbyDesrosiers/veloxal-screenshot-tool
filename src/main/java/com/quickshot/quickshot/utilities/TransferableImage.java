package com.quickshot.quickshot.utilities;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

// found -> https://stackoverflow.com/questions/29511334/why-cant-i-cast-a-buffered-image-into-a-transferrable-object-to-send-it-to-the
class TransferableImage implements Transferable {
    final BufferedImage image;

    public TransferableImage(final BufferedImage image) {
        this.image = image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DataFlavor.imageFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return image;
        }

        throw new UnsupportedFlavorException(flavor);
    }
}