package com.plus.mevanspn.BBCSpriteEd.ui.panels.TimelinePanel;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.menus.AnimationMenu;

import javax.swing.*;
import java.awt.*;

final public class TimelinePanel extends JScrollPane {
    public TimelinePanel(MainFrame mainFrame, int previewHeight) {
        super();
        this.mainFrame = mainFrame;
        this.previewHeight = (previewHeight > 32) ? (int) (Math.ceil(previewHeight / 32.0f) * previewHeight) : 64;
        initialiseComponents();
        registerTimelinePopupWithAppBar();
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

    public MainFrame GetMainFrame() {
        return mainFrame;
    }

    int GetPreviewHeight() {
        return previewHeight;
    }

    private void registerTimelinePopupWithAppBar() {
        TimelinePanelViewportViewPopup popup = viewportView.GetViewportPopup();
        AnimationMenu aniMenu = mainFrame.GetMenuBar().GetAnimationMenu();
        aniMenu.RegisterModuleMenuItems(popup.GetMenuItems());
    }

    private void initialiseComponents() {
        this.setPreferredSize(new Dimension(640, previewHeight + (TimelinePanelViewportView.SEPARATOR_WIDTH * 2)));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.viewportView = new TimelinePanelViewportView(this);
        this.setViewportView(this.viewportView);
    }

    private final MainFrame mainFrame;
    private TimelinePanelViewportView viewportView;
    private final int previewHeight;
}
