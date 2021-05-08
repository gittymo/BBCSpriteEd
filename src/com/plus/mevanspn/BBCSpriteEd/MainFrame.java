package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import java.awt.*;

final public class MainFrame extends JFrame {
    public MainFrame() {
        super("BBC Sprite Editor - By Morgan Evans");
        this.sprite = null;
        this.onionSkinManager = null;
        this.zoom = 16;
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

        this.scrollPane = new JScrollPane();
        this.imagePanel = new ImagePanel(this);
        this.scrollPane.setViewportView(imagePanel);
        this.scrollPane.setPreferredSize(new Dimension(640, 480));
        getContentPane().add(this.scrollPane,BorderLayout.CENTER);

        this.colourPickerToolbar = new ColourPickerToolbar(this);
        this.drawingToolbar = new DrawingToolbar(this);
        ToolbarsContainer toolbarsContainer = new ToolbarsContainer(this);
        getContentPane().add(toolbarsContainer, BorderLayout.WEST);

        this.timelinePanel = new TimelinePanel(this);
        getContentPane().add(this.timelinePanel, BorderLayout.SOUTH);
    }

    public void LoadSprite(BBCSprite newSprite) {
        if (onionSkinManager != null) onionSkinManager.Quit();
        sprite = newSprite;
        if (sprite != null && sprite.GetActiveFrame() != null) {
            ResizeImagePane();
            colourPickerToolbar.CreatePaletteUsingSprite(sprite);
            timelinePanel.SetSprite(sprite);
            onionSkinManager = new OnionSkinManager(this);
            onionSkinManager.start();
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
        if (drawingToolbar.GetActiveButton() == drawingToolbar.buttonEraser) return (byte) GetSprite().GetDisplayMode().colours.length;
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
        timelinePanel.Refresh();
    }

    public OnionSkinManager GetOnionSkinManager() {
        return onionSkinManager;
    }

    public void Quit() {
        System.exit(-1);
    }

    private BBCSprite sprite;
    private OnionSkinManager onionSkinManager;
    private float zoom;

    private ImagePanel imagePanel;
    private JScrollPane scrollPane;
    private ColourPickerToolbar colourPickerToolbar;
    private DrawingToolbar drawingToolbar;
    private TimelinePanel timelinePanel;
    private MainFrameMenuBar mainFrameMenuBar;

    public static Color[] maskColours = new Color[] { new Color(176, 176, 176), new Color( 176, 192, 192) };
}
