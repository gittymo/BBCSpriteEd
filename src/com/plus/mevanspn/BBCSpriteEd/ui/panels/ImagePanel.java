package com.plus.mevanspn.BBCSpriteEd.ui.panels;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressBroadcaster;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.BBCColour;
import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.DrawingToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.DrawingToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.PaintBrushButton;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.MultiFunctionButton.MultiFunctionButton;
import com.plus.mevanspn.BBCSpriteEd.ui.OnionSkinManager.OnionSkinManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

final public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, KeyPressBroadcaster {
    public ImagePanel(MainFrame parent) {
        super();
        this.parent = parent;
        this.keyPressListenerLinkedList = new LinkedList<>();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
    }

    public void ResetDrawPoints() {
        drawPointA = drawPointB = new Point(0,0);
    }

    private Rectangle GetImageArea() {
        final BBCSpriteFrame activeImage = parent.GetActiveFrame();
        if (activeImage == null) return null;
        final float zoom = parent.GetZoom();
        final int panelWidth = this.getWidth();
        final int panelHeight = this.getHeight();
        final int imageWidth = (int) (activeImage.GetWidth() * zoom * activeImage.GetHorizontalPixelRatio());
        final int imageHeight = (int) (activeImage.GetHeight() * zoom);
        return new Rectangle((panelWidth - imageWidth) / 2, (panelHeight - imageHeight) / 2, imageWidth, imageHeight);
    }

    private Point GetPixelPositionInImage(int x, int y) {
        final Rectangle r = GetImageArea();
        final BBCSpriteFrame image = parent.GetActiveFrame();
        if (r != null && image != null) {
            final float zoom = parent.GetZoom();
            Point p = new Point();
            p.x = (int) ((x - r.x) / (zoom * image.GetHorizontalPixelRatio()));
            p.y = (int) ((y - r.y) / zoom);
            return p;
        } else return null;
    }

    private BufferedImage GetBackgroundImage() {
        final int width = parent.GetSprite().GetWidth() * 2;
        final int height = parent.GetSprite().GetHeight() * 2;
        BufferedImage background = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        int mo = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final Color maskColour = MainFrame.maskColours[mo];
                background.setRGB(x, y, maskColour.getRGB());
                mo = (mo + 1) % MainFrame.maskColours.length;
            }
            mo = (mo + 1) % MainFrame.maskColours.length;
        }
        return background;
    }

    private DrawingToolbarButton getActiveDrawingToolbarButton() {
        return parent.GetActiveDrawingToolbarButton();
    }

    private DrawingToolbarButton getDrawingToolbarButton(String keyValue) {
        return parent.GetDrawingToolbar().GetButton(keyValue);
    }

    private Point getGridAlignedXY(int x, int y) {
        Rectangle imageArea = GetImageArea();
        if (imageArea != null) {
            int iZoom = (int) parent.GetZoom();
            int hZoom = (int) (parent.GetZoom() * parent.GetSprite().GetHorizontalPixelRatio());
            if (iZoom <= 1) {
                return new Point(x < imageArea.x ? imageArea.x : x >= imageArea.x + imageArea.width ? imageArea.x + imageArea.width - 1 : x,
                        y < imageArea.y ? imageArea.y : y >= imageArea.y + imageArea.height ? imageArea.y + imageArea.height - 1 : y);
            } else {
                int alignedX, alignedY;
                alignedX = (((x - imageArea.x) / hZoom) * hZoom) + (hZoom / 2);
                alignedY = (((y - imageArea.y) / iZoom) * iZoom) + (iZoom / 2);
                return new Point(imageArea.x + alignedX, imageArea.y + alignedY);
            }
        } else return null;
    }

    boolean isActiveDrawingTool(String buttonKeyValue) {
        return parent.IsActiveDrawingToolbarButton(buttonKeyValue);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        final float zoom = parent.GetZoom();
        final BBCSpriteFrame activeImage = parent.GetActiveFrame();
        if (activeImage != null) {
            Rectangle r = GetImageArea();
            int imageOffsetX = isActiveDrawingTool("translate") ? drawPointB.x - drawPointA.x : 0;
            int imageOffsetY = isActiveDrawingTool("translate") ? drawPointB.y - drawPointA.y : 0;
            if (zoom > 1) {
                imageOffsetX = (imageOffsetX / (int) (zoom * parent.GetSprite().GetHorizontalPixelRatio())) * (int) (zoom * parent.GetSprite().GetHorizontalPixelRatio());
                imageOffsetY = (imageOffsetY / (int) parent.GetZoom()) * (int) zoom;
            }
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
                    BBCColour outlineColour = !isActiveDrawingTool("paintbrush") ?
                            activeImage.GetColours()[parent.GetActiveColourIndex()] : new BBCColour(192, 160, 255);
                    g2.setColor(outlineColour);
                    if (isActiveDrawingTool("line")) {
                        g2.drawLine(drawPointA.x, drawPointA.y, drawPointB.x, drawPointB.y);
                    } else if (isActiveDrawingTool("rectangle") || (isActiveDrawingTool("paintbrush")
                                 && ((PaintBrushButton) getActiveDrawingToolbarButton()).GetMode() == PaintBrushButton.CAPTURE_MODE)) {
                        final int left = Math.min(drawPointA.x, drawPointB.x);
                        final int top = Math.min(drawPointA.y, drawPointB.y);
                        final int right = Math.max(drawPointA.x, drawPointB.x);
                        final int bottom = Math.max(drawPointA.y, drawPointB.y);
                        g2.drawRect(left, top, right - left, bottom - top);
                    } else if (isActiveDrawingTool("paintbrush")) {
                        final PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                        if (paintBrushButton.GetMode() == PaintBrushButton.DRAWING_MODE) {
                            final BufferedImage paintBrushImage = paintBrushButton.GetActiveBrush();
                            final float hZoom = parent.GetZoom() * parent.GetSprite().GetHorizontalPixelRatio();
                            final int scaledBrushWidth = (int) (paintBrushImage.getWidth() * hZoom);
                            final int scaledBrushHeight = (int) (paintBrushImage.getHeight() * zoom);
                            int xOffset = overlayPoint.x - (scaledBrushWidth / 2);
                            int yOffset = overlayPoint.y - (scaledBrushHeight / 2);
                            if (scaledBrushWidth % (int) (hZoom * 2) == 0) xOffset -= (int) (hZoom / 2);
                            if (scaledBrushHeight % (int) (zoom * 2) == 0) yOffset -= (int) (zoom / 2);
                            g2.drawImage(paintBrushImage, xOffset, yOffset, scaledBrushWidth, scaledBrushHeight, null);
                        }
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
                if (isActiveDrawingTool("pencil") || isActiveDrawingTool("eraser")) {
                    activeImage.SetPixel(p.x, p.y, parent.GetActiveColourIndex());
                    repaint(this.getVisibleRect());
                    parent.UpdateTimeline();
                } else if (isActiveDrawingTool("floodfill")) {
                    activeImage.FloodFill(p, parent.GetActiveColourIndex(), 0, false);
                    repaint(this.getVisibleRect());
                    parent.UpdateTimeline();
                } else if (isActiveDrawingTool("paintbrush")) {
                    PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                    if (paintBrushButton.GetMode() == PaintBrushButton.DRAWING_MODE) {
                        final BufferedImage paintBrushImage = paintBrushButton.GetActiveBrush();
                        activeImage.PaintImage(paintBrushImage, p);
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!mouseDown && e.getButton() == 1) {
            final BBCSpriteFrame activeImage = parent.GetActiveFrame();
            if (activeImage != null) {
                if (activeImage.GetSprite() != null) activeImage.GetSprite().RecordHistory();
                mouseDown = true;
            }
        }

        drawPointA = getGridAlignedXY(e.getX(), e.getY());
        drawPointB = getGridAlignedXY(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        drawPointB = getGridAlignedXY(e.getX(), e.getY());
        final Point pixelPointA = GetPixelPositionInImage(drawPointA.x, drawPointA.y);
        final Point pixelPointB = GetPixelPositionInImage(drawPointB.x, drawPointB.y);
        if (pixelPointA != null & pixelPointB != null) {
            final int left = Math.min(pixelPointA.x, pixelPointB.x);
            final int top = Math.min(pixelPointA.y, pixelPointB.y);
            final int right = Math.max(pixelPointA.x, pixelPointB.x);
            final int bottom = Math.max(pixelPointA.y, pixelPointB.y);
            final BBCSpriteFrame activeImage = parent.GetActiveFrame();
            if (activeImage != null) {
                if (isActiveDrawingTool("line")) {
                    activeImage.DrawLine(pixelPointA, pixelPointB, parent.GetActiveColourIndex());
                } else if (isActiveDrawingTool("rectangle")) {
                    final int rectButtonState = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue();
                    final boolean isFilled = rectButtonState == DrawingToolbar.DRAW_RECT_FILL;
                    activeImage.DrawRectangle(left, top, right - left, bottom - top, isFilled, parent.GetActiveColourIndex());
                } else if (isActiveDrawingTool("translate")) {
                    BBCImage newFrameImage = new BBCImage(activeImage.GetSprite());
                    float zoom = parent.GetZoom() >= 1 ? (int) parent.GetZoom() : 1;
                    final int offsetX = (int) Math.ceil((drawPointB.x - drawPointA.x) / (zoom * parent.GetSprite().GetHorizontalPixelRatio()));
                    final int offsetY = (int) Math.ceil((drawPointB.y - drawPointA.y) / zoom);
                    newFrameImage.getGraphics().drawImage(activeImage.GetRenderedImage(), offsetX, offsetY, null);
                    activeImage.SetRenderedImage(newFrameImage);
                } else if (isActiveDrawingTool("paintbrush")) {
                    PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                    if (paintBrushButton.GetMode() == PaintBrushButton.CAPTURE_MODE) {
                        paintBrushButton.CreateBrush(activeImage.GetRenderedImage(), new Rectangle(left, top, right - left, bottom - top));
                        overlayPoint = getGridAlignedXY(e.getX(), e.getY());
                    }
                }
                parent.RefreshPanels();
            }
            ResetDrawPoints();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.grabFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseDown) {
            final BBCSpriteFrame activeImage = parent.GetActiveFrame();
            final Point p = GetPixelPositionInImage(e.getX(), e.getY());
            if (p != null) {
                if (isActiveDrawingTool("pencil") || isActiveDrawingTool("eraser")) {
                    if (activeImage != null) {
                        if (p != null) {
                            activeImage.SetPixel(p.x, p.y, parent.GetActiveColourIndex());
                            parent.UpdateTimeline();
                        }
                    }
                } else if (isActiveDrawingTool("paintbrush")) {
                    PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                    if (paintBrushButton.GetMode() == PaintBrushButton.DRAWING_MODE) {
                        final BufferedImage paintBrushImage = paintBrushButton.GetActiveBrush();
                        if (paintBrushImage != null) activeImage.PaintImage(paintBrushImage, p);
                    }
                }
            }
            drawPointB = getGridAlignedXY(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!this.hasFocus()) this.grabFocus();
        if (isActiveDrawingTool("paintbrush") &&
                ((PaintBrushButton) getActiveDrawingToolbarButton()).GetMode() == PaintBrushButton.DRAWING_MODE) {
            overlayPoint = getGridAlignedXY(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        final OnionSkinManager osm = parent.GetOnionSkinManager();
        if (osm != null) {
            if (e.getWheelRotation() > 0) osm.RollBack();
            else osm.RollForward();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        BroadcastKeyPress(e);
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void AddKeyPressListener(KeyPressListener keyPressListener) {
        if (!keyPressListenerLinkedList.contains(keyPressListener)) keyPressListenerLinkedList.add(keyPressListener);
    }

    @Override
    public void RemoveKeyPressListener(KeyPressListener keyPressListener) {
        if (keyPressListenerLinkedList.contains(keyPressListener)) keyPressListenerLinkedList.remove(keyPressListener);
    }

    @Override
    public void BroadcastKeyPress(KeyEvent keyEvent) {
        for (KeyPressListener keyPressListener : keyPressListenerLinkedList) {
            keyPressListener.KeyPressed(keyEvent);
        }
    }

    private final MainFrame parent;
    private boolean mouseDown = false;
    private Point drawPointA, drawPointB, overlayPoint;
    private LinkedList<KeyPressListener> keyPressListenerLinkedList;
}
