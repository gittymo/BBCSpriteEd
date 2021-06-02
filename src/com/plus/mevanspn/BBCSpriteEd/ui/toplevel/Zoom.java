package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;

final public class Zoom {
    public Zoom(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        Update(16);
    }

    public Zoom(MainFrame mainFrame, float zoom) {
        this.mainFrame = mainFrame;
        Update(zoom);
    }

    public void Update(float zoom) {
        if (zoom < 0) zoom = -zoom;
        else if (zoom == 0) zoom = 1;
        final BBCImage bbcImage = mainFrame.GetActiveImage();
        this.X = bbcImage != null ? zoom * bbcImage.GetHorizontalPixelAspectRatio() : zoom;
        this.Y = zoom;
        this.ZOOM = zoom;
        this.iX = (int) this.X;
        this.iY = (int) this.Y;
        this.iZOOM = (int) this.ZOOM;
    }

    public void Update() {
        final BBCImage bbcImage = mainFrame.GetActiveImage();
        this.X = bbcImage != null ? this.Y * bbcImage.GetHorizontalPixelAspectRatio() : this.Y;
        this.iX = (int) this.X;
    }

    public float X, Y, ZOOM;
    public int iX, iY, iZOOM;
    private MainFrame mainFrame;
}
