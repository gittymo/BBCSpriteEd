package com.plus.mevanspn.BBCSpriteEd.ui;

import com.plus.mevanspn.BBCSpriteEd.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import javax.swing.*;

final public class AnimationMenu extends JMenu {
    public AnimationMenu(MainFrame parent) {
        super("Animation");

        JMenuItem addFrame = new JMenuItem("Add Frame");
        addFrame.addActionListener(e -> {
            parent.GetSprite().AddFrame();
            parent.UpdateTimeline();
            parent.GetOnionSkinManager().GetToolbar().UpdateControls();
        });

        JMenuItem nextFrame = new JMenuItem("Next Frame");
        nextFrame.addActionListener(e -> {
            final BBCSprite currentSprite = parent.GetSprite();
            final int currentFrameIndex = currentSprite.GetCurrentFrameIndex();
            if (currentFrameIndex < currentSprite.GetFrameCount() - 1) {
                currentSprite.SetActiveFrame(currentSprite.GetFrame(currentFrameIndex + 1));
                parent.UpdateTimeline();
                parent.GetOnionSkinManager().GetToolbar().UpdateControls();
            }
        });

        JMenuItem previousFrame = new JMenuItem("Previous Frame");
        previousFrame.addActionListener(e -> {
            final BBCSprite currentSprite = parent.GetSprite();
            final int currentFrameIndex = currentSprite.GetCurrentFrameIndex();
            if (currentFrameIndex > 0) {
                currentSprite.SetActiveFrame(currentSprite.GetFrame(currentFrameIndex - 1));
                parent.UpdateTimeline();
                parent.GetOnionSkinManager().GetToolbar().UpdateControls();
            }
        });

        this.add(addFrame);
        this.add(new JSeparator());
        this.add(nextFrame);
        this.add(previousFrame);
    }
}
