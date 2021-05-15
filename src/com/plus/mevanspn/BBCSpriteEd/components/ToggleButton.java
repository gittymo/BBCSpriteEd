package com.plus.mevanspn.BBCSpriteEd.components;

import javax.swing.*;
import java.awt.*;

public final class ToggleButton extends JButton {
    public ToggleButton(String onIconFile, String offIconFile) {
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

    private final ImageIcon onImageIcon;
    private final ImageIcon offImageIcon;
    private boolean state;
}