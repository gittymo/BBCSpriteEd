package com.plus.mevanspn.BBCSpriteEd.ui.ColourPicker;

import com.plus.mevanspn.BBCSpriteEd.BBCSprite;

import javax.swing.*;

public final class ColourPickerMenu extends JPopupMenu {
    public ColourPickerMenu(ColourPickerButton colourPickerButton) {
        super();
        this.colourPickerButton = colourPickerButton;
        for (int i = 0; i < BBCSprite.DisplayMode.allColours.length; i++)
            add(new ColourPickerMenuItem(BBCSprite.DisplayMode.allColours, (byte) i));
    }

    private ColourPickerButton colourPickerButton;

}