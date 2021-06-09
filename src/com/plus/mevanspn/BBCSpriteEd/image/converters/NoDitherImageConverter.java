package com.plus.mevanspn.BBCSpriteEd.image.converters;

import com.plus.mevanspn.BBCSpriteEd.image.*;

import java.awt.image.*;

final public class NoDitherImageConverter implements IImageConverter {
    public NoDitherImageConverter() {
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
            byte[] bbcImageRasterData = new byte[DATA_SIZE];
            for (int i = 0; i < DATA_SIZE; i++) {
                final int impRed = (importImageRasterData[i] & 0x00FF0000) >> 16;
                final int impGreen = (importImageRasterData[i] & 0x0000FF00) >> 8;
                final int impBlue = importImageRasterData[i] & 0x000000FF;
                final int impAlpha = (importImageRasterData[i] & 0xFF000000) >> 24;
                byte colourIndex = BBCColour.GetPaletteIndexesFor(impRed, impGreen, impBlue, displayMode.colours)[0];
                if (colourIndex < 0 || impAlpha >= 0) colourIndex = (byte) displayMode.colours.length;
                bbcImageRasterData[i] = colourIndex;
            }

            // Use the converted pixel data as the BBCImage raster data.
            bbcImage.getRaster().setDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), bbcImageRasterData);
        }
        return bbcImage;
    }

    @Override
    public String toString() {
        return "No Dithering";
    }
}
