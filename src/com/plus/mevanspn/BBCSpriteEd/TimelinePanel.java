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
        this.setPreferredSize(new Dimension(640, TimelinePanelViewportView.PREVIEW_HEIGHT_IN_PIXELS + (TimelinePanelViewportView.SEPARATOR_WIDTH * 2)));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.parent = parent;
    }

    public void SetSprite(BBCSprite sprite) {
        this.viewportView = new TimelinePanelViewportView(sprite, this);
        this.setViewportView(this.viewportView);
        Refresh();
    }

    public void Refresh() {
        if (viewportView != null) {
            viewportView.revalidate();
            viewportView.repaint();
            revalidate();
        }
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

        private int GetClickFrame(MouseEvent e) {
            final float yRatio = (float) PREVIEW_HEIGHT_IN_PIXELS / (float) sprite.GetHeight();
            final int scaledWidth = (int) (sprite.GetWidth() * yRatio * sprite.GetHorizontalPixelRatio());
            int frameX = SEPARATOR_WIDTH;
            int frame = 0;
            boolean found = false;
            while (!found && frame < sprite.GetFrameCount()) {
                if (e.getX() > frameX && e.getX() < frameX + scaledWidth && e.getY() > SEPARATOR_WIDTH &&
                    e.getY() < SEPARATOR_WIDTH + PREVIEW_HEIGHT_IN_PIXELS) found = true;
                if (!found) {
                    frameX += SEPARATOR_WIDTH + scaledWidth;
                    frame++;
                }
            }
            return found ? frame : -1;
        }

        void DuplicateFrame(MouseEvent e, boolean atEnd) {
            final int frameToDuplicate = GetClickFrame(e);
            if (frameToDuplicate > -1) {
                sprite.DuplicateFrame(sprite.GetFrame(frameToDuplicate), atEnd);
            }
        }

        void InsertFrame(MouseEvent e) {
            final int previousFrame = GetClickFrame(e);
            sprite.AddFrame(previousFrame + 1);
        }

        void DeleteFrame(MouseEvent e) {
            final int targetFrameIndex = GetClickFrame(e);
            sprite.DeleteFrame(targetFrameIndex);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (sprite != null) {
                Graphics2D g2 = (Graphics2D) g;
                int x = SEPARATOR_WIDTH;
                int y = SEPARATOR_WIDTH / 2;
                final float yRatio = (float) PREVIEW_HEIGHT_IN_PIXELS / (float) sprite.GetHeight();
                final int scaledWidth = (int) (sprite.GetWidth() * yRatio * sprite.GetHorizontalPixelRatio());
                for (int i = 0; i < sprite.GetFrameCount(); i++) {
                    g2.drawImage(sprite.GetFrame(i).GetRenderedImage(), x, y, scaledWidth, PREVIEW_HEIGHT_IN_PIXELS, null);
                    g2.setColor(i == sprite.GetCurrentFrameIndex() ? Color.BLUE : Color.BLACK);
                    g2.setStroke(new BasicStroke(i == sprite.GetCurrentFrameIndex() ? 2.0f : 1.0f));
                    g2.drawRect(x - 1, y - 1, scaledWidth + 2, PREVIEW_HEIGHT_IN_PIXELS + 2);
                    x += scaledWidth + SEPARATOR_WIDTH;
                }
            }
        }

        @Override
        public Dimension getMinimumSize() {
            int requiredWidth = 640, requiredHeight = PREVIEW_HEIGHT_IN_PIXELS + (SEPARATOR_WIDTH * 2);
            if (sprite != null && sprite.GetFrameCount() > 0) {
                final float yRatio = (float) PREVIEW_HEIGHT_IN_PIXELS / (float) sprite.GetHeight();
                final int scaledWidth = (int) (sprite.GetWidth() * yRatio);
                requiredWidth = SEPARATOR_WIDTH + (SEPARATOR_WIDTH * sprite.GetFrameCount()) +
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
            if (e.getButton() == 3) {
                TimelinePanelViewportViewPopup popup = new TimelinePanelViewportViewPopup(this, e);
                popup.show(e.getComponent(), e.getX(), e.getY());
            } else {
                final int clickedFrame = GetClickFrame(e);
                if (clickedFrame > -1) {
                    parent.parent.SetActiveFrameIndex(clickedFrame);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

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
        private final static int SEPARATOR_WIDTH = 24, PREVIEW_HEIGHT_IN_PIXELS = 64;

        final class TimelinePanelViewportViewPopup extends JPopupMenu {
            public TimelinePanelViewportViewPopup(TimelinePanelViewportView parent, MouseEvent me) {
                this.parent = parent;
                this.duplicateFrame = new JMenuItem("Duplicate Frame");
                this.duplicateFrame.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parent.DuplicateFrame(me, false);
                    }
                });
                add(duplicateFrame);
                this.duplicateFrameEnd = new JMenuItem("Duplicate (At End)");
                this.duplicateFrameEnd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parent.DuplicateFrame(me, true);
                    }
                });
                add(duplicateFrameEnd);
                this.insertFrame = new JMenuItem("Insert Frame (after)");
                this.insertFrame.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parent.InsertFrame(me);
                    }
                });
                add(insertFrame);
                this.deleteFrame = new JMenuItem("Delete Frame");
                this.deleteFrame.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parent.DeleteFrame(me);
                    }
                });
                add(deleteFrame);
            }

            private JMenuItem duplicateFrame;
            private JMenuItem duplicateFrameEnd;
            private JMenuItem insertFrame;
            private JMenuItem deleteFrame;

            private TimelinePanelViewportView parent;
        }
    }
}
