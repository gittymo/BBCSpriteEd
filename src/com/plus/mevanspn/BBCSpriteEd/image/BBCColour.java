package com.plus.mevanspn.BBCSpriteEd.image;

import java.awt.Color;
import java.awt.image.IndexColorModel;

final public class BBCColour extends Color {
    public BBCColour(int red, int green, int blue) {
        super(red >= 0 && red < 256 ? red : 0, green >= 0 && green < 256 ? green : 0, blue >= 0 && blue < 256 ? blue : 0);
        this.red = getRed();
        this.green = getGreen();
        this.blue = getBlue();
        this.alpha = getAlpha();
        this.argb = getRGB();
    }

    public BBCColour(int red, int green, int blue, int alpha) {
        super(red >= 0 && red < 256 ? red : 0, green >= 0 && green < 256 ? green : 0, blue >= 0 && blue < 256 ? blue : 0,
                alpha >=0 && alpha < 256 ? alpha : 0);
        this.red = getRed();
        this.green = getGreen();
        this.blue = getBlue();
        this.alpha = getAlpha();
        this.argb = getRGB();
    }

    public BBCColour(int argb) {
        super(argb);
        this.red = getRed();
        this.green = getGreen();
        this.blue = getBlue();
        this.alpha = getAlpha();
        this.argb = getRGB();
    }

    private static byte[] getReds(BBCColour[] colours) {
        byte[] reds = new byte[colours.length + 1];
        for (int i = 0; i < reds.length - 1; i++) reds[i] = (byte) colours[i].red;
        reds[colours.length] = 0;
        return reds;
    }

    private static byte[] getGreens(BBCColour[] colours) {
        byte[] greens = new byte[colours.length + 1];
        for (int i = 0; i < greens.length - 1; i++) greens[i] = (byte) colours[i].green;
        greens[colours.length] = 0;
        return greens;
    }

    private static byte[] getBlues(BBCColour[] colours) {
        byte[] blues = new byte[colours.length + 1];
        for (int i = 0; i < blues.length - 1; i++) blues[i] = (byte) colours[i].blue;
        blues[colours.length] = 0;
        return blues;
    }

    private static byte[] getAlphas(BBCColour[] colours) {
        byte[] alphas = new byte[colours.length + 1];
        for (int i = 0; i < alphas.length - 1; i++) alphas[i] = (byte) colours[i].alpha;
        alphas[colours.length] = 0;
        return alphas;
    }

    public static IndexColorModel GenerateIndexColourModel(BBCColour[] colours) {
        return colours != null ? new IndexColorModel(4, colours.length + 1, getReds(colours), getGreens(colours), getBlues(colours), colours.length) : null;
    }

    public static BBCColour[] GetCopy(BBCColour[] colours) {
        BBCColour[] coloursCopy = null;
        if (colours != null) {
            coloursCopy = new BBCColour[colours.length];
            for (int i = 0; i < colours.length; i++) {
                coloursCopy[i] = new BBCColour(colours[i].red, colours[i].green, colours[i].blue, colours[i].alpha);
            }
        }
        return coloursCopy;
    }

    public final static BBCColour[] PHYSICAL_COLOURS = new BBCColour[] {
            new BBCColour(0, 0, 0), new BBCColour(255,0,0),
            new BBCColour(0,255,0), new BBCColour(255,255,0),
            new BBCColour(0,0,255), new BBCColour(255, 0,255),
            new BBCColour(0,255,255), new BBCColour(255,255,255)
    };

    public final static BBCColour[] TWO_COLOUR_MODE_DEFAULT = new BBCColour[] {
            new BBCColour(0,0,0), new BBCColour(255,255,255)
    };

    public final static BBCColour[] FOUR_COLOUR_MODE_DEFAULT = new BBCColour[] {
            new BBCColour(0,0,0), new BBCColour(255,0,0),
            new BBCColour(255,255,0), new BBCColour(255,255,255)
    };

    public final static BBCColour[] EIGHT_COLOUR_MODE_DEFAULT = PHYSICAL_COLOURS;

    public int red, green, blue, alpha, argb;
}
