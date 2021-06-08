package com.plus.mevanspn.BBCSpriteEd.image.convert;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import java.awt.image.BufferedImage;

public interface IImageConverter {
    public BufferedImage Convert(BufferedImage importedImage, BBCSprite.DisplayMode displayMode);
    public String toString();
}
