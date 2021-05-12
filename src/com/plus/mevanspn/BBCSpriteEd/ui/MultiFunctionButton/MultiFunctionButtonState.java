package com.plus.mevanspn.BBCSpriteEd.ui.MultiFunctionButton;

import javax.swing.*;
import java.awt.*;

public final class MultiFunctionButtonState {
    public MultiFunctionButtonState(String iconFile, String name, int value) {
        this.icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile)));
        this.iconFile = iconFile;
        this.value = value;
        this.name = name;
    }

    ImageIcon GetIcon() {
        return icon;
    }

    String GetIconFile() {
        return iconFile;
    }

    String GetName() { return name; }

    int GetValue() {
        return value;
    }

    private final ImageIcon icon;
    private final String iconFile, name;
    private final int value;
}