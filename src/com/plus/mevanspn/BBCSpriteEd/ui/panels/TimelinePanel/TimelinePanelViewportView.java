package com.plus.mevanspn.BBCSpriteEd.ui.panels.TimelinePanel;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public final class TimelinePanelViewportView extends JPanel implements MouseListener, MouseMotionListener {
    public TimelinePanelViewportView(TimelinePanel parent) {
        super();
        this.parent = parent;
        this.sprite = null;
        this.clickPoint = null;
        this.timelinePanelViewportViewPopup = new TimelinePanelViewportViewPopup(this);
        initialiseComponents();
        revalidate();
        repaint();
    }

    public void SetSprite(BBCSprite sprite) {
        this.sprite = sprite;
        Refresh();
    }

    public void Refresh() {
        this.revalidate();
        this.repaint();
    }

    void DuplicateFrame(boolean atEnd) {
        final int frameToDuplicate = clickPoint != null ? GetClickFrame(clickPoint) : sprite.GetActiveFrameIndex();
        if (frameToDuplicate > -1) {
            sprite.DuplicateFrame(sprite.GetFrame(frameToDuplicate), atEnd);
        }
    }

    void InsertFrameAfter() {
        if (clickPoint != null) {
            final int previousFrame = GetClickFrame(clickPoint);
            sprite.AddFrame(previousFrame + 1);
        } else sprite.AddFrame(sprite.GetActiveFrameIndex() + 1);
    }

    void InsertFrameBefore() {
        if (clickPoint != null) {
            final int previousFrame = GetClickFrame(clickPoint);
            sprite.AddFrame(previousFrame);
        } else sprite.AddFrame(0);
    }

    void DeleteFrame() {
        if (clickPoint != null) {
            final int targetFrameIndex = GetClickFrame(clickPoint);
            sprite.DeleteFrame(targetFrameIndex);
        } else sprite.DeleteFrame(sprite.GetActiveFrameIndex());
    }

    void AddFrame() {
        sprite.AddFrame();
    }

    void SetClickPointToActiveFrame(int frameIndex) {
        if (sprite != null && frameIndex >= 0 && frameIndex < sprite.GetFrameCount()) {
            final float yRatio = (float) parent.GetPreviewHeight() / (float) sprite.GetHeight();
            final int scaledWidth = (int) (sprite.GetWidth() * yRatio * sprite.GetHorizontalPixelRatio());
            clickPoint = new Point(SEPARATOR_WIDTH + ((scaledWidth + SEPARATOR_WIDTH) * frameIndex) + (scaledWidth / 2), SEPARATOR_WIDTH + parent.GetPreviewHeight() / 2);
        }
    }

    TimelinePanelViewportViewPopup GetViewportPopup() {
        return timelinePanelViewportViewPopup;
    }

    private int GetClickFrame(Point clickPoint) {
        final float yRatio = (float) parent.GetPreviewHeight() / (float) sprite.GetHeight();
        final int scaledWidth = (int) (sprite.GetWidth() * yRatio * sprite.GetHorizontalPixelRatio());
        int frameX = SEPARATOR_WIDTH;
        int frame = 0;
        boolean found = false;
        while (!found && frame < sprite.GetFrameCount() && clickPoint != null) {
            if (clickPoint.x > frameX && clickPoint.x < frameX + scaledWidth && clickPoint.y > SEPARATOR_WIDTH &&
                    clickPoint.y < SEPARATOR_WIDTH + parent.GetPreviewHeight()) found = true;
            if (mouseDown && dragFrame > -1) {
                if (clickPoint.x > frameX - SEPARATOR_WIDTH && clickPoint.x < frameX + (scaledWidth / 2) &&
                        clickPoint.y > SEPARATOR_WIDTH && clickPoint.y < SEPARATOR_WIDTH + parent.GetPreviewHeight()) found = true;
                else {
                    if (frame != sprite.GetFrameCount() - 1) {
                        if (clickPoint.x >= frameX + (scaledWidth / 2) && clickPoint.x <= frameX + scaledWidth + SEPARATOR_WIDTH &&
                                clickPoint.y > SEPARATOR_WIDTH && clickPoint.y < SEPARATOR_WIDTH + parent.GetPreviewHeight()) {
                            found = true;
                        }
                    } else {
                        if (clickPoint.x >= frameX + (scaledWidth / 2) &&
                                clickPoint.y > SEPARATOR_WIDTH && clickPoint.y < SEPARATOR_WIDTH + parent.GetPreviewHeight()) {
                            found = true;
                        }
                    }
                }
            }
            if (!found) {
                frameX += SEPARATOR_WIDTH + scaledWidth;
                frame++;
            }
        }
        return found ? frame : -1;
    }

    private void initialiseComponents() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sprite != null) {
            Graphics2D g2 = (Graphics2D) g;
            int x = SEPARATOR_WIDTH;
            int y = SEPARATOR_WIDTH / 2;
            final float yRatio = (float) parent.GetPreviewHeight() / (float) sprite.GetHeight();
            final int scaledWidth = (int) (sprite.GetWidth() * yRatio * sprite.GetHorizontalPixelRatio());
            for (int i = 0; i < sprite.GetFrameCount(); i++) {
                BufferedImage frameImage = sprite.GetFrame(i) != null ? sprite.GetFrame(i).GetRenderedImage() : null;
                if (frameImage != null) {
                    if (!mouseDown) {
                        g2.drawImage(frameImage, x, y, scaledWidth, parent.GetPreviewHeight(), null);
                    } else {
                        if (dragFrame == -1 || frameImage != sprite.GetFrame(dragFrame).GetRenderedImage()) {
                            g2.drawImage(frameImage, x, y, scaledWidth, parent.GetPreviewHeight(), null);
                        }
                    }
                }
                g2.setColor(frameImage != null ? i == sprite.GetActiveFrameIndex() ? Color.BLUE : Color.BLACK : Color.RED);
                g2.setStroke(new BasicStroke(i == sprite.GetActiveFrameIndex() ? 2.0f : 1.0f));
                g2.drawRect(x - 1, y - 1, scaledWidth + 2, parent.GetPreviewHeight() + 2);
                x += scaledWidth + SEPARATOR_WIDTH;
            }
            if (mouseDown && dragFrame > -1) {
                final BufferedImage draggedImage = sprite.GetImage(dragFrame);
                final int xpos = dragPoint.x - (draggedImage.getWidth() / 2);
                final int ypos = dragPoint.y - (draggedImage.getHeight() / 2);
                g2.drawImage(draggedImage, xpos, ypos, scaledWidth, parent.GetPreviewHeight(), null);
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRect(xpos, ypos, scaledWidth, parent.GetPreviewHeight());
            }
        }
    }

    @Override
    public Dimension getMinimumSize() {
        int requiredWidth = 640, requiredHeight = parent.GetPreviewHeight() + (SEPARATOR_WIDTH * 2);
        if (sprite != null && sprite.GetFrameCount() > 0) {
            final float yRatio = (float) parent.GetPreviewHeight() / (float) sprite.GetHeight();
            final int scaledWidth = (int) (sprite.GetWidth() * yRatio);
            requiredWidth = SEPARATOR_WIDTH + (SEPARATOR_WIDTH * sprite.GetFrameCount()) +
                    (sprite.GetFrameCount() * scaledWidth);
        }
        return new Dimension(requiredWidth, requiredHeight);
    }

    @Override
    public Dimension getPreferredSize() { return getMinimumSize(); }

    @Override
    public Dimension getMaximumSize() { return getMinimumSize(); }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        clickPoint = new Point(e.getX(), e.getY());
        if (e.getButton() == 3) {
            timelinePanelViewportViewPopup.show(e.getComponent(), e.getX(), e.getY());
        } else {
            final int clickedFrame = GetClickFrame(clickPoint);
            if (clickedFrame > -1) {
                parent.GetMainFrame().SetActiveFrameIndex(clickedFrame);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mouseDown && dragFrame > -1) {
            final int releaseFrame = GetClickFrame(new Point(e.getX(), e.getY()));
            if (releaseFrame > -1 && releaseFrame != dragFrame) {
                System.out.println("Moving frame " + dragFrame + " to before frame " + releaseFrame);
                sprite.RecordHistory();
                sprite.MoveFrame(dragFrame, releaseFrame);
                sprite.SetActiveFrame(sprite.GetFrame(dragFrame));
                parent.GetMainFrame().RefreshPanels();
            }
        }
        mouseDown = false;
        dragFrame = -1;
        dragPoint = null;
        clickPoint = null;
        dragBuffer = 8;
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseDown = false;
        dragFrame = -1;
        dragPoint = null;
        clickPoint = null;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseDown) {
            if (dragBuffer > -1) dragBuffer--;
            if (dragBuffer == 0) {
                final int clickedFrame = GetClickFrame(clickPoint);
                dragFrame = clickedFrame;
            } else if (dragBuffer == -1) {
                if (dragFrame != -1) {
                    dragPoint = new Point(e.getX(), e.getY());
                    repaint();
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private final TimelinePanel parent;
    private final TimelinePanelViewportViewPopup timelinePanelViewportViewPopup;
    private BBCSprite sprite;
    final static int SEPARATOR_WIDTH = 24;
    private Point clickPoint, dragPoint;
    private boolean mouseDown = false;
    private int dragFrame = -1, dragBuffer;
}
