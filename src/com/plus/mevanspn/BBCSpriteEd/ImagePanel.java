package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

final public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    public ImagePanel(MainFrame parent) {
        super();
        this.parent = parent;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        BBCSpriteFrame activeImage = null;
    }

    private Rectangle GetImageArea() {
        final BBCSpriteFrame activeImage = parent.GetActiveFrame();
        final float zoom = parent.GetZoom();
        if (activeImage == null) return null;
        final int panelWidth = this.getWidth();
        final int panelHeight = this.getHeight();
        final int imageWidth = (int) (activeImage.GetWidth() * zoom * activeImage.GetHorizontalPixelRatio());
        final int imageHeight = (int) (activeImage.GetHeight() * zoom);
        return new Rectangle((panelWidth - imageWidth) / 2, (panelHeight - imageHeight) / 2, imageWidth, imageHeight);
    }

    private boolean ClickInImageArea(int x, int y) {
        Rectangle r = GetImageArea();
        return (r != null && x >= r.x && x <= r.x + r.width && y >= r.y && y <= r.y + r.height);
    }

    private Point GetImageXY(int x, int y) {
        if (!ClickInImageArea(x, y)) return null;
        final Rectangle r = GetImageArea();
        if (r != null) {
            final float zoom = parent.GetZoom();
            final BBCSpriteFrame image = parent.GetActiveFrame();
            Point p = new Point();
            p.x = (int) ((x - r.x) / (zoom * image.GetHorizontalPixelRatio()));
            p.y = (int) ((y - r.y) / zoom);
            return p;
        }
        return null;
    }

    private BufferedImage GetBackground() {
        final int width = parent.GetSprite().GetWidth() * 2;
        final int height = parent.GetSprite().GetHeight() * 2;
        BufferedImage background = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        int mo = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final Color maskColour = parent.maskColours[mo];
                background.setRGB(x, y, maskColour.getRGB());
                mo = (mo + 1) % parent.maskColours.length;
            }
            mo = (mo + 1) % parent.maskColours.length;
        }
        return background;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        final float zoom = parent.GetZoom();
        final BBCSpriteFrame activeImage = parent.GetActiveFrame();
        if (activeImage != null) {
            Rectangle r = GetImageArea();
            if (r != null) {
                g2.drawImage(GetBackground(), r.x, r.y, r.width, r.height, null);
                g2.drawImage(activeImage.GetRenderedImage(), r.x, r.y, r.width, r.height, null);
                final int frameIndex = parent.GetSprite().GetFrameIndex(activeImage);
                final OnionSkinManager osm = parent.GetOnionSkinManager();
                if (osm != null) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                    BBCSpriteFrame onionSkinFrame = osm.GetOnionSkin();
                    if (onionSkinFrame != null) g2.drawImage(onionSkinFrame.GetRenderedImage(), r.x, r.y, r.width, r.height, null);
                    g2.setComposite(AlphaComposite.SrcOver);
                }
                if (zoom > 4) {
                    g2.setColor(new Color(0, 0, 128, 255));
                    g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            1.0f, new float[]{1.0f}, 1.0f));
                    for (float x = zoom * activeImage.GetHorizontalPixelRatio(); x < r.width; x += zoom * activeImage.GetHorizontalPixelRatio()) {
                        g2.drawLine((int) x + r.x, r.y, (int) x + r.x, r.y + r.height);
                    }
                    for (float y = zoom; y < r.height; y += zoom) {
                        g2.drawLine(r.x, (int) y + r.y, r.x + r.width, (int) y + r.y);
                    }
                    g2.setStroke(new BasicStroke());
                }
                g2.setColor(Color.BLACK);
                g2.drawRect(r.x - 1, r.y - 1, r.width + 1, r.height + 1);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final BBCSpriteFrame activeImage = parent.GetActiveFrame();
        if (activeImage != null) {
            final Point p = GetImageXY(e.getX(), e.getY());
            if (p != null) {
                activeImage.SetPixel(p.x, p.y, parent.GetActiveColourIndex());
                repaint();
                parent.UpdateTimeline();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!mouseDown) mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseDown) {
            final BBCSpriteFrame activeImage = parent.GetActiveFrame();
            if (activeImage != null) {
                final Point p = GetImageXY(e.getX(), e.getY());
                if (p != null) {
                    activeImage.SetPixel(p.x, p.y, parent.GetActiveColourIndex());
                    repaint();
                    parent.UpdateTimeline();
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        final OnionSkinManager osm = parent.GetOnionSkinManager();
        if (osm != null) {
            if (e.getWheelRotation() > 0) osm.UserRollForward();
            else osm.UserRollBack();
        }
    }

    private final MainFrame parent;
    private boolean mouseDown = false;
}
