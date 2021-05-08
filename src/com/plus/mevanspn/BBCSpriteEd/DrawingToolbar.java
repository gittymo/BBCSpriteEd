package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

final public class DrawingToolbar extends JPanel {
    public DrawingToolbar(MainFrame parent) {
        super();
        this.activeButton = buttonPencil;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(4,4,4,4));
        this.buttonPencil = new DrawingToolButton("img/pencil.png", "Draw freehand lines.", this);
        this.buttonEraser = new DrawingToolButton("img/eraser.png", "Erase pixels in a freehand fashion.", this);
        this.buttonRectangle = new DrawingToolButton("img/rect.png", "Draw outline/filled rectangles.", this);
        this.buttonLine = new DrawingToolButton("img/line.png", "Draw straight lines.", this);
        this.buttonFill = new DrawingToolButton("img/fill.png", "Fill an enclosed space with the chosen colour.", this);
        this.add(buttonPencil);
        this.add(buttonEraser);
        this.add(buttonFill);
        this.add(buttonRectangle);
        this.add(buttonLine);
    }

    public void SetActiveButton(DrawingToolButton activeButton) {
        this.activeButton = activeButton;
    }

    public DrawingToolButton GetActiveButton() {
        return activeButton;
    }

    DrawingToolButton buttonPencil;
    DrawingToolButton buttonEraser;
    DrawingToolButton buttonLine;
    DrawingToolButton buttonRectangle;
    DrawingToolButton buttonFill;
    private DrawingToolButton activeButton;

    final class DrawingToolButton extends JButton {
        public DrawingToolButton(String iconFile, String tooltipText, DrawingToolbar parent) {
            super();
            this.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile))));
            this.setToolTipText(tooltipText);
            this.addActionListener(e -> parent.SetActiveButton((DrawingToolButton) e.getSource()));
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(24, 24);
        }

        @Override
        public Dimension getPreferredSize() {
            return getMinimumSize();
        }

    }
}
