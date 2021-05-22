package com.plus.mevanspn.BBCSpriteEd.image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.Arrays;

final public class BBCImage extends BufferedImage {
    public BBCImage(BBCSpriteFrame bbcSpriteFrame) {
        super(bbcSpriteFrame.GetWidth(), bbcSpriteFrame.GetHeight(), BBCImage.TYPE_BYTE_INDEXED, BBCColour.GenerateIndexColourModel(bbcSpriteFrame.GetSprite().GetColours()));
        this.bbcSpriteFrame = bbcSpriteFrame;
        makeTransparent();
    }

    public BBCImage(int width, int height, BBCSpriteFrame bbcSpriteFrame) {
        super(width, height, BBCImage.TYPE_BYTE_INDEXED, BBCColour.GenerateIndexColourModel(bbcSpriteFrame.GetSprite().GetColours()));
        this.bbcSpriteFrame = bbcSpriteFrame;
        makeTransparent();
    }

    public BBCImage(BBCImage bbcImage, IndexColorModel newColourModel) {
        super(bbcImage.getWidth(), bbcImage.getHeight(), bbcImage.getType(), newColourModel);
        WritableRaster wr = bbcImage.getRaster();
        byte[] dataElements = new byte[bbcImage.getWidth() * bbcImage.getHeight()];
        wr.getDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), dataElements);
        this.getRaster().setDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), dataElements);
        this.bbcSpriteFrame = bbcImage.GetSpriteFrame();
    }

    public BBCImage(BBCImage originalImage) {
        this(originalImage, originalImage.GetSprite().GetColourModel());
    }

    public BufferedImage GetOnionSkinImage(boolean past) {
        final BufferedImage onionSkinImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        byte[] sourceDataElements = new byte[getWidth() * getHeight()];
        getRaster().getDataElements(0, 0, getWidth(), getHeight(), sourceDataElements);
        int[] destRGBElements = new int[getWidth() * getHeight()];
        for (int i = 0; i < sourceDataElements.length; i++) {
            final int colourIndex = sourceDataElements[i];
            if (colourIndex < GetSprite().GetColours().length) {
                final int luminance = BBCColour.GetLuminance(GetSprite().GetColours()[colourIndex]);
                destRGBElements[i] = new Color(past ? luminance : 0, !past ? luminance : 0, 0).getRGB();
            }
        }
        onionSkinImage.setRGB(0, 0,getWidth(),getHeight(), destRGBElements, 0, getWidth());
        return onionSkinImage;
    }

    public BBCSprite GetSprite() {
        return bbcSpriteFrame.GetSprite();
    }

    public BBCSpriteFrame GetSpriteFrame() {
        return bbcSpriteFrame;
    }

    public void SetPixel(int x, int y, byte colourIndex) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            byte[] pixelData = new byte[1];
            getRaster().getDataElements(x, y, pixelData);
            if (pixelData[0] != colourIndex) {
                pixelData[0] = colourIndex;
                getRaster().setDataElements(x, y, pixelData);
            }
        }
    }

    public void DrawRectangle(int left, int top, int width, int height, boolean filled, byte colourIndex) {
        if (colourIndex < bbcSpriteFrame.GetColours().length && width > 0 && height > 0) {
            if (left + width > getWidth()) width = getWidth() - left;
            if (top + height > getHeight()) height = getHeight() - top;
            Graphics2D g2 = (Graphics2D) getGraphics();
            g2.setColor(bbcSpriteFrame.GetSprite().GetColours()[colourIndex]);
            if (filled) g2.fillRect(left, top, width, height);
            else g2.drawRect(left, top, width - 1, height - 1);
        }
    }

    public void DrawOval(int left, int top, int width, int height, boolean filled, byte colourIndex) {
        if (colourIndex < bbcSpriteFrame.GetColours().length && width >=0 && height >= 0) {
            if (left + width > getWidth()) width = getWidth() - left;
            if (top + height > getHeight()) height = getHeight() - top;
            Graphics2D g2 = (Graphics2D) getGraphics();
            g2.setColor(bbcSpriteFrame.GetSprite().GetColours()[colourIndex]);
            if (filled) g2.fillOval(left, top, width - 1, height - 1);
            g2.drawOval(left, top, width - 1, height - 1);
        }
    }

    public void DrawLine(Point pointA, Point pointB, byte colourIndex) {
        if (colourIndex < bbcSpriteFrame.GetColours().length && pointA != null && pointB != null) {
            Graphics2D g2 = (Graphics2D) getGraphics();
            g2.setColor(bbcSpriteFrame.GetSprite().GetColours()[colourIndex]);
            g2.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
        }
    }

    public void PaintImage(BufferedImage image, Point origin) {
        if (image != null && origin != null) {
            int xOffset = (int) Math.round(image.getWidth() / 2.0);
            int yOffset = (int) Math.round(image.getHeight() / 2.0);
            final int imageX = origin.x - xOffset;
            final int imageY = origin.y - yOffset;
            getGraphics().drawImage(image, imageX, imageY, null);
        }
    }

    public void FloodFill(Point p, byte colourToUseIndex, byte colourToReplace, boolean started) {
        if (colourToUseIndex < bbcSpriteFrame.GetColours().length && p.x >= 0 && p.x < getWidth() && p.y >=0 && p.y < getHeight()) {
            // final int colourToUse = bbcSprite.GetColours()[colourToUseIndex].getRGB();

            byte[] colourToReplaceArray = new byte[1];
            final byte[] colourToUseArray = new byte[] { colourToUseIndex };

            if (!started) {
                getRaster().getDataElements(p.x, p.y, colourToReplaceArray);
                if (colourToReplaceArray[0] == colourToUseIndex) return;
                else {
                    colourToReplace = colourToReplaceArray[0];
                    started = true;
                }
            }

            if (pixelMatchesColourToReplace(p.x, p.y, colourToReplace)) {
                for (int x = p.x; x >= 0 && pixelMatchesColourToReplace(x, p.y, colourToReplace); x--) {
                    getRaster().setDataElements(x, p.y, colourToUseArray);
                    // renderedImage.setRGB(x, p.y, colourToUse);
                    if (p.y > 0 && pixelMatchesColourToReplace(x, p.y - 1, colourToReplace)) {
                        FloodFill(new Point(x, p.y - 1), colourToUseIndex, colourToReplace, started);
                    }
                    if (p.y < getHeight() - 1 && pixelMatchesColourToReplace(x, p.y + 1, colourToReplace)) {
                        FloodFill(new Point(x, p.y + 1), colourToUseIndex, colourToReplace, started);
                    }
                }
                if (p.x < getWidth() - 1) {
                    for (int x = p.x + 1; x < getWidth() && pixelMatchesColourToReplace(x, p.y, colourToReplace); x++) {
                        //renderedImage.setRGB(x, p.y, colourToUse);
                        getRaster().setDataElements(x, p.y, colourToUseArray);
                        if (p.y > 0 && pixelMatchesColourToReplace(x, p.y - 1, colourToReplace)) {
                            FloodFill(new Point(x, p.y - 1), colourToUseIndex, colourToReplace, started);
                        }
                        if (p.y < getHeight() - 1 && pixelMatchesColourToReplace(x, p.y + 1, colourToReplace)) {
                            FloodFill(new Point(x, p.y + 1), colourToUseIndex, colourToReplace, started);
                        }
                    }
                }
            } else if (pixelMatchesColourToReplace(p.x, p.y, colourToUseIndex)){
                if (p.y > 0) FloodFill(new Point(p.x , p.y - 1), colourToUseIndex, colourToReplace, started);
                if (p.y < getHeight() - 1) FloodFill(new Point(p.x, p.y + 1), colourToUseIndex, colourToReplace, started);
            }
        }
    }

    public void Rotate(double degrees) {
        if (degrees < 0) {
            while (degrees < 0) degrees += 360;
        } else if (degrees >= 360) {
            while (degrees >= 360) degrees -= 360;
        }
        BBCImage newFrameImage = new BBCImage(this.GetSpriteFrame());
        final int originX = newFrameImage.getWidth() / 2;
        final int originY = newFrameImage.getHeight() / 2;
        final double rotation = Math.toRadians(degrees);
        final AffineTransform tx = AffineTransform.getRotateInstance(rotation, originX, originY);
        final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        newFrameImage.getGraphics().drawImage(op.filter(this, null), 0, 0, null);
        this.GetSpriteFrame().SetRenderedImage(newFrameImage);
    }

    private boolean pixelMatchesColourToReplace(int x, int y, byte colourToReplace) {
        byte[] pixelColourIndex = new byte[1];
        getRaster().getDataElements(x, y, pixelColourIndex);
        return pixelColourIndex[0] == colourToReplace;
    }

    private void makeTransparent() {
        WritableRaster wr = this.getRaster();
        byte[] dataElements = new byte[getWidth() * getHeight()];
        wr.getDataElements(0, 0, getWidth(), getHeight(), dataElements);
        Arrays.fill(dataElements, (byte) bbcSpriteFrame.GetColours().length);
        wr.setDataElements(0, 0, getWidth(), getHeight(), dataElements);
    }

    private final BBCSpriteFrame bbcSpriteFrame;
}
