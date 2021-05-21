package com.plus.mevanspn.BBCSpriteEd.ui.menus;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;

import javax.swing.*;

final public class AnimationMenu extends AppMenu {
    public AnimationMenu(MainFrame parent) {
        super("Animation");

        JMenuItem nextFrame = new JMenuItem("Next Frame");
        nextFrame.addActionListener(e -> {
            final BBCSprite currentSprite = parent.GetSprite();
            final int currentFrameIndex = currentSprite.GetActiveFrameIndex();
            if (currentFrameIndex < currentSprite.GetFrameCount() - 1) {
                currentSprite.SetActiveFrame(currentSprite.GetFrame(currentFrameIndex + 1));
                parent.UpdateTimeline();
                // parent.GetOnionSkinManager().GetToolbar().UpdateControls();
            }
        });

        JMenuItem previousFrame = new JMenuItem("Previous Frame");
        previousFrame.addActionListener(e -> {
            final BBCSprite currentSprite = parent.GetSprite();
            final int currentFrameIndex = currentSprite.GetActiveFrameIndex();
            if (currentFrameIndex > 0) {
                currentSprite.SetActiveFrame(currentSprite.GetFrame(currentFrameIndex - 1));
                parent.UpdateTimeline();
                // parent.GetOnionSkinManager().GetToolbar().UpdateControls();
            }
        });

        this.add(nextFrame);
        this.add(previousFrame);
    }
}
