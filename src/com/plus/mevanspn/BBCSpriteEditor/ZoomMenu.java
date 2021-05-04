package com.plus.mevanspn.BBCSpriteEditor;

import com.sun.tools.javac.Main;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class ZoomMenu extends JMenu {
    public ZoomMenu(MainFrame parent) {
        super("Zoom");
        this.parent = parent;
        float zoom = 0.25f;
        while (zoom < 64) {
            add(new ZoomMenuItem(zoom, this));
            zoom = zoom * 2;
        }
        add(new JSeparator());
        JMenuItem customZoom = new JMenuItem("Custom zoom...");
        add(customZoom);
    }

    private MainFrame parent;

    final class ZoomMenuItem extends JMenuItem {
        public ZoomMenuItem(float zoom, ZoomMenu parent) {
            super((zoom * 100) + "%");
            this.zoom = zoom;
            this.parent = parent;
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.parent.SetZoom(zoom);
                }
            });
        }

        private ZoomMenu parent;
        private float zoom;
    }
}
