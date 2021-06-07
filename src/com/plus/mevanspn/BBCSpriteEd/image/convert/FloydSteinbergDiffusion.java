package com.plus.mevanspn.BBCSpriteEd.image.convert;

import com.plus.mevanspn.BBCSpriteEd.image.BBCColour;
import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

final public class FloydSteinbergDiffusion {
    public static BBCSprite GetConvertedSprite(String filename, BBCSprite.DisplayMode displayMode, MainFrame mainFrame) {
        BBCSprite sprite = null;
        BufferedImage image = Convert(filename, displayMode);
        if (image != null) {
            sprite = new BBCSprite(image.getWidth(), image.getHeight(), displayMode, mainFrame);
            BBCSpriteFrame spriteFrame = sprite.GetActiveFrame();
            BBCImage spriteFrameImage = spriteFrame.GetRenderedImage();
            byte[] importData = new byte[image.getWidth() * image.getHeight()];
            image.getRaster().getDataElements(0, 0, image.getWidth(), image.getHeight(), importData);
            spriteFrameImage.getRaster().setDataElements(0, 0, image.getWidth(), image.getHeight(), importData);
        }
        return sprite;
    }

    private static BufferedImage Convert(String filename, BBCSprite.DisplayMode displayMode) {
        BufferedImage bbcImage = null;
        if (filename != null && filename.length() > 0) {
            try {
                // Load the image to be imported.
                BufferedImage importedImage = ImageIO.read(new File(filename));
                // Make sure the image has loaded.
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
                                offsetRed = offsetRed + ((errorRed * 7) >> 4);
                                offsetGreen = offsetGreen + ((errorGreen * 7) >> 4);
                                offsetBlue = offsetBlue + ((errorBlue * 7) >> 4);
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
                                offsetRed = offsetRed + ((errorRed * 5) >> 4);
                                offsetGreen = offsetGreen + ((errorGreen * 5) >> 4);
                                offsetBlue = offsetBlue + ((errorBlue * 5) >> 4);
                                offsetRed = offsetRed > 255 ? 255 : Math.max(offsetRed, 0);
                                offsetGreen = offsetGreen > 255 ? 255 : Math.max(offsetGreen, 0);
                                offsetBlue = offsetBlue > 255 ? 255 : Math.max(offsetBlue, 0);
                                importImageRasterData[down] = (offsetAlpha << 24) + (offsetRed << 16) + (offsetGreen << 8) + offsetBlue;

                                final int leftDown = i + importedImage.getWidth() - 1;
                                if (leftDown % importedImage.getWidth() > 0) {
                                    offsetRed = (importImageRasterData[leftDown] & 0x00FF0000) >> 16;
                                    offsetGreen = (importImageRasterData[leftDown] & 0x0000FF00) >> 8;
                                    offsetBlue = importImageRasterData[leftDown] & 0x000000FF;
                                    offsetAlpha = (importImageRasterData[leftDown] & 0xFF000000) >> 24;
                                    offsetRed = offsetRed + ((errorRed * 3) >> 4);
                                    offsetGreen = offsetGreen + ((errorGreen * 3) >> 4);
                                    offsetBlue = offsetBlue + ((errorBlue * 3) >> 4);
                                    offsetRed = offsetRed > 255 ? 255 : Math.max(offsetRed, 0);
                                    offsetGreen = offsetGreen > 255 ? 255 : Math.max(offsetGreen, 0);
                                    offsetBlue = offsetBlue > 255 ? 255 : Math.max(offsetBlue, 0);
                                    importImageRasterData[leftDown] = (offsetAlpha << 24) + (offsetRed << 16) + (offsetGreen << 8) + offsetBlue;
                                }

                                final int rightDown = i + importedImage.getWidth() + 1;
                                if (right % importedImage.getWidth() != 0) {
                                    offsetRed = (importImageRasterData[rightDown] & 0x00FF0000) >> 16;
                                    offsetGreen = (importImageRasterData[rightDown] & 0x0000FF00) >> 8;
                                    offsetBlue = importImageRasterData[rightDown] & 0x000000FF;
                                    offsetAlpha = (importImageRasterData[rightDown] & 0xFF000000) >> 24;
                                    offsetRed = offsetRed + (errorRed >> 4);
                                    offsetGreen = offsetGreen + (errorGreen >> 4);
                                    offsetBlue = offsetBlue + (errorBlue >> 4);
                                    offsetRed = offsetRed > 255 ? 255 : Math.max(offsetRed, 0);
                                    offsetGreen = offsetGreen > 255 ? 255 : Math.max(offsetGreen, 0);
                                    offsetBlue = offsetBlue > 255 ? 255 : Math.max(offsetBlue, 0);
                                    importImageRasterData[rightDown] = (offsetAlpha << 24) + (offsetRed << 16) + (offsetGreen << 8) + offsetBlue;
                                }
                            }

                            colourIndex = colourIndices[0];
                        }
                        bbcImageRasterData[i] = colourIndex;
                    }

                    // Use the converted pixel data as the BBCImage raster data.
                    bbcImage.getRaster().setDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), bbcImageRasterData);
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return bbcImage;
    }
}
