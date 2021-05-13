package com.plus.mevanspn.BBCSpriteEd.ui;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class EditToolbar extends JToolBar {
    public EditToolbar(MainFrame mainFrame) {
        super();
        JButton undoButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("img/undo.png"))));
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditToolbar.this.mainFrame.GetSprite().RollBack();
            }
        });
        add(undoButton);

        JButton redoButton = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("img/redo.png"))));
        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditToolbar.this.mainFrame.GetSprite().RollForward();
            }
        });
        add(redoButton);

        this.mainFrame = mainFrame;
    }

    private MainFrame mainFrame;
}
