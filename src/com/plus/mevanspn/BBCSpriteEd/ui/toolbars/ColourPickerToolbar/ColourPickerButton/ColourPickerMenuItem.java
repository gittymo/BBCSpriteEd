package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar.ColourPickerButton;

import com.plus.mevanspn.BBCSpriteEd.image.BBCColour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ColourPickerMenuItem extends JMenuItem implements ActionListener {
    public ColourPickerMenuItem(BBCColour[] colours, byte colourIndex, ColourPickerButton colourPickerButton) {
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
        colourPickerButton.UpdateColour(this.colours[this.colourIndex]);
    }

    private final BBCColour[] colours;
    private final byte colourIndex;
    private final ColourPickerButton colourPickerButton;
}