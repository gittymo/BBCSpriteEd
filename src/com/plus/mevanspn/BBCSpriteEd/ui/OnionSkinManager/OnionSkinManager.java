package com.plus.mevanspn.BBCSpriteEd.ui.OnionSkinManager;

import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import java.awt.*;
import java.awt.image.*;

final public class OnionSkinManager extends Thread {
    public OnionSkinManager(MainFrame parent) {
        this.parent = parent;
        this.frameOffset = -1;
        this.onionSkinFrame = 0;
        this.enabled = true;
        this.maxFrameOffset = 5;
        this.maxWaitTime = 15;
        this.onionSkinManagerToolbar = new OnionSkinManagerToolbar(this);
    }

    public BufferedImage GetOnionSkin() {
        if (enabled && parent.GetSprite() != null) {
            final float zoom = parent.GetZoom();
            final BBCSpriteFrame onionSkinSourceFrame = parent.GetSprite().GetFrame(onionSkinFrame);
            if (onionSkinSourceFrame != null) {
                final int onionSkinImageWidth = (int) (onionSkinSourceFrame.GetWidth() * zoom);
                final int onionSkinImageHeight = (int) (onionSkinSourceFrame.GetHeight() * zoom);

                BufferedImage onionSkinImage = new BufferedImage(onionSkinImageWidth, onionSkinImageHeight, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2 = (Graphics2D) onionSkinImage.getGraphics();

                if (zoom > 4 && onionSkinFrame != parent.GetSprite().GetCurrentFrameIndex()) {
                    final BBCImage onionSkinSourceImage = onionSkinSourceFrame.GetRenderedImage();
                    byte[] dataElements = new byte[onionSkinSourceImage.getWidth() * onionSkinSourceImage.getHeight()];
                    onionSkinSourceImage.getRaster().getDataElements(0, 0,onionSkinSourceImage.getWidth(), onionSkinSourceImage.getHeight(), dataElements);

                    final int quarterZoom = (int) (parent.GetZoom() / 4);
                    final int halfZoom = quarterZoom * 2;
                    for (int y = 0; y < onionSkinSourceFrame.GetHeight(); y++) {
                        for (int x = 0; x < onionSkinSourceFrame.GetWidth(); x++) {
                            final int offset = (y * onionSkinSourceFrame.GetWidth()) + x;
                            if (dataElements[offset] != onionSkinSourceFrame.GetSprite().GetColours().length) {
                                final Color pixelColour = onionSkinSourceFrame.GetSprite().GetColours()[dataElements[offset]];
                                g2.setColor(pixelColour);
                                final int rectX = (int) (x * parent.GetZoom());
                                final int rectY = (int) (y * parent.GetZoom());
                                g2.fillRect(rectX + halfZoom - 1, rectY + (quarterZoom * 3) - 1, halfZoom, quarterZoom);
                                g2.setColor(Color.BLACK);
                                g2.drawRect(rectX + halfZoom - 1, rectY + (quarterZoom * 3) - 1, halfZoom, quarterZoom);
                            }
                        }
                    }
                    return onionSkinImage;
                }
            }
        }
        return null;
    }

    public boolean IsEnabled() {
        return enabled;
    }

    public void SetEnabled(boolean enabled) {
        this.enabled = enabled;
        parent.RefreshPanels();
    }

    public void SetFrameOffset(int frameOffset) {
        if (enabled && parent.GetSprite() != null) {
            this.onionSkinFrame = parent.GetSprite().GetCurrentFrameIndex() + frameOffset;
            this.frameOffset = frameOffset > 0 ? 1 : frameOffset < 0 ? -1 : 0;
            this.wait = this.maxWaitTime;
            if (this.onionSkinFrame < 0) this.onionSkinFrame = 0;
            if (this.onionSkinFrame >= parent.GetSprite().GetFrameCount())
                this.onionSkinFrame = parent.GetSprite().GetFrameCount() - 1;
            parent.GetImagePanel().repaint();
        }
    }

    public int GetOffset() {
        return parent.GetSprite() != null ? onionSkinFrame - parent.GetSprite().GetCurrentFrameIndex() : 0;
    }

    public void ResetOnionSkinFrame() {
        this.onionSkinFrame = parent.GetSprite().GetCurrentFrameIndex() + onionSkinManagerToolbar.GetOffset();
        if (this.onionSkinFrame > parent.GetSprite().GetFrameCount() - 1) {
            this.onionSkinFrame = parent.GetSprite().GetFrameCount() - 1;
        }
        if (this.onionSkinFrame < 0) this.onionSkinFrame = 0;
        this.onionSkinManagerToolbar.UpdateControls();
    }

    public int GetMinimumAllowedFrameOffset() {
        if (enabled && parent.GetSprite() != null) {
            int currentLessMaxOffset = parent.GetSprite().GetCurrentFrameIndex() - maxFrameOffset;
            if (currentLessMaxOffset < 0) return -(maxFrameOffset + currentLessMaxOffset);
            else return -maxFrameOffset;
        } else return 0;
    }

    public int GetMaximumAllowedFrameOffset() {
        if (enabled && parent.GetSprite() != null) {
            int currentPlusMaxOffset = parent.GetSprite().GetCurrentFrameIndex() + maxFrameOffset;
            if (currentPlusMaxOffset > parent.GetSprite().GetFrameCount())
                return (parent.GetSprite().GetFrameCount() - parent.GetSprite().GetCurrentFrameIndex()) - 1;
            else return maxFrameOffset;
        } else return 0;
    }

    public void SetMaxWaitTime(int givenWaitTime) {
        if (enabled && parent.GetSprite() != null && givenWaitTime >= 0) {
            maxWaitTime = givenWaitTime;
            wait = maxWaitTime;
        }
    }

    public void UserRollForward() {
        if (enabled && parent.GetSprite() != null) {
            if (onionSkinFrame < parent.GetSprite().GetFrameCount() - 1) {
                frameOffset = 1;
                onionSkinFrame += maxFrameOffset > 0 ?
                        onionSkinFrame - parent.GetSprite().GetCurrentFrameIndex() < maxFrameOffset ?
                                frameOffset : 0 : frameOffset;
                wait = maxWaitTime;
                parent.RefreshPanels();
            }
        }
    }

    public void UserRollBack() {
        if (enabled && parent.GetSprite() != null) {
            if (onionSkinFrame > 0) {
                frameOffset = -1;
                onionSkinFrame += maxFrameOffset > 0 ?
                        onionSkinFrame - parent.GetSprite().GetCurrentFrameIndex() > -maxFrameOffset ?
                                frameOffset : 0 : frameOffset;
                wait = maxWaitTime;
                parent.RefreshPanels();
            }
        }
    }

    public void Update() {
        if (enabled && parent.GetSprite() != null) {
            onionSkinFrame = parent.GetSprite().GetCurrentFrameIndex();
            onionSkinManagerToolbar.UpdateControls();
            parent.RefreshPanels();
        }
    }

    public void Animate() {
        if (enabled && parent.GetSprite() != null && frameOffset != 0) {
            if (maxWaitTime > 0) {
                if (wait > 0) {
                    if (onionSkinFrame > parent.GetSprite().GetCurrentFrameIndex() + frameOffset) onionSkinFrame--;
                    else if (onionSkinFrame < parent.GetSprite().GetCurrentFrameIndex() + frameOffset) onionSkinFrame++;
                    parent.RefreshPanels();
                    onionSkinManagerToolbar.UpdateControls();
                } else wait--;
            }
        }
    }

    public int GetOnionSkinFrame() {
        return onionSkinFrame;
    }

    public int GetMaxWaitTime() {
        return maxWaitTime;
    }

    public void Quit() {
        quit = true;
    }

    @Override
    public void run() {
        quit = false;
        while (!quit) {
            try {
                Animate();
                sleep(100);
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public OnionSkinManagerToolbar GetToolbar() {
        return onionSkinManagerToolbar;
    }

    private final MainFrame parent;
    private int frameOffset, onionSkinFrame, wait = 0;
    private boolean enabled;
    private int maxFrameOffset;
    private int maxWaitTime;
    private boolean quit;
    private final OnionSkinManagerToolbar onionSkinManagerToolbar;


}
