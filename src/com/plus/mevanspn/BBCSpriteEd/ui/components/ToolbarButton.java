package com.plus.mevanspn.BBCSpriteEd.ui.components;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class ToolbarButton extends JButton implements KeyPressListener, ActionListener {
    public ToolbarButton(String iconFile, KeyPressEventMatcher keyPressEventMatcher) {
        super();
        this.iconImage = loadIcon(iconFile);
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.addActionListener(this);
    }

    public ToolbarButton(KeyPressEventMatcher keyPressEventMatcher) {
        super();
        this.iconImage = null;
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.addActionListener(this);
    }

    public KeyPressEventMatcher GetKeyPressEventMatcher() {
        return keyPressEventMatcher;
    }

    public BufferedImage loadIcon(String filename) {
        BufferedImage image = null;
        try {
            InputStream imageInputStream = ToolbarButton.class.getResourceAsStream("/img/" + filename);
            if (imageInputStream != null) {
                image = ImageIO.read(imageInputStream);
                this.setIcon(new ImageIcon(image));
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        return image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle paintArea = getVisibleRect();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(isSelected() ? Color.ORANGE : Color.LIGHT_GRAY);
        g2.fillRect(paintArea.x, paintArea.y, paintArea.width, paintArea.height);
        if (iconImage != null) {
            final int xoffset = (paintArea.width - iconImage.getWidth()) / 2;
            final int yoffset = (paintArea.height - iconImage.getHeight()) / 2;
            g2.drawImage(iconImage, xoffset, yoffset, iconImage.getWidth(), iconImage.getHeight(), null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(28,28);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    private KeyPressEventMatcher keyPressEventMatcher;
    public BufferedImage iconImage;
}

