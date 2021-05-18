package com.plus.mevanspn.BBCSpriteEd.ui.panels.TimelinePanel;

import javax.swing.*;
import java.util.LinkedList;

public final class TimelinePanelViewportViewPopup extends JPopupMenu {
    public TimelinePanelViewportViewPopup(TimelinePanelViewportView parent) {
        this.parent = parent;
        menuItems = new LinkedList<>();

        addFrame = new JMenuItem("Add Frame");
        duplicateFrame = new JMenuItem("Duplicate Frame");
        duplicateFrameEnd = new JMenuItem("Duplicate (Put At End)");
        insertFrameAfter = new JMenuItem("Insert Frame (after)");
        insertFrameBefore = new JMenuItem("Insert Frame (before)");
        deleteFrame = new JMenuItem("Delete Frame");
        duplicateFrame.addActionListener(e -> parent.DuplicateFrame(false));
        duplicateFrameEnd.addActionListener(e -> parent.DuplicateFrame(true));
        insertFrameAfter.addActionListener(e -> parent.InsertFrameAfter());
        insertFrameBefore.addActionListener(e -> parent.InsertFrameBefore());
        deleteFrame.addActionListener(e -> parent.DeleteFrame());
        addFrame.addActionListener(e -> parent.AddFrame());

        this.add(addFrame);
        this.add(insertFrameAfter);
        this.add(insertFrameBefore);
        this.add(deleteFrame);
        this.add(duplicateFrame);
        this.add(duplicateFrameEnd);
    }

    LinkedList<JMenuItem> GetMenuItems() {
        return menuItems;
    }

    @Override
    public JMenuItem add(JMenuItem menuItem) {
        super.add(menuItem);
        menuItems.add(menuItem);
        return menuItem;
    }

    TimelinePanelViewportView parent;
    LinkedList<JMenuItem> menuItems;
    JMenuItem addFrame, duplicateFrame, duplicateFrameEnd, insertFrameAfter, insertFrameBefore, deleteFrame;
}
