package com.plus.mevanspn.BBCSpriteEd.ui.TimelinePanel;

import javax.swing.*;
import java.util.LinkedList;

public final class TimelinePanelViewportViewPopup extends JPopupMenu {
    public TimelinePanelViewportViewPopup(TimelinePanelViewportView parent) {
        this.parent = parent;
        menuItems = new LinkedList<>();

        addFrame = new JMenuItem("Add Frame");
        duplicateFrame = new JMenuItem("Duplicate Frame");
        duplicateFrameEnd = new JMenuItem("Duplicate (At End)");
        insertFrame = new JMenuItem("Insert Frame (after)");
        deleteFrame = new JMenuItem("Delete Frame");
        duplicateFrame.addActionListener(e -> parent.DuplicateFrame(false));
        duplicateFrameEnd.addActionListener(e -> parent.DuplicateFrame(true));
        insertFrame.addActionListener(e -> parent.InsertFrame());
        deleteFrame.addActionListener(e -> parent.DeleteFrame());
        addFrame.addActionListener(e -> parent.AddFrame());

        this.add(addFrame);
        this.add(insertFrame);
        this.add(deleteFrame);
        this.add(duplicateFrame);
        this.add(duplicateFrame);
    }

    @Override
    public JMenuItem add(JMenuItem menuItem) {
        super.add(menuItem);
        menuItems.add(menuItem);
        return menuItem;
    }

    public LinkedList<JMenuItem> GetMenuItems() {
        return menuItems;
    }

    TimelinePanelViewportView parent;
    LinkedList<JMenuItem> menuItems;
    JMenuItem addFrame, duplicateFrame, duplicateFrameEnd, insertFrame, deleteFrame;
}
