package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class DrawingToolbar extends JPanel {
    public DrawingToolbar(MainFrame parent) {
        super();
        this.parent = parent;
        this.activeButton = buttonPencil;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(4,4,4,4));
        this.buttonPencil = new DrawingToolButton("./img/pencil.png", "Draw freehand lines.", this);
        this.buttonEraser = new DrawingToolButton("./img/eraser.png", "Erase pixels in a freehand fashion.", this);
        this.buttonRectangle = new DrawingToolButton("./img/rect.png", "Draw outline/filled rectangles.", this);
        this.buttonLine = new DrawingToolButton("./img/line.png", "Draw straight lines.", this);
        this.buttonFill = new DrawingToolButton("./img/fill.png", "Fill an enclosed space with the chosen colour.", this);
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

    private MainFrame parent;
    DrawingToolButton buttonPencil;
    DrawingToolButton buttonEraser;
    DrawingToolButton buttonLine;
    DrawingToolButton buttonRectangle;
    DrawingToolButton buttonFill;
    private DrawingToolButton activeButton;

    final class DrawingToolButton extends JButton {
        public DrawingToolButton(String iconPath, String tooltipText, DrawingToolbar parent) {
            super(new ImageIcon(iconPath, tooltipText));
            this.setToolTipText(tooltipText);
            this.parent = parent;
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parent.SetActiveButton((DrawingToolButton) e.getSource());
                }
            });
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(24, 24);
        }

        @Override
        public Dimension getPreferredSize() {
            return getMinimumSize();
        }

        private DrawingToolbar parent;
    }
}
