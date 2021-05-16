package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.MultiFunctionButton;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;

import javax.swing.*;
import java.awt.*;

public final class MultiFunctionButtonState {
    public MultiFunctionButtonState(String iconFile, String name, int value, KeyPressEventMatcher keyPressEventMatcher) {
        this.icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile)));
        this.iconFile = iconFile;
        this.value = value;
        this.name = name;
        this.keyPressEventMatcher = keyPressEventMatcher;
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

    KeyPressEventMatcher GetKeyPressEventMatcher() { return keyPressEventMatcher; }

    private final ImageIcon icon;
    private final String iconFile, name;
    private final int value;
    private KeyPressEventMatcher keyPressEventMatcher;
}