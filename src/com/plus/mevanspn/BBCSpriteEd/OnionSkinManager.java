package com.plus.mevanspn.BBCSpriteEd;

final public class OnionSkinManager extends Thread {
    public OnionSkinManager(MainFrame parent) {
        this.parent = parent;
        this.frameOffset = -1;
        this.onionSkinFrame = -0;
    }

    public BBCSpriteFrame GetOnionSkin() {
        if (parent.GetSprite() != null) {
            return parent.GetSprite().GetFrame(onionSkinFrame);
        } else return null;
    }

    public void UserRollBack() {
        if (parent.GetSprite() != null) {
            frameOffset = 1;
            if (onionSkinFrame < parent.GetSprite().GetFrameCount() - 1) onionSkinFrame += frameOffset;
            parent.RefreshImagePane();
        }
    }

    public void UserRollForward() {
        if (parent.GetSprite() != null) {
            frameOffset = -1;
            if (onionSkinFrame > 0) onionSkinFrame += frameOffset;
            parent.RefreshImagePane();
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
            if (onionSkinFrame > parent.GetSprite().GetCurrentFrameIndex() + frameOffset) onionSkinFrame--;
            else if (onionSkinFrame < parent.GetSprite().GetCurrentFrameIndex() + frameOffset) onionSkinFrame++;
            parent.RefreshImagePane();
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
                sleep(500);
            } catch (Exception ex) {}
        }
    }

    private MainFrame parent;
    private int frameOffset, onionSkinFrame = 0;
    private boolean quit = false;
}
