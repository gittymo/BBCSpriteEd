package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class DrawingToolbarButton extends ToolbarButton {
    public DrawingToolbarButton(String iconFile, String tooltipText, DrawingToolbar drawingToolbar, KeyPressEventMatcher keyPressEventMatcher) {
        super(keyPressEventMatcher);
        try {
            ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile)));
            this.setIcon(imageIcon);
        } catch (Exception e) { e.printStackTrace(); }
        this.setToolTipText(tooltipText);
        this.setSelected(false);
        this.drawingToolbar = drawingToolbar;
        this.GetDrawingToolbar().GetMainFrame().GetImagePanel().AddKeyPressListener(this);
        this.addActionListener(this);
    }

    public DrawingToolbar GetDrawingToolbar() {
        return drawingToolbar;
    }

    private final DrawingToolbar drawingToolbar;

    @Override
    public void KeyPressed(KeyEvent keyEvent) {
        if (GetKeyPressEventMatcher() != null && GetKeyPressEventMatcher().IsMatch(keyEvent, true)) GetDrawingToolbar().SetActiveButton(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Drawing button clicked.");
        drawingToolbar.SetActiveButton((DrawingToolbarButton) e.getSource());
    }
}