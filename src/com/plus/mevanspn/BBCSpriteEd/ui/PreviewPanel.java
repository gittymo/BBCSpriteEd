package com.plus.mevanspn.BBCSpriteEd.ui;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

final public class PreviewPanel extends JPanel {
    public PreviewPanel(MainFrame parent) {
        super();
        this.parent = parent;
        this.frame = 0;
        this.zoom = 2;
        this.frameRotaterThread = new FrameRotaterThread(this);
        this.frameRotaterThread.start();
        this.setBorder(new EmptyBorder(PADDING,PADDING,PADDING,PADDING));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (parent != null) {
            Rectangle usableArea = getBounds();
            BBCSprite sprite = parent.GetSprite();
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
                BufferedImage frameImage = sprite.GetFrame(frame).GetRenderedImage();
                if (frameImage != null) g2.drawImage(sprite.GetFrame(frame).GetRenderedImage(), displayX + (PADDING / 2), displayY + (PADDING / 2), zoomedWidth, zoomedHeight, null);
            }
        }
    }

    public void RotateFrames() {
        if (parent != null && parent.GetSprite() != null && parent.GetSprite().GetFrameCount() > 0) {
            frame = (frame + 1) % parent.GetSprite().GetFrameCount();
            revalidate();
            repaint();
        }
    }

    public void SetFrame(int frame) {
        if (frame >= 0 && parent != null && parent.GetSprite() != null && frame < parent.GetSprite().GetFrameCount()) this.frame = frame;
    }

    @Override
    public Dimension getPreferredSize() {
        if (parent != null) {
            Rectangle usableArea = getBounds();
            BBCSprite sprite = parent.GetSprite();
            if (sprite != null) {
                final int zoomedWidth = (int) (sprite.GetWidth() * zoom);
                final int zoomedHeight = (int) (sprite.GetHeight() * zoom);
                final int displayWidth = zoomedWidth + PADDING;
                final int displayHeight = zoomedHeight + PADDING;
                return new Dimension(displayWidth + (PADDING * 2), displayHeight + (PADDING * 2));
            } else return new Dimension(PADDING, PADDING);
        } else return new Dimension(PADDING, PADDING);
    }

    private MainFrame parent;
    private float zoom;
    private int frame;
    private FrameRotaterThread frameRotaterThread;

    private static int PADDING = 32;


    final class FrameRotaterThread extends Thread {
        public FrameRotaterThread(PreviewPanel parent) {
            this.parent = parent;
        }

        public void run() {
            while (!killed) {
                try {
                    sleep(1000 / fps);
                    parent.RotateFrames();
                } catch (Exception ex) { }
            }
        }

        public void SetFPS(int fps) {
            this.fps = fps;
        }

        private boolean killed = false;
        private int fps = 10;
        private PreviewPanel parent;
    }
}
