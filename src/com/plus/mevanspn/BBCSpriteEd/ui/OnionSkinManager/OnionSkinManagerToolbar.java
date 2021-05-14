package com.plus.mevanspn.BBCSpriteEd.ui.OnionSkinManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class OnionSkinManagerToolbar extends JToolBar {
    public OnionSkinManagerToolbar(OnionSkinManager onionSkinManager) {
        super(JToolBar.HORIZONTAL);
        this.onionSkinManager = onionSkinManager;
        this.animationWaitTime = onionSkinManager.GetMaxWaitTime();
        this.onionSkinSlider = new JSlider(JSlider.HORIZONTAL);
        this.onionSkinSlider.setAlignmentX(JSlider.CENTER_ALIGNMENT);
        this.onionSkinSlider.setMinorTickSpacing(1);
        this.onionSkinSlider.setPaintTicks(true);
        this.onionSkinSlider.setPaintLabels(true);
        this.onionSkinSlider.addChangeListener(e -> {
            onionSkinSlider.setToolTipText("Set onion skinning offset (current " + onionSkinSlider.getValue() + " frames)");
            onionSkinManager.SetFrameOffset(onionSkinSlider.getValue());
        });
        this.onionSkinToggle = new OnionSkinToolbarToggleButton("img/OnionOn.png", "img/OnionOff.png");
        this.onionSkinToggle.setToolTipText("Toggle onion skinning on/off");
        this.onionSkinToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onionSkinToggle.Toggle();
                onionSkinManager.SetEnabled(onionSkinToggle.GetState());
                onionSkinSlider.setEnabled(onionSkinToggle.GetState());
                onionSkinAnimateToggle.setEnabled(onionSkinToggle.GetState());
            }
        });
        this.onionSkinAnimateToggle = new OnionSkinToolbarToggleButton("img/OnionAniOn.png", "img/OnionAniOff.png");
        this.onionSkinAnimateToggle.setToolTipText("Toggle animation of onion skin rollback.");
        this.onionSkinAnimateToggle.addActionListener(e -> {
            onionSkinAnimateToggle.Toggle();
            onionSkinManager.SetMaxWaitTime(onionSkinAnimateToggle.GetState() ? animationWaitTime : 0);
        });
        this.add(onionSkinSlider);
        this.add(onionSkinToggle);
        this.add(onionSkinAnimateToggle);
        UpdateControls();
    }

    public void UpdateControls() {
        this.onionSkinToggle.SetState(onionSkinManager.IsEnabled());
        this.onionSkinSlider.setMinimum(onionSkinManager.GetMinimumAllowedFrameOffset());
        this.onionSkinSlider.setMaximum(onionSkinManager.GetMaximumAllowedFrameOffset());
        this.onionSkinSlider.setValue(onionSkinManager.GetFrameOffset());
        this.onionSkinAnimateToggle.SetState(onionSkinManager.GetMaxWaitTime() > 0);
    }

    int GetOffset() {
        return onionSkinSlider.getValue();
    }

    private final OnionSkinManager onionSkinManager;
    private final JSlider onionSkinSlider;
    private final OnionSkinToolbarToggleButton onionSkinToggle;
    private final OnionSkinToolbarToggleButton onionSkinAnimateToggle;
    private final int animationWaitTime;


}