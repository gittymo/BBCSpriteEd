package com.plus.mevanspn.BBCSpriteEd.ui.toolbars;

import com.plus.mevanspn.BBCSpriteEd.ui.components.*;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.config.OnionSkinConfigPanel;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class OnionSkinManagerToolbar extends JToolBar {
    public OnionSkinManagerToolbar(MainFrame mainFrame) {
        super(JToolBar.HORIZONTAL);
        this.onionSkinToggle = new ToggleButton("OnionOn.png", "OnionOff.png", new KeyPressEventMatcher('O'));
        this.onionSkinToggle.addActionListener(e -> {
            mainFrame.GetImagePanel().repaint();
        });

        this.onionSkinToggle.setToolTipText("Toggle onion skinning on/off (Key O).");
        mainFrame.AddKeyPressListener(onionSkinToggle);
        this.add(onionSkinToggle);

        final OnionSkinConfigPanel onionSkinConfigPanel = (OnionSkinConfigPanel) mainFrame.GetConfigPanel("onionskinning");
        this.pastSpinner = onionSkinConfigPanel.GetPastFramesCountSpinner().CreateDerivedComponent();
        this.pastSpinner.setToolTipText("Set onion skinning past frames to display.");
        this.pastSpinner.addChangeListener(e -> mainFrame.GetImagePanel().repaint());
        this.add(this.pastSpinner);
        futureSpinner = onionSkinConfigPanel.GetFutureFramesCountSpinner().CreateDerivedComponent();
        this.futureSpinner.setToolTipText("Set onion skinning future frames to display.");
        this.futureSpinner.addChangeListener(e -> mainFrame.GetImagePanel().repaint());
        this.add(this.futureSpinner);
    }

    public boolean OnionSkinningEnabled() {
        return onionSkinToggle.GetState();
    }

    public int GetPastFramesToDisplay() { return (Integer) pastSpinner.getValue(); }

    public int GetFutureFramesToDisplay() { return (Integer) futureSpinner.getValue(); }

    private final ToggleButton onionSkinToggle;
    private final NumberSpinner pastSpinner, futureSpinner;
}