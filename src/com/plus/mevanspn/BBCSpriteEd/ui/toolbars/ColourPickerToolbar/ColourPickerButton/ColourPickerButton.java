package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar.ColourPickerButton;

import com.plus.mevanspn.BBCSpriteEd.ui.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.image.*;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar.ColourPickerToolbar;

import java.awt.*;
import java.awt.event.*;

public final class ColourPickerButton extends ToolbarButton implements ActionListener, MouseWheelListener, MouseListener, KeyPressListener {
    public ColourPickerButton(byte paletteIndex, BBCSprite bbcSprite, ColourPickerToolbar colourPickerToolbar, KeyPressEventMatcher keyPressEventMatcher) {
        super(keyPressEventMatcher);
        this.colourPickerToolbar = colourPickerToolbar;
        this.paletteIndex = paletteIndex;
        this.active = false;
        this.bbcSprite = bbcSprite;
        this.addActionListener(this);
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
    }

    void UpdateColour(BBCColour newColour) {
        if (BBCSprite.DisplayMode.GetColourIndex(newColour) != BBCSprite.DisplayMode.GetColourIndex(bbcSprite.GetColours()[paletteIndex])) {
            colourPickerToolbar.GetParent().GetSprite().RecordHistory();
            bbcSprite.GetColours()[paletteIndex] = newColour;
            repaint();
            colourPickerToolbar.GetParent().GetSprite().UpdateColourModel();
            colourPickerToolbar.GetParent().RefreshPanels();
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public byte GetPaletteIndex() {
        return paletteIndex;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Color fillColour = paletteIndex < bbcSprite.GetColours().length ? (Color) bbcSprite.GetColours()[paletteIndex] : Color.LIGHT_GRAY;
        g2.setColor(fillColour);
        g2.fillRect(2, 2, getWidth() - 5, getHeight() - 5);
        g2.setColor(Color.BLACK);
        if (active) g2.setStroke(new BasicStroke(2.0f));
        g2.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(32,32);
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
        colourPickerToolbar.SetActiveColourButton(this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        BBCSprite.DisplayMode dm = colourPickerToolbar.GetParent().GetSprite().GetDisplayMode();
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

    @Override
    public void KeyPressed(KeyEvent keyEvent) {
        if (GetKeyPressEventMatcher() != null && GetKeyPressEventMatcher().IsMatch(keyEvent, false)) {
            colourPickerToolbar.SetActiveColourButton(this);
        }
    }

    private final ColourPickerToolbar colourPickerToolbar;
    boolean active;
    final byte paletteIndex;
    private final BBCSprite bbcSprite;
}