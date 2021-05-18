package com.plus.mevanspn.BBCSpriteEd.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

final public class BBCSpriteFrame {
    public BBCSpriteFrame(BBCSprite bbcSprite) {
        this.bbcSprite = bbcSprite;
        this.renderedImage = new BBCImage(bbcSprite);
    }

    public BBCSpriteFrame(BBCSprite bbcSprite, DataInputStream dataInputStream) throws IOException {
        this.bbcSprite = bbcSprite;
        final int dataLength = bbcSprite.GetWidth() * bbcSprite.GetHeight();
        byte[] data = new byte[dataLength];
        dataInputStream.read(data);
        this.renderedImage = new BBCImage(bbcSprite);
        this.renderedImage.getRaster().setDataElements(0, 0, GetWidth(), GetHeight(), data);
    }

    public BBCSpriteFrame(BBCSpriteFrame originalSpriteFrame) {
        this(originalSpriteFrame.GetSprite());
        this.renderedImage = new BBCImage(originalSpriteFrame.renderedImage);
    }

    public int GetWidth() {
        return bbcSprite.GetWidth();
    }

    public int GetHeight() {
        return bbcSprite.GetHeight();
    }

    public float GetHorizontalPixelRatio() {
        return bbcSprite.GetHorizontalPixelRatio();
    }

    public BBCColour[] GetColours() {
        return bbcSprite.GetColours();
    }

    public void SetPixel(int x, int y, byte colourIndex) {
        if (bbcSprite != null && renderedImage != null ) {
            if (x >= 0 && x < GetWidth() && y >= 0 && y < GetHeight()) {
                final int colourRGB =
                        colourIndex < bbcSprite.GetColours().length ? bbcSprite.GetColours()[colourIndex].getRGB() : 0x000000FF;
                if (renderedImage.getRGB(x, y) != colourRGB) renderedImage.setRGB(x, y, colourRGB);
            }
        }
    }

    public void DrawRectangle(int left, int top, int width, int height, boolean filled, byte colourIndex) {
        if (bbcSprite != null && renderedImage != null && colourIndex < bbcSprite.GetColours().length &&
                width >= 0 && height >= 0) {
            if (left + width > GetWidth()) width = GetWidth() - left;
            if (top + height > GetHeight()) height = GetHeight() - top;
            Graphics2D g2 = (Graphics2D) renderedImage.getGraphics();
            g2.setColor(bbcSprite.GetColours()[colourIndex]);
            if (filled) g2.fillRect(left, top, width + 1, height + 1);
            else g2.drawRect(left, top, width, height);
        }
    }

    public void DrawLine(Point pointA, Point pointB, byte colourIndex) {
        if (bbcSprite != null && renderedImage != null && colourIndex < bbcSprite.GetColours().length && pointA != null && pointB != null) {
            Graphics2D g2 = (Graphics2D) renderedImage.getGraphics();
            g2.setColor(bbcSprite.GetColours()[colourIndex]);
            g2.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
        }
    }

    public void PaintImage(BufferedImage image, Point origin) {
        if (image != null && origin != null) {
            final int imageX = origin.x - (image.getWidth() > 1 ? image.getWidth() / 2 : 0);
            final int imageY = origin.y - (image.getHeight() > 1 ? image.getHeight() / 2 : 0);
            renderedImage.getGraphics().drawImage(image, imageX, imageY, null);
        }
    }

    public void FloodFill(Point p, byte colourToUseIndex, int colourToReplace, boolean started) {
        if (bbcSprite != null && renderedImage != null && colourToUseIndex < bbcSprite.GetColours().length &&
                p.x >= 0 && p.x < GetWidth() && p.y >=0 && p.y < GetHeight()) {
            final int colourToUse = bbcSprite.GetColours()[colourToUseIndex].getRGB();

            if (!started) {
                colourToReplace = renderedImage.getRGB(p.x, p.y);
                if (colourToReplace == colourToUseIndex) return;
                started = true;
            }

            if (colourToReplace == renderedImage.getRGB(p.x, p.y)) {
                for (int x = p.x; x >= 0 && renderedImage.getRGB(x, p.y) == colourToReplace; x--) {
                    renderedImage.setRGB(x, p.y, colourToUse);
                    if (p.y > 0 && renderedImage.getRGB(x, p.y - 1) == colourToReplace) {
                        FloodFill(new Point(x, p.y - 1), colourToUseIndex, colourToReplace, started);
                    }
                    if (p.y < renderedImage.getHeight() - 1 && renderedImage.getRGB(x, p.y + 1) == colourToReplace) {
                        FloodFill(new Point(x, p.y + 1), colourToUseIndex, colourToReplace, started);
                    }
                }
                if (p.x < renderedImage.getWidth() - 1) {
                    for (int x = p.x + 1; x < renderedImage.getWidth() && renderedImage.getRGB(x, p.y) == colourToReplace; x++) {
                        renderedImage.setRGB(x, p.y, colourToUse);
                        if (p.y > 0 && renderedImage.getRGB(x, p.y - 1) == colourToReplace) {
                            FloodFill(new Point(x, p.y - 1), colourToUseIndex, colourToReplace, started);
                        }
                        if (p.y < renderedImage.getHeight() - 1 && renderedImage.getRGB(x, p.y + 1) == colourToReplace) {
                            FloodFill(new Point(x, p.y + 1), colourToUseIndex, colourToReplace, started);
                        }
                    }
                }
            } else if (colourToUse == renderedImage.getRGB(p.x, p.y)){
                if (p.y > 0) FloodFill(new Point(p.x , p.y - 1), colourToUseIndex, colourToReplace, started);
                if (p.y < GetHeight() - 1) FloodFill(new Point(p.x, p.y + 1), colourToUseIndex, colourToReplace, started);
            }
        }
    }

    public BBCImage GetRenderedImage() {
        return renderedImage;
    }

    public void UpdateColourModel() {
        this.renderedImage = new BBCImage(this.renderedImage, BBCColour.GenerateIndexColourModel(bbcSprite.GetColours()));
    }

    public void SetRenderedImage(BBCImage newRenderedImage) {
        renderedImage = newRenderedImage;
        GetSprite().GetMainFrame().RefreshPanels();
    }

    public void WriteToStream(DataOutputStream dataOutputStream) throws IOException {
        if (dataOutputStream != null && renderedImage != null) {
            byte[] data = new byte[GetWidth() * GetHeight()];
            renderedImage.getRaster().getDataElements(0, 0, GetWidth(), GetHeight(), data);
            dataOutputStream.write(data);
        }
    }

    public BBCSprite GetSprite() {
        return bbcSprite;
    }

    public byte[] GetCompressedData() throws IOException {
        BBCSprite sprite = GetSprite();
        byte[] bytePackedData = null;
        if (sprite != null && GetWidth() > 0 && GetHeight() > 0) {
            final int rawDataLength = GetWidth() * GetHeight();
            final int bpp = sprite.GetDisplayMode().GetBitsPerPixel();
            byte[] rawData = new byte[rawDataLength];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GetRenderedImage().getRaster().getDataElements(0, 0, GetWidth(), GetHeight(), rawData);
            int i = 0, n = 0;
            while (i < rawDataLength) {
                final byte sampleByte = rawData[i];
                if (sampleByte == sprite.GetColours().length) {
                    n = i;
                    while (n < rawDataLength && n < i + 256 && rawData[n] == sampleByte) n++;
                    byteArrayOutputStream.write(1);
                    byteArrayOutputStream.write(n - i);
                } else if (sampleByte < sprite.GetColours().length) {
                    n = i;
                    byte packedSample = getPackedByte(rawData, n, bpp);
                    int occurances = 0;
                    while (n < rawDataLength && occurances < 256 && getPackedByte(rawData, n, bpp) == packedSample) {
                        n += 8 / bpp;
                        occurances++;
                    }
                    if (occurances > 1) {
                        byteArrayOutputStream.write(3);
                        byteArrayOutputStream.write(occurances);
                        byteArrayOutputStream.write(packedSample);
                    } else {
                        n = i;
                        ByteArrayOutputStream rawPackedBOS = new ByteArrayOutputStream();
                        while (n < rawDataLength && n < i + 256 && (n == i || rawData[n] != sampleByte)) {
                            byte value = 0;
                            for (int shift = 8 - bpp; shift >= 0 && n < rawDataLength && n < i + 256; shift -= bpp) {
                                value += rawData[n++] << shift;
                            }
                            if (n < rawDataLength && rawData[n] == sampleByte) n--;
                            else rawPackedBOS.write(value);
                        }
                        rawPackedBOS.flush();
                        byteArrayOutputStream.write(2);
                        byteArrayOutputStream.write(n - i);
                        byteArrayOutputStream.write(rawPackedBOS.toByteArray());
                        rawPackedBOS.close();
                    }
                }
                i = n;
            }
            bytePackedData = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        }

        return bytePackedData;
    }

    private byte getPackedByte(byte[] data, int index, int bpp) {
        byte value = 0;
        for (int shift = 8 - bpp; shift >= 0 && index < data.length; shift -= bpp) value += data[index++] << shift;
        return value;
    }

    private final BBCSprite bbcSprite;
    private BBCImage renderedImage;
}
