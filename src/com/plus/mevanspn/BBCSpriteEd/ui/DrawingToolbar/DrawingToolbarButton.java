package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public final class DrawingToolbarButton extends JButton {
    public DrawingToolbarButton(String iconFile, String tooltipText, DrawingToolbar parent) {
        super();
        try {
            this.imageIcon = ImageIO.read(DrawingToolbarButton.class.getResourceAsStream(iconFile));
        } catch (Exception e) { System.out.println(e.getMessage()); }
        this.setToolTipText(tooltipText);
        this.addActionListener(e -> parent.SetActiveButton((DrawingToolbarButton) e.getSource()));
        this.setSelected(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        // super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        Rectangle r = getVisibleRect();
        g2.setColor(isSelected() ? Color.ORANGE : Color.LIGHT_GRAY);
        g2.fillRect(r.x, r.y, r.width, r.height);
        int x = (r.width - imageIcon.getWidth(null)) / 2;
        int y = (r.height - imageIcon.getHeight(null)) / 2;
        g2.drawImage(imageIcon, r.x + x, r.y + y, null);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(34, 34);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    private BufferedImage imageIcon;
}