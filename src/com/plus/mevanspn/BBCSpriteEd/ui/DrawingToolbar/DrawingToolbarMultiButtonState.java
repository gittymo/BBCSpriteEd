package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import javax.swing.*;
import java.awt.*;

public final class DrawingToolbarMultiButtonState {
    public DrawingToolbarMultiButtonState(String iconFile, int value) {
        this.icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile)));
        this.iconFile = iconFile;
        this.value = value;
    }

    ImageIcon GetIcon() {
        return icon;
    }

    String GetIconFile() {
        return iconFile;
    }

    int GetValue() {
        return value;
    }

    private ImageIcon icon;
    private String iconFile;
    private int value;
}