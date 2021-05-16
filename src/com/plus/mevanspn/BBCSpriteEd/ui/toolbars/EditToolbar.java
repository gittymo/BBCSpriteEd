package com.plus.mevanspn.BBCSpriteEd.ui.toolbars;

import com.plus.mevanspn.BBCSpriteEd.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

final public class EditToolbar extends JToolBar {
    public EditToolbar(MainFrame mainFrame) {
        super();
        ToolbarButton undoButton = new ToolbarButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("img/undo.png"))),
                new KeyPressEventMatcher('Z', true, false, false)) {
            @Override
            public void KeyPressed(KeyEvent keyEvent) {
                if (this.GetKeyPressEventMatcher().IsMatch(keyEvent, true)) {
                    System.out.println("UNdo got key event.");
                    EditToolbar.this.mainFrame.GetSprite().RollBack();
                }
            }
        };
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditToolbar.this.mainFrame.GetSprite().RollBack();
            }
        });
        add(undoButton);
        mainFrame.GetImagePanel().AddKeyPressListener(undoButton);

        ToolbarButton redoButton = new ToolbarButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("img/redo.png"))),
                new KeyPressEventMatcher('Y',true,false,false)) {
            @Override
            public void KeyPressed(KeyEvent keyEvent) {
                if (this.GetKeyPressEventMatcher().IsMatch(keyEvent, true)) {
                    EditToolbar.this.mainFrame.GetSprite().RollForward();
                }
            }
        };
        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditToolbar.this.mainFrame.GetSprite().RollForward();
            }
        });
        add(redoButton);
        mainFrame.GetImagePanel().AddKeyPressListener(redoButton);

        this.mainFrame = mainFrame;
    }

    private final MainFrame mainFrame;
}
