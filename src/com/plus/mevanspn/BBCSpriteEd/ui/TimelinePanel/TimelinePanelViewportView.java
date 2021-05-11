package com.plus.mevanspn.BBCSpriteEd.ui.TimelinePanel;

import com.plus.mevanspn.BBCSpriteEd.BBCSprite;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

    private void initialiseComponents() {
        this.addMouseListener(this);
    }

    public void SetSprite(BBCSprite sprite) {
        this.sprite = sprite;
        Refresh();
    }

    public void Refresh() {
        this.revalidate();
        this.repaint();
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

    void DuplicateFrame(boolean atEnd) {
        final int frameToDuplicate = clickPoint != null ? GetClickFrame() : sprite.GetCurrentFrameIndex();
        if (frameToDuplicate > -1) {
            sprite.DuplicateFrame(sprite.GetFrame(frameToDuplicate), atEnd);
        }
    }

    void InsertFrame() {
        if (clickPoint != null) {
            final int previousFrame = GetClickFrame();
            sprite.AddFrame(previousFrame + 1);
        } else sprite.AddFrame(sprite.GetCurrentFrameIndex() + 1);
    }

    void DeleteFrame() {
        if (clickPoint != null) {
            final int targetFrameIndex = GetClickFrame();
            sprite.DeleteFrame(targetFrameIndex);
        } else sprite.DeleteFrame(sprite.GetCurrentFrameIndex());
    }

    void AddFrame() {
        sprite.AddFrame();
    }

    TimelinePanelViewportViewPopup GetViewportPopup() {
        return timelinePanelViewportViewPopup;
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
                g2.setColor(frameImage != null ? i == sprite.GetCurrentFrameIndex() ? Color.BLUE : Color.BLACK : Color.RED);
                g2.setStroke(new BasicStroke(i == sprite.GetCurrentFrameIndex() ? 2.0f : 1.0f));
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
                parent.GetParent().SetActiveFrameIndex(clickedFrame);
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
    private TimelinePanelViewportViewPopup timelinePanelViewportViewPopup;
    private BBCSprite sprite;
    final static int SEPARATOR_WIDTH = 24;
    private Point clickPoint;


}
