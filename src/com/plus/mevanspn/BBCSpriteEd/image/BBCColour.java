package com.plus.mevanspn.BBCSpriteEd.image;

import java.awt.Color;
import java.awt.image.IndexColorModel;

/**
 * The BBCColour class is a wrapper for the standard java.awt.Color class that provides some convenience methods
 * for handling colour models and a few public members that expose component details of the colour for fast
 * execution.
 */
final public class BBCColour extends Color {
    /**
     * Creates instances of BBCColour using three colour components (red, green and blue), following the
     * standard parameters available to the java.awt.Color constructor.  Any component value greater than zero
     * will be changed to 255, whereas any value <=0 will be changed to 0.
     * @param red Red colour component
     * @param green Green colour component
     * @param blue Blue colour component
     */
    public BBCColour(int red, int green, int blue) {
        super(red > 0 ? 255 : 0, green > 0 ? 255 : 0, blue > 0 ? 255 : 0);
        setPublicVars();
    }

    /**
     * Creates instances of BBCColour using four colour components (red, green, blue and alpha), following the
     * standard parameters available to the java.awt.Color constructor.  Any component value greater than zero
     * will be changed to 255, whereas any value <=0 will be changed to 0.  The alpha channel allows for the
     * creation of BBCColour objects that represent completely opaque or completely transparent colour.
     * @param red Red colour component
     * @param green Green colour component
     * @param blue Blue colour component
     * @param alpha Alpha colour component
     */
    public BBCColour(int red, int green, int blue, int alpha) {
        super(red > 0 ? 255 : 0, green > 0 ? 255 : 0, blue > 0 ? 255 : 0, alpha > 0 ? 255 : 0);
        setPublicVars();
    }

    /**
     * Creates instances of BBCColour using a packed integer format, following that used by the standard
     * java.awt.Color constructor.  The format of the value must be ARGB (Alpha, Red, Green and Blue) with
     * Alpha using the high end 8 bits (24-31) and Blue using the low end 8-bits (0-7).  The packed values
     * will be changed to suit the red, green and blue levels expected to represent the BBC micro's palette,
     * i.e. any value > 0 will become 255.  This also applies to the value channel value.
     * @param argb The packed alpha, red, green and blue values.
     */
    public BBCColour(int argb) {
        super((argb & 0x00FF0000) != 0 ? 255 : 0, (argb & 0x0000FF00) != 0 ? 255 : 0, (argb & 0x000000FF) != 0 ? 255 : 0,
                ((argb & 0xFF000000) >> 8) != 0 ? 255 : 0);
        setPublicVars();
    }

    /**
     * Internal method, simply populates various publicly accessible variables that provide RGBA and HSV information
     * about the colour.
     */
    private void setPublicVars() {
        this.red = getRed();
        this.green = getGreen();
        this.blue = getBlue();
        this.alpha = getAlpha();
        this.argb = getRGB();
        float[] hsv = new float[3];
        Color.RGBtoHSB(this.red, this.green, this.blue, hsv);
        this.hue = hsv[0];
        this.saturation = hsv[1];
        this.value = hsv[2];
    }

    /**
     * A convenience method that allows all red colour components to be retrieved from an array of BBCColour
     * objects.  This method is used internally by the BBCColour class when generating IndexColourModel objects
     * for images.
     * @param colours Array of BBCColour objects to extract red component values from.
     * @return Array of byte values representing the red components of a palette.
     */
    private static byte[] getReds(BBCColour[] colours) {
        byte[] reds = null;
        if (colours != null) {
            reds = new byte[colours.length + 1];
            for (int i = 0; i < reds.length - 1; i++) reds[i] = (byte) colours[i].red;
            reds[colours.length] = 0;
        }
        return reds;
    }

    /**
     * A convenience method that allows all green colour components to be retrieved from an array of BBCColour
     * objects.  This method is used internally by the BBCColour class when generating IndexColourModel objects
     * for images.
     * @param colours Array of BBCColour objects from which the green components will be extracted.
     * @return Array of bytes representing the green colour components of a palette.
     */
    private static byte[] getGreens(BBCColour[] colours) {
        byte[] greens = null;
        if (colours != null) {
            greens = new byte[colours.length + 1];
            for (int i = 0; i < greens.length - 1; i++) greens[i] = (byte) colours[i].green;
            greens[colours.length] = 0;
        }
        return greens;
    }

    /**
     * A convenience method that allows all blue colour components to be retrieved from an array of BBCColour
     * objects.  This method is used internally by the BBCColour class when generating IndexColourModel objects
     * for images.
     * @param colours Array of BBCColour objects from which the blue components will be extracted.
     * @return Array of bytes representing the blue colour components of a palette.
     */
    private static byte[] getBlues(BBCColour[] colours) {
        byte[] blues = new byte[colours.length + 1];
        for (int i = 0; i < blues.length - 1; i++) blues[i] = (byte) colours[i].blue;
        blues[colours.length] = 0;
        return blues;
    }

    /**
     * A convenience method that allows all alpha colour components to be retrieved from an array of BBCColour
     * objects.  This method is used internally by the BBCColour class when generating IndexColourModel objects
     * for images.
     * @param colours Array of BBCColour objects from which the alpha components will be extracted.
     * @return Array of bytes representing the alpha colour components of the palette.
     */
    private static byte[] getAlphas(BBCColour[] colours) {
        byte[] alphas = new byte[colours.length + 1];
        for (int i = 0; i < alphas.length - 1; i++) alphas[i] = (byte) colours[i].alpha;
        alphas[colours.length] = 0;
        return alphas;
    }

    /**
     * Convenience method that allows for the creation of IndexColorModel objects based on a BBC colour palette.
     * These IndexColourModels are usually applied in the creation of images within the application.
     * @param colours Array of BBCColours representing a colour palette.
     * @return An IndexColorModel object using colours within the BBC Micro's colour palette.
     */
    public static IndexColorModel GenerateIndexColorModel(BBCColour[] colours) {
        return colours != null ? new IndexColorModel(4, colours.length + 1, getReds(colours), getGreens(colours), getBlues(colours), colours.length) : null;
    }

    /**
     * Creates a copy of the array of BBCColour objects passed to it.  The copied colour references are
     * independent of the originals.
     * @param colours Array of BBCColour objects to copy.
     * @return A copy of the BBCColour objects.
     */
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

    /**
     * Determines the luminance value of a colour represented by a BBCColour object.  The algorithm to determine
     * the luminance is Lum = (Red * 0.299) + (Green * 0.587) + (Blue * 0.114).
     * @param bbcColour
     * @return Luminance of the colour represented by the BBCColour object passed in.
     */
    public static int GetLuminance(BBCColour bbcColour) {
        int luminance = 0;
        if (bbcColour != null) {
            luminance = (int) ((bbcColour.red * 0.299) + (bbcColour.green * 0.587) + (bbcColour.blue * 0.114));
        }
        return luminance;
    }

    /** Determines the luminance value of a colour represented by three channel values (red, green and blue)
     * the luminance is Lum = (Red * 0.299) + (Green * 0.587) + (Blue * 0.114).
     * @param red Red value (0 - 255)
     * @param green Green value (0 - 255)
     * @param blue Blue value (0 - 255)
     * @return Luminance value (0 - 255)
     */
    public static int GetLuminance(int red, int green, int blue) {
        int luminance = 0;
        if (red >=0 && red < 256 && green >= 0 && green < 256 && blue >=0 && blue < 256) {
            luminance = (int) ((red * 0.299) + (green * 0.587) + (blue * 0.114));
        }
        return Math.round(luminance);
    }

    /**
     * Tries to return an index for a colour in the given palette that matches the given red, green and blue values.
     * @param red Red component value to match.
     * @param green Green component value to match.
     * @param blue Blue component value to match.
     * @param colours Palette to match against.
     * @return Index of colour within the palette or -1 if no matching colour was found.
     */
    public static byte[] GetPaletteIndexesFor(int red, int green, int blue, BBCColour[] colours) {
        byte[] bestIndices = new byte[2];
        for (int i = 0; i < bestIndices.length; i++) bestIndices[i] = -1;

        double shortestDistance = 255;
        float hsv[] = new float[3];
        Color.RGBtoHSB(red, green, blue, hsv);
        for (int i = 0; i < colours.length; i++) {
            double distance = Math.pow(
                    Math.pow(colours[i].red - red, 2) +
                    Math.pow(colours[i].green - green, 2) +
                    Math.pow(colours[i].blue - blue, 2), 0.5);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                for (int j = bestIndices.length - 1; j > 0; j--) bestIndices[j] = bestIndices[j - 1];
                bestIndices[0] = (byte) i;
            }
        }

        for (int j = bestIndices.length - 1; j > 0; j--) {
            if (bestIndices[j] == -1) bestIndices[j] = bestIndices[j - 1];
        }

        return bestIndices;
    }

    /**
     * Represents the physical colour model of the BBC Micro as an array of BBCColour objects.
     */
    public final static BBCColour[] PHYSICAL_COLOURS = new BBCColour[] {
            new BBCColour(0, 0, 0), new BBCColour(255,0,0),
            new BBCColour(0,255,0), new BBCColour(255,255,0),
            new BBCColour(0,0,255), new BBCColour(255, 0,255),
            new BBCColour(0,255,255), new BBCColour(255,255,255)
    };

    /**
     * Represents the default colour model of the palette used by a BBC Micro's two colour modes using an array
     * of BBCColour objects.
     */
    public final static BBCColour[] TWO_COLOUR_MODE_DEFAULT = new BBCColour[] {
            new BBCColour(0,0,0), new BBCColour(255,255,255)
    };

    /**
     * Represents the default colour model of the palette used by a BBC Micro's four colour modes using an array
     * of BBCColour objects.
     */
    public final static BBCColour[] FOUR_COLOUR_MODE_DEFAULT = new BBCColour[] {
            new BBCColour(0,0,0), new BBCColour(255,0,0),
            new BBCColour(255,255,0), new BBCColour(255,255,255)
    };

    /**
     * Represents the default colour model of the palette used by a BBC Micro's eight colour modes using an array
     * of BBCColour objects.  This palette does not include the flashing colours used by indices 8-15.
     */
    public final static BBCColour[] EIGHT_COLOUR_MODE_DEFAULT = PHYSICAL_COLOURS;

    public int red, green, blue, alpha, argb;
    public float hue, saturation, value;
}
