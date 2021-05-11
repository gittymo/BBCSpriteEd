package com.plus.mevanspn.BBCSpriteEd.ui;

import com.plus.mevanspn.BBCSpriteEd.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.OnionSkinManager.OnionSkinManager;

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

    private boolean ClickIsInImageArea(int x, int y) {
        Rectangle r = GetImageArea();
        return (r != null && x >= r.x && x <= r.x + r.width && y >= r.y && y <= r.y + r.height);
    }

    private Point GetPixelPositionInImage(int x, int y) {
        if (!ClickIsInImageArea(x, y)) return null;
        final Rectangle r = GetImageArea();
        final BBCSpriteFrame image = parent.GetActiveFrame();
        if (r != null && image != null) {
            final float zoom = parent.GetZoom();
            Point p = new Point();
            p.x = (int) ((x - r.x) / (zoom * image.GetHorizontalPixelRatio()));
            p.y = (int) ((y - r.y) / zoom);
            return p;
        }
        return null;
    }

    private BufferedImage GetBackgroundImage() {
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
                g2.drawImage(GetBackgroundImage(), r.x, r.y, r.width, r.height, null);
                g2.drawImage(activeImage.GetRenderedImage(), r.x, r.y, r.width, r.height, null);
                final OnionSkinManager osm = parent.GetOnionSkinManager();
                if (osm != null && osm.IsEnabled()) {
                    BufferedImage onionSkinImage = osm.GetOnionSkin();
                    if (onionSkinImage != null) g2.drawImage(onionSkinImage, r.x, r.y, r.width, r.height, null);
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
                if (realLineStart != null && realLineEnd != null) {
                    g2.setColor(activeImage.GetColours()[parent.GetActiveColourIndex()]);
                    g2.drawLine(realLineStart.x, realLineStart.y, realLineEnd.x, realLineEnd.y);
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
            final Point p = GetPixelPositionInImage(e.getX(), e.getY());
            if (p != null) {
                if (parent.GetDrawingToolbar().GetActiveButton() == parent.GetDrawingToolbar().buttons.get("pencil") ||
                        parent.GetDrawingToolbar().GetActiveButton() == parent.GetDrawingToolbar().buttons.get("eraser")) {
                    activeImage.SetPixel(p.x, p.y, parent.GetActiveColourIndex());
                    repaint();
                    parent.UpdateTimeline();
                }
                if (parent.GetDrawingToolbar().GetActiveButton() == parent.GetDrawingToolbar().buttons.get("floodfill")) {
                    activeImage.FloodFill(p, parent.GetActiveColourIndex(), (byte) 127, false);
                    repaint();
                    parent.UpdateTimeline();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!mouseDown) mouseDown = true;
        if (parent.GetDrawingToolbar().GetActiveButton() == parent.GetDrawingToolbar().buttons.get("line")) {
            lineStart = GetPixelPositionInImage(e.getX(), e.getY());
            realLineStart = new Point(e.getX(), e.getY());
            lineEnd = null;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        if (parent.GetDrawingToolbar().GetActiveButton() == parent.GetDrawingToolbar().buttons.get("line")) {
            lineEnd = GetPixelPositionInImage(e.getX(), e.getY());
            final BBCSpriteFrame activeImage = parent.GetActiveFrame();
            if (activeImage != null) {
                activeImage.DrawLine(lineStart, lineEnd, parent.GetActiveColourIndex());
                lineStart = lineEnd = realLineStart = null;
                repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseDown) {
            if (parent.GetDrawingToolbar().GetActiveButton() == parent.GetDrawingToolbar().buttons.get("pencil") ||
                    parent.GetDrawingToolbar().GetActiveButton() == parent.GetDrawingToolbar().buttons.get("eraser")) {
                final BBCSpriteFrame activeImage = parent.GetActiveFrame();
                if (activeImage != null) {
                    final Point p = GetPixelPositionInImage(e.getX(), e.getY());
                    if (p != null) {
                        activeImage.SetPixel(p.x, p.y, parent.GetActiveColourIndex());
                        repaint();
                        parent.UpdateTimeline();
                    }
                }
            } else if (parent.GetDrawingToolbar().GetActiveButton() == parent.GetDrawingToolbar().buttons.get("line")) {
                realLineEnd = new Point(e.getX(), e.getY());
                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        final OnionSkinManager osm = parent.GetOnionSkinManager();
        if (osm != null) {
            if (e.getWheelRotation() > 0) osm.UserRollBack();
            else osm.UserRollForward();
        }
    }

    private final MainFrame parent;
    private boolean mouseDown = false;
    private Point lineStart, lineEnd, realLineStart, realLineEnd;
}
