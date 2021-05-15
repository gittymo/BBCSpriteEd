package com.plus.mevanspn.BBCSpriteEd.components.MultiFunctionButton;

import javax.swing.*;

public final class MultiFunctionButtonMenuItem extends JMenuItem {
    public MultiFunctionButtonMenuItem(MultiFunctionButtonState multiFunctionButtonState, MultiFunctionButtonMenu parent) {
        super(multiFunctionButtonState.GetName(), multiFunctionButtonState.GetIcon());
        this.stateValue = multiFunctionButtonState.GetValue();
        this.addActionListener(e -> parent.SetButtonState(stateValue));
    }

    private final int stateValue;
}
