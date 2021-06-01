package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.MultiFunctionButton;

import com.plus.mevanspn.BBCSpriteEd.ui.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class MultiFunctionButtonState {
    public MultiFunctionButtonState(String iconFile, String name, int value, KeyPressEventMatcher keyPressEventMatcher) {
        loadIcon(iconFile);
        this.iconFile = iconFile;
        this.value = value;
        this.name = name;
        this.keyPressEventMatcher = keyPressEventMatcher;
    }

    BufferedImage GetIcon() {
        return iconImage;
    }

    String GetIconFile() {
        return iconFile;
    }

    String GetName() { return name; }

    int GetValue() {
        return value;
    }

    KeyPressEventMatcher GetKeyPressEventMatcher() { return keyPressEventMatcher; }

    private void loadIcon(String filename) {
        try {
            this.iconImage = ImageIO.read(MultiFunctionButtonState.class.getResourceAsStream("/img/" + filename));
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private BufferedImage iconImage;
    private final String iconFile, name;
    private final int value;
    private KeyPressEventMatcher keyPressEventMatcher;
}