package com.plus.mevanspn.BBCSpriteEd;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

final public class BBCSpriteFrame {
    public BBCSpriteFrame(BBCSprite parent) {
        this.parent = parent;
        this.renderedImage = new BufferedImage(parent.GetWidth(), parent.GetHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    public BBCSpriteFrame(BBCSprite parent, DataInputStream dataInputStream) throws IOException {
        this.parent = parent;
        final int dataLength = parent.GetWidth() * parent.GetHeight();
        byte[] data = new byte[dataLength];
        dataInputStream.read(data);
        this.renderedImage = new BufferedImage(parent.GetWidth(), parent.GetHeight(), BufferedImage.TYPE_INT_ARGB);
        int i = 0;
        for (int y = 0; y < parent.GetHeight(); y++) {
            for (int x = 0; x < parent.GetWidth(); x++) {
                if (data[i] < this.parent.GetColours().length) this.renderedImage.setRGB(x, y, this.parent.GetColours()[data[i]].getRGB());
                i++;
            }
        }
    }

    public int GetWidth() {
        return parent.GetWidth();
    }

    public int GetHeight() {
        return parent.GetHeight();
    }

    public float GetHorizontalPixelRatio() {
        return parent.GetHorizontalPixelRatio();
    }

    public Color[] GetColours() {
        return parent.GetColours();
    }

    public void SetPixel(int x, int y, byte colourIndex) {
        if (parent != null && renderedImage != null ) {
            if (x >= 0 && x < GetWidth() && y >= 0 && y < GetHeight()) {
                final int colourRGB =
                        colourIndex < parent.GetColours().length ? parent.GetColours()[colourIndex].getRGB() : 0x000000FF;
                if (renderedImage.getRGB(x, y) != colourRGB) renderedImage.setRGB(x, y, colourRGB);
            }
        }
    }

    public void DrawRectangle(int left, int top, int width, int height, boolean filled, byte colourIndex) {
        if (parent != null && renderedImage != null && colourIndex < parent.GetColours().length &&
                left >= 0 && top >= 0 && width > 0 && height > 0) {
            width = width + 1;
            height = height + 1;
            if (left + width > GetWidth()) width = GetWidth() - left;
            if (top + height > GetHeight()) height = GetHeight() - top;
            Graphics2D g2 = (Graphics2D) renderedImage.getGraphics();
            g2.setColor(parent.GetColours()[colourIndex]);
            if (filled) g2.fillRect(left, top, width, height);
            else g2.drawRect(left, top, width, height);
        }
    }

    public void DrawLine(Point pointA, Point pointB, byte colourIndex) {
        if (parent != null && renderedImage != null && colourIndex < parent.GetColours().length && pointA != null && pointB != null) {
            Graphics2D g2 = (Graphics2D) renderedImage.getGraphics();
            g2.setColor(parent.GetColours()[colourIndex]);
            g2.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
        }
    }

    public void FloodFill(Point p, byte colourToUseIndex, int colourToReplace, boolean started) {
        if (parent != null && renderedImage != null && colourToUseIndex < parent.GetColours().length &&
                p.x >= 0 && p.x < GetWidth() && p.y >=0 && p.y < GetHeight()) {
            final int colourToUse = parent.GetColours()[colourToUseIndex].getRGB();

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

    public BufferedImage GetRenderedImage() {
        return renderedImage;
    }

    public void SetRenderedImage(BufferedImage newRenderedImage) {
        renderedImage = newRenderedImage;
    }

    public void WriteToStream(DataOutputStream dataOutputStream) throws IOException {
        if (dataOutputStream != null && renderedImage != null) {
            byte[] data = new byte[renderedImage.getWidth() * renderedImage.getHeight()];
            int i = 0;
            for (int y = 0; y < renderedImage.getHeight(); y++) {
                for (int x = 0; x < renderedImage.getWidth(); x++) {
                    data[i++] = parent.GetColourIndexForRGB(renderedImage.getRGB(x, y));
                }
            }
            dataOutputStream.write(data);
        }
    }

    private final BBCSprite parent;
    private BufferedImage renderedImage;
}
