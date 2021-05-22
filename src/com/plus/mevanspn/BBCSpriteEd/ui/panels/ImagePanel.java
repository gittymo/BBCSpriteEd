package com.plus.mevanspn.BBCSpriteEd.ui.panels;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressBroadcaster;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.OnionSkinManagerToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.*;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.*;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.MultiFunctionButton.MultiFunctionButton;

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
        final BBCSpriteFrame activeSpriteFrame = mainFrame.GetActiveFrame();
        if (activeSpriteFrame == null) return null;
        final float zoom = mainFrame.GetZoom();
        final int panelWidth = this.getWidth();
        final int panelHeight = this.getHeight();
        final int imageWidth = (int) (activeSpriteFrame.GetWidth() * zoom * activeSpriteFrame.GetHorizontalPixelRatio());
        final int imageHeight = (int) (activeSpriteFrame.GetHeight() * zoom);
        return new Rectangle((panelWidth - imageWidth) / 2, (panelHeight - imageHeight) / 2, imageWidth, imageHeight);
    }

    private Point GetPixelPositionInImage(int x, int y) {
        final Rectangle r = GetImageArea();
        final BBCSpriteFrame spriteFrame = mainFrame.GetActiveFrame();
        if (r != null && spriteFrame != null) {
            final float zoom = mainFrame.GetZoom();
            Point p = new Point();
            p.x = (int) ((x - r.x) / (zoom * spriteFrame.GetHorizontalPixelRatio()));
            p.y = (int) ((y - r.y) / zoom);
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
            final float zoom = mainFrame.GetZoom();
            final float hZoom = zoom * mainFrame.GetSprite().GetHorizontalPixelRatio();
            if (zoom <= 1) {
                return new Point(x < imageArea.x ? imageArea.x : x >= imageArea.x + imageArea.width ? imageArea.x + imageArea.width - 1 : x,
                        y < imageArea.y ? imageArea.y : y >= imageArea.y + imageArea.height ? imageArea.y + imageArea.height - 1 : y);
            } else {
                int alignedX = (int) (Math.floor((x - imageArea.x) / hZoom) * hZoom);
                int alignedY = (int) (Math.floor((y - imageArea.y) / zoom) * zoom);
                return new Point(imageArea.x + alignedX, imageArea.y + alignedY);
            }
        } else return null;
    }

    boolean isActiveDrawingTool(String buttonKeyValue) {
        return mainFrame.IsActiveDrawingToolbarButton(buttonKeyValue);
    }

    private BufferedImage generateOvalOverlay(int width, int height, boolean filled) {
        final BBCImage activeFrameImage = mainFrame.GetActiveImage();
        if (activeFrameImage != null & width > 0 & height > 0) {
            final int vZoom = (int) mainFrame.GetZoom();
            final int hZoom = (int) (vZoom * mainFrame.GetSprite().GetHorizontalPixelRatio());
            width = width / hZoom;
            height = height / vZoom;
            BufferedImage ovalOverlayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) ovalOverlayImage.getGraphics();
            g2.setColor(mainFrame.GetSprite().GetColours()[mainFrame.GetActiveColourIndex()]);
            if (filled) g2.fillOval(0, 0, width - 1, height - 1);
            g2.drawOval(0,0, width - 1, height - 1);
            return ovalOverlayImage;
        } else return null;
    }

    private BufferedImage generateRectangleOverlay(int width, int height, boolean filled, Color colour) {
        final BBCImage activeFrameImage = mainFrame.GetActiveImage();
        if (activeFrameImage != null & width > 0 & height > 0) {
            final int vZoom = (int) mainFrame.GetZoom();
            final int hZoom = (int) (vZoom * mainFrame.GetSprite().GetHorizontalPixelRatio());
            width = width / hZoom;
            height = height / vZoom;
            BufferedImage rectOverlayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) rectOverlayImage.getGraphics();
            g2.setColor(colour);
            if (filled) g2.fillRect(0, 0, width, height);
            else g2.drawRect(0,0, width - 1, height - 1);
            return rectOverlayImage;
        } else return null;
    }

    private BufferedImage generateLineOverlay(Point pointA, Point pointB) {
        final BBCImage activeFrameImage = mainFrame.GetActiveImage();
        if (activeFrameImage != null && pointA != null && pointB != null) {
            final Rectangle imageArea = GetImageArea();
            final int vZoom = (int) mainFrame.GetZoom();
            final int hZoom = (int) (vZoom * mainFrame.GetSprite().GetHorizontalPixelRatio());
            Point originPointA = new Point(pointA.x - imageArea.x, pointA.y - imageArea.y);
            Point originPointB = new Point(pointB.x - imageArea.x, pointB.y - imageArea.y);
            final int hDiff = originPointA.x < originPointB.x ? originPointA.x : originPointB.x;
            final int vDiff = originPointA.y < originPointB.y ? originPointA.y : originPointB.y;
            final Point pixelPointA = new Point((originPointA.x - hDiff) / hZoom, (originPointA.y - vDiff) / vZoom);
            final Point pixelPointB = new Point((originPointB.x - hDiff) / hZoom, (originPointB.y - vDiff) / vZoom);
            final int left = Math.min(pixelPointA.x, pixelPointB.x);
            final int top = Math.min(pixelPointA.y, pixelPointB.y);
            final int right = Math.max(pixelPointA.x, pixelPointB.x);
            final int bottom = Math.max(pixelPointA.y, pixelPointB.y);
            final int width = (right - left) + 1;
            final int height = (bottom - top) + 1;
            if (width > 0 && height > 0) {
                BufferedImage lineOverlayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = (Graphics2D) lineOverlayImage.getGraphics();
                g2.setColor(mainFrame.GetSprite().GetColours()[mainFrame.GetActiveColourIndex()]);
                g2.drawLine(pixelPointA.x, pixelPointA.y, pixelPointB.x, pixelPointB.y);
                return lineOverlayImage;
            }
        }
        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        final float zoom = mainFrame.GetZoom();
        final float hZoom = zoom * mainFrame.GetSprite().GetHorizontalPixelRatio();
        final BBCSpriteFrame spriteFrame = mainFrame.GetActiveFrame();
        if (spriteFrame != null) {
            Rectangle r = GetImageArea();
            int imageOffsetX = isActiveDrawingTool("translate") ? drawPointB.x - drawPointA.x : 0;
            int imageOffsetY = isActiveDrawingTool("translate") ? drawPointB.y - drawPointA.y : 0;
            if (zoom > 1) {
                imageOffsetX = (imageOffsetX / (int) (zoom * mainFrame.GetSprite().GetHorizontalPixelRatio())) * (int) (zoom * mainFrame.GetSprite().GetHorizontalPixelRatio());
                imageOffsetY = (imageOffsetY / (int) mainFrame.GetZoom()) * (int) zoom;
            }
            if (r != null) {
                g2.drawImage(GetBackgroundImage(), r.x, r.y, r.width, r.height, null);
                final OnionSkinManagerToolbar onionSkinManagerToolbar = mainFrame.GetOnionSkinManagerToolbar();
                if (onionSkinManagerToolbar != null && onionSkinManagerToolbar.OnionSkinningEnabled()) {
                    final int currentFrameIndex = mainFrame.GetSprite().GetActiveFrameIndex();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    if (currentFrameIndex > 0) {
                        final int pastFrame = Math.max(currentFrameIndex - onionSkinManagerToolbar.GetPastFramesToDisplay(), 0);
                        for (int f = pastFrame; f < currentFrameIndex; f++) {
                            BufferedImage pastImage = mainFrame.GetSprite().GetOnionSkinImage(f,true);
                            if (pastImage != null) g2.drawImage(pastImage, r.x, r.y, r.width, r.height, null);
                        }
                    }
                    if (currentFrameIndex < mainFrame.GetSprite().GetFrameCount() - 1) {
                        final int futureFrame =
                                currentFrameIndex + onionSkinManagerToolbar.GetFutureFramesToDisplay() >= mainFrame.GetSprite().GetFrameCount() ?
                                        mainFrame.GetSprite().GetFrameCount() - 1 : currentFrameIndex + onionSkinManagerToolbar.GetFutureFramesToDisplay();
                        for (int f = futureFrame; f > currentFrameIndex; f--) {
                            BufferedImage futureImage = mainFrame.GetSprite().GetOnionSkinImage(f,false);
                            if (futureImage != null) g2.drawImage(futureImage, r.x, r.y, r.width, r.height, null);
                        }
                    }
                    g2.setComposite(AlphaComposite.SrcOver);
                }
                g2.drawImage(spriteFrame.GetRenderedImage(), r.x + imageOffsetX, r.y + imageOffsetY, r.width, r.height, null);

                if (zoom > 4) {
                    g2.setColor(new Color(0, 0, 128, 255));
                    g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            1.0f, new float[]{1.0f}, 1.0f));
                    for (float x = zoom * spriteFrame.GetHorizontalPixelRatio(); x < r.width; x += zoom * spriteFrame.GetHorizontalPixelRatio()) {
                        g2.drawLine((int) x + r.x, r.y, (int) x + r.x, r.y + r.height);
                    }
                    for (float y = zoom; y < r.height; y += zoom) {
                        g2.drawLine(r.x, (int) y + r.y, r.x + r.width, (int) y + r.y);
                    }
                    g2.setStroke(new BasicStroke());
                }
                if (drawPointA != null && drawPointB != null && mainFrame.GetActiveColourIndex() < mainFrame.GetSprite().GetColours().length) {
                    BBCColour outlineColour = !isActiveDrawingTool("paintbrush") ?
                            spriteFrame.GetColours()[mainFrame.GetActiveColourIndex()] : new BBCColour(192, 160, 255);
                    final int left = Math.min(drawPointA.x, drawPointB.x);
                    final int top = Math.min(drawPointA.y, drawPointB.y);
                    final int right = Math.max(drawPointA.x, drawPointB.x);
                    final int bottom = Math.max(drawPointA.y, drawPointB.y);
                    g2.setColor(outlineColour);
                    if (isActiveDrawingTool("line") && mouseDown) {
                        final BufferedImage lineImage = generateLineOverlay(drawPointA, drawPointB);
                        if (lineImage != null) g2.drawImage(lineImage, left, top, (int) (lineImage.getWidth() * hZoom), (int) (lineImage.getHeight() * zoom),null);
                    } else if (isActiveDrawingTool("rectangle") || (isActiveDrawingTool("paintbrush")
                                 && ((PaintBrushButton) getActiveDrawingToolbarButton()).GetMode() == PaintBrushButton.CAPTURE_MODE) ||
                                isActiveDrawingTool("oval")) {
                        if (isActiveDrawingTool("rectangle")) {
                            final boolean filled = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue() == DrawingToolbar.DRAW_RECT_FILL;
                            final BufferedImage rectImage = generateRectangleOverlay(right - left, bottom - top, filled,
                                    mainFrame.GetSprite().GetColours()[mainFrame.GetActiveColourIndex()]);
                            if (rectImage != null) {
                                g2.drawImage(rectImage, left, top, right - left, bottom - top, null);
                            }
                        } else if (isActiveDrawingTool("oval")) {
                            final boolean filled = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue() == DrawingToolbar.DRAW_OVAL_FILL;
                            final BufferedImage ovalImage = generateOvalOverlay(right - left, bottom - top, filled);
                            if (ovalImage != null) {
                                g2.drawImage(ovalImage, left, top, right - left, bottom - top, null);
                            }
                        } else if (isActiveDrawingTool("paintbrush")) {
                            final BufferedImage selectImage = generateRectangleOverlay(right - left, bottom - top, false,
                                    Color.ORANGE);
                            if (selectImage != null) {
                                g2.drawImage(selectImage, left, top, right - left, bottom - top, null);
                            }
                        }
                    } else if (isActiveDrawingTool("paintbrush")) {
                        final PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                        if (paintBrushButton.GetMode() == PaintBrushButton.DRAWING_MODE) {
                            final BufferedImage paintBrushImage = paintBrushButton.GetActiveBrush();
                            final int scaledBrushWidth = (int) (paintBrushImage.getWidth() * hZoom);
                            final int scaledBrushHeight = (int) (paintBrushImage.getHeight() * zoom);
                            int xOffset = (int) (Math.round(paintBrushImage.getWidth() / 2.0) * hZoom);
                            int yOffset = (int) (Math.round(paintBrushImage.getHeight() / 2.0) * zoom);
                            g2.drawImage(paintBrushImage, overlayPoint.x - xOffset, overlayPoint.y - yOffset,
                                    scaledBrushWidth, scaledBrushHeight, null);
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
        final BBCImage activeImage = mainFrame.GetActiveImage();
        if (activeImage != null) {
            final Point alignedXY = getGridAlignedXY(e.getX(), e.getY());
            final Point p = GetPixelPositionInImage(alignedXY.x, alignedXY.y);
            if (p != null) {
                if (isActiveDrawingTool("pencil") || isActiveDrawingTool("eraser")) {
                    activeImage.SetPixel(p.x, p.y, mainFrame.GetActiveColourIndex());
                    repaint(this.getVisibleRect());
                    mainFrame.UpdateTimeline();
                } else if (isActiveDrawingTool("floodfill")) {
                    activeImage.FloodFill(p, mainFrame.GetActiveColourIndex(), (byte) 0, false);
                    repaint(this.getVisibleRect());
                    mainFrame.UpdateTimeline();
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
                final int left = Math.min(pixelPointA.x, pixelPointB.x);
                final int top = Math.min(pixelPointA.y, pixelPointB.y);
                final int right = Math.max(pixelPointA.x, pixelPointB.x);
                final int bottom = Math.max(pixelPointA.y, pixelPointB.y);
                if (isActiveDrawingTool("line")) {
                    activeImage.DrawLine(pixelPointA, pixelPointB, mainFrame.GetActiveColourIndex());
                } else if (isActiveDrawingTool("rectangle")) {
                    final int rectButtonState = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue();
                    final boolean isFilled = rectButtonState == DrawingToolbar.DRAW_RECT_FILL;
                    activeImage.DrawRectangle(left, top, right - left, bottom - top, isFilled, mainFrame.GetActiveColourIndex());
                } else if (isActiveDrawingTool("oval")) {
                    final int ovalButtonState = ((MultiFunctionButton) getActiveDrawingToolbarButton()).GetStateValue();
                    final boolean isFilled = ovalButtonState == DrawingToolbar.DRAW_OVAL_FILL;
                    activeImage.DrawOval(left, top, right - left, bottom - top, isFilled, mainFrame.GetActiveColourIndex());
                } else if (isActiveDrawingTool("translate")) {
                    BBCImage newFrameImage = new BBCImage(activeImage.GetSpriteFrame());
                    float zoom = mainFrame.GetZoom() >= 1 ? (int) mainFrame.GetZoom() : 1;
                    final int offsetX = (int) Math.ceil((drawPointB.x - drawPointA.x) / (zoom * mainFrame.GetSprite().GetHorizontalPixelRatio()));
                    final int offsetY = (int) Math.ceil((drawPointB.y - drawPointA.y) / zoom);
                    newFrameImage.getGraphics().drawImage(activeImage, offsetX, offsetY, null);
                    activeImage.GetSpriteFrame().SetRenderedImage(newFrameImage);
                } else if (isActiveDrawingTool("paintbrush")) {
                    PaintBrushButton paintBrushButton = (PaintBrushButton) getActiveDrawingToolbarButton();
                    if (paintBrushButton.GetMode() == PaintBrushButton.CAPTURE_MODE) {
                        paintBrushButton.CreateBrush(activeImage, new Rectangle(left, top, right - left, bottom - top));
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
