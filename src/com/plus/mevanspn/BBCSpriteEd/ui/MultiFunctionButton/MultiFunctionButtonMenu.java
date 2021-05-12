package com.plus.mevanspn.BBCSpriteEd.ui.MultiFunctionButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class MultiFunctionButtonMenu extends JPopupMenu {
    public MultiFunctionButtonMenu(MultiFunctionButton parent) {
        super();
        this.parent = parent;

        for (MultiFunctionButtonState multiFunctionButtonState : parent.GetStates()) {
            this.add(new MultiFunctionButtonMenuItem(multiFunctionButtonState, this));
        }
    }

    void SetButtonState(int stateValue) {
        parent.SetStateValue(stateValue);
    }

    private MultiFunctionButton parent;

    final class MultiFunctionButtonMenuItem extends JMenuItem {
        public MultiFunctionButtonMenuItem(MultiFunctionButtonState multiFunctionButtonState, MultiFunctionButtonMenu parent) {
            super(multiFunctionButtonState.GetName(), multiFunctionButtonState.GetIcon());
            this.stateValue = multiFunctionButtonState.GetValue();
            this.parent = parent;
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.SetButtonState(stateValue);
                }
            });
        }

        private int stateValue;
        private MultiFunctionButtonMenu parent;
    }
}
