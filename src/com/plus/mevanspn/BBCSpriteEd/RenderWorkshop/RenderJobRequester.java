package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

import java.awt.image.BufferedImage;

public interface RenderJobRequester {
    boolean IsJobDone();
    void JobDone();
    BufferedImage GetRenderedImage();
}
