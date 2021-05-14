package com.plus.mevanspn.BBCSpriteEd;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.*;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.ImagePanel;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.PaintBrushButton;
import com.plus.mevanspn.BBCSpriteEd.ui.OnionSkinManager.OnionSkinManager;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar.ColourPickerToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.DrawingToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel.PreviewPanel;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.TimelinePanel.TimelinePanel;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.EditToolbar;

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
        this.drawingToolbar = new DrawingToolbar(this);

        this.editToolbar = new EditToolbar(this);

        ToolbarsContainer toolbarsContainer = new ToolbarsContainer(this);
        getContentPane().add(toolbarsContainer, BorderLayout.NORTH);

        this.timelinePanel = new TimelinePanel(this, this.timelinePreviewHeight);
        getContentPane().add(this.timelinePanel, BorderLayout.SOUTH);

        this.previewPanel = new PreviewPanel(this);
        getContentPane().add(this.previewPanel, BorderLayout.EAST);

        this.pack();
    }

    public void LoadSprite(BBCSprite newSprite) {
        sprite = newSprite;
        if (sprite != null && sprite.GetActiveFrame() != null) {
            ResizeImagePane();
            colourPickerToolbar.CreatePaletteUsingSprite(sprite);
            timelinePanel.SetSprite(sprite);
            previewPanel.SetFrame(0);
            onionSkinManager.ResetOnionSkinControls();
            ((PaintBrushButton) drawingToolbar.GetButton("paintbrush")).Reset();
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
        return (drawingToolbar.GetActiveButton() == drawingToolbar.GetButton("eraser")) ?
                (byte) GetSprite().GetDisplayMode().colours.length : colourPickerToolbar.GetActiveColourIndex();
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
        if (colourPickerToolbar != null) colourPickerToolbar.repaint();
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

    public EditToolbar GetEditToolbar() { return editToolbar; }

    public MainFrameMenuBar GetMenuBar() { return mainFrameMenuBar; }

    public BBCSprite GetSprite() {
        return sprite;
    }

    public PreviewPanel GetPreviewPanel() { return previewPanel; }

    public ImagePanel GetImagePanel() { return imagePanel; }

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
    private EditToolbar editToolbar;
    private TimelinePanel timelinePanel;
    private MainFrameMenuBar mainFrameMenuBar;
    private OnionSkinManager onionSkinManager;
    private PreviewPanel previewPanel;

    public static Color[] maskColours = new Color[] { new Color(176, 176, 176), new Color( 176, 192, 192) };
}
