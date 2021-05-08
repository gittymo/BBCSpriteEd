package com.plus.mevanspn.BBCSpriteEd;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

final public class ColourPickerToolbar extends JPanel {
    public ColourPickerToolbar(MainFrame parent) {
        this.parent = parent;
        this.activeColourButton = null;
        this.colourPickerButtons = new LinkedList<>();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(4, 4, 4, 4));
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

    final class ColourPickerButton extends JButton implements ActionListener, MouseWheelListener {
        ColourPickerButton(byte paletteIndex, Color[] colourPalette, ColourPickerToolbar parent) {
            super();
            this.parent = parent;
            this.colourPalette = colourPalette;
            this.paletteIndex = paletteIndex;
            this.colour = paletteIndex < colourPalette.length ? colourPalette[paletteIndex] : Color.LIGHT_GRAY;
            this.isActive = false;
            this.addActionListener(this);
            this.addMouseWheelListener(this);
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
            return new Dimension(24,24);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(24, 24);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(24, 24);
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

        private Color colour;
        private final ColourPickerToolbar parent;
        private final Color[] colourPalette;
        private boolean isActive;
        private final byte paletteIndex;
    }

    private final MainFrame parent;
    private ColourPickerButton activeColourButton;
    private final LinkedList<ColourPickerButton> colourPickerButtons;
}
