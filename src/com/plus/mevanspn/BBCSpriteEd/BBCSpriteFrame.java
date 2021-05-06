package com.plus.mevanspn.BBCSpriteEd;

import java.awt.*;
import java.awt.image.*;

final public class BBCSpriteFrame {
    public BBCSpriteFrame(BBCSprite parent) {
        this.parent = parent;
        final int dataLength = parent.GetWidth() * parent.GetHeight();
        this.data = new byte[dataLength];
        for (int i = 0; i < dataLength; i++) this.data[i] = (byte) parent.GetColours().length;
    }

    public BBCSprite GetParent() {
        return this.parent;
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
            for (int y = top; y < top + height; y++) {
                int offset = (y * GetWidth()) + left;
                for (int x = 0; x < width; x++) {
                    if (filled || (!filled && (x == 0 || x == width - 1))) data[offset] = colourIndex;
                    offset++;
                }
            }
        }
    }

    public void DrawLine(Point leftPoint, Point rightPoint, byte colourIndex) {
        if (data != null) {
            if (leftPoint.x > rightPoint.x) {
                Point tempPoint = leftPoint;
                leftPoint = rightPoint;
                rightPoint = tempPoint;
            }
            final int yDistance = leftPoint.y > rightPoint.y ? leftPoint.y - rightPoint.y : rightPoint.y - leftPoint.y;
            final float gradient = (rightPoint.x - leftPoint.x) / (float) yDistance;
            final int yDir = leftPoint.y > rightPoint.y ? -1 : 1;
            System.out.println("Drawing line from " + leftPoint.toString() + " to " + rightPoint.toString());
            System.out.println("Gradient = " + gradient + ", vdir = " + yDir);
            float nextX = 0;
            int intNextX = 0, lastIntNextX = 0;
            int offset = (leftPoint.y * GetWidth()) + leftPoint.x, endOffset = 0;
            for (int i = 0; i <= yDistance; i++) {
                lastIntNextX = intNextX;
                nextX += gradient;
                intNextX = (int) nextX;
                endOffset = offset + intNextX - lastIntNextX;
                while (offset < data.length && offset < endOffset) {
                    data[offset++] = colourIndex;
                }
                offset += GetWidth() * yDir;
            }
        }
    }

    public void FloodFill(Point p, byte colourToUseIndex, byte colourToReplaceIndex) {
        if (p.x >= 0 && p.x < GetWidth() && p.y >=0 && p.y < GetHeight()) {
            final int pixelOffset = (p.y * GetWidth()) + p.x;
            final int startOfLineOffset = p.y * GetWidth();
            final int endOfLineOffset = startOfLineOffset + GetWidth();
            if (colourToReplaceIndex == 127) colourToReplaceIndex = data[pixelOffset];
            else if (colourToReplaceIndex != data[pixelOffset] && colourToUseIndex != data[pixelOffset]) return;
            if (colourToReplaceIndex != colourToUseIndex) {
                int offset = pixelOffset;
                while (offset >= startOfLineOffset && data[offset] == colourToReplaceIndex) {
                    data[offset] = colourToUseIndex;
                    if (p.y > 0) FloodFill(new Point(p.x + (offset - pixelOffset), p.y - 1), colourToUseIndex, colourToReplaceIndex);
                    if (p.y < GetHeight() - 1) FloodFill(new Point(p.x + (offset - pixelOffset), p.y + 1), colourToUseIndex, colourToReplaceIndex);
                    offset--;
                }
                offset = pixelOffset + 1;
                while (offset < endOfLineOffset && data[offset] == colourToReplaceIndex) {
                    data[offset] = colourToUseIndex;
                    if (p.y > 0) FloodFill(new Point(p.x + (offset - pixelOffset), p.y - 1), colourToUseIndex, colourToReplaceIndex);
                    if (p.y < GetHeight() - 1) FloodFill(new Point(p.x + (offset - pixelOffset), p.y + 1), colourToUseIndex, colourToReplaceIndex);
                    offset++;
                }
            } else {
                if (p.y > 0) FloodFill(new Point(p.x , p.y - 1), colourToUseIndex, colourToReplaceIndex);
                if (p.y < GetHeight() - 1) FloodFill(new Point(p.x, p.y + 1), colourToUseIndex, colourToReplaceIndex);
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

    private byte[] data;
    private BBCSprite parent;
}
