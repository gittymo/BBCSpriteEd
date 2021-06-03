package com.plus.mevanspn.BBCSpriteEd.image.convert;

import com.plus.mevanspn.BBCSpriteEd.image.*;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

final public class NoDither {
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
                        final int impAlpha = (importImageRasterData[i] & 0xFF000000) != 0 ? 255 : 0;
                        byte colourIndex = BBCColour.GetPaletteIndexFor(impRed, impGreen, impBlue, displayMode.colours);
                        if (colourIndex < 0) colourIndex = (byte) displayMode.colours.length;
                        bbcImageRasterData[i] = impAlpha == 255 ? colourIndex : (byte) displayMode.colours.length;
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
