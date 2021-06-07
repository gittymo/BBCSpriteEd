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
import java.util.Random;

final public class RandomDither {
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
                    int ditherMatrixRow = 0;
                    byte[] bbcImageRasterData = new byte[DATA_SIZE];
                    for (int i = 0; i < DATA_SIZE; i++) {
                        final int impRed = (importImageRasterData[i] & 0x00FF0000) >> 16;
                        final int impGreen = (importImageRasterData[i] & 0x0000FF00) >> 8;
                        final int impBlue = importImageRasterData[i] & 0x000000FF;
                        final int impAlpha = (importImageRasterData[i] & 0xFF000000) >> 24;
                        byte[] colourIndices = BBCColour.GetPaletteIndexesFor(impRed, impGreen, impBlue, displayMode.colours);
                        byte colourIndex = -1;
                        if (colourIndices[0] < 0) colourIndex = (byte) displayMode.colours.length;
                        else {
                            float[] hsv = new float[3];
                            BBCColour.RGBtoHSB(impRed, impGreen,impBlue,hsv);
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
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }

        return bbcImage;
    }
}
