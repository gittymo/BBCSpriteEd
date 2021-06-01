package com.plus.mevanspn.BBCSpriteEd.ui.toolbars;

import com.plus.mevanspn.BBCSpriteEd.ui.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

final public class EditToolbar extends JToolBar {
    public EditToolbar(MainFrame mainFrame) {
        super();
        ToolbarButton undoButton = new ToolbarButton("undo.png",
                new KeyPressEventMatcher('Z', true, false, false)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditToolbar.this.mainFrame.GetSprite().RollBack();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) {
                if (this.GetKeyPressEventMatcher().IsMatch(keyEvent, true)) {
                    EditToolbar.this.mainFrame.GetSprite().RollBack();
                }
            }
        };

        add(undoButton);
        undoButton.setToolTipText("Undo previous operations (Key Ctrl+Z)");
        mainFrame.GetImagePanel().AddKeyPressListener(undoButton);

        ToolbarButton redoButton = new ToolbarButton("redo.png",
                new KeyPressEventMatcher('Y',true,false,false)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditToolbar.this.mainFrame.GetSprite().RollForward();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) {
                if (this.GetKeyPressEventMatcher().IsMatch(keyEvent, true)) {
                    EditToolbar.this.mainFrame.GetSprite().RollForward();
                }
            }
        };
        add(redoButton);
        redoButton.setToolTipText("Redo previous operations (Key Ctrl+Y)");
        mainFrame.GetImagePanel().AddKeyPressListener(redoButton);

        this.mainFrame = mainFrame;
    }

    private final MainFrame mainFrame;
}
