package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.*;

final public class TimelinePanel extends JScrollPane {
    public TimelinePanel(MainFrame parent) {
        super();
        this.setPreferredSize(new Dimension(640, TimelinePanelViewportView.PREVIEW_HEIGHT_IN_PIXELS + TimelinePanelViewportView.SEPARATOR_WIDTH));
        this.parent = parent;
    }

    public void SetSprite(BBCSprite sprite) {
        this.viewportView = new TimelinePanelViewportView(sprite, this);
        this.setViewportView(this.viewportView);
        Refresh();
    }

    public void Refresh() {
        viewportView.revalidate();
        viewportView.repaint();
        revalidate();
    }

    private MainFrame parent;
    private TimelinePanelViewportView viewportView;

    final class TimelinePanelViewportView extends JPanel {
        public TimelinePanelViewportView(BBCSprite sprite, TimelinePanel parent) {
            super();
            this.parent = parent;
            this.sprite = sprite;
            revalidate();
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (sprite != null) {
                Graphics2D g2 = (Graphics2D) g;
                int x = SEPARATOR_WIDTH / 2;
                int y = SEPARATOR_WIDTH / 2;
                final float yRatio = (float) PREVIEW_HEIGHT_IN_PIXELS / (float) sprite.GetHeight();
                final int scaledWidth = (int) (sprite.GetWidth() * yRatio);
                for (int i = 0; i < sprite.GetFrameCount(); i++) {
                    g2.drawImage(sprite.GetFrame(i).GetRenderedImage(yRatio), x, y, scaledWidth, PREVIEW_HEIGHT_IN_PIXELS, null);
                    g2.setColor(i == sprite.GetCurrentFrameIndex() ? Color.BLUE : Color.BLACK);
                    g2.drawRect(x - 1, y - 1, scaledWidth + 2, PREVIEW_HEIGHT_IN_PIXELS + 2);
                    x += scaledWidth + (SEPARATOR_WIDTH / 2);
                }
            }
        }

        @Override
        public Dimension getMinimumSize() {
            int requiredWidth = 640, requiredHeight = PREVIEW_HEIGHT_IN_PIXELS + SEPARATOR_WIDTH;
            if (sprite != null && sprite.GetFrameCount() > 0) {
                final float yRatio = (float) PREVIEW_HEIGHT_IN_PIXELS / (float) sprite.GetHeight();
                final int scaledWidth = (int) (sprite.GetWidth() * yRatio);
                requiredWidth = SEPARATOR_WIDTH + (SEPARATOR_WIDTH * (sprite.GetFrameCount() - 1)) +
                        (sprite.GetFrameCount() * scaledWidth);
            }
            return new Dimension(requiredWidth, requiredHeight);
        }

        @Override
        public Dimension getPreferredSize() {
            return getMinimumSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getMinimumSize();
        }

        private TimelinePanel parent;
        private BBCSprite sprite;
        private final static int SEPARATOR_WIDTH = 32, PREVIEW_HEIGHT_IN_PIXELS = 64;
    }
}
