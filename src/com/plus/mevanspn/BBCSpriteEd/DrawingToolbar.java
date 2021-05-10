package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.*;

final public class DrawingToolbar extends JToolBar {
    public DrawingToolbar(MainFrame parent) {
        super(JToolBar.HORIZONTAL);
        this.activeButton = buttonPencil;
        this.buttonPencil = new DrawingToolButton("res/img/pencil.png", "Draw freehand lines.", this);
        this.buttonEraser = new DrawingToolButton("res/img/eraser.png", "Erase pixels in a freehand fashion.", this);
        this.buttonRectangle = new DrawingToolButton("res/img/rect.png", "Draw outline/filled rectangles.", this);
        this.buttonLine = new DrawingToolButton("res/img/line.png", "Draw straight lines.", this);
        this.buttonFill = new DrawingToolButton("res/img/fill.png", "Fill an enclosed space with the chosen colour.", this);
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
