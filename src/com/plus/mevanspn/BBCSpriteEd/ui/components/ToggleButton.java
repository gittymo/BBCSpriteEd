package com.plus.mevanspn.BBCSpriteEd.ui.components;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public abstract class ToggleButton extends ToolbarButton {
    public ToggleButton(String onIconFile, String offIconFile, KeyPressEventMatcher keyPressEventMatcher) {
        super(keyPressEventMatcher);
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.onImageIcon = loadIcon(onIconFile);
        this.offImageIcon = loadIcon(offIconFile);
        SetState(true);
    }

    public void SetState(boolean state) {
        this.state = state;
        if (state) {
            setIcon(new ImageIcon(onImageIcon));
            this.iconImage = onImageIcon;
        }
        else {
            setIcon(new ImageIcon(offImageIcon));
            this.iconImage = offImageIcon;
        }
        repaint();
    }

    public boolean GetState() {
        return state;
    }

    public void Toggle() {
        SetState(!this.state);
    }

    @Override
    public void KeyPressed(KeyEvent keyEvent) {
        if (keyPressEventMatcher != null && keyPressEventMatcher.IsMatch(keyEvent,true)) Toggle();
    }

    private final BufferedImage onImageIcon;
    private final BufferedImage offImageIcon;
    private boolean state;
    private final KeyPressEventMatcher keyPressEventMatcher;
}