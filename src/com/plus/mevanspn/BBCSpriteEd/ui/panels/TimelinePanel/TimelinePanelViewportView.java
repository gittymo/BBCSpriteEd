package com.plus.mevanspn.BBCSpriteEd.ui.panels.TimelinePanel;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public final class TimelinePanelViewportView extends JPanel implements MouseListener {
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
        final int frameToDuplicate = clickPoint != null ? GetClickFrame() : sprite.GetActiveFrameIndex();
        if (frameToDuplicate > -1) {
            sprite.DuplicateFrame(sprite.GetFrame(frameToDuplicate), atEnd);
        }
    }

    void InsertFrameAfter() {
        if (clickPoint != null) {
            final int previousFrame = GetClickFrame();
            sprite.AddFrame(previousFrame + 1);
        } else sprite.AddFrame(sprite.GetActiveFrameIndex() + 1);
    }

    void InsertFrameBefore() {
        if (clickPoint != null) {
            final int previousFrame = GetClickFrame();
            sprite.AddFrame(previousFrame);
        } else sprite.AddFrame(0);
    }

    void DeleteFrame() {
        if (clickPoint != null) {
            final int targetFrameIndex = GetClickFrame();
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

    private int GetClickFrame() {
        final float yRatio = (float) parent.GetPreviewHeight() / (float) sprite.GetHeight();
        final int scaledWidth = (int) (sprite.GetWidth() * yRatio * sprite.GetHorizontalPixelRatio());
        int frameX = SEPARATOR_WIDTH;
        int frame = 0;
        boolean found = false;
        while (!found && frame < sprite.GetFrameCount() && clickPoint != null) {
            if (clickPoint.x > frameX && clickPoint.x < frameX + scaledWidth && clickPoint.y > SEPARATOR_WIDTH &&
                    clickPoint.y < SEPARATOR_WIDTH + parent.GetPreviewHeight()) found = true;
            if (!found) {
                frameX += SEPARATOR_WIDTH + scaledWidth;
                frame++;
            }
        }
        return found ? frame : -1;
    }

    private void initialiseComponents() {
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
                    g2.drawImage(frameImage, x, y, scaledWidth, parent.GetPreviewHeight(), null);
                }
                g2.setColor(frameImage != null ? i == sprite.GetActiveFrameIndex() ? Color.BLUE : Color.BLACK : Color.RED);
                g2.setStroke(new BasicStroke(i == sprite.GetActiveFrameIndex() ? 2.0f : 1.0f));
                g2.drawRect(x - 1, y - 1, scaledWidth + 2, parent.GetPreviewHeight() + 2);
                x += scaledWidth + SEPARATOR_WIDTH;
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
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3) {
            this.clickPoint = new Point(e.getX(), e.getY());
            timelinePanelViewportViewPopup.show(e.getComponent(), e.getX(), e.getY());
        } else {
            clickPoint = new Point(e.getX(), e.getY());
            final int clickedFrame = GetClickFrame();
            if (clickedFrame > -1) {
                parent.GetMainFrame().SetActiveFrameIndex(clickedFrame);
            }
            clickPoint = null;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    private final TimelinePanel parent;
    private final TimelinePanelViewportViewPopup timelinePanelViewportViewPopup;
    private BBCSprite sprite;
    final static int SEPARATOR_WIDTH = 24;
    private Point clickPoint;


}
