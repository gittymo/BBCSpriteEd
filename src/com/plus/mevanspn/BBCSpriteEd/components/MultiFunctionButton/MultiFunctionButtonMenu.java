package com.plus.mevanspn.BBCSpriteEd.components.MultiFunctionButton;

import javax.swing.*;

final public class MultiFunctionButtonMenu extends JPopupMenu {
    public MultiFunctionButtonMenu(MultiFunctionButton multiFunctionButton) {
        super();
        this.multiFunctionButton = multiFunctionButton;

        for (MultiFunctionButtonState multiFunctionButtonState : multiFunctionButton.GetStates()) {
            this.add(new MultiFunctionButtonMenuItem(multiFunctionButtonState, this));
        }
    }

    void SetButtonState(int stateValue) {
        multiFunctionButton.SetStateValue(stateValue);
    }

    private final MultiFunctionButton multiFunctionButton;
}
