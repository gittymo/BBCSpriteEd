package com.plus.mevanspn.BBCSpriteEd.ui.OnionSkinManager;

import com.plus.mevanspn.BBCSpriteEd.ui.panels.ConfigPanel;

import javax.swing.*;

public final class OnionSkinManagerConfigTab extends JPanel implements ConfigPanel {
    public OnionSkinManagerConfigTab(OnionSkinManager onionSkinManager) {
        super();
    }

    public String GetTitle() { return "Onion Skinning"; }

    private OnionSkinManager onionSkinManager;
}