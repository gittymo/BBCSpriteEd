package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.LinkedList;

final public class PaintBrushButton extends DrawingToolbarButton implements MouseListener {
    public PaintBrushButton(DrawingToolbar drawingToolbar) {
        super("paintbrush.png",
                "Paint using shapes and preselected areas of the image (Key: P, G to toggle capture when tool active).",
                drawingToolbar, new KeyPressEventMatcher('P'));
        Reset();
        this.addMouseListener(this);
    }

    public void CreateBrush(BBCImage sourceImage, Rectangle sourceArea) {
        BufferedImage brushImage = new BufferedImage(sourceArea.width, sourceArea.height, sourceImage.getType(), sourceImage.GetSprite().GetColourModel());
        WritableRaster sourceImageRaster = sourceImage.getRaster();
        byte[] sourceAreaSamples = new byte[sourceArea.width * sourceArea.height];
        sourceImageRaster.getDataElements(sourceArea.x, sourceArea.y, sourceArea.width, sourceArea.height, sourceAreaSamples);
        WritableRaster brushImageRaster = brushImage.getRaster();
        brushImageRaster.setDataElements(0, 0, sourceArea.width, sourceArea.height, sourceAreaSamples);
        brushImages.add(brushImage);
        createNewMenuItem(brushImage);
        SetMode(PaintBrushButton.DRAWING_MODE);
    }

    public int GetMode() {
        return brushMode;
    }

    public void SetMode(int brushMode) {
        this.brushMode = brushMode;
    }

    public void Reset() {
        this.brushImages = new LinkedList<>();
        this.brushPopupMenu = new JPopupMenu();
        this.activeBrush = null;
        this.brushMode = CAPTURE_MODE;
    }

    public BufferedImage GetActiveBrush() {
        return activeBrush;
    }

    void SetActiveBrush(BufferedImage activeBrush) {
        this.activeBrush = activeBrush;
        SetMode(PaintBrushButton.DRAWING_MODE);
        this.drawingToolbar.SetActiveButton(this);
    }

    private void createNewMenuItem(BufferedImage brushImage) {
        JMenuItem brushMenuItem = new PaintBrushButtonMenuItem(brushImage);
        brushPopupMenu.add(brushMenuItem);
        activeBrush = brushImage;
    }

    private LinkedList<BufferedImage> brushImages;
    private JPopupMenu brushPopupMenu;
    private BufferedImage activeBrush;
    private int brushMode;

    public static int CAPTURE_MODE=0;
    public static int DRAWING_MODE=1;

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3) {
            brushPopupMenu.show(this, e.getX(), e.getY());
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

    @Override
    public void KeyPressed(KeyEvent keyEvent) {
        super.KeyPressed(keyEvent);
        final char keyChar = keyEvent.getKeyChar();
        if (GetDrawingToolbar().IsActiveButton(this)) {
            if (keyChar == 'g' || keyChar == 'G') {
                if (GetActiveBrush() != null) {
                    GetDrawingToolbar().GetMainFrame().RefreshPanels();
                    SetMode(GetMode() == PaintBrushButton.CAPTURE_MODE ? PaintBrushButton.DRAWING_MODE : PaintBrushButton.CAPTURE_MODE);
                } else SetMode(PaintBrushButton.CAPTURE_MODE);
            }
        }
    }

    final class PaintBrushButtonMenuItem extends JMenuItem {
        public PaintBrushButtonMenuItem(BufferedImage brushImage) {
            super();
            this.brushImage = brushImage;
            this.addActionListener(e -> SetActiveBrush(GetBrushImage()));
        }

        @Override
        public Dimension getPreferredSize() { return new Dimension(24, 24); }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            final float scaleX = 24 / (float) brushImage.getWidth();
            final float scaleY = 24 / (float) brushImage.getHeight();
            int scaledWidth, scaledHeight;
            if (scaleY < scaleX) {
                scaledWidth = (int) (brushImage.getWidth() * scaleY);
            } else {
                scaledWidth =(int) (brushImage.getWidth() * scaleX);
            }
            scaledHeight = (int) (brushImage.getHeight() * scaleY);
            final int xPos = (24 - scaledWidth) / 2;
            final int yPos = (24 - scaledHeight) / 2;
            g2.drawImage(brushImage, xPos, yPos, scaledWidth, scaledWidth, null);
        }

        BufferedImage GetBrushImage() {
            return brushImage;
        }

        private final BufferedImage brushImage;
    }
}
