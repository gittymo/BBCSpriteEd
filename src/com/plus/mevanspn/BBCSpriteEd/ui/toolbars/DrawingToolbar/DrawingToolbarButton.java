package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.ui.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawingToolbarButton extends ToolbarButton {
    public DrawingToolbarButton(String iconFile, String tooltipText, DrawingToolbar drawingToolbar, KeyPressEventMatcher keyPressEventMatcher) {
        super(iconFile, keyPressEventMatcher);
        this.setToolTipText(tooltipText);
        this.setSelected(false);
        this.drawingToolbar = drawingToolbar;
        this.GetDrawingToolbar().GetMainFrame().GetImagePanel().AddKeyPressListener(this);
        this.addActionListener(this);
    }

    public DrawingToolbar GetDrawingToolbar() {
        return drawingToolbar;
    }

    final DrawingToolbar drawingToolbar;

    @Override
    public void KeyPressed(KeyEvent keyEvent) {
        if (GetKeyPressEventMatcher() != null && GetKeyPressEventMatcher().IsMatch(keyEvent, true)) GetDrawingToolbar().SetActiveButton(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        drawingToolbar.SetActiveButton((DrawingToolbarButton) e.getSource());
    }
}