package com.plus.mevanspn.BBCSpriteEd.ui.panels;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressBroadcaster;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.OnionSkinManagerToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.Bounds;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.*;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.*;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.MultiFunctionButton.MultiFunctionButton;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.Zoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

final public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, KeyPressBroadcaster {
    public ImagePanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
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
        Rectangle imageArea = null;
        final BBCImage bbcImage = mainFrame.GetActiveImage();
        if (bbcImage != null) {
            final int panelWidth = this.getWidth();
            final int panelHeight = this.getHeight();
            final Rectangle renderedImageDimensions = bbcImage.GetRenderDimensions(mainFrame.zoom);
            imageArea = new Rectangle((panelWidth - renderedImageDimensions.width) / 2,
                    (panelHeight - renderedImageDimensions.height) / 2, renderedImageDimensions.width,
                    renderedImageDimensions.height);
        }
        return imageArea;
    }

    private Point GetPixelPositionInImage(int x, int y) {
        final BBCSpriteFrame spriteFrame = mainFrame.GetActiveFrame();
        if (spriteFrame != null) {
            Rectangle r = GetImageArea();
            final Zoom zoom = mainFrame.GetZoom();
            Point p = new Point();
            p.x = (int) ((x - r.x) / (zoom.X));
            p.y = (int) ((y - r.y) / zoom.Y);
            return p;
        } else return null;
    }

    private BufferedImage GetBackgroundImage() {
        final int width = mainFrame.GetSprite().GetWidth() * 2;
        final int height = mainFrame.GetSprite().GetHeight() * 2;
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
        return mainFrame.GetActiveDrawingToolbarButton();
    }

    private Point getGridAlignedXY(int x, int y) {
        Rectangle imageArea = GetImageArea();
        if (imageArea != null) {
            final Zoom zoom = mainFrame.GetZoom();
            if (zoom.Y <= 1) {
                return new Point(x < imageArea.x ? imageArea.x : x >= imageArea.x + imageArea.width ? imageArea.x + imageArea.width - 1 : x,
                        y < imageArea.y ? imageArea.y : y >= imageArea.y + imageArea.height ? imageArea.y + imageArea.height - 1 : y);
            } else {
                int alignedX = (int) (Math.floor((x - imageArea.x) / zoom.X) * zoom.X);
                int alignedY = (int) (Math.floor((y - imageArea.y) / zoom.Y) * zoom.Y);
                return new Point(imageArea.x + alignedX, imageArea.y + alignedY);
            }
        } else return null;
    }

    private boolean isActiveDrawingTool(String buttonKeyValue) {
        return mainFrame.IsActiveDrawingToolbarButton(buttonKeyValue);
    }

    private BufferedImage generateOvalOverlay(int width, int height, boolean filled) {
        final BBCImage activeFrameImage = mainFrame.GetActiveImage();
        if (activeFrameImage != null & width > 0 & height > 0) {
            final Zoom zoom = mainFrame.zoom;
            final int renderWidth = (width / zoom.iX) + 1;
            final int renderHeight = (height / zoom.iY) + 1;
            BufferedImage ovalOverlayImage = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) ovalOverlayImage.getGraphics();
            g2.setColor(activeFrameImage.GetActiveColour());
            if (filled) g2.fillOval(0, 0, renderWidth - 1, renderHeight - 1);
            g2.drawOval(0,0, renderWidth - 1, renderHeight - 1);
            return ovalOverlayImage;
        } else return null;
    }

    private BufferedImage generateRectangleOverlay(int width, int height, boolean filled, Color colour) {
        final BBCImage activeFrameImage = mainFrame.GetActiveImage();
        if (activeFrameImage != null & width > 0 & height > 0) {
            final Zoom zoom = mainFrame.zoom;
            final int renderWidth = (width / zoom.iX) + 1;
            final int renderHeight = (height / zoom.iY) + 1;
            BufferedImage rectOverlayImage = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rectOverlayImage.getGraphics();
            g2.setColor(colour);
            if (filled) g2.fillRect(0, 0, renderWidth, renderHeight);
            else g2.drawRect(0,0, renderWidth - 1, renderHeight - 1);
            return rectOverlayImage;
        } else return null;
    }

    private BufferedImage generateLineOverlay(Point pointA, Point pointB) {
        final BBCImage activeFrameImage = mainFrame.GetActiveImage();
        if (activeFrameImage != null && pointA != null && pointB != null) {
            final Rectangle imageArea = GetImageArea();
            final Zoom zoom = mainFrame.zoom;
            Point originPointA = new Point(pointA.x - imageArea.x, pointA.y - imageArea.y);
            Point originPointB = new Point(pointB.x - imageArea.x, pointB.y - imageArea.y);
            final int hDiff = originPointA.x < originPointB.x ? originPointA.x : originPointB.x;
            final int vDiff = originPointA.y < originPointB.y ? originPointA.y : originPointB.y;
            final Point pixelPointA = new Point((originPointA.x - hDiff) / zoom.iX, (originPointA.y - vDiff) / zoom.iY);
            final Point pixelPointB = new Point((originPointB.x - hDiff) / zoom.iX, (originPointB.y - vDiff) / zoom.iY);
            final Bounds bounds = new Bounds(pixelPointA, pixelPointB);
            if (bounds.width > 0 && bounds.height > 0) {
                BufferedImage lineOverlayImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = (Graphics2D) lineOverlayImage.getGraphics();
                g2.setColor(mainFrame.GetSprite().GetColours()[mainFrame.GetActiveColourIndex()]);
                g2.drawLine(pixelPointA.x, pixelPointA.y, pixelPointB.x, pixelPointB.y);
                return lineOverlayImage;
            }
        }
        return null;
    }

    private Point getImageOffset(Zoom zoom) {
        int imageOffsetX = isActiveDrawingTool("translate") ? drawPointB.x - drawPointA.x : 0;
        int imageOffsetY = isActiveDrawingTool("translate") ? drawPointB.y - drawPointA.y : 0;
        if (zoom.ZOOM > 1) {
            imageOffsetX = (imageOffsetX / zoom.iX) * zoom.iX;
            imageOffsetY = (imageOffsetY / zoom.iY) * zoom.iY;
        }
        return new Point(imageOffsetX, imageOffsetY);
    }

    private void drawBackgroundImage(Graphics2D g2, Rectangle r) {
        g2.drawImage(GetBackgroundImage(), r.x, r.y, r.width, r.height, null);
    }

    private void drawOnionSkinImages(Graphics2D g2, Rectangle r) {
        final OnionSkinManagerToolbar onionSkinManagerToolbar = mainFrame.GetOnionSkinManagerToolbar();
        if (onionSkinManagerToolbar != null && onionSkinManagerToolbar.OnionSkinningEnabled()) {
            final int currentFrameIndex = mainFrame.GetSprite().GetActiveFrameIndex();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            if (currentFrameIndex > 0) {
                final int pastFrame = Math.max(currentFrameIndex - onionSkinManagerToolbar.GetPastFramesToDisplay(), 0);
                for (int f = pastFrame; f < currentFrameIndex; f++) {
                    BufferedImage pastImage = mainFrame.GetSprite().GetOnionSkinImage(f, true);
                    if (pastImage != null) g2.drawImage(pastImage, r.x, r.y, r.width, r.height, null);
                }
            }
            if (currentFrameIndex < mainFrame.GetSprite().GetFrameCount() - 1) {
                final int futureFrame =
                        currentFrameIndex + onionSkinManagerToolbar.GetFutureFramesToDisplay() >= mainFrame.GetSprite().GetFrameCount() ?
                                mainFrame.GetSprite().GetFrameCount() - 1 : currentFrameIndex + onionSkinManagerToolbar.GetFutureFramesToDisplay();
                for (int f = futureFrame; f > currentFrameIndex; f--) {
                    BufferedImage futureImage = mainFrame.GetSprite().GetOnionSkinImage(f, false);
                    if (futureImage != null) g2.drawImage(futureImage, r.x, r.y, r.width, r.height, null);
                }
            }
            g2.setComposite(AlphaComposite.SrcOver);
        }
    }

    private void drawGrid(Zoom zoom, Graphics2D g2, Rectangle r) {
        if (zoom.ZOOM > 4) {
            g2.setColor(new Color(0, 0, 128, 255));
            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    1.0f, new float[]{1.0f}, 1.0f));
            for (float x = zoom.X; x < r.width; x += zoom.X) {
                g2.drawLine((int) x + r.x, r.y, (int) x + r.x, r.y + r.height);
            }
            for (float y = zoom.Y; y < r.height; y += zoom.Y) {
                g2.drawLine(r.x, (int) y + r.y, r.x + r.width, (int) y + r.y);
            }
            g2.setStroke(new BasicStroke());
        }
    }

    private void drawActiveImage(Graphics2D g2, Rectangle r, Point imageOffset) {
        g2.drawImage(mainFrame.GetActiveImage(), r.x + imageOffset.x, r.y + imageOffset.y,
                r.width, r.height, null);
    }

    private void drawLine(Graphics2D g2, Bounds bounds, Zoom zoom) {
        final BufferedImage lineImage = generateLineOverlay(drawPointA, drawPointB);
        if (lineImage != null) {
            g2.drawImage(lineImage, bounds.left, bounds.top, (int) (lineImage.getWidth() * zoom.X),
                    (int) (lineImage.getHeight() * zoom.Y),null);
        }
    }

    private void drawRectangle(Graphics2D g2, Bounds bounds, Zoom zoom) {
        final boolean filled = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue() == DrawingToolbar.DRAW_RECT_FILL;
        final BufferedImage rectImage = generateRectangleOverlay(bounds.width, bounds.height, filled,
                mainFrame.GetActiveColour());
        if (rectImage != null) {
            g2.drawImage(rectImage, bounds.left, bounds.top, rectImage.getWidth() * zoom.iX,
                    rectImage.getHeight() * zoom.iY, null);
        }
    }

    private void drawOval(Graphics2D g2, Bounds bounds, Zoom zoom) {
        final boolean filled = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue() == DrawingToolbar.DRAW_OVAL_FILL;
        final BufferedImage ovalImage = generateOvalOverlay(bounds.width, bounds.height, filled);
        if (ovalImage != null) {
            g2.drawImage(ovalImage, bounds.left, bounds.top, ovalImage.getWidth() * zoom.iX,
                    ovalImage.getHeight() * zoom.iY, null);
        }
    }

    private void drawSelectionRectangle(Graphics2D g2, Bounds bounds) {
        final BufferedImage selectImage = generateRectangleOverlay(bounds.width, bounds.height, false,
                Color.ORANGE);
        if (selectImage != null) {
            g2.drawImage(selectImage, bounds.left, bounds.top, bounds.width, bounds.height, null);
        }
    }

    private void drawPaintbrush(Graphics2D g2, Zoom zoom) {
        final PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
        if (paintBrushButton.GetMode() == PaintBrushButton.DRAWING_MODE) {
            final BufferedImage paintBrushImage = paintBrushButton.GetActiveBrush();
            if (paintBrushImage != null) {
                final int scaledBrushWidth = (int) (paintBrushImage.getWidth() * zoom.X);
                final int scaledBrushHeight = (int) (paintBrushImage.getHeight() * zoom.Y);
                int xOffset = (int) (Math.round(paintBrushImage.getWidth() / 2.0) * zoom.X);
                int yOffset = (int) (Math.round(paintBrushImage.getHeight() / 2.0) * zoom.Y);
                g2.drawImage(paintBrushImage, overlayPoint.x - xOffset, overlayPoint.y - yOffset,
                        scaledBrushWidth, scaledBrushHeight, null);
            }
        }
    }

    private void drawBorder(Graphics2D g2, Rectangle r) {
        g2.setColor(Color.BLACK);
        g2.drawRect(r.x - 1, r.y - 1, r.width + 1, r.height + 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        final Zoom zoom = mainFrame.zoom;
        final BBCSpriteFrame spriteFrame = mainFrame.GetActiveFrame();
        if (spriteFrame != null) {
            final Point imageOffset = getImageOffset(zoom);
            Rectangle r = GetImageArea();
            if (r != null) {
                drawBackgroundImage(g2, r);
                drawOnionSkinImages(g2, r);
                drawActiveImage(g2, r, imageOffset);
                drawGrid(zoom, g2, r);
                if (drawPointA != null && drawPointB != null) {
                    final Bounds bounds = new Bounds(drawPointA, drawPointB);
                    Color outlineColour = !isActiveDrawingTool("paintbrush") ? mainFrame.GetActiveColour()
                            : mainFrame.GetSelectionColour();
                    g2.setColor(outlineColour);
                    if (mouseDown) {
                        if (isActiveDrawingTool("line")) drawLine(g2, bounds, zoom);
                        else if (isActiveDrawingTool("rectangle")) drawRectangle(g2, bounds, zoom);
                        else if (isActiveDrawingTool("oval")) drawOval(g2, bounds, zoom);
                        else if (isActiveDrawingTool("paintbrush")) {
                            final PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                            if (paintBrushButton.GetMode() != PaintBrushButton.DRAWING_MODE) {
                                drawSelectionRectangle(g2, bounds);
                            }
                        }
                    } else {
                        if (isActiveDrawingTool("paintbrush")) {
                            drawPaintbrush(g2, zoom);
                        }
                    }
                }
                drawBorder(g2, r);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final BBCImage activeImage = mainFrame.GetActiveImage();
        if (activeImage != null) {
            final Point alignedXY = getGridAlignedXY(e.getX(), e.getY());
            final Point p = GetPixelPositionInImage(alignedXY.x, alignedXY.y);
            if (p != null) {
                if (isActiveDrawingTool("pencil") || isActiveDrawingTool("eraser")) {
                    activeImage.SetPixel(p.x, p.y, mainFrame.GetActiveColourIndex());
                    repaint(this.getVisibleRect());
                } else if (isActiveDrawingTool("floodfill")) {
                    activeImage.FloodFill(p, mainFrame.GetActiveColourIndex(), (byte) 0, false);
                    repaint(this.getVisibleRect());
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
        if (!mouseDown && e.getButton() == 1 && !mainFrame.SpriteIsNull()) {
            mainFrame.GetSprite().RecordHistory();
            mouseDown = true;
        }

        drawPointA = getGridAlignedXY(e.getX(), e.getY());
        drawPointB = getGridAlignedXY(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        final BBCImage activeImage = mainFrame.GetActiveImage();
        if (activeImage != null) {
            mouseDown = false;
            drawPointB = getGridAlignedXY(e.getX(), e.getY());
            final Point pixelPointA = GetPixelPositionInImage(drawPointA.x, drawPointA.y);
            final Point pixelPointB = GetPixelPositionInImage(drawPointB.x, drawPointB.y);
            if (pixelPointA != null & pixelPointB != null) {
                final Bounds bounds = new Bounds(pixelPointA, pixelPointB);
                if (isActiveDrawingTool("line")) {
                    activeImage.DrawLine(pixelPointA, pixelPointB, mainFrame.GetActiveColourIndex());
                } else if (isActiveDrawingTool("rectangle")) {
                    final int rectButtonState = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue();
                    final boolean isFilled = rectButtonState == DrawingToolbar.DRAW_RECT_FILL;
                    activeImage.DrawRectangle(bounds.left, bounds.top, bounds.width, bounds.height, isFilled,
                            mainFrame.GetActiveColourIndex());
                } else if (isActiveDrawingTool("oval")) {
                    final int ovalButtonState = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue();
                    final boolean isFilled = ovalButtonState == DrawingToolbar.DRAW_OVAL_FILL;
                    activeImage.DrawOval(bounds.left, bounds.top, bounds.width, bounds.height, isFilled,
                            mainFrame.GetActiveColourIndex());
                } else if (isActiveDrawingTool("translate")) {
                    BBCImage newFrameImage = new BBCImage(activeImage.GetSpriteFrame());
                    final Zoom zoom = mainFrame.zoom;
                    final int offsetX = (int) Math.ceil((drawPointB.x - drawPointA.x) / (zoom.X));
                    final int offsetY = (int) Math.ceil((drawPointB.y - drawPointA.y) / zoom.Y);
                    newFrameImage.getGraphics().drawImage(activeImage, offsetX, offsetY, null);
                    activeImage.GetSpriteFrame().SetRenderedImage(newFrameImage);
                } else if (isActiveDrawingTool("paintbrush")) {
                    PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                    if (paintBrushButton.GetMode() == PaintBrushButton.CAPTURE_MODE) {
                        paintBrushButton.CreateBrush(activeImage, bounds.GetRectangle());
                        overlayPoint = getGridAlignedXY(e.getX(), e.getY());
                    }
                }
                mainFrame.RefreshPanels();
                ResetDrawPoints();
            }
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
        final BBCImage activeImage = mainFrame.GetActiveImage();
        if (mouseDown && activeImage != null) {
            final Point p = GetPixelPositionInImage(e.getX(), e.getY());
            if (p != null) {
                if (isActiveDrawingTool("pencil") || isActiveDrawingTool("eraser")) {
                    activeImage.SetPixel(p.x, p.y, mainFrame.GetActiveColourIndex());
                } else if (isActiveDrawingTool("paintbrush")) {
                    PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                    if (paintBrushButton.GetMode() == PaintBrushButton.DRAWING_MODE) {
                        final BufferedImage paintBrushImage = paintBrushButton.GetActiveBrush();
                        if (paintBrushImage != null) activeImage.PaintImage(paintBrushImage, p);
                    }
                }
            }
            drawPointB = getGridAlignedXY(e.getX(), e.getY());
            mainFrame.RefreshPanels();
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
    public void mouseWheelMoved(MouseWheelEvent e) { }

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
        keyPressListenerLinkedList.remove(keyPressListener);
    }

    @Override
    public void BroadcastKeyPress(KeyEvent keyEvent) {
        for (KeyPressListener keyPressListener : keyPressListenerLinkedList) {
            keyPressListener.KeyPressed(keyEvent);
        }
    }

    private final MainFrame mainFrame;
    private boolean mouseDown = false;
    private Point drawPointA, drawPointB, overlayPoint;
    private final LinkedList<KeyPressListener> keyPressListenerLinkedList;
}
