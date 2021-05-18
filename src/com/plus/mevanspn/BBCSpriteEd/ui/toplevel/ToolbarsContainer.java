package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

import javax.swing.*;

final public class ToolbarsContainer extends JPanel {
    public ToolbarsContainer(MainFrame parent) {
        super();
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(boxLayout);
        add(parent.GetColourPickerToolbar());
        add(parent.GetDrawingToolbar());
        add(parent.GetEditToolbar());
        add(parent.GetOnionSkinManager().GetToolbar());
    }
}
