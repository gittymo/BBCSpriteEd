package com.plus.mevanspn.BBCSpriteEd.components;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;

import javax.swing.*;
import java.awt.event.KeyEvent;

public abstract class ToolbarButton extends JButton implements KeyPressListener {
    public ToolbarButton(ImageIcon imageIcon, KeyPressEventMatcher keyPressEventMatcher) {
        super(imageIcon);
        this.keyPressEventMatcher = keyPressEventMatcher;
    }

    public ToolbarButton(KeyPressEventMatcher keyPressEventMatcher) {
        super();
        this.keyPressEventMatcher = keyPressEventMatcher;
    }

    public KeyPressEventMatcher GetKeyPressEventMatcher() {
        return keyPressEventMatcher;
    }

    private KeyPressEventMatcher keyPressEventMatcher;
}

