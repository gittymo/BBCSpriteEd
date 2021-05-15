package com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu.RotateMenu;

import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu.EditMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

final public class RotateMenu extends JMenu {
    public RotateMenu(EditMenu editMenu) {
        super("Rotate");
        this.editMenu = editMenu;

        JMenuItem rotateFourtyFive = new JMenuItem("Rotate 45 Degrees");
        rotateFourtyFive.addActionListener(e -> RotateFrame(45));
        add(rotateFourtyFive);

        JMenuItem rotateNinety = new JMenuItem("Rotate 90 Degrees");
        rotateNinety.addActionListener(e -> RotateFrame(90));
        add(rotateNinety);

        JMenuItem rotateOneEighty = new JMenuItem("Rotate 180 Degrees");
        rotateOneEighty.addActionListener(e -> RotateFrame(180));
        add(rotateOneEighty);

        JMenuItem rotateTwoSeventy = new JMenuItem("Rotate 270 Degrees");
        rotateTwoSeventy.addActionListener(e -> RotateFrame(270));
        add(rotateTwoSeventy);

        add(new JSeparator());

        JMenuItem rotateArbitrary = new JMenuItem("Rotate...");
        rotateArbitrary.addActionListener(e -> {
            RotateImageDialog rotateImageDialog = new RotateImageDialog(GetThisMenu());
            rotateImageDialog.setVisible(true);
            if (!rotateImageDialog.Cancelled()) {
                RotateFrame(rotateImageDialog.GetRotationDegrees());
            }
        });
        add(rotateArbitrary);
    }

    void RotateFrame(double degrees) {
        if (degrees < 0) {
            while (degrees < 0) degrees += 360;
        } else if (degrees >= 360) {
            while (degrees >= 360) degrees -= 360;
        }
        final BBCSprite sprite = editMenu.GetMainFrame().GetSprite();
        sprite.RecordHistory();
        if (sprite != null) {
            BBCSpriteFrame spriteFrame = editMenu.GetMainFrame().GetActiveFrame();
            if (spriteFrame != null) {
                final BBCImage sourceImage = spriteFrame.GetRenderedImage();
                BBCImage newFrameImage = new BBCImage(sprite);
                final int originX = newFrameImage.getWidth() / 2;
                final int originY = newFrameImage.getHeight() / 2;
                final double rotation = Math.toRadians(degrees);
                final AffineTransform tx = AffineTransform.getRotateInstance(rotation, originX, originY);
                final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                newFrameImage.getGraphics().drawImage(op.filter(sourceImage, null), 0, 0, null);
                spriteFrame.SetRenderedImage(newFrameImage);
            }
        }
    }

    RotateMenu GetThisMenu() {
        return this;
    }

    EditMenu GetEditMenu() {
        return editMenu;
    }

    private EditMenu editMenu;
}
