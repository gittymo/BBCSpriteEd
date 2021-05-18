package com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel;

public final class FrameRotaterThread extends Thread {
    public FrameRotaterThread(PreviewPanel parent) {
        this.parent = parent;
    }

    public void run() {
        while (!killed) {
            try {
                sleep(1000 / fps);
                parent.RotateFrames();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public void Kill() {
        killed = true;
    }

    public void SetFPS(int fps) {
        this.fps = fps;
    }

    private boolean killed = false;
    private int fps = 10;
    private final PreviewPanel parent;
}