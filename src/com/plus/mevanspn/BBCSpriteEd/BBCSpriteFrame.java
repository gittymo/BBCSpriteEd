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
    }

    public BBCSpriteFrame(BBCSprite parent, DataInputStream dataInputStream) throws IOException {
        this.parent = parent;
        final int dataLength = parent.GetWidth() * parent.GetHeight();
        this.data = new byte[dataLength];
        dataInputStream.read(this.data);
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
                if (data[offset] != colourIndex) data[offset] = colourIndex;
            }
        }
    }

    public void DrawRectangle(int left, int top, int width, int height, boolean filled, byte colourIndex) {
        if (data != null && left > 0 && top > 0 && width > 0 && height > 0) {
            if (left + width > GetWidth()) width = GetWidth() - left;
            if (top + height > GetHeight()) height = GetHeight() - top;
            for (int y = top; y <= top + height; y++) {
                int offset = (y * GetWidth()) + left;
                for (int x = 0; x <= width; x++) {
                    if (filled || (!filled && (x == 0 || x == width || y == top || y == (top + height)))) {
                        data[offset] = colourIndex;
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
                    if (offset < drawToOffset) offset++;
                } while (offset < drawToOffset);
                y += vDir;
                lines++;
                x += nextX;
            } while (x < leftPoint.x + hDiff && lines < vDiff);
        }
    }

    public void FloodFill(Point p, byte colourToUseIndex, byte colourToReplaceIndex, boolean border) {
        if (p.x >= 0 && p.x < GetWidth() && p.y >=0 && p.y < GetHeight()) {
            final int pixelOffset = (p.y * GetWidth()) + p.x;
            final int startOfLineOffset = p.y * GetWidth();
            final int endOfLineOffset = startOfLineOffset + GetWidth();
            final int endOfImageOffset = GetWidth() * GetHeight();
            if (data[pixelOffset] == colourToUseIndex && !border) return;
            if (colourToReplaceIndex == 127) colourToReplaceIndex = data[pixelOffset];
            else if (colourToReplaceIndex != data[pixelOffset] && colourToUseIndex != data[pixelOffset]) return;
            int offset = pixelOffset;
            final int pixelAbove = offset - GetWidth();
            final int pixelBelow = offset + GetWidth();
            if (colourToReplaceIndex != colourToUseIndex && !border) {
                while (offset >= startOfLineOffset && data[offset] == colourToReplaceIndex) {
                    data[offset] = colourToUseIndex;
                    if (p.y > 0 && pixelAbove > 0 && data[pixelAbove] == colourToReplaceIndex) FloodFill(new Point(p.x + (offset - pixelOffset), p.y - 1), colourToUseIndex, colourToReplaceIndex, false);
                    if (p.y < GetHeight() - 1 && pixelBelow < endOfImageOffset && data[pixelBelow] == colourToReplaceIndex) FloodFill(new Point(p.x + (offset - pixelOffset), p.y + 1), colourToUseIndex, colourToReplaceIndex, false);
                    offset--;
                }
                offset = pixelOffset + 1;
                while (offset < endOfLineOffset && data[offset] == colourToReplaceIndex) {
                    data[offset] = colourToUseIndex;
                    if (p.y > 0 && pixelAbove > 0 && data[pixelAbove] == colourToReplaceIndex) FloodFill(new Point(p.x + (offset - pixelOffset), p.y - 1), colourToUseIndex, colourToReplaceIndex, false);
                    if (p.y < GetHeight() - 1 && pixelBelow < endOfImageOffset && data[pixelBelow] == colourToReplaceIndex) FloodFill(new Point(p.x + (offset - pixelOffset), p.y + 1), colourToUseIndex, colourToReplaceIndex, false);
                    offset++;
                }
            } else {
                if (p.y > 0 && pixelAbove > 0 && data[pixelAbove] == colourToReplaceIndex) FloodFill(new Point(p.x , p.y - 1), colourToUseIndex, colourToReplaceIndex, true);
                if (p.y < GetHeight() - 1 && pixelBelow < endOfImageOffset && data[pixelBelow] == colourToReplaceIndex) FloodFill(new Point(p.x, p.y + 1), colourToUseIndex, colourToReplaceIndex, true);
            }
        }
    }

    public BufferedImage GetRenderedImage() {
        if (data != null) {
            final int width = GetWidth();
            final int height = GetHeight();
            final Color[] palette = GetColours();
            BufferedImage render = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final int offset = (y * width) + x;
                    final int paletteIndex = data[offset];
                    if (paletteIndex < palette.length) render.setRGB(x, y, palette[paletteIndex].getRGB());
                }
            }
            return render;
        } else return null;
    }

    public void WriteToStream(DataOutputStream dataOutputStream) throws IOException {
        if (dataOutputStream != null) dataOutputStream.write(data);
    }

    private final byte[] data;
    private final BBCSprite parent;
}
