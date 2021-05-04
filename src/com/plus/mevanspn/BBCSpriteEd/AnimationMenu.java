package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class AnimationMenu extends JMenu {
    public AnimationMenu(MainFrame parent) {
        super("Animation");
        this.parent = parent;

        JMenuItem addFrame = new JMenuItem("Add Frame");
        addFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.GetSprite().AddFrame();
                parent.UpdateTimeline();
            }
        });

        JMenuItem nextFrame = new JMenuItem("Next Frame");
        nextFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final BBCSprite currentSprite = parent.GetSprite();
                final int currentFrameIndex = currentSprite.GetCurrentFrameIndex();
                if (currentFrameIndex < currentSprite.GetFrameCount() - 1) {
                    currentSprite.SetActiveFrame(currentSprite.GetFrame(currentFrameIndex + 1));
                    parent.UpdateTimeline();
                }
            }
        });

        JMenuItem previousFrame = new JMenuItem("Previous Frame");
        previousFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final BBCSprite currentSprite = parent.GetSprite();
                final int currentFrameIndex = currentSprite.GetCurrentFrameIndex();
                if (currentFrameIndex > 0) {
                    currentSprite.SetActiveFrame(currentSprite.GetFrame(currentFrameIndex - 1));
                    parent.UpdateTimeline();
                }
            }
        });

        this.add(addFrame);
        this.add(new JSeparator());
        this.add(nextFrame);
        this.add(previousFrame);
    }

    private MainFrame parent;
}
