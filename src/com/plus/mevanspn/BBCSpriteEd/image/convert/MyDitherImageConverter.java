package com.plus.mevanspn.BBCSpriteEd.image.convert;

import com.plus.mevanspn.BBCSpriteEd.image.BBCColour;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import java.awt.image.BufferedImage;

final public class MyDitherImageConverter implements IImageConverter {
    public MyDitherImageConverter() {
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
                byte[] colourIndices = BBCColour.GetPaletteIndexesFor(impRed, impGreen, impBlue, displayMode.colours);
                byte colourIndex;
                if (colourIndices[0] < 0 || impAlpha >= 0) colourIndex = (byte) displayMode.colours.length;
                else {
                    final int errorRed = impRed - displayMode.colours[colourIndices[0]].red;
                    final int errorGreen = impGreen - displayMode.colours[colourIndices[0]].green;
                    final int errorBlue = impBlue - displayMode.colours[colourIndices[0]].blue;
                    final int right = i + 1;
                    if (right % importedImage.getWidth() != 0) {
                        int offsetRed = (importImageRasterData[right] & 0x00FF0000) >> 16;
                        int offsetGreen = (importImageRasterData[right] & 0x0000FF00) >> 8;
                        int offsetBlue = importImageRasterData[right] & 0x000000FF;
                        int offsetAlpha = (importImageRasterData[right] & 0xFF000000) >> 24;
                        offsetRed = offsetRed + ((errorRed * 8) >> 4);
                        offsetGreen = offsetGreen + ((errorGreen * 8) >> 4);
                        offsetBlue = offsetBlue + ((errorBlue * 8) >> 4);
                        offsetRed = offsetRed > 255 ? 255 : Math.max(offsetRed, 0);
                        offsetGreen = offsetGreen > 255 ? 255 : Math.max(offsetGreen, 0);
                        offsetBlue = offsetBlue > 255 ? 255 : Math.max(offsetBlue, 0);
                        importImageRasterData[right] = (offsetAlpha << 24) + (offsetRed << 16) + (offsetGreen << 8) + offsetBlue;
                    }

                    final int down = i + importedImage.getWidth();
                    if (down < importImageRasterData.length) {
                        int offsetRed = (importImageRasterData[down] & 0x00FF0000) >> 16;
                        int offsetGreen = (importImageRasterData[down] & 0x0000FF00) >> 8;
                        int offsetBlue = importImageRasterData[down] & 0x000000FF;
                        int offsetAlpha = (importImageRasterData[down] & 0xFF000000) >> 24;
                        offsetRed = offsetRed + ((errorRed * 8) >> 4);
                        offsetGreen = offsetGreen + ((errorGreen * 8) >> 4);
                        offsetBlue = offsetBlue + ((errorBlue * 8) >> 4);
                        offsetRed = offsetRed > 255 ? 255 : Math.max(offsetRed, 0);
                        offsetGreen = offsetGreen > 255 ? 255 : Math.max(offsetGreen, 0);
                        offsetBlue = offsetBlue > 255 ? 255 : Math.max(offsetBlue, 0);
                        importImageRasterData[down] = (offsetAlpha << 24) + (offsetRed << 16) + (offsetGreen << 8) + offsetBlue;
                    }

                    colourIndex = colourIndices[0];
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
        return "Morgan's Custom Dither";
    }
}
