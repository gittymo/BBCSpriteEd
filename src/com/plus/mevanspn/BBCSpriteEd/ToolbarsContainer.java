package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;

final public class ToolbarsContainer extends JPanel {
    public ToolbarsContainer(MainFrame parent) {
        super();
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(boxLayout);
        add(parent.GetColourPickerToolbar());
        add(parent.GetDrawingToolbar());
        add(parent.GetOnionSkinManager().GetToolbar());
    }
}
