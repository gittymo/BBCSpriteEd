package com.plus.mevanspn.BBCSpriteEd.ui.components;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class ToolbarButton extends JButton implements KeyPressListener, ActionListener {
    public ToolbarButton(ImageIcon imageIcon, KeyPressEventMatcher keyPressEventMatcher) {
        super(imageIcon);
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.addActionListener(this);
    }

    public ToolbarButton(String iconFile, KeyPressEventMatcher keyPressEventMatcher) {
        super(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile))));
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.addActionListener(this);
    }

    public ToolbarButton(KeyPressEventMatcher keyPressEventMatcher) {
        super();
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.addActionListener(this);
    }

    public KeyPressEventMatcher GetKeyPressEventMatcher() {
        return keyPressEventMatcher;
    }

    private KeyPressEventMatcher keyPressEventMatcher;
}

