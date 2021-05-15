package com.plus.mevanspn.BBCSpriteEd.ui.menus.ZoomMenu;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.JMenu;

final public class ZoomMenu extends JMenu {
    public ZoomMenu(MainFrame mainFrame) {
        super("Zoom");
        this.mainFrame = mainFrame;
        float zoom = 0.25f;
        while (zoom < 64) {
            add(new ZoomMenuItem(zoom, this));
            zoom = zoom * 2;
        }
    }

    MainFrame GetMainFrame() {
        return mainFrame;
    }

    private final MainFrame mainFrame;
}
