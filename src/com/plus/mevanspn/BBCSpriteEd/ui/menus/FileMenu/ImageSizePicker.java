package com.plus.mevanspn.BBCSpriteEd.ui.menus.FileMenu;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class ImageSizePicker extends JDialog {
    public ImageSizePicker(int maxWidth, int maxHeight, MainFrame parentFrame) {
        super(parentFrame, "Choose sprite size", true);

        this.imageWidth = this.imageHeight = 0;
        this.cancelled = false;

        this.imageWidthField = new JTextField();
        this.imageHeightField = new JTextField();
        JLabel imageWidthLabel = new JLabel("Width");
        JLabel imageHeightLabel = new JLabel("Height");
        JButton buttonOkay = new JButton("Create Sprite");
        buttonOkay.addActionListener(e -> {
            try {
                imageWidth = Integer.parseInt(imageWidthField.getText());
                imageHeight = Integer.parseInt(imageHeightField.getText());
                if (imageWidth < 1 || imageWidth > maxWidth) imageWidth = 0;
                if (imageHeight < 1 || imageHeight > maxHeight) imageHeight = 0;
            } catch (Exception ex) { ex.printStackTrace(); }
            cancelled = false;
            close();
        });
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(e -> {
            cancelled = true;
            close();
        });
        GridLayout gridLayout = new GridLayout(3, 2, 4, 4);
        setLayout(gridLayout);
        add(imageWidthLabel);
        add(imageHeightLabel);
        add(this.imageWidthField);
        add(this.imageHeightField);
        add(buttonOkay);
        add(buttonCancel);
        pack();

        getRootPane().setBorder(new EmptyBorder(4,4,4,4));
        this.setLocationRelativeTo(parentFrame);
    }

    boolean WasCancelled() {
        return cancelled;
    }

    int GetImageWidth() {
        return imageWidth;
    }

    int GetImageHeight() {
        return imageHeight;
    }

    private void close() {
        setVisible(false);
    }

    private int imageWidth, imageHeight;
    private boolean cancelled;
    private final JTextField imageWidthField, imageHeightField;
}
