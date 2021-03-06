package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar.ColourPickerButton.ColourPickerButton;

import java.util.LinkedList;
import javax.swing.*;

final public class ColourPickerToolbar extends JToolBar {
    public ColourPickerToolbar(MainFrame parent) {
        super(JToolBar.HORIZONTAL);
        this.parent = parent;
        this.activeColourButton = null;
        this.colourPickerButtons = new LinkedList<>();
    }

    public void CreatePaletteUsingSprite(BBCSprite sprite) {
        if (this.parent != null) {
            reset();
            if (sprite != null) {
                for (int i = 0; i < sprite.GetColours().length; i++) {
                    ColourPickerButton cpb = new ColourPickerButton((byte) i, sprite, this,
                            i < 8 ? new KeyPressEventMatcher( (char) ('1' + i)) : null );
                    addColourPickerButton(cpb);
                    if (i < 8) cpb.setToolTipText("Key " + (i + 1));
                }
                revalidate();
                repaint();
                SetActiveColourButton(colourPickerButtons.get(0));
            }
        }
    }

    public byte GetActiveColourIndex() {
        return activeColourButton != null ? activeColourButton.GetPaletteIndex() : -1;
    }

    public void SetActiveColourButton(ColourPickerButton colourButton) {
        activeColourButton = colourButton;
        for (ColourPickerButton cpb : colourPickerButtons) {
            cpb.setActive(cpb == activeColourButton);
        }
        repaint();
    }

    public MainFrame GetParent() {
        return this.parent;
    }

    private void addColourPickerButton(ColourPickerButton cpb) {
        add(cpb);
        colourPickerButtons.add(cpb);
        GetParent().GetImagePanel().AddKeyPressListener(cpb);
    }

    private void reset() {
        this.removeAll();
        colourPickerButtons.clear();
    }

    private final MainFrame parent;
    private ColourPickerButton activeColourButton;
    private final LinkedList<ColourPickerButton> colourPickerButtons;
}
