package com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu;

import javax.swing.*;
import java.awt.*;

public final class ResizeSpriteDialog extends JDialog {
    public ResizeSpriteDialog(EditMenu editMenu) {
        super(editMenu.GetMainFrame(), "Resize Sprite", true);
        final int originalWidth = editMenu.GetMainFrame().GetSprite().GetWidth();
        final int originalHeight = editMenu.GetMainFrame().GetSprite().GetHeight();
        JLabel newImageWidthLabel = new JLabel("New Width");
        JLabel newImageHeightLabel = new JLabel("New Height");
        JTextField newImageWidth = new JTextField("" + originalWidth);
        JTextField newImageHeight = new JTextField("" + originalHeight);
        JButton okButton = new JButton("Resize");
        okButton.addActionListener(e -> {
            int newWidth = originalWidth, newHeight = originalHeight;
            try {
                newWidth = Integer.parseInt(newImageWidth.getText());
                newHeight = Integer.parseInt(newImageHeight.getText());
            } catch (Exception ex) { ex.printStackTrace(); }
            if (newWidth < 1 || newWidth > editMenu.GetMainFrame().GetSprite().GetDisplayMode().width ||
                    newHeight < 1 || newHeight > editMenu.GetMainFrame().GetSprite().GetDisplayMode().height) {
                newWidth = originalWidth;
                newHeight = originalHeight;
            }
            if (newWidth != originalWidth || newHeight != originalHeight) {
                editMenu.GetMainFrame().GetSprite().RecordHistory();
                editMenu.GetMainFrame().GetSprite().Resize(newWidth, newHeight);
                Close();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> Close());

        this.setLayout(new GridLayout(3,2));
        this.add(newImageWidthLabel);
        this.add(newImageHeightLabel);
        this.add(newImageWidth);
        this.add(newImageHeight);
        this.add(okButton);
        this.add(cancelButton);
        this.pack();
    }

    void Close() {
        this.setVisible(false);
        this.dispose();
    }
}