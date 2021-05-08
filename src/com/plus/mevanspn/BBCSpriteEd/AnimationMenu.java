package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;

final public class AnimationMenu extends JMenu {
    public AnimationMenu(MainFrame parent) {
        super("Animation");

        JMenuItem addFrame = new JMenuItem("Add Frame");
        addFrame.addActionListener(e -> {
            parent.GetSprite().AddFrame();
            parent.UpdateTimeline();
        });

        JMenuItem nextFrame = new JMenuItem("Next Frame");
        nextFrame.addActionListener(e -> {
            final BBCSprite currentSprite = parent.GetSprite();
            final int currentFrameIndex = currentSprite.GetCurrentFrameIndex();
            if (currentFrameIndex < currentSprite.GetFrameCount() - 1) {
                currentSprite.SetActiveFrame(currentSprite.GetFrame(currentFrameIndex + 1));
                parent.UpdateTimeline();
            }
        });

        JMenuItem previousFrame = new JMenuItem("Previous Frame");
        previousFrame.addActionListener(e -> {
            final BBCSprite currentSprite = parent.GetSprite();
            final int currentFrameIndex = currentSprite.GetCurrentFrameIndex();
            if (currentFrameIndex > 0) {
                currentSprite.SetActiveFrame(currentSprite.GetFrame(currentFrameIndex - 1));
                parent.UpdateTimeline();
            }
        });

        this.add(addFrame);
        this.add(new JSeparator());
        this.add(nextFrame);
        this.add(previousFrame);
    }
}
