package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.*;

public final class ColourPickerMenu extends JPopupMenu {
    public ColourPickerMenu(ColourPickerButton colourPickerButton) {
        super();
        for (int i = 0; i < BBCSprite.DisplayMode.allColours.length; i++)
            add(new ColourPickerMenuItem(BBCSprite.DisplayMode.allColours, (byte) i, colourPickerButton));
    }

}