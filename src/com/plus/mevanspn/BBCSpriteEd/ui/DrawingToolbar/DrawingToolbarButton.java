package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class DrawingToolbarButton extends JButton {
    public DrawingToolbarButton(String iconFile, String tooltipText, DrawingToolbar parent) {
        super();
        try {
            this.imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(iconFile)));
            this.setIcon(this.imageIcon);
        } catch (Exception e) { System.out.println(e.getMessage()); }
        this.setToolTipText(tooltipText);
        this.addActionListener(e -> parent.SetActiveButton((DrawingToolbarButton) e.getSource()));
        this.setSelected(false);
    }

    private ImageIcon imageIcon;
}