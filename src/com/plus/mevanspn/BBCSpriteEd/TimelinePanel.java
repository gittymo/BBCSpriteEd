package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

final public class TimelinePanel extends JScrollPane {
    public TimelinePanel(MainFrame parent) {
        super();
        initialiseComponents();
        this.parent = parent;
    }

    private void initialiseComponents() {
        this.setPreferredSize(new Dimension(640, TimelinePanelViewportView.PREVIEW_HEIGHT_IN_PIXELS + (TimelinePanelViewportView.SEPARATOR_WIDTH * 2)));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // Initialise the menu items used by both the application menu bar and the timeline.
        duplicateFrame = new JMenuItem("Duplicate Frame");
        duplicateFrameEnd = new JMenuItem("Duplicate (At End)");
        insertFrame = new JMenuItem("Insert Frame (after)");
        deleteFrame = new JMenuItem("Delete Frame");
        duplicateFrame.addActionListener(e -> getActiveViewportView().DuplicateFrame(false));
        duplicateFrameEnd.addActionListener(e -> getActiveViewportView().DuplicateFrame(true));
        insertFrame.addActionListener(e -> getActiveViewportView().InsertFrame());
        deleteFrame.addActionListener(e -> getActiveViewportView().DeleteFrame());
        timelineMenuSeperator = new JSeparator();
    }

    public void SetSprite(BBCSprite sprite) {
        this.viewportView = new TimelinePanelViewportView(sprite, this);
        this.setViewportView(this.viewportView);
        Refresh();
    }

    private TimelinePanelViewportView getActiveViewportView() {
        return viewportView;
    }

    public void Refresh() {
        if (viewportView != null) {
            viewportView.revalidate();
            viewportView.repaint();
            revalidate();
        }
    }

    private final MainFrame parent;
    private TimelinePanelViewportView viewportView;
    JMenuItem duplicateFrame, duplicateFrameEnd, insertFrame, deleteFrame;
    JSeparator timelineMenuSeperator;

    final class TimelinePanelViewportView extends JPanel implements MouseListener {
        public TimelinePanelViewportView(BBCSprite sprite, TimelinePanel parent) {
            super();
            this.parent = parent;
            this.sprite = sprite;
            this.clickPoint = null;
            initialiseComponents();
            revalidate();
            repaint();
        }

        private void initialiseComponents() {
            this.addMouseListener(this);
            resetAnimationMenuItems();
        }

        private void resetAnimationMenuItems() {
            // Get the animation menu from the main application menu bar.
            final MainFrame mainFrame = parent.parent;
            final MainFrameMenuBar mainFrameMenuBar = mainFrame.GetMenuBar();
            final AnimationMenu animationMenu = mainFrameMenuBar.GetAnimationMenu();

            if (animationMenu != null) {
                animationMenu.add(timelineMenuSeperator);
                animationMenu.add(duplicateFrame);
                animationMenu.add(duplicateFrameEnd);
                animationMenu.add(insertFrame);
                animationMenu.add(deleteFrame);
            }
            clickPoint = null;
        }

        private int GetClickFrame() {
            final float yRatio = (float) PREVIEW_HEIGHT_IN_PIXELS / (float) sprite.GetHeight();
            final int scaledWidth = (int) (sprite.GetWidth() * yRatio * sprite.GetHorizontalPixelRatio());
            int frameX = SEPARATOR_WIDTH;
            int frame = 0;
            boolean found = false;
            while (!found && frame < sprite.GetFrameCount() && clickPoint != null) {
                if (clickPoint.x > frameX && clickPoint.x < frameX + scaledWidth && clickPoint.y > SEPARATOR_WIDTH &&
                    clickPoint.y < SEPARATOR_WIDTH + PREVIEW_HEIGHT_IN_PIXELS) found = true;
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
                    BufferedImage frameImage = sprite.GetFrame(i) != null ? sprite.GetFrame(i).GetRenderedImage() : null;
                    if (frameImage != null) {
                        g2.drawImage(frameImage, x, y, scaledWidth, PREVIEW_HEIGHT_IN_PIXELS, null);
                    }
                    g2.setColor(frameImage != null ? i == sprite.GetCurrentFrameIndex() ? Color.BLUE : Color.BLACK : Color.RED);
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
        public Dimension getPreferredSize() { return getMinimumSize(); }

        @Override
        public Dimension getMaximumSize() { return getMinimumSize(); }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 3) {
                TimelinePanelViewportViewPopup popup = new TimelinePanelViewportViewPopup(this, e);
                popup.show(e.getComponent(), e.getX(), e.getY());
            } else {
                clickPoint = new Point(e.getX(), e.getY());
                final int clickedFrame = GetClickFrame();
                if (clickedFrame > -1) {
                    parent.parent.SetActiveFrameIndex(clickedFrame);
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
        private final BBCSprite sprite;
        private final static int SEPARATOR_WIDTH = 24, PREVIEW_HEIGHT_IN_PIXELS = 64;
        private Point clickPoint;

        final class TimelinePanelViewportViewPopup extends JPopupMenu {
            public TimelinePanelViewportViewPopup(TimelinePanelViewportView parent, MouseEvent me) {
                parent.clickPoint = new Point(me.getX(), me.getY());
                add(duplicateFrame);
                add(duplicateFrameEnd);
                add(insertFrame);
                add(deleteFrame);

                this.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) { }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        parent.resetAnimationMenuItems();
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) { }
                });
            }
        }
    }
}
