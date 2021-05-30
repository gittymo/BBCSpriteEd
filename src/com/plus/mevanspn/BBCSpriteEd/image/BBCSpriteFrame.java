package com.plus.mevanspn.BBCSpriteEd.image;

import java.io.*;

final public class BBCSpriteFrame {
    public BBCSpriteFrame(BBCSprite bbcSprite) {
        this.bbcSprite = bbcSprite;
        this.renderedImage = new BBCImage(this);
    }

    public BBCSpriteFrame(BBCSprite bbcSprite, DataInputStream dataInputStream) throws IOException {
        this.bbcSprite = bbcSprite;
        final int dataLength = bbcSprite.GetWidth() * bbcSprite.GetHeight();
        byte[] data = new byte[dataLength];
        dataInputStream.read(data);
        this.renderedImage = new BBCImage(this);
        this.renderedImage.getRaster().setDataElements(0, 0, GetWidth(), GetHeight(), data);
    }

    public BBCSpriteFrame(BBCSpriteFrame originalSpriteFrame) {
        this(originalSpriteFrame.GetSprite());
        this.renderedImage = new BBCImage(originalSpriteFrame.renderedImage);
    }

    public BBCSprite GetSprite() {
        return bbcSprite;
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

    public BBCImage GetRenderedImage() {
        return renderedImage;
    }

    public void UpdateColourModel() {
        this.renderedImage = new BBCImage(this.renderedImage, BBCColour.GenerateIndexColorModel(bbcSprite.GetColours()));
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
            if (index == data.length) throw new PartByteValueException(value);
            if (data[index] == bbcSprite.GetColours().length) throw new PartByteValueException(value);
            else value += data[index++] << shift;
        }
        return value;
    }

    private final BBCSprite bbcSprite;
    private BBCImage renderedImage;
}
