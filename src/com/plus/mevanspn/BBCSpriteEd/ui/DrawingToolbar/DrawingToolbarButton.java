package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import javax.swing.*;
import java.awt.*;

public class DrawingToolbarButton extends JButton {
    public DrawingToolbarButton(String iconFile, String tooltipText, DrawingToolbar parent) {
        super();
        try {
            ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile)));
            this.setIcon(imageIcon);
        } catch (Exception e) { System.out.println(e.getMessage()); }
        this.setToolTipText(tooltipText);
        this.addActionListener(e -> parent.SetActiveButton((DrawingToolbarButton) e.getSource()));
        this.setSelected(false);
    }
}