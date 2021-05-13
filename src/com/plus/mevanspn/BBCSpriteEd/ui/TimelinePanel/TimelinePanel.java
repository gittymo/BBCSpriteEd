package com.plus.mevanspn.BBCSpriteEd.ui.TimelinePanel;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.AnimationMenu;

import javax.swing.*;
import java.awt.*;

final public class TimelinePanel extends JScrollPane {
    public TimelinePanel(MainFrame parent, int previewHeight) {
        super();
        this.parent = parent;
        this.previewHeight = (previewHeight > 32) ? (int) (Math.ceil(previewHeight / 32.0f) * previewHeight) : 64;
        initialiseComponents();
        registerTimelinePopupWithAppBar();
    }

    private void initialiseComponents() {
        this.setPreferredSize(new Dimension(640, previewHeight + (TimelinePanelViewportView.SEPARATOR_WIDTH * 2)));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.viewportView = new TimelinePanelViewportView(this);
        this.setViewportView(this.viewportView);
    }

    public void Refresh() {
        if (viewportView != null) {
            viewportView.Refresh();
            revalidate();
        }
    }

    public void SetActiveFrame(int frameIndex) {
        viewportView.SetClickPointToActiveFrame(frameIndex);
    }

    public void SetSprite(BBCSprite sprite) {
        this.viewportView.SetSprite(sprite);
        Refresh();
    }

    public int GetPreviewHeight() {
        return previewHeight;
    }

    public MainFrame GetParent() {
        return parent;
    }

    private void registerTimelinePopupWithAppBar() {
        TimelinePanelViewportViewPopup popup = viewportView.GetViewportPopup();
        AnimationMenu aniMenu = parent.GetMenuBar().GetAnimationMenu();
        aniMenu.RegisterModuleMenuItems(popup.GetMenuItems());
    }

    private final MainFrame parent;
    private TimelinePanelViewportView viewportView;
    private int previewHeight;
}
