package com.plus.mevanspn.BBCSpriteEd;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.DirectoryNotEmptyException;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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

    final class ColourPickerButton extends JButton implements ActionListener, MouseWheelListener, MouseListener {
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

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 3) {
                ColourMenu cm = new ColourMenu(this);
                cm.show(parent, e.getX(), e.getY());
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

        private Color colour;
        private final ColourPickerToolbar parent;
        private final Color[] colourPalette;
        private boolean isActive;
        private final byte paletteIndex;

        final class ColourMenu extends JPopupMenu {
            public ColourMenu(ColourPickerButton colourPickerButton) {
                super();
                this.colourPickerButton = colourPickerButton;
                for (int i = 0; i < BBCSprite.DisplayMode.allColours.length; i++)
                    add(new ColourMenuItem(BBCSprite.DisplayMode.allColours, (byte) i));
            }

            private ColourPickerButton colourPickerButton;
            final class ColourMenuItem extends JMenuItem implements ActionListener {
                public ColourMenuItem(Color[] colours, byte colourIndex) {
                    super();
                    this.colours = colours;
                    this.colourIndex = colourIndex;
                    addActionListener(this);
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(48, 24);
                }

                @Override
                public void paintComponent(Graphics g) {
                    // super.paint(g);
                    Graphics2D g2 = (Graphics2D) g;
                    Rectangle r = ColourMenu.this.getVisibleRect();
                    g2.setColor(colours[colourIndex]);
                    g2.fillRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(r.x, r.y, r.width, r.height);
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    colourPickerButton.colourPalette[colourPickerButton.paletteIndex] = this.colours[this.colourIndex];
                    colourPickerButton.colour = this.colours[this.colourIndex];
                    parent.repaint();
                    parent.parent.RefreshPanels();
                }

                private Color[] colours;
                private byte colourIndex;
            }
        }
    }

    private final MainFrame parent;
    private ColourPickerButton activeColourButton;
    private final LinkedList<ColourPickerButton> colourPickerButtons;
}
