package com.plus.mevanspn.BBCSpriteEd.components;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class ToggleButton extends ToolbarButton {
    public ToggleButton(String onIconFile, String offIconFile, KeyPressEventMatcher keyPressEventMatcher) {
        super(keyPressEventMatcher);
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.onImageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(onIconFile)));
        this.offImageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(offIconFile)));
        SetState(true);
    }

    public void SetState(boolean state) {
        this.state = state;
        if (state) setIcon(onImageIcon);
        else setIcon(offImageIcon);
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

    private final ImageIcon onImageIcon;
    private final ImageIcon offImageIcon;
    private boolean state;
    private KeyPressEventMatcher keyPressEventMatcher;
}