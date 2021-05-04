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

    public void SetPixel(int x, int y, byte colourIndex) {
        if (data != null) {
            if (x >= 0 && x < GetWidth() && y >= 0 && y < GetHeight()) {
                int offset = (y * GetWidth()) + x;
                if (data[offset] != colourIndex) data[offset] = colourIndex;
            }
        }
    }

    public BufferedImage GetRenderedImage(float zoom) {
        if (data != null) {
            if (zoom < 1) zoom = 1.0f;
            final int width = GetWidth();
            final int height = GetHeight();
            final Color[] palette = GetColours();
            final Color[] maskColours = GetParent().GetParent().maskColours;
            final int zoomedWidth = (int) (width * zoom);
            final int zoomedHeight = (int) (height * zoom);

            BufferedImage render = new BufferedImage(zoomedWidth, zoomedHeight, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2 = (Graphics2D) render.getGraphics();
            int mo = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final int zoomX = (int) (x * zoom);
                    final int zoomY = (int) (y * zoom);
                    final int offset = (y * width) + x;
                    if (zoom >= 2) {
                        if (data[offset] < palette.length) {
                            g2.setColor(palette[data[offset]]);
                            g2.fillRect(zoomX, zoomY, (int) zoom, (int) zoom);
                        } else {
                            final int halfZoom = (int) zoom / 2;
                            for (int my = zoomY; my < (int) (zoomY + zoom); my += halfZoom) {
                                for (int mx = zoomX; mx < (int) (zoomX + zoom); mx += halfZoom) {
                                    g2.setColor(maskColours[mo]);
                                    g2.fillRect(mx, my, halfZoom, halfZoom);
                                    mo = (mo + 1) % maskColours.length;
                                }
                                mo = (mo + 1) % maskColours.length;
                            }
                        }
                    } else {
                        Color pixelColour = data[offset] < palette.length ? palette[data[offset]] : maskColours[0];
                        render.setRGB(zoomX, zoomY, pixelColour.getRGB());
                    }
                }
            }
            return render;
        } else return null;
    }

    private byte[] data;
    private BBCSprite parent;
}
