package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

import java.awt.image.BufferedImage;

public interface RenderJobRequester {
    public boolean IsJobDone();
    public void JobDone();
    public BufferedImage GetRenderedImage();
}
