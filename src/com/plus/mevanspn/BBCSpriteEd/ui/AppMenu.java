package com.plus.mevanspn.BBCSpriteEd.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class AppMenu extends JMenu {
    public AppMenu(String title) {
        super(title);
    }

    public void RegisterModuleMenuItems(LinkedList<JMenuItem> menuItems) {
        this.add(new JSeparator());
        for (JMenuItem menuItem : menuItems) {
            JMenuItem copyMenuItem = new JMenuItem(menuItem.getText());
            copyMenuItem.setIcon(menuItem.getIcon());
            if (menuItem.getActionListeners() != null) {
                copyMenuItem.addActionListener(menuItem.getActionListeners()[0]);
            }
            this.add(copyMenuItem);
        }
    }
}
