package com.plus.mevanspn.BBCSpriteEd;

import com.plus.mevanspn.BBCSpriteEd.ui.OnionSkinManager.OnionSkinManager;
import com.plus.mevanspn.BBCSpriteEd.ui.*;
import com.plus.mevanspn.BBCSpriteEd.ui.ColourPicker.ColourPickerToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar.DrawingToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.TimelinePanel.TimelinePanel;

import javax.swing.*;
import java.awt.*;

final public class MainFrame extends JFrame {
    public MainFrame() {
        super("BBC Sprite Editor - By Morgan Evans");
        this.sprite = null;
        this.onionSkinManager = null;
        this.zoom = 16;
        this.timelinePreviewHeight = 32;

        initComponents();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoadSprite(new BBCSprite(24, 24, BBCSprite.DisplayMode.ModeOne, this));
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    private void initComponents() {
        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        this.mainFrameMenuBar = new MainFrameMenuBar(this);
        setJMenuBar(this.mainFrameMenuBar);

        onionSkinManager = new OnionSkinManager(this);
        onionSkinManager.start();

        this.scrollPane = new JScrollPane();
        this.imagePanel = new ImagePanel(this);
        this.scrollPane.setViewportView(imagePanel);
        this.scrollPane.setPreferredSize(new Dimension(640, 480));
        getContentPane().add(this.scrollPane,BorderLayout.CENTER);

        this.colourPickerToolbar = new ColourPickerToolbar(this);
        this.drawingToolbar = new DrawingToolbar();
        ToolbarsContainer toolbarsContainer = new ToolbarsContainer(this);
        getContentPane().add(toolbarsContainer, BorderLayout.NORTH);

        this.timelinePanel = new TimelinePanel(this, this.timelinePreviewHeight);
        getContentPane().add(this.timelinePanel, BorderLayout.SOUTH);
    }

    public void LoadSprite(BBCSprite newSprite) {
        sprite = newSprite;
        if (sprite != null && sprite.GetActiveFrame() != null) {
            ResizeImagePane();
            colourPickerToolbar.CreatePaletteUsingSprite(sprite);
            timelinePanel.SetSprite(sprite);
        }
    }

    public float GetZoom() {
        return zoom;
    }

    public void SetZoom(float newZoom) {
        if (newZoom >= 0.25 && newZoom <= 32 && newZoom != zoom) zoom = newZoom;
        ResizeImagePane();
    }

    public byte GetActiveColourIndex() {
        if (drawingToolbar.GetActiveButton() == drawingToolbar.buttons.get("eraser")) return (byte) GetSprite().GetDisplayMode().colours.length;
        return colourPickerToolbar.GetActiveColourIndex();
    }

    public BBCSpriteFrame GetActiveFrame() {
        return sprite != null ? sprite.GetActiveFrame() : null;
    }

    public void SetActiveFrameIndex(int index) {
        sprite.SetActiveFrame(sprite.GetFrame(index));
        RefreshPanels();
    }

    public void RefreshPanels() {
        if (imagePanel != null) imagePanel.repaint();
        if (timelinePanel != null) UpdateTimeline();
    }

    public void ResizeImagePane() {
        imagePanel.setPreferredSize(new Dimension(
                (int) (sprite.GetWidth() * zoom * sprite.GetHorizontalPixelRatio()) + 8,
                (int) (sprite.GetHeight() * zoom) + 8));
        scrollPane.revalidate();
        imagePanel.repaint();
    }

    public ColourPickerToolbar GetColourPickerToolbar() {
        return colourPickerToolbar;
    }

    public DrawingToolbar GetDrawingToolbar() {
        return drawingToolbar;
    }

    public MainFrameMenuBar GetMenuBar() { return mainFrameMenuBar; }

    public BBCSprite GetSprite() {
        return sprite;
    }

    public void UpdateTimeline() {
        if (sprite != null) timelinePanel.SetActiveFrame(sprite.GetCurrentFrameIndex());
        timelinePanel.Refresh();
    }

    public OnionSkinManager GetOnionSkinManager() {
        return onionSkinManager;
    }

    public void Quit() {
        System.exit(-1);
    }

    private BBCSprite sprite;
    private float zoom;
    private final int timelinePreviewHeight;

    private ImagePanel imagePanel;
    private JScrollPane scrollPane;
    private ColourPickerToolbar colourPickerToolbar;
    private DrawingToolbar drawingToolbar;
    private TimelinePanel timelinePanel;
    private MainFrameMenuBar mainFrameMenuBar;
    private OnionSkinManager onionSkinManager;

    public static Color[] maskColours = new Color[] { new Color(176, 176, 176), new Color( 176, 192, 192) };
}
