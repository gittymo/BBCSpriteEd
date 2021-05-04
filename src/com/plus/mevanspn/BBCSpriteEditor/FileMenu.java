package com.plus.mevanspn.BBCSpriteEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class FileMenu extends JMenu {
    public FileMenu(MainFrame parent) {
        super("File");
        this.parent = parent;
        this.newFileMenu = new NewFileMenu(this);
        this.add(this.newFileMenu);
        this.add(new JSeparator());
        JMenuItem exitApp = new JMenuItem("Quit");
        exitApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.Quit();
            }
        });
        this.add(exitApp);
    }

    private MainFrame parent;
    private NewFileMenu newFileMenu;

    final class NewFileMenu extends JMenu {
        public NewFileMenu(FileMenu parent) {
            super("New");
            this.parent = parent;

            for (int i = 0; i < BBCSprite.DisplayMode.values().length; i++)
                add(new SpriteTypeMenuItem(BBCSprite.DisplayMode.values()[i],this));
        }

        private FileMenu parent;
        private SpriteTypeMenuItem[] spriteTypeMenuItems;

        final class SpriteTypeMenuItem extends JMenuItem {
            public SpriteTypeMenuItem( BBCSprite.DisplayMode displayMode, NewFileMenu parent) {
                super("Mode " + displayMode.number + ", " + displayMode.colours.length + " colours. " +
                        displayMode.width + "x" + displayMode.height + " max.");
                        addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parent.parent.parent.LoadSprite(new BBCSprite(24, 24, displayMode, parent.parent.parent));
                    }
                });
                this.parent = parent;
                this.displayMode = displayMode;
            }

            private BBCSprite.DisplayMode displayMode;
            private NewFileMenu parent;
        }
    }
}
