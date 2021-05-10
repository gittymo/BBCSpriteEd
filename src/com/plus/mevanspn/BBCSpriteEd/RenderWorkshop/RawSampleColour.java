package com.plus.mevanspn.BBCSpriteEd.RenderWorkshop;

import java.awt.Color;

final public class RawSampleColour extends Color {
    public RawSampleColour(int red, int green, int blue) {
        super(red, green, blue);
        this.rawValue = this.getRGB();
    }

    int rawValue;
}
