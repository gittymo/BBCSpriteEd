package com.plus.mevanspn.BBCSpriteEd.image;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;

final public class BBCImage extends BufferedImage {
    public BBCImage(BBCSprite bbcSprite) {
        super(bbcSprite.GetWidth(), bbcSprite.GetHeight(), BBCImage.TYPE_BYTE_INDEXED, BBCColour.GenerateIndexColourModel(bbcSprite.GetColours()));
        this.bbcSprite = bbcSprite;
        makeTransparent();
    }

    public BBCImage(int width, int height, BBCSprite bbcSprite) {
        super(width, height, BBCImage.TYPE_BYTE_INDEXED, BBCColour.GenerateIndexColourModel(bbcSprite.GetColours()));
        this.bbcSprite = bbcSprite;
        makeTransparent();
    }

    public BBCImage(BBCImage bbcImage, IndexColorModel newColourModel) {
        super(bbcImage.getWidth(), bbcImage.getHeight(), bbcImage.getType(), newColourModel);
        WritableRaster wr = bbcImage.getRaster();
        byte[] dataElements = new byte[bbcImage.getWidth() * bbcImage.getHeight()];
        wr.getDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), dataElements);
        this.getRaster().setDataElements(0, 0, bbcImage.getWidth(), bbcImage.getHeight(), dataElements);
        this.bbcSprite = bbcImage.GetSprite();
    }

    public BBCImage(BBCImage originalImage) {
        this(originalImage, originalImage.GetSprite().GetColourModel());
    }

    public BBCSprite GetSprite() {
        return bbcSprite;
    }

    private void makeTransparent() {
        WritableRaster wr = this.getRaster();
        byte[] dataElements = new byte[getWidth() * getHeight()];
        wr.getDataElements(0, 0, getWidth(), getHeight(), dataElements);
        Arrays.fill(dataElements, (byte) bbcSprite.GetColours().length);
        wr.setDataElements(0, 0, getWidth(), getHeight(), dataElements);
    }

    private final BBCSprite bbcSprite;
}
