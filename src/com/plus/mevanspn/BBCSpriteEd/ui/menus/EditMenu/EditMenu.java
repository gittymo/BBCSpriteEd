package com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu;

import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu.RotateMenu.RotateMenu;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;

final public class EditMenu extends JMenu {
    public EditMenu(MainFrame mainFrame) {
        super("Edit");
        this.mainFrame = mainFrame;
        JMenuItem resizeMenuItem = new JMenuItem("Resize...");
        resizeMenuItem.addActionListener(e -> new ResizeSpriteDialog(GetThis()).setVisible(true));
        add(resizeMenuItem);
        add(new JSeparator());
        add(new RotateMenu(this));

        JMenuItem flipHorizontalMenuItem = new JMenuItem("Flip Horizontally");
        flipHorizontalMenuItem.addActionListener(e -> {
            final BBCSprite sprite = GetMainFrame().GetSprite();
            if (sprite != null) {
                final BBCSpriteFrame activeSpriteFrame = sprite.GetActiveFrame();
                if (activeSpriteFrame != null && activeSpriteFrame.GetRenderedImage() != null) {
                    final BBCImage frameImage = activeSpriteFrame.GetRenderedImage();
                    final int imageWidth = frameImage.getWidth(), imageHeight = frameImage.getHeight();
                    final int rasterDataSize = imageWidth * imageHeight;
                    byte[] rasterData = new byte[rasterDataSize];
                    frameImage.getRaster().getDataElements(0, 0, imageWidth, imageHeight, rasterData);
                    int l = 0, r = imageWidth - 1;
                    for (int y = 0; y < imageHeight; y++) {
                        int ll = l, rr = r;
                        while (ll < rr) {
                            byte tempByte = rasterData[rr];
                            rasterData[rr--] = rasterData[ll];
                            rasterData[ll++] = tempByte;
                        }
                        l += imageWidth;
                        r += imageWidth;
                    }
                    frameImage.getRaster().setDataElements(0, 0, imageWidth, imageHeight, rasterData);
                    GetMainFrame().RefreshPanels();
                }
            }
        });
        add(flipHorizontalMenuItem);

        JMenuItem flipVerticalMenuItem = new JMenuItem("Flip Vertically");
        flipVerticalMenuItem.addActionListener(e -> {
            final BBCSprite sprite = GetMainFrame().GetSprite();
            if (sprite != null) {
                final BBCSpriteFrame activeSpriteFrame = sprite.GetActiveFrame();
                if (activeSpriteFrame != null && activeSpriteFrame.GetRenderedImage() != null) {
                    final BBCImage frameImage = activeSpriteFrame.GetRenderedImage();
                    final int imageWidth = frameImage.getWidth(), imageHeight = frameImage.getHeight();
                    final int rasterDataSize = imageWidth * imageHeight;
                    byte[] rasterData = new byte[rasterDataSize];
                    frameImage.getRaster().getDataElements(0, 0, imageWidth, imageHeight, rasterData);
                    for (int x = 0; x < imageWidth; x++) {
                        int t = x, b = x + (imageWidth * (imageHeight - 1));
                        while (t < b) {
                            byte tempByte = rasterData[b];
                            rasterData[b] = rasterData[t];
                            rasterData[t] = tempByte;
                            t += imageWidth;
                            b -= imageWidth;
                        }
                    }
                    frameImage.getRaster().setDataElements(0, 0, imageWidth, imageHeight, rasterData);
                    GetMainFrame().RefreshPanels();
                }
            }
        });
        add(flipVerticalMenuItem);
    }

    public MainFrame GetMainFrame() {
        return mainFrame;
    }

    public EditMenu GetThis() {
        return this;
    }

    private final MainFrame mainFrame;
}
