package com.plus.mevanspn.BBCSpriteEd;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

final public class BBCSpriteFrame {
    public BBCSpriteFrame(BBCSprite parent) {
        this.parent = parent;
        final int dataLength = parent.GetWidth() * parent.GetHeight();
        this.data = new byte[dataLength];
        for (int i = 0; i < dataLength; i++) this.data[i] = (byte) parent.GetColours().length;
        this.renderedImage = new BufferedImage(parent.GetWidth(), parent.GetHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    public BBCSpriteFrame(BBCSprite parent, DataInputStream dataInputStream) throws IOException {
        this.parent = parent;
        final int dataLength = parent.GetWidth() * parent.GetHeight();
        this.data = new byte[dataLength];
        dataInputStream.read(this.data);
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

    public byte[] GetData() {
        return data;
    }

    public void SetPixel(int x, int y, byte colourIndex) {
        if (data != null) {
            if (x >= 0 && x < GetWidth() && y >= 0 && y < GetHeight()) {
                int offset = (y * GetWidth()) + x;
                if (data[offset] != colourIndex) {
                    data[offset] = colourIndex;
                    renderedImage.setRGB(x, y, colourIndex < parent.GetColours().length ? parent.GetColours()[colourIndex].getRGB() : 0x000000FF);
                }
            }
        }
    }

    public void DrawRectangle(int left, int top, int width, int height, boolean filled, byte colourIndex) {
        if (data != null && left >= 0 && top >= 0 && width > 0 && height > 0) {
            if (left + width > GetWidth()) width = GetWidth() - left;
            if (top + height > GetHeight()) height = GetHeight() - top;
            for (int y = top; y <= top + height; y++) {
                int offset = (y * GetWidth()) + left;
                for (int x = 0; x <= width; x++) {
                    if (filled || (!filled && (x == 0 || x == width || y == top || y == (top + height)))) {
                        data[offset] = colourIndex;
                        renderedImage.setRGB(left + x, y, parent.GetColours()[colourIndex].getRGB());
                    }
                    offset++;
                }
            }
        }
    }

    public void DrawLine(Point leftPoint, Point rightPoint, byte colourIndex) {
        if (data != null && leftPoint != null && rightPoint != null) {
            if (leftPoint.x > rightPoint.x) {
                Point tempPoint = leftPoint;
                leftPoint = rightPoint;
                rightPoint = tempPoint;
            }
            final int vDiff = 1 + (leftPoint.y > rightPoint.y ? leftPoint.y - rightPoint.y : rightPoint.y - leftPoint.y);
            final int hDiff = 1 + (rightPoint.x - leftPoint.x);
            final float gradient = vDiff > 1 ? hDiff / (float) vDiff : hDiff;
            final int vDir = leftPoint.y > rightPoint.y ? -1 : 1;
            int y = 0, lines = 0;
            float x = leftPoint.x;
            do {
                int offset = (leftPoint.y + y) * GetWidth() + (int) x;
                float nextX = x + gradient > leftPoint.x + hDiff ? (leftPoint.x + hDiff) - x : gradient;
                int drawToOffset = offset + (int) Math.ceil(nextX);
                do {
                    data[offset] = colourIndex;
                    renderedImage.setRGB(offset % GetWidth(), leftPoint.y + y, parent.GetColours()[colourIndex].getRGB());
                    if (offset < drawToOffset) offset++;
                } while (offset < drawToOffset);
                y += vDir;
                lines++;
                x += nextX;
            } while (x < leftPoint.x + hDiff && lines < vDiff);
        }
    }

    public void FloodFill(Point p, byte colourToUseIndex, byte colourToReplaceIndex) {
        if (p.x >= 0 && p.x < GetWidth() && p.y >=0 && p.y < GetHeight()) {
            final int pixelOffset = (p.y * GetWidth()) + p.x;
            if (colourToReplaceIndex == 127) colourToReplaceIndex = data[pixelOffset];
            else if (colourToReplaceIndex == colourToUseIndex || (colourToReplaceIndex != data[pixelOffset] && colourToUseIndex != data[pixelOffset])) return;

            final int startOfLineOffset = p.y * GetWidth();
            final int endOfLineOffset = startOfLineOffset + GetWidth();
            int offset = pixelOffset;
            int pixelAbove, pixelBelow;
            if (data[pixelOffset] == colourToReplaceIndex) {
                while (offset >= startOfLineOffset && data[offset] == colourToReplaceIndex) {
                    pixelAbove = offset - GetWidth();
                    pixelBelow = offset + GetWidth();
                    data[offset] = colourToUseIndex;
                    renderedImage.setRGB(p.x + (offset - pixelOffset), p.y, parent.GetColours()[colourToUseIndex].getRGB());
                    if (p.y > 0 && data[pixelAbove] == colourToReplaceIndex) FloodFill(new Point(p.x + (offset - pixelOffset), p.y - 1), colourToUseIndex, colourToReplaceIndex);
                    if (p.y < GetHeight() - 1 && data[pixelBelow] == colourToReplaceIndex) FloodFill(new Point(p.x + (offset - pixelOffset), p.y + 1), colourToUseIndex, colourToReplaceIndex);
                    offset--;
                }
                offset = pixelOffset + 1;
                while (offset < endOfLineOffset && data[offset] == colourToReplaceIndex) {
                    pixelAbove = offset - GetWidth();
                    pixelBelow = offset + GetWidth();
                    data[offset] = colourToUseIndex;
                    renderedImage.setRGB(p.x + (offset - pixelOffset), p.y, parent.GetColours()[colourToUseIndex].getRGB());
                    if (p.y > 0 && data[pixelAbove] == colourToReplaceIndex) FloodFill(new Point(p.x + (offset - pixelOffset), p.y - 1), colourToUseIndex, colourToReplaceIndex);
                    if (p.y < GetHeight() - 1 && data[pixelBelow] == colourToReplaceIndex) FloodFill(new Point(p.x + (offset - pixelOffset), p.y + 1), colourToUseIndex, colourToReplaceIndex);
                    offset++;
                }
            } else {
                if (p.y > 0) FloodFill(new Point(p.x , p.y - 1), colourToUseIndex, colourToReplaceIndex);
                if (p.y < GetHeight() - 1) FloodFill(new Point(p.x, p.y + 1), colourToUseIndex, colourToReplaceIndex);
            }
        }
    }

    public BufferedImage GetRenderedImage() {
        if (data != null) return renderedImage;
        else return null;
    }

    public void WriteToStream(DataOutputStream dataOutputStream) throws IOException {
        if (dataOutputStream != null) dataOutputStream.write(data);
    }

    private final byte[] data;
    private final BBCSprite parent;
    private final BufferedImage renderedImage;
}
