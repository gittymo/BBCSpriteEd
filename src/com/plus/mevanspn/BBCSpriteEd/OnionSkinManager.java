package com.plus.mevanspn.BBCSpriteEd;

import java.awt.*;
import java.awt.image.*;

final public class OnionSkinManager extends Thread {
    public OnionSkinManager(MainFrame parent) {
        this.parent = parent;
        this.frameOffset = -1;
        this.onionSkinFrame = -0;
    }

    public BufferedImage GetOnionSkin() {
        if (parent.GetSprite() != null) {
            final float zoom = parent.GetZoom();
            final BBCSpriteFrame onionSkinSourceFrame = parent.GetSprite().GetFrame(onionSkinFrame);
            if (onionSkinSourceFrame != null) {
                final int onionSkinImageWidth = (int) (onionSkinSourceFrame.GetWidth() * zoom);
                final int onionSkinImageHeight = (int) (onionSkinSourceFrame.GetHeight() * zoom);

                BufferedImage onionSkinImage = new BufferedImage(onionSkinImageWidth, onionSkinImageHeight, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2 = (Graphics2D) onionSkinImage.getGraphics();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2.drawImage(onionSkinSourceFrame.GetRenderedImage(), 0, 0, onionSkinImageWidth, onionSkinImageHeight, null);

                if (zoom > 4) {
                    byte[] samples = new byte[4 * onionSkinImageWidth * onionSkinImageHeight];
                    onionSkinImage.getRaster().getDataElements(0, 0, onionSkinImageWidth, onionSkinImageHeight, samples);
                    final int izoom = (int) zoom;
                    final int halfizoom = (izoom / 2) < 1 ? 1 : izoom / 2;
                    final int rowStride = onionSkinImageWidth * 4;
                    int offinc = 0, zoomCount = 0;
                    for (int offset = 0; offset < samples.length; offset += 4) {
                        if (offset % rowStride == 0) {
                            offset += (4 * offinc);
                            offinc = (offinc + 1) % halfizoom;
                            zoomCount = 0;
                        }
                        if (zoomCount < halfizoom) samples[offset + 3] = 0;
                        zoomCount = (zoomCount + 1) % izoom;
                    }
                    onionSkinImage.getRaster().setDataElements(0, 0, onionSkinImageWidth, onionSkinImageHeight, samples);
                }
                return onionSkinImage;
            } return null;
        } else return null;
    }

    public void UserRollForward() {
        if (parent.GetSprite() != null) {
            if (onionSkinFrame < parent.GetSprite().GetFrameCount() - 1) {
                frameOffset = 1;
                onionSkinFrame += frameOffset;
                wait = 15;
                parent.RefreshImagePane();
            }
        }
    }

    public void UserRollBack() {
        if (parent.GetSprite() != null) {
            if (onionSkinFrame > 0) {
                frameOffset = -1;
                onionSkinFrame += frameOffset;
                wait = 15;
                parent.RefreshImagePane();
            }
        }
    }

    public void Update() {
        if (parent.GetSprite() != null) {
            onionSkinFrame = parent.GetSprite().GetCurrentFrameIndex() + frameOffset;
            parent.RefreshImagePane();
        }
    }

    public void Animate() {
        if (parent.GetSprite() != null && frameOffset != 0) {
            if (wait == 0) {
                if (onionSkinFrame > parent.GetSprite().GetCurrentFrameIndex() + frameOffset) onionSkinFrame--;
                else if (onionSkinFrame < parent.GetSprite().GetCurrentFrameIndex() + frameOffset) onionSkinFrame++;
                parent.RefreshImagePane();
            } else wait--;
        }
    }

    public void Quit() {
        quit = true;
    }

    @Override
    public void run() {
        while (!quit) {
            try {
                Animate();
                sleep(100);
            } catch (Exception ex) {}
        }
    }

    private MainFrame parent;
    private int frameOffset, onionSkinFrame = 0, wait = 0;
    private boolean quit = false;
}
