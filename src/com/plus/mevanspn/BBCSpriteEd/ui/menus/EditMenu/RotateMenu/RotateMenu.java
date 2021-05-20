package com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu.RotateMenu;

import com.plus.mevanspn.BBCSpriteEd.image.*;
import com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu.EditMenu;

import javax.swing.*;

final public class RotateMenu extends JMenu {
    public RotateMenu(EditMenu editMenu) {
        super("Rotate");
        this.editMenu = editMenu;

        JMenuItem rotateFortyFive = new JMenuItem("Rotate 45 Degrees");
        rotateFortyFive.addActionListener(e -> RotateFrame(45));
        add(rotateFortyFive);

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
        if (editMenu != null && editMenu.GetMainFrame() != null && editMenu.GetMainFrame().GetSprite() != null) {
            final BBCSprite sprite = editMenu.GetMainFrame().GetSprite();
            sprite.RecordHistory();
            if (sprite.GetActiveFrame() != null && sprite.GetActiveFrame().GetRenderedImage() != null) {
                sprite.GetActiveFrame().GetRenderedImage().Rotate(degrees);
            }
        }
    }

    RotateMenu GetThisMenu() {
        return this;
    }

    EditMenu GetEditMenu() {
        return editMenu;
    }

    private final EditMenu editMenu;
}
