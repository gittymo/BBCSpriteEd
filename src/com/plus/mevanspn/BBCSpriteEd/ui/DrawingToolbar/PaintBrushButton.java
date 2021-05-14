package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.LinkedList;

final public class PaintBrushButton extends DrawingToolbarButton implements MouseListener {
    public PaintBrushButton(DrawingToolbar drawingToolbar) {
        super("img/paintbrush.png","Paint using shapes and preselected areas of the image.", drawingToolbar);
        Reset();
        this.addMouseListener(this);
    }

    public void CreateBrush(BBCImage sourceImage, Rectangle sourceArea) {
        sourceArea.width++;
        sourceArea.height++;
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

    final class PaintBrushButtonMenuItem extends JMenuItem {
        public PaintBrushButtonMenuItem(BufferedImage brushImage) {
            super();
            BufferedImage brushIconImage = new BufferedImage(24, 24, brushImage.getType(), (IndexColorModel) brushImage.getColorModel());
            final float scaleX = 24 / (float) brushImage.getWidth();
            final float scaleY = 24 / (float) brushImage.getHeight();
            int scaledWidth, scaledHeight;
            if (scaleY < scaleX) {
                scaledWidth = (int) (brushImage.getWidth() * scaleY);
                scaledHeight = (int) (brushImage.getHeight() * scaleY);
            } else {
                scaledWidth =(int) (brushImage.getWidth() * scaleX);
                scaledHeight = (int) (brushImage.getHeight() * scaleY);
            }
            final int xpos = (24 - scaledWidth) / 2;
            final int ypos = (24 - scaledHeight) / 2;
            brushIconImage.getGraphics().drawImage(brushImage, xpos, ypos, scaledWidth, scaledHeight, null);
            this.setIcon(new ImageIcon(brushIconImage));
            this.brushImage = brushImage;
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SetActiveBrush(GetBrushImage());
                }
            });
        }

        BufferedImage GetBrushImage() {
            return brushImage;
        }

        private BufferedImage brushImage;
    }
}
