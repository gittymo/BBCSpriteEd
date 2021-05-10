package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

import java.awt.image.*;

public class RawImageSamples {
    public RawImageSamples(BufferedImage sourceImage) throws InvalidSourceImageException {
        this.getRawImageSamples(sourceImage, 0, 0, sourceImage.getWidth(), sourceImage.getHeight());
    }

    public RawImageSamples(BufferedImage sourceImage, int x, int y, int width, int height) throws InvalidSourceImageException {
        this.getRawImageSamples(sourceImage, x, y, width, height);
    }

    private void getRawImageSamples(BufferedImage sourceImage, int x, int y, int width, int height) throws InvalidSourceImageException {
        if (sourceImage.getType() != BufferedImage.TYPE_INT_ARGB) throw new InvalidSourceImageException("Can only accept images of type INT_ARGB");
        if (width < 0) {
            width = -width;
            x -= width;
        }
        if (height < 0) {
            height = -height;
            y -= height;
        }
        if (x + width > sourceImage.getWidth()) width = sourceImage.getWidth() - x;
        if (y + height > sourceImage.getHeight()) height = sourceImage.getHeight() - y;
        if (x < 0 || x > sourceImage.getWidth()) throw new InvalidSourceImageException("Region is outside of horizontal image bounds.");
        if (y < 0 || y > sourceImage.getHeight()) throw new InvalidSourceImageException("Region is outside of vertical image bounds.");
        this.width = width;
        this.height = height;
        this.dataBuffer = new DataBufferInt(this.width * this.height);
        this.data = (int[]) sourceImage.getSampleModel().getDataElements(0, 0, this.width, this.height, null, this.dataBuffer);
    }

    public int[] GetSampleArea(int x, int y, int width, int height) {
        if (width < 0) {
            width = -width;
            x -= width;
        }
        if (height < 0) {
            height = -height;
            y -= height;
        }
        if (x + width > this.width) width = this.width - x;
        if (y + height > this.height) height = this.height - y;
        if (x < 0 || x > this.width || y < 0 || y > this.height) return null;
        int[] samples = new int[width * height];
        int si = 0;
        int offset = (this.width * y) + x;
        for (int v = 0; v < height; v++) {
            for (int h = 0; h < width; h++) samples[si++] = this.data[offset++];
            offset += width;
        }
        return samples;
    }

    public void SetSampleArea(int x, int y, int width, int height, int[] samples) {
        if (width < 0) {
            width = -width;
            x -= width;
        }
        if (height < 0) {
            height = -height;
            y -= height;
        }
        if (x + width > this.width) width = this.width - x;
        if (y + height > this.height) height = this.height - y;
        if (x < 0 || x > this.width || y < 0 || y > this.height) return;
        if (samples.length != width * height) return;
        int si = 0;
        int offset = (this.width * y) + x;
        for (int v = 0; v < height; v++) {
            for (int h = 0; h < width; h++) this.data[offset++] = samples[si++];
            offset += width;
        }
    }

    int[] data;
    private DataBufferInt dataBuffer;
    int width, height;
}
