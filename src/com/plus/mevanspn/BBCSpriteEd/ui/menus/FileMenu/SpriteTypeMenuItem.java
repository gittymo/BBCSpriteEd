package com.plus.mevanspn.BBCSpriteEd.ui.menus.FileMenu;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.*;

public final class SpriteTypeMenuItem extends JMenuItem {
    public SpriteTypeMenuItem(BBCSprite.DisplayMode displayMode, NewFileMenu newFileMenu) {
        super("Mode " + displayMode.number + ", " + displayMode.colours.length + " colours. " +
                displayMode.width + "x" + displayMode.height + "");
        this.newFileMenu = newFileMenu;

        addActionListener(e -> {
            var imageSizeDialog = new ImageSizePicker(256, 256, getTopLevel());
            imageSizeDialog.setVisible(true);
            if (!imageSizeDialog.WasCancelled() && imageSizeDialog.GetImageWidth() > 0 && imageSizeDialog.GetImageHeight() > 0) {
                getTopLevel().LoadSprite(new BBCSprite(imageSizeDialog.GetImageWidth(), imageSizeDialog.GetImageHeight(), displayMode, getTopLevel()));
            } else imageSizeDialog.setVisible(false);
        });
    }

    private MainFrame getTopLevel() {
        return newFileMenu.GetFileMenu().GetMainFrame();
    }

    private NewFileMenu newFileMenu;
}