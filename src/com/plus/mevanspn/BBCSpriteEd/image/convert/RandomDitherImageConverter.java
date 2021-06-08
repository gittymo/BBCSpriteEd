package com.plus.mevanspn.BBCSpriteEd.image.convert;

import com.plus.mevanspn.BBCSpriteEd.image.BBCColour;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import java.awt.image.BufferedImage;
import java.util.Random;

final public class RandomDitherImageConverter implements IImageConverter {
    public RandomDitherImageConverter() {
        super();
    }

    public BufferedImage Convert(BufferedImage importedImage, BBCSprite.DisplayMode displayMode) {
        BufferedImage bbcImage = null;
        if (importedImage != null) {
            // Create a BBCImage compatible image to hold the converted data.
            bbcImage = new BufferedImage(importedImage.getWidth(), importedImage.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY, BBCColour.GenerateIndexColorModel(displayMode.colours));

            // Get the imported image's RGBA pixel values.
            final int DATA_SIZE = importedImage.getWidth() * importedImage.getHeight();
            int[] importImageRasterData = new int[DATA_SIZE];
            importedImage.getRGB(0, 0, importedImage.getWidth(), importedImage.getHeight(),
                    importImageRasterData, 0, importedImage.getWidth());

            // Create an array of indexes for BBC colours that most closely match the imported pixel data.
            int ditherMatrixRow = 0;
            byte[] bbcImageRasterData = new byte[DATA_SIZE];
            for (int i = 0; i < DATA_SIZE; i++) {
                final int impRed = (importImageRasterData[i] & 0x00FF0000) >> 16;
                final int impGreen = (importImageRasterData[i] & 0x0000FF00) >> 8;
                final int impBlue = importImageRasterData[i] & 0x000000FF;
                final int impAlpha = (importImageRasterData[i] & 0xFF000000) >> 24;
                byte[] colourIndices = BBCColour.GetPaletteIndexesFor(impRed, impGreen, impBlue, displayMode.colours);
                byte colourIndex = -1;
                if (colourIndices[0] < 0 || impAlpha >= 0) colourIndex = (byte) displayMode.colours.length;
                else {
                    float[] hsv = new float[3];
                    BBCColour.RGBtoHSB(impRed, impGreen, impBlue, hsv);
                    final int pixelValue = BBCColour.GetLuminance(impRed, impGreen, impBlue);
                    final int randomValue = (new Random().nextInt()) % 256;
                    if (i != 0 && i % importedImage.getWidth() == 0) {
                        ditherMatrixRow = (ditherMatrixRow + 1) % 4;
                    }
                    colourIndex = pixelValue >= randomValue ? colourIndices[0] : colourIndices[1];
                }
                bbcImageRasterData[i] = colourIndex;
            }

            // Use the converted pixel data as the BBCImage raster data.
            bbcImage.getRaster().setDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), bbcImageRasterData);

        }
        return bbcImage;
    }

    @Override
    public String toString() {
        return "Random Dither";
    }
}
