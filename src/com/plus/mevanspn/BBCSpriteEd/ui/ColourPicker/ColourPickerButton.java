package com.plus.mevanspn.BBCSpriteEd.ui.ColourPicker;

import com.plus.mevanspn.BBCSpriteEd.BBCSprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class ColourPickerButton extends JButton implements ActionListener, MouseWheelListener, MouseListener {
    ColourPickerButton(byte paletteIndex, Color[] colourPalette, ColourPickerToolbar parent) {
        super();
        this.parent = parent;
        this.colourPalette = colourPalette;
        this.paletteIndex = paletteIndex;
        this.colour = paletteIndex < colourPalette.length ? colourPalette[paletteIndex] : Color.LIGHT_GRAY;
        this.isActive = false;
        this.addActionListener(this);
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(colour);
        g2.fillRect(2, 2, getWidth() - 5, getHeight() - 5);
        g2.setColor(Color.BLACK);
        if (isActive) g2.setStroke(new BasicStroke(2.0f));
        g2.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(34,34);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.SetActiveColourButton(this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        BBCSprite.DisplayMode dm = parent.GetParent().GetSprite().GetDisplayMode();
        if (e.getWheelRotation() > 0) colour = dm.GetPreviousColour(colour);
        else colour = dm.GetNextColour(colour);
        colourPalette[paletteIndex] = colour;
        repaint();
        parent.GetParent().RefreshPanels();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3) {
            ColourPickerMenu cm = new ColourPickerMenu(this);
            cm.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    Color colour;
    private final ColourPickerToolbar parent;
    final Color[] colourPalette;
    boolean isActive;
    final byte paletteIndex;
}