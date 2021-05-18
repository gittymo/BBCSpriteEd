package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

import com.plus.mevanspn.BBCSpriteEd.ui.panels.FileInfoPanel;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.TimelinePanel.TimelinePanel;

import javax.swing.*;
import java.awt.*;

final public class TimelineStatusContainer extends JPanel {
    public TimelineStatusContainer(TimelinePanel timelinePanel, FileInfoPanel fileInfoPanel) {
        super();
        this.setLayout(new BorderLayout(4, 4));
        add(timelinePanel, BorderLayout.NORTH);
        add(fileInfoPanel, BorderLayout.SOUTH);
    }
}
