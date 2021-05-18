package com.plus.mevanspn.BBCSpriteEd.ui.menus.ZoomMenu;

import javax.swing.*;

public final class ZoomMenuItem extends JMenuItem {
    public ZoomMenuItem(float zoom, ZoomMenu zoomMenu) {
        super((zoom * 100) + "%");
        addActionListener(e -> zoomMenu.GetMainFrame().SetZoom(zoom));
    }

    ZoomMenu zoomMenu;
}
