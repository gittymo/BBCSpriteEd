package com.plus.mevanspn.BBCSpriteEd.ui.ColourPicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ColourPickerMenuItem extends JMenuItem implements ActionListener {
    public ColourPickerMenuItem(Color[] colours, byte colourIndex, ColourPickerButton colourPickerButton) {
        super();
        this.colours = colours;
        this.colourIndex = colourIndex;
        this.colourPickerButton = colourPickerButton;
        addActionListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(48, 24);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle r = getVisibleRect();
        g2.setColor(colours[colourIndex]);
        g2.fillRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);
        g2.setColor(Color.BLACK);
        g2.drawRect(r.x, r.y, r.width, r.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        colourPickerButton.colourPalette[colourPickerButton.paletteIndex] = this.colours[this.colourIndex];
        colourPickerButton.colour = this.colours[this.colourIndex];
        final ColourPickerToolbar parent = (ColourPickerToolbar) colourPickerButton.getParent();
        parent.UpdatePaletteColour();
    }

    private Color[] colours;
    private byte colourIndex;
    private ColourPickerButton colourPickerButton;
}