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

    public void FloodFill(Point p, byte colourToUseIndex, byte colourToReplace, boolean started) {
        if (bbcSprite != null && renderedImage != null && colourToUseIndex < bbcSprite.GetColours().length &&
                p.x >= 0 && p.x < GetWidth() && p.y >=0 && p.y < GetHeight()) {
            // final int colourToUse = bbcSprite.GetColours()[colourToUseIndex].getRGB();

            byte[] colourToReplaceArray = new byte[1];
            final byte[] colourToUseArray = new byte[] { colourToUseIndex };

            if (!started) {
                renderedImage.getRaster().getDataElements(p.x, p.y, colourToReplaceArray);
                if (colourToReplaceArray[0] == colourToUseIndex) return;
                else {
                    colourToReplace = colourToReplaceArray[0];
                    started = true;
                }
            }

            if (pixelMatchesColourToReplace(p.x, p.y, colourToReplace)) {
                for (int x = p.x; x >= 0 && pixelMatchesColourToReplace(x, p.y, colourToReplace); x--) {
                    renderedImage.getRaster().setDataElements(x, p.y, colourToUseArray);
                    // renderedImage.setRGB(x, p.y, colourToUse);
                    if (p.y > 0 && pixelMatchesColourToReplace(x, p.y - 1, colourToReplace)) {
                        FloodFill(new Point(x, p.y - 1), colourToUseIndex, colourToReplace, started);
                    }
                    if (p.y < renderedImage.getHeight() - 1 && pixelMatchesColourToReplace(x, p.y + 1, colourToReplace)) {
                        FloodFill(new Point(x, p.y + 1), colourToUseIndex, colourToReplace, started);
                    }
                }
                if (p.x < renderedImage.getWidth() - 1) {
                    for (int x = p.x + 1; x < renderedImage.getWidth() && pixelMatchesColourToReplace(x, p.y, colourToReplace); x++) {
                        //renderedImage.setRGB(x, p.y, colourToUse);
                        renderedImage.getRaster().setDataElements(x, p.y, colourToUseArray);
                        if (p.y > 0 && pixelMatchesColourToReplace(x, p.y - 1, colourToReplace)) {
                            FloodFill(new Point(x, p.y - 1), colourToUseIndex, colourToReplace, started);
                        }
                        if (p.y < renderedImage.getHeight() - 1 && pixelMatchesColourToReplace(x, p.y + 1, colourToReplace)) {
                            FloodFill(new Point(x, p.y + 1), colourToUseIndex, colourToReplace, started);
                        }
                    }
                }
            } else if (pixelMatchesColourToReplace(p.x, p.y, colourToUseIndex)){
                if (p.y > 0) FloodFill(new Point(p.x , p.y - 1), colourToUseIndex, colourToReplace, started);
                if (p.y < GetHeight() - 1) FloodFill(new Point(p.x, p.y + 1), colourToUseIndex, colourToReplace, started);
            }
        }
    }

    private boolean pixelMatchesColourToReplace(int x, int y, byte colourToReplace) {
        byte[] pixelColourIndex = new byte[1];
        renderedImage.getRaster().getDataElements(x, y, pixelColourIndex);
        return pixelColourIndex[0] == colourToReplace;
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
                    while (n < rawDataLength && n < i + 64 && rawData[n] == sampleByte) n++;
                    writeCompressBlockHeader((byte) 1, (byte) (n - i), byteArrayOutputStream);
                } else if (sampleByte < sprite.GetColours().length) {
                    n = i;
                    byte packedSample = 0;
                    int occurances = 0;
                    try {
                        packedSample = getPackedByte(rawData, n, bpp);
                        while (n < rawDataLength && occurances < 64 && getPackedByte(rawData, n, bpp) == packedSample) {
                            n += 8 / bpp;
                            occurances++;
                        }
                    } catch (PartByteValueException ex) { }
                    if (occurances > 1) {
                        writeCompressBlockHeader((byte) 3, (byte) occurances, byteArrayOutputStream);
                        byteArrayOutputStream.write(packedSample);
                    } else {
                        n = i;
                        ByteArrayOutputStream rawPackedBOS = new ByteArrayOutputStream();
                        while (n < rawDataLength && n < i + 64 && rawData[n] != bbcSprite.GetColours().length &&
                                (n == i || rawData[n] != sampleByte)) {
                            byte value = 0;
                            for (int shift = 8 - bpp; shift >= 0 && n < rawDataLength && n < i + 64; shift -= bpp) {
                                if (rawData[n] == bbcSprite.GetColours().length) break;
                                value += rawData[n++] << shift;
                            }
                            if (n < rawDataLength && rawData[n] == sampleByte) n--;
                            else rawPackedBOS.write(value);
                        }
                        rawPackedBOS.flush();
                        writeCompressBlockHeader((byte) 2, (byte) (n - i), byteArrayOutputStream);
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

    private void writeCompressBlockHeader(byte code, byte value, ByteArrayOutputStream bos) {
        if (bos != null) {
            bos.write((value << 2) + code);
        }
    }
    private byte getPackedByte(byte[] data, int index, int bpp) throws PartByteValueException {
        byte value = 0;
        for (int shift = 8 - bpp; shift >= 0; shift -= bpp) {
            if (shift >=0 && index == data.length) throw new PartByteValueException(value);
            if (data[index] == bbcSprite.GetColours().length) throw new PartByteValueException(value);
            else value += data[index++] << shift;
        }
        return value;
    }

    private final BBCSprite bbcSprite;
    private BBCImage renderedImage;
}
