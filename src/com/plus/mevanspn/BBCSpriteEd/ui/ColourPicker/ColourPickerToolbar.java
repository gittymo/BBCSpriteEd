package com.plus.mevanspn.BBCSpriteEd.ui.ColourPicker;

import com.plus.mevanspn.BBCSpriteEd.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.*;

final public class ColourPickerToolbar extends JToolBar {
    public ColourPickerToolbar(MainFrame parent) {
        super(JToolBar.HORIZONTAL);
        this.parent = parent;
        this.activeColourButton = null;
        this.colourPickerButtons = new LinkedList<>();
    }

    MainFrame GetParent() {
        return this.parent;
    }

    public void CreatePaletteUsingSprite(BBCSprite sprite) {
        if (this.parent != null) {
            reset();
            if (sprite != null) {
                Color[] spriteColours = sprite.GetColours();
                for (int i = 0; i < spriteColours.length; i++) {
                    addColourPickerButton(new ColourPickerButton((byte) i, spriteColours, this));
                }
                revalidate();
                repaint();
                SetActiveColourButton(colourPickerButtons.get(0));
            }
        }
    }

    private void addColourPickerButton(ColourPickerButton cpb) {
        add(cpb);
        colourPickerButtons.add(cpb);
    }

    private void reset() {
        this.removeAll();
        colourPickerButtons.clear();
    }

    public byte GetActiveColourIndex() {
        return activeColourButton != null ? activeColourButton.paletteIndex : -1;
    }

    public void SetActiveColourButton(ColourPickerButton colourButton) {
        activeColourButton = colourButton;
        for (ColourPickerButton cpb : colourPickerButtons) {
            cpb.isActive = cpb == activeColourButton;
        }
        repaint();
    }

    public void UpdatePaletteColour() {
        repaint();
        parent.RefreshPanels();
    }

    private final MainFrame parent;
    private ColourPickerButton activeColourButton;
    private final LinkedList<ColourPickerButton> colourPickerButtons;
}