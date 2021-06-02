package com.plus.mevanspn.BBCSpriteEd.image;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.Zoom;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.Arrays;

/**
 * Objects derived from the BBCImage class represent bitmaps using colours compatible with those available to
 * the BBC Micro.  The objects provide methods to retrieve parent container classes such as BBCSpriteFrame and
 * BBCSprite, along with methods for performing rudimentary drawing and editing operations.
 */
final public class BBCImage extends BufferedImage {
    /**
     * Allows for the creation of a BBCImage based on the dimensions taken from it's parent container
     * (BBCSpriteFrame)  This allows for images to be created in a consistent format.
     * @param bbcSpriteFrame Reference to the BBCSpriteFrame parent container.
     */
    public BBCImage(BBCSpriteFrame bbcSpriteFrame) {
        // Create a sprite using the parent container's dimension and colour info.
        super(bbcSpriteFrame.GetWidth(), bbcSpriteFrame.GetHeight(),
                BBCImage.TYPE_BYTE_INDEXED,
                BBCColour.GenerateIndexColorModel(bbcSpriteFrame.GetColours()));
        this.bbcSpriteFrame = bbcSpriteFrame;
        // Make sure all the pixels of the image are initially transparent.
        makeTransparent();
    }

    /**
     * Allows for the creation of a BBCImage of the given dimensions.  A reference to the parent
     * container must be provided in order to copy the colour palette information.
     * @param width Width of the image in pixels.
     * @param height Height of the image in pixels.
     * @param bbcSpriteFrame Reference to the parent container containing colour palette information.
     */
    public BBCImage(int width, int height, BBCSpriteFrame bbcSpriteFrame) {
        // Create an image of width x height pixels using the parent container's colour info.
        super(width, height, BBCImage.TYPE_BYTE_INDEXED,
                BBCColour.GenerateIndexColorModel(bbcSpriteFrame.GetSprite().GetColours()));
        this.bbcSpriteFrame = bbcSpriteFrame;
        // Make sure all the pixels are initially transparent.
        makeTransparent();
    }

    /**
     * Creates a copy of the BBCImage object referenced by bbcImage, but uses an alternative colour palette
     * provided by the IndexColorModel newColourModel.  This constructor is usually only called when the
     * logical colours of a sprite are changed.  A restriction in the underlying IndexColorModel model used
     * by BBCImage means colour data cannot be changed on the fly, leading to the need to create a new image
     * with the colour palette changes.
     * @param bbcImage Reference to the original BBCImage image object.
     * @param newColourModel Reference to the new IndexColorModel holding the new colour palette for the image.
     */
    public BBCImage(BBCImage bbcImage, IndexColorModel newColourModel) {
        // Create a new image using the new colour model.
        super(bbcImage.getWidth(), bbcImage.getHeight(), bbcImage.getType(), newColourModel);
        // Get a copy of the raster data from the original image.
        WritableRaster wr = bbcImage.getRaster();
        byte[] dataElements = new byte[bbcImage.getWidth() * bbcImage.getHeight()];
        wr.getDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), dataElements);
        // Use the copied data as the source data for the new image.
        this.getRaster().setDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), dataElements);
        // Record a reference to the parent container.
        this.bbcSpriteFrame = bbcImage.GetSpriteFrame();
    }

    /**
     * Creates a 1:1 copy of the given BBCImage object.  This constructor is usually used for frame duplication
     * operations.
     * @param originalImage Reference to the original image.
     */
    public BBCImage(BBCImage originalImage) {
        // Call the appropriate constructor to make a copy of the given image.
        this(originalImage, originalImage.GetSprite().GetColourModel());
    }

    /**
     * Creates a BufferedImage object that is used to represent an onionskin layer version of the image.
     * These images are used by the editor application to represent the image as a frame in the future or
     * past in relation to the current frame being edited.  Future frames are rendered in green, whilst past
     * frames are rendered in red.  The algorithm creates the onionskin image pixels based on the luminance
     * of the original pixel data.
     * @param past Used to determine if a past (true) or future (false) onionskin image should be created.
     * @return A reference to a BufferImage object representing the onionskin image.
     */
    public BufferedImage GetOnionSkinImage(boolean past) {
        // Create a new BufferedImage object.
        final BufferedImage onionSkinImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        // Get this image's pixel data.
        byte[] sourceDataElements = new byte[getWidth() * getHeight()];
        getRaster().getDataElements(0, 0, getWidth(), getHeight(), sourceDataElements);
        // Create an array of integers which will hold the ARGB values for the onionskin image pixel data.
        int[] destRGBElements = new int[getWidth() * getHeight()];
        // Populate the array with future/past representations of this image's RGB pixel data.
        for (int i = 0; i < sourceDataElements.length; i++) {
            final int colourIndex = sourceDataElements[i];
            if (colourIndex < GetSprite().GetColours().length) {
                final int luminance = BBCColour.GetLuminance(GetSprite().GetColours()[colourIndex]);
                destRGBElements[i] = new Color(past ? luminance : 0, !past ? luminance : 0, 0).getRGB();
            }
        }
        // Set the pixel data for the onionskin image from the newly created array.
        onionSkinImage.setRGB(0, 0,getWidth(),getHeight(), destRGBElements, 0, getWidth());
        // Return the reference to the onionskin image.
        return onionSkinImage;
    }

    /**
     * Returns the top-level parent container for the image object.
     * @return The top-level parent container for the image object.
     */
    public BBCSprite GetSprite() {
        return bbcSpriteFrame.GetSprite();
    }

    /**
     * Returns the immediate parent container for the image object.
     * @return The immediate parent container for the image object.
     */
    public BBCSpriteFrame GetSpriteFrame() {
        return bbcSpriteFrame;
    }

    /**
     * Returns an array of BBCColour, representing the colour palette for the image.
     * @return A reference to an array of BBCColour objects.
     */
    public BBCColour[] GetColours() {
        return bbcSpriteFrame.GetColours();
    }

    /**
     * Returns the horizontal pixel aspect ratio for the image.  BBC Micro display modes usually use
     * 1:2, 1:1 and 2:1 ratios for displaying pixels.
     * @return The horizontal pixel aspect ratio for the image.
     */
    public float GetHorizontalPixelAspectRatio() {
        return bbcSpriteFrame.GetHorizontalPixelRatio();
    }

    /**
     * Sets and individual pixel in the image to the given palette index.
     * @param x Horizontal position of the pixel.
     * @param y Vertical position of the pixel.
     * @param colourIndex The palette index to use.
     */
    public void SetPixel(int x, int y, byte colourIndex) {
        // Make sure the position of the pixel falls within the bounds of the image and that the palette index
        // is within the palette range.
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight() &&
                colourIndex >= 0 && colourIndex <= GetColours().length) {
            // It is so make sure the current pixel palette index is different.
            byte[] pixelData = new byte[1];
            getRaster().getDataElements(x, y, pixelData);
            if (pixelData[0] != colourIndex) {
                // It is different, so we can change it.
                pixelData[0] = colourIndex;
                getRaster().setDataElements(x, y, pixelData);
            }
        }
    }

    /**
     * Draws a rectangle over an area of the image.  The rectangle can be an outline or can be filled.
     * @param left Coordinate of the left edge of the rectangle in pixels.
     * @param top  Coordinate of the top edge of the rectangle in pixels.
     * @param width Width of the rectangle in pixels.
     * @param height    Height of the rectangle in pixels.
     * @param filled    If true, the rectangle will be filled.
     * @param colourIndex   Colour index to use for rendering the rectangle.
     */
    public void DrawRectangle(int left, int top, int width, int height, boolean filled, byte colourIndex) {
        // Make sure the colour index is valid and that the rectangle has dimensions >0.
        if (colourIndex >=0 && colourIndex < GetColours().length && width > 0 && height > 0) {
            // Make sure the rectangle's dimensions fall within the image's area.
            if (left + width > getWidth()) width = getWidth() - left;
            if (top + height > getHeight()) height = getHeight() - top;
            // Draw the rectangle.
            Graphics2D g2 = (Graphics2D) getGraphics();
            g2.setColor(bbcSpriteFrame.GetSprite().GetColours()[colourIndex]);
            if (filled) g2.fillRect(left, top, width + 1, height + 1);
            else g2.drawRect(left, top, width, height);
        }
    }

    public void DrawOval(int left, int top, int width, int height, boolean filled, byte colourIndex) {
        if (colourIndex < bbcSpriteFrame.GetColours().length && width >=0 && height >= 0) {
            if (left + width > getWidth()) width = getWidth() - left;
            if (top + height > getHeight()) height = getHeight() - top;
            Graphics2D g2 = (Graphics2D) getGraphics();
            g2.setColor(bbcSpriteFrame.GetSprite().GetColours()[colourIndex]);
            if (filled) g2.fillOval(left, top, width, height);
            g2.drawOval(left, top, width, height);
        }
    }

    public void DrawLine(Point pointA, Point pointB, byte colourIndex) {
        if (colourIndex < bbcSpriteFrame.GetColours().length && pointA != null && pointB != null) {
            Graphics2D g2 = (Graphics2D) getGraphics();
            g2.setColor(bbcSpriteFrame.GetSprite().GetColours()[colourIndex]);
            g2.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
        }
    }

    public void PaintImage(BufferedImage image, Point origin) {
        if (image != null && origin != null) {
            int xOffset = (int) Math.round(image.getWidth() / 2.0);
            int yOffset = (int) Math.round(image.getHeight() / 2.0);
            final int imageX = origin.x - xOffset;
            final int imageY = origin.y - yOffset;
            getGraphics().drawImage(image, imageX, imageY, null);
        }
    }

    public void FloodFill(Point p, byte colourToUseIndex, byte colourToReplace, boolean started) {
        if (colourToUseIndex < bbcSpriteFrame.GetColours().length && p.x >= 0 && p.x < getWidth() && p.y >=0 && p.y < getHeight()) {
            // final int colourToUse = bbcSprite.GetColours()[colourToUseIndex].getRGB();

            byte[] colourToReplaceArray = new byte[1];
            final byte[] colourToUseArray = new byte[] { colourToUseIndex };

            if (!started) {
                getRaster().getDataElements(p.x, p.y, colourToReplaceArray);
                if (colourToReplaceArray[0] == colourToUseIndex) return;
                else {
                    colourToReplace = colourToReplaceArray[0];
                    started = true;
                }
            }

            if (pixelMatchesColourToReplace(p.x, p.y, colourToReplace)) {
                for (int x = p.x; x >= 0 && pixelMatchesColourToReplace(x, p.y, colourToReplace); x--) {
                    getRaster().setDataElements(x, p.y, colourToUseArray);
                    // renderedImage.setRGB(x, p.y, colourToUse);
                    if (p.y > 0 && pixelMatchesColourToReplace(x, p.y - 1, colourToReplace)) {
                        FloodFill(new Point(x, p.y - 1), colourToUseIndex, colourToReplace, started);
                    }
                    if (p.y < getHeight() - 1 && pixelMatchesColourToReplace(x, p.y + 1, colourToReplace)) {
                        FloodFill(new Point(x, p.y + 1), colourToUseIndex, colourToReplace, started);
                    }
                }
                if (p.x < getWidth() - 1) {
                    for (int x = p.x + 1; x < getWidth() && pixelMatchesColourToReplace(x, p.y, colourToReplace); x++) {
                        //renderedImage.setRGB(x, p.y, colourToUse);
                        getRaster().setDataElements(x, p.y, colourToUseArray);
                        if (p.y > 0 && pixelMatchesColourToReplace(x, p.y - 1, colourToReplace)) {
                            FloodFill(new Point(x, p.y - 1), colourToUseIndex, colourToReplace, started);
                        }
                        if (p.y < getHeight() - 1 && pixelMatchesColourToReplace(x, p.y + 1, colourToReplace)) {
                            FloodFill(new Point(x, p.y + 1), colourToUseIndex, colourToReplace, started);
                        }
                    }
                }
            } else if (pixelMatchesColourToReplace(p.x, p.y, colourToUseIndex)){
                if (p.y > 0) FloodFill(new Point(p.x , p.y - 1), colourToUseIndex, colourToReplace, started);
                if (p.y < getHeight() - 1) FloodFill(new Point(p.x, p.y + 1), colourToUseIndex, colourToReplace, started);
            }
        }
    }

    /**
     * Rotates the image by the given number of degrees.
     * @param degrees Number of degrees to rotate the image by.
     */
    public void Rotate(double degrees) {
        // Adjust the passed value so that it falls within the expected 0-360 degree range.
        if (degrees < 0) {
            while (degrees < 0) degrees += 360;
        } else if (degrees >= 360) {
            while (degrees >= 360) degrees -= 360;
        }
        // Create a new image to hold the rotated version.
        BBCImage newFrameImage = new BBCImage(this.GetSpriteFrame());
        // Get the centre origin of the image.
        final int originX = newFrameImage.getWidth() / 2;
        final int originY = newFrameImage.getHeight() / 2;
        // Convert degrees to radians in order to perform the rotation.
        final double rotation = Math.toRadians(degrees);
        // Apply the rotation transformation to the original image and paint the rotated image to the new one.
        final AffineTransform tx = AffineTransform.getRotateInstance(rotation, originX, originY);
        final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        newFrameImage.getGraphics().drawImage(op.filter(this, null), 0, 0, null);
        // Update the sprite container so that it points to the new rotated version of the image.
        this.GetSpriteFrame().SetRenderedImage(newFrameImage);
    }

    /**
     * Translates the image xoffset pixels on the horizontal plane and yoffset pixels on the vertical plane.  The
     * image wraps around, so that any pixels that are moved off of one side will appear on the other.
     * @param xoffset Number of pixels to move the image on the horizontal axis.
     * @param yoffset Number of pixels to move the image on the vertical axis.
     */
    public void TranslateWithWrap(int xoffset, int yoffset) {
        // Make sure the offsets are within the dimensions of the image.
        if (xoffset < 0) {
            while (xoffset <= -getWidth()) xoffset += getWidth();
        } else {
            while (xoffset >= getWidth()) xoffset -= getWidth();
        }
        if (yoffset < 0) {
            while (yoffset <= -getHeight()) yoffset += getHeight();
        } else {
            while (yoffset >= getHeight()) yoffset -= getHeight();
        }

        // Make sure we are moving the image (i.e. xoffset and yoffset are not both 0).  If so, do nothing and return.
        if (xoffset == 0 && yoffset == 0) return;

        // Get the size of the image's raster data.
        final int dataLength = getWidth() * getHeight();

        // Get the raster data for the image
        byte[] sourceImageData = new byte[dataLength];
        getRaster().getDataElements(0,0, getWidth(), getHeight(), sourceImageData);

        // Create a new byte array to hold the translated image data.
        byte[] translatedImageData = new byte[dataLength];

        int i = 0;
        for (int y = 0; y < getHeight(); y++) {
            final int tyOffset = (y + yoffset < 0 ? getHeight() + yoffset : y + yoffset >= getHeight() ?
                    (y + yoffset) - getHeight() : y + yoffset) * getWidth();
            final int tyEndOffset = tyOffset + getWidth();
            int ti = tyOffset + (xoffset < 0 ? getWidth() + xoffset : xoffset);
            for (int x = 0; x < getWidth(); x++) {
                translatedImageData[ti++] = sourceImageData[i++];
                if (ti == tyEndOffset) ti = tyOffset;
            }
        }

        // Replace the original image's data with the translated version.
        getRaster().setDataElements(0, 0, getWidth(), getHeight(), translatedImageData);
    }

    /**
     * Gets the size of the image, taking into account the given zoom level and the horizontal pixel aspect ratio of
     * the sprite's display mode.
     * @param zoom Required zoom level.
     * @return A rectangle object representing the size of the sprite (in pixels) when zoomed.
     */
    public Rectangle GetRenderDimensions(Zoom zoom) {
        return new Rectangle((int) (getWidth() * zoom.X), (int) (getHeight() * zoom.Y));
    }

    /**
     * Gets the active colour index from the application.
     * @return Current index of the active colour selected in the application.
     */
    public BBCColour GetActiveColour() {
        return GetColours()[bbcSpriteFrame.GetSprite().GetMainFrame().GetActiveColourIndex()];
    }

    /**
     * Checks to see if the pixel uses the same colour index as the one we're trying to replace it with.
     * @param x Horizontal coordinate of the pixel.
     * @param y Vertical coordinate of the pixel.
     * @param colourToReplace Index of the colour we're going to use to replace the pixel in the image.
     * @return Returns true if the replacement colour index is the same as the pixel's colour index.
     */
    private boolean pixelMatchesColourToReplace(int x, int y, byte colourToReplace) {
        byte[] pixelColourIndex = new byte[1];
        getRaster().getDataElements(x, y, pixelColourIndex);
        return pixelColourIndex[0] == colourToReplace;
    }

    /**
     * Makes all the pixels of the image transparent.  This method is usually called as part of the BBCImage constructor.
     */
    private void makeTransparent() {
        WritableRaster wr = this.getRaster();
        byte[] dataElements = new byte[getWidth() * getHeight()];
        wr.getDataElements(0, 0, getWidth(), getHeight(), dataElements);
        Arrays.fill(dataElements, (byte) bbcSpriteFrame.GetColours().length);
        wr.setDataElements(0, 0, getWidth(), getHeight(), dataElements);
    }

    private final BBCSpriteFrame bbcSpriteFrame;
}
