package com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel;

public final class FrameRotaterThread extends Thread {
    public FrameRotaterThread(PreviewPanel previewPanel) {
        this.previewPanel = previewPanel;
    }

    public void run() {
        stopped = true;
        while (!killed) {
            try {
                sleep(1000 / fps);
                if (!stopped) previewPanel.RotateFrames();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public void Kill() {
        killed = true;
    }

    public void Stop() { stopped = true; }

    public void Pause() { stopped = true; }

    public void Play() { stopped = false; }

    public void ToEnd() {

    }

    public void SetFPS(int fps) {
        this.fps = fps;
    }

    private boolean killed = false, stopped = false;
    private int fps = 10;
    private final PreviewPanel previewPanel;
}