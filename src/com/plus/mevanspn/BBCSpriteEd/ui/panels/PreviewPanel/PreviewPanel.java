package com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel;

import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.PreviewPanelToolbar.PreviewPanelToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

final public class PreviewPanel extends JPanel {
    public PreviewPanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        this.frame = 0;
        this.zoom = 2;
        this.frameRotaterThread = new FrameRotaterThread(this);
        this.frameRotaterThread.start();
        this.previewPanelToolbar = new PreviewPanelToolbar(this);
        this.setBorder(new EmptyBorder(PADDING,PADDING,PADDING,PADDING));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mainFrame != null) {
            Rectangle usableArea = getBounds();
            BBCSprite sprite = mainFrame.GetSprite();
            final int zoomedWidth = (int) (sprite.GetWidth() * sprite.GetHorizontalPixelRatio() * zoom);
            final int zoomedHeight = (int) (sprite.GetHeight() * zoom);

            if (sprite != null && sprite.GetFrameCount() > 0) {
                Graphics2D g2 = (Graphics2D) g;
                final int displayWidth = zoomedWidth + PADDING;
                final int displayHeight = zoomedHeight + PADDING;
                final int displayX = (usableArea.width - displayWidth) / 2;
                final int displayY = (usableArea.height - displayHeight) / 2;
                g2.setColor(Color.GRAY);
                g2.fillRect(displayX, displayY, displayWidth, displayHeight);
                g2.setColor(Color.BLACK);
                g2.drawRect(displayX, displayY, displayWidth, displayHeight);
                BufferedImage frameImage = sprite.GetFrame(frame) != null ? sprite.GetFrame(frame).GetRenderedImage() : null;
                if (frameImage != null) g2.drawImage(frameImage, displayX + (PADDING / 2), displayY + (PADDING / 2), zoomedWidth, zoomedHeight, null);
            }
        }
    }

    public void Refresh() {
        revalidate();
        repaint();
    }

    public void RotateFrames() {
        if (mainFrame != null && mainFrame.GetSprite() != null && mainFrame.GetSprite().GetFrameCount() > 0) {
            frame = (frame + 1) % mainFrame.GetSprite().GetFrameCount();
            revalidate();
            repaint();
        }
    }

    public void SetFrame(int frame) {
        if (frame >= 0 && mainFrame != null && mainFrame.GetSprite() != null && frame < mainFrame.GetSprite().GetFrameCount()) this.frame = frame;
    }

    public void Stop() {
        ToEnd();
        frameRotaterThread.Stop();
        RotateFrames();
    }

    public void Pause() {
        frameRotaterThread.Pause();
    }

    public void ToStart() {
        SetFrame(0);
    }

    public void ToEnd() {
        SetFrame(mainFrame.GetSprite().GetFrameCount() - 1);
    }

    public void Play() {
        frameRotaterThread.Play();
    }

    public void SetZoom(float zoom) {
        this.zoom = zoom;
        revalidate();
        repaint();
    }

    public PreviewPanelToolbar GetToolbar() {
        return previewPanelToolbar;
    }

    @Override
    public Dimension getPreferredSize() {
        if (mainFrame != null) {
            BBCSprite sprite = mainFrame.GetSprite();
            if (sprite != null) {
                final int zoomedWidth = (int) (sprite.GetWidth() * sprite.GetHorizontalPixelRatio() * zoom);
                final int zoomedHeight = (int) (sprite.GetHeight() * zoom);
                final int displayWidth = zoomedWidth + PADDING;
                final int displayHeight = zoomedHeight + PADDING;
                return new Dimension(displayWidth + (PADDING * 2), displayHeight + (PADDING * 2));
            } else return new Dimension(PADDING, PADDING);
        } else return new Dimension(PADDING, PADDING);
    }

    private final MainFrame mainFrame;
    private float zoom;
    private int frame;
    private final FrameRotaterThread frameRotaterThread;
    private PreviewPanelToolbar previewPanelToolbar;

    private static final int PADDING = 32;
}
