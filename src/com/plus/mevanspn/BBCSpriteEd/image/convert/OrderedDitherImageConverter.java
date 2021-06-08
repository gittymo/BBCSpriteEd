package com.plus.mevanspn.BBCSpriteEd.image.convert;

import com.plus.mevanspn.BBCSpriteEd.image.BBCColour;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import java.awt.image.BufferedImage;

final public class OrderedDitherImageConverter implements IImageConverter {
    public OrderedDitherImageConverter() {
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
                byte colourIndex;
                if (colourIndices[0] < 0 || impAlpha >= 0) colourIndex = (byte) displayMode.colours.length;
                else {
                    final int ditherMatrixSize = ditherMatrix.length * ditherMatrix[0].length;
                    final int pixelValue = Math.round(BBCColour.GetLuminance(impRed, impGreen, impBlue) / (float) (256 / ditherMatrixSize));
                    if (i != 0 && i % importedImage.getWidth() == 0) {
                        ditherMatrixRow = (ditherMatrixRow + 1) % ditherMatrix.length;
                    }
                    int matrixValue = ditherMatrix[ditherMatrixRow][i % ditherMatrix[ditherMatrixRow].length];
                    colourIndex = pixelValue > matrixValue ? colourIndices[0] : 0;
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
        return "Ordered Dither";
    }

    private final static int[][] ditherMatrix = //new int [][] { { 0, 2}, {3, 1} };
            //new int[][] { { 0, 8, 2, 10}, { 12, 4, 14, 6}, { 3, 11, 1, 9}, {15, 7, 13, 5}};
            // new int[][] { { 0, 8, 2, 10}, { 12, 4, 14, 6}, { 3, 11, 1, 9}, {15, 7, 13, 5}};
            new int[][]{{0, 32, 8, 40, 2, 34, 10, 42},
                    {48, 16, 56, 24, 50, 18, 58, 26},
                    {12, 44, 4, 36, 14, 46, 6, 38},
                    {60, 28, 52, 20, 62, 30, 54, 22},
                    {3, 35, 11, 43, 1, 33, 9, 41},
                    {51, 19, 59, 27, 49, 17, 57, 25},
                    {15, 47, 7, 39, 13, 45, 5, 37},
                    {63, 31, 55, 23, 61, 29, 53, 21}};

}
