package com.plus.mevanspn.BBCSpriteEd.ui.panels;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class FileInfoPanel extends JPanel {
    public FileInfoPanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(boxLayout);
        this.setBorder(new EmptyBorder(4, 4,4,4));
        this.spriteDimensions = new JLabel();
        this.spriteDisplayMode = new JLabel();
        this.spriteDataSizes = new JLabel();
        this.frameDataSizes = new JLabel();
        this.frameCount = new JLabel();
        this.add(spriteDimensions);
        this.add(Box.createRigidArea(new Dimension(8,8)));
        this.add(spriteDisplayMode);
        this.add(Box.createRigidArea(new Dimension(8,8)));
        this.add(frameCount);
        this.add(Box.createRigidArea(new Dimension(8,8)));
        this.add(spriteDataSizes);
        this.add(Box.createRigidArea(new Dimension(8,8)));
        this.add(frameDataSizes);
        this.Refresh();
    }

    public void Refresh() {
        if (mainFrame != null) {
            BBCSprite bbcSprite = mainFrame.GetSprite();
            if (bbcSprite != null && bbcSprite.GetWidth() > 0 && bbcSprite.GetHeight() > 0 && bbcSprite.GetFrameCount() > 0) {
                spriteDimensions.setText("Size: " + bbcSprite.GetWidth() + " x " + bbcSprite.GetHeight());
                spriteDisplayMode.setText("Display Mode: " + bbcSprite.GetDisplayMode().number);
                frameCount.setText("Frame " + (bbcSprite.GetCurrentFrameIndex() + 1) + " of " + bbcSprite.GetFrameCount());
                final int rawFrameSize = (bbcSprite.GetWidth() * bbcSprite.GetHeight()) / (8 / bbcSprite.GetDisplayMode().GetBitsPerPixel());
                final int rawSpriteSize = rawFrameSize * bbcSprite.GetFrameCount();
                int compressedSpriteSize = 0, compressedFrameSize = 0;
                try {
                    compressedSpriteSize = bbcSprite.GetCompressedData().length;
                    compressedFrameSize = bbcSprite.GetActiveFrame().GetCompressedData().length + 1;
                } catch (IOException ioex) { ioex.printStackTrace(); }
                spriteDataSizes.setText("Raw/Compressed Sprite Size: " + rawSpriteSize + "/" + compressedSpriteSize + " bytes.");
                frameDataSizes.setText("Raw/Compressed Frame Size: " + rawFrameSize + "/" + compressedFrameSize + " bytes.");
            } else {
                spriteDisplayMode.setText("");
                spriteDimensions.setText("");
                spriteDataSizes.setText("");
                frameDataSizes.setText("");
                frameCount.setText("");
            }
        }
    }

    private final MainFrame mainFrame;
    private final JLabel spriteDimensions, spriteDisplayMode, spriteDataSizes, frameDataSizes, frameCount;
}
