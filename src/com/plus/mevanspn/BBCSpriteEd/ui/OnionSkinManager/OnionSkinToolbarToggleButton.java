package com.plus.mevanspn.BBCSpriteEd.ui.OnionSkinManager;

import javax.swing.*;
import java.awt.*;

public final class OnionSkinToolbarToggleButton extends JButton {
    public OnionSkinToolbarToggleButton(String onIconFile, String offIconFile) {
        this.onImageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(onIconFile)));
        this.offImageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(offIconFile)));
        SetState(true);
    }

    void SetState(boolean state) {
        this.state = state;
        if (state) setIcon(onImageIcon);
        else setIcon(offImageIcon);
        repaint();
    }

    boolean GetState() {
        return state;
    }

    void Toggle() {
        SetState(!this.state);
    }

    private final ImageIcon onImageIcon;
    private final ImageIcon offImageIcon;
    private boolean state;
}