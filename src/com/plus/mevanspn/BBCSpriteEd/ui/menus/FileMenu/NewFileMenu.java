package com.plus.mevanspn.BBCSpriteEd.ui.menus.FileMenu;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.*;

public final class NewFileMenu extends JMenu {
    public NewFileMenu(FileMenu fileMenu) {
        super("New");
        this.fileMenu = fileMenu;

        for (int i = 0; i < BBCSprite.DisplayMode.values().length; i++)
            add(new SpriteTypeMenuItem(BBCSprite.DisplayMode.values()[i],this));
    }

    FileMenu GetFileMenu() { return fileMenu; }

    private final FileMenu fileMenu;
}
