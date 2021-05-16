package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class DrawingToolbarButton extends JButton implements KeyPressListener {
    public DrawingToolbarButton(String iconFile, String tooltipText, DrawingToolbar drawingToolbar, KeyPressEventMatcher keyPressEventMatcher) {
        super();
        try {
            ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile)));
            this.setIcon(imageIcon);
        } catch (Exception e) { e.printStackTrace(); }
        this.setToolTipText(tooltipText);
        this.addActionListener(e -> drawingToolbar.SetActiveButton((DrawingToolbarButton) e.getSource()));
        this.setSelected(false);
        this.drawingToolbar = drawingToolbar;
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.GetDrawingToolbar().GetMainFrame().GetImagePanel().AddKeyPressListener(this);
    }

    public DrawingToolbar GetDrawingToolbar() {
        return drawingToolbar;
    }

    public void MakeActiveDrawingTool() {
        GetDrawingToolbar().SetActiveButton(this);
    }

    private final DrawingToolbar drawingToolbar;
    private KeyPressEventMatcher keyPressEventMatcher;

    @Override
    public void KeyPressed(KeyEvent keyEvent) {
        if (keyPressEventMatcher.IsMatch(keyEvent, true)) GetDrawingToolbar().SetActiveButton(this);
    }
}