package com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import javax.swing.*;

final public class EditMenu extends JMenu {
    public EditMenu(MainFrame mainFrame) {
        super("Edit");
        this.mainFrame = mainFrame;
        JMenuItem resizeMenuItem = new JMenuItem("Resize...");
        resizeMenuItem.addActionListener(e -> new ResizeSpriteDialog(GetThis()).setVisible(true));
        add(resizeMenuItem);
    }

    public MainFrame GetMainFrame() {
        return mainFrame;
    }

    public EditMenu GetThis() {
        return this;
    }

    private final MainFrame mainFrame;
}
