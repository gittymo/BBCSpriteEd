package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar;

import com.plus.mevanspn.BBCSpriteEd.image.BBCColour;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class ColourPickerButton extends JButton implements ActionListener, MouseWheelListener, MouseListener {
    ColourPickerButton(byte paletteIndex, BBCSprite bbcSprite, ColourPickerToolbar parent) {
        super();
        this.parent = parent;
        this.paletteIndex = paletteIndex;
        this.isActive = false;
        this.bbcSprite = bbcSprite;
        this.addActionListener(this);
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
    }

    void UpdateColour(BBCColour newColour) {
        if (BBCSprite.DisplayMode.GetColourIndex(newColour) != BBCSprite.DisplayMode.GetColourIndex(bbcSprite.GetColours()[paletteIndex])) {
            parent.GetParent().GetSprite().RecordHistory();
            bbcSprite.GetColours()[paletteIndex] = newColour;
            repaint();
            parent.GetParent().GetSprite().UpdateColourModel();
            parent.GetParent().RefreshPanels();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(paletteIndex < bbcSprite.GetColours().length ? bbcSprite.GetColours()[paletteIndex] : Color.LIGHT_GRAY);
        g2.fillRect(2, 2, getWidth() - 5, getHeight() - 5);
        g2.setColor(Color.BLACK);
        if (isActive) g2.setStroke(new BasicStroke(2.0f));
        g2.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(24,24);
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
        if (e.getWheelRotation() > 0) UpdateColour(dm.GetPreviousColour(bbcSprite.GetColours()[paletteIndex]));
        else UpdateColour(dm.GetNextColour(bbcSprite.GetColours()[paletteIndex]));
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

    private final ColourPickerToolbar parent;
    boolean isActive;
    final byte paletteIndex;
    private final BBCSprite bbcSprite;
}