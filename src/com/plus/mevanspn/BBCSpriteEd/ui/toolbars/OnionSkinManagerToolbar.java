package com.plus.mevanspn.BBCSpriteEd.ui.toolbars;

import com.plus.mevanspn.BBCSpriteEd.components.ToggleButton;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class OnionSkinManagerToolbar extends JToolBar {
    public OnionSkinManagerToolbar(MainFrame mainFrame) {
        super(JToolBar.HORIZONTAL);
        this.mainFrame = mainFrame;
        this.onionSkinToggle = new ToggleButton("img/OnionOn.png", "img/OnionOff.png", new KeyPressEventMatcher('O')) {
            @Override
            public void actionPerformed(ActionEvent e) {
                onionSkinToggle.Toggle();
            }
        };
        this.onionSkinToggle.setToolTipText("Toggle onion skinning on/off (Key O).");
        mainFrame.AddKeyPressListener(onionSkinToggle);
        this.add(onionSkinToggle);
    }

    public boolean OnionSkinningEnabled() {
        return onionSkinToggle.GetState();
    }

    private final ToggleButton onionSkinToggle;
    private final MainFrame mainFrame;
}