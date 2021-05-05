package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

final public class TimelinePanel extends JScrollPane {
    public TimelinePanel(MainFrame parent) {
        super();
        this.setPreferredSize(new Dimension(640, TimelinePanelViewportView.PREVIEW_HEIGHT_IN_PIXELS + TimelinePanelViewportView.SEPARATOR_WIDTH));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
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

    final class TimelinePanelViewportView extends JPanel implements MouseListener {
        public TimelinePanelViewportView(BBCSprite sprite, TimelinePanel parent) {
            super();
            this.parent = parent;
            this.sprite = sprite;
            this.addMouseListener(this);
            revalidate();
            repaint();
        }

        void DuplicateFrame(MouseEvent e) {
            final float yRatio = (float) PREVIEW_HEIGHT_IN_PIXELS / (float) sprite.GetHeight();
            final int scaledWidth = (int) (sprite.GetWidth() * yRatio);
            final int halfSep = SEPARATOR_WIDTH / 2;
            final int frameToDuplicate = (e.getX() - halfSep) / (scaledWidth + halfSep);
            if (frameToDuplicate < sprite.GetFrameCount()) {
                sprite.DuplicateFrame(sprite.GetFrame(frameToDuplicate));
                parent.parent.UpdateTimeline();
                parent.parent.RefreshImagePane();
            }
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
                    g2.drawImage(sprite.GetFrame(i).GetRenderedImage(), x, y, scaledWidth, PREVIEW_HEIGHT_IN_PIXELS, null);
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

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == 3) {
                TimelinePanelViewportViewPopup popup = new TimelinePanelViewportViewPopup(this, e);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        private TimelinePanel parent;
        private BBCSprite sprite;
        private final static int SEPARATOR_WIDTH = 32, PREVIEW_HEIGHT_IN_PIXELS = 64;

        final class TimelinePanelViewportViewPopup extends JPopupMenu {
            public TimelinePanelViewportViewPopup(TimelinePanelViewportView parent, MouseEvent me) {
                this.parent = parent;
                this.duplicateFrame = new JMenuItem("Duplicate Frame");
                this.duplicateFrame.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parent.DuplicateFrame(me);
                    }
                });
                add(duplicateFrame);
            }

            private JMenuItem duplicateFrame;
            private TimelinePanelViewportView parent;
        }
    }
}
