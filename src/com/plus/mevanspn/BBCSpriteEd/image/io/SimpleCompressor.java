package com.plus.mevanspn.BBCSpriteEd.image.io;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;

import java.io.*;

public class SimpleCompressor {
    public SimpleCompressor(BBCSprite sprite, String filename) throws IOException {
        if (sprite != null && sprite.GetFrameCount() > 0) {
            this.dataOutputStream = new DataOutputStream(new FileOutputStream(new File(filename)));
            writeSpriteHeader(sprite);
        }
        dataOutputStream.writeByte((byte) 'B');
        dataOutputStream.writeByte((byte) '1');
        dataOutputStream.writeByte((byte) sprite.GetDisplayMode().number);
        dataOutputStream.writeShort(sprite.GetWidth());
        dataOutputStream.writeShort(sprite.GetHeight());
        dataOutputStream.writeShort(sprite.GetFrameCount());
        for (BBCSpriteFrame bbcSpriteFrame : sprite.GetFrames()) {
            ByteArrayOutputStream frameByteStream = new ByteArrayOutputStream();
            DataOutputStream frameOutputStream = new DataOutputStream(frameByteStream);
            byte[] data = new byte[sprite.GetWidth() * sprite.GetHeight()];
            bbcSpriteFrame.GetRenderedImage().getRaster().getDataElements(0, 0, sprite.GetWidth(), sprite.GetHeight(), data);
            int i = 0;
            do {
                byte sampleByte = data[i];
                int n = i;
                if (sampleByte == sprite.GetColours().length) {
                    while (n < data.length && n < i + 256 && data[n] == sampleByte) n++;
                    frameOutputStream.writeByte(1);
                } else if (sampleByte < sprite.GetColours().length) {
                    while (n < data.length && n < i + 256 && data[n] == sampleByte) n++;
                    if (n - i > 1) {
                        frameOutputStream.writeByte(3);
                    } else {
                        n = i;
                        while (n < data.length && n < i + 256 && (n == i || data[n] != sampleByte)) {
                            sampleByte = data[n++];
                        }
                        frameOutputStream.writeByte(2);
                        if (data[n] == sampleByte) n--;
                    }
                }
                frameOutputStream.writeByte((byte) n - i);
                i = n;
            } while (i < data.length);
            if (bbcSpriteFrame == sprite.GetFrames().getLast()) {
                frameOutputStream.writeByte(255);
            } else {
                frameOutputStream.writeByte(0);
            }
            frameOutputStream.flush();
            byte[] compressedFrameData = frameByteStream.toByteArray();
            dataOutputStream.writeShort(compressedFrameData.length);
            dataOutputStream.write(compressedFrameData);
        }
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    private void writeSpriteHeader(BBCSprite sprite) {

    }


    private DataOutputStream dataOutputStream = null;
}