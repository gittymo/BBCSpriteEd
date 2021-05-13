package com.plus.mevanspn.BBCSpriteEd.ui;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar.DrawingToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar.DrawingToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.MultiFunctionButton.MultiFunctionButton;
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

    private DrawingToolbarButton getActiveDrawingToolbarButton() {
        return parent.GetDrawingToolbar().GetActiveButton();
    }

    private DrawingToolbarButton getDrawingToolbarButton(String keyValue) {
        return parent.GetDrawingToolbar().GetButton(keyValue);
    }

    public void ResetDrawPoints() {
        drawPointA = drawPointB = new Point(0,0);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        final float zoom = parent.GetZoom();
        final BBCSpriteFrame activeImage = parent.GetActiveFrame();
        if (activeImage != null) {
            Rectangle r = GetImageArea();
            int imageOffsetX = (getActiveDrawingToolbarButton() == getDrawingToolbarButton("translate") ? drawPointB.x - drawPointA.x : 0);
            int imageOffsetY = getActiveDrawingToolbarButton() == getDrawingToolbarButton("translate") ? drawPointB.y - drawPointA.y : 0;
            imageOffsetX = (imageOffsetX / (int) (parent.GetZoom() * parent.GetSprite().GetHorizontalPixelRatio())) * (int) (zoom * parent.GetSprite().GetHorizontalPixelRatio());
            imageOffsetY = (imageOffsetY / (int) parent.GetZoom()) * (int) zoom;
            if (r != null) {
                g2.drawImage(GetBackgroundImage(), r.x, r.y, r.width, r.height, null);
                g2.drawImage(activeImage.GetRenderedImage(), r.x + imageOffsetX, r.y + imageOffsetY, r.width, r.height, null);
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
                if (drawPointA != null && drawPointB != null && parent.GetActiveColourIndex() < parent.GetSprite().GetColours().length) {
                    g2.setColor(activeImage.GetColours()[parent.GetActiveColourIndex()]);
                    if (getActiveDrawingToolbarButton() == getDrawingToolbarButton("line")) {
                        g2.drawLine(drawPointA.x, drawPointA.y, drawPointB.x, drawPointB.y);
                    } else if (getActiveDrawingToolbarButton() == getDrawingToolbarButton("rectangle")) {
                        final int left = Math.min(drawPointA.x, drawPointB.x);
                        final int top = Math.min(drawPointA.y, drawPointB.y);
                        final int right = Math.max(drawPointA.x, drawPointB.x);
                        final int bottom = Math.max(drawPointA.y, drawPointB.y);
                        g2.drawRect(left, top, right - left, bottom - top);
                    }
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
                if (getActiveDrawingToolbarButton() == getDrawingToolbarButton("pencil") ||
                        getActiveDrawingToolbarButton() == getDrawingToolbarButton("eraser")) {
                    activeImage.SetPixel(p.x, p.y, parent.GetActiveColourIndex());
                    repaint(this.getVisibleRect());
                    parent.UpdateTimeline();
                }
                if (getActiveDrawingToolbarButton() == getDrawingToolbarButton("floodfill")) {
                    activeImage.GetSprite().RecordHistory();
                    activeImage.FloodFill(p, parent.GetActiveColourIndex(), 0, false);
                    repaint(this.getVisibleRect());
                    parent.UpdateTimeline();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!mouseDown && e.getButton() == 1) {
            final BBCSpriteFrame activeImage = parent.GetActiveFrame();
            activeImage.GetSprite().RecordHistory();
            mouseDown = true;
        }

        drawPointA = new Point(e.getX(), e.getY());
        drawPointB = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        drawPointB = new Point(e.getX(), e.getY());
        final BBCSpriteFrame activeImage = parent.GetActiveFrame();
        if (activeImage != null) {
            final Point pixelPointA = GetPixelPositionInImage(drawPointA.x, drawPointA.y);
            final Point pixelPointB = GetPixelPositionInImage(drawPointB.x, drawPointB.y);
            if (getActiveDrawingToolbarButton() == getDrawingToolbarButton("line")) {
                activeImage.DrawLine(pixelPointA, pixelPointB, parent.GetActiveColourIndex());
            } else if (getActiveDrawingToolbarButton() == getDrawingToolbarButton("rectangle")) {
                final int left = Math.min(pixelPointA.x, pixelPointB.x);
                final int top = Math.min(pixelPointA.y, pixelPointB.y);
                final int right = Math.max(pixelPointA.x, pixelPointB.x);
                final int bottom = Math.max(pixelPointA.y, pixelPointB.y);
                final int rectButtonState = ((MultiFunctionButton) parent.GetDrawingToolbar().GetButton("rectangle")).GetStateValue();
                final boolean isFilled = rectButtonState == DrawingToolbar.DRAW_RECT_FILL;
                activeImage.DrawRectangle(left, top, right - left, bottom - top, isFilled, parent.GetActiveColourIndex());
            } else if (getActiveDrawingToolbarButton() == getDrawingToolbarButton("translate")) {
                BBCImage newFrameImage = new BBCImage(activeImage.GetSprite());
                final int offsetX = (int) Math.ceil((drawPointB.x - drawPointA.x) / (parent.GetZoom() * parent.GetSprite().GetHorizontalPixelRatio()));
                final int offsetY = (int) Math.ceil((drawPointB.y - drawPointA.y) / (int) parent.GetZoom());
                newFrameImage.getGraphics().drawImage(activeImage.GetRenderedImage(), offsetX, offsetY, null);
                activeImage.SetRenderedImage(newFrameImage);
            }
            parent.RefreshPanels();
        }
        ResetDrawPoints();
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
                        parent.UpdateTimeline();
                    }
                }
            }
            drawPointB = new Point(e.getX(), e.getY());
            repaint();
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
    private Point drawPointA, drawPointB;
}
