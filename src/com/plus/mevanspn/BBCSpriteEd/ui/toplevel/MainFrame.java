package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSpriteFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressListener;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.FileInfoPanel;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.ImagePanel;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.config.ConfigPanel;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.config.OnionSkinConfigPanel;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.DrawingToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.PaintBrushButton;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.ColourPickerToolbar.ColourPickerToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.DrawingToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel.PreviewPanel;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.TimelinePanel.TimelinePanel;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.EditToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.OnionSkinManagerToolbar;

import javax.swing.*;
import java.awt.*;

final public class MainFrame extends JFrame {
    public MainFrame() {
        super("BBC Sprite Editor - By Morgan Evans");
        this.sprite = null;
        this.zoom = 16;
        this.timelinePreviewHeight = 32;

        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoadSprite(new BBCSprite(24, 24, BBCSprite.DisplayMode.ModeOne, this));
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

        this.colourPickerToolbar = new ColourPickerToolbar(this);
        this.drawingToolbar = new DrawingToolbar(this);

        this.editToolbar = new EditToolbar(this);

        this.onionSkinManagerToolbar = new OnionSkinManagerToolbar(this);

        ToolbarsContainer toolbarsContainer = new ToolbarsContainer(this);
        ImagePanelContainer imagePanelContainer = new ImagePanelContainer(scrollPane, toolbarsContainer);
        getContentPane().add(imagePanelContainer, BorderLayout.CENTER);

        this.timelinePanel = new TimelinePanel(this, this.timelinePreviewHeight);
        this.fileInfoPanel = new FileInfoPanel(this);
        TimelineStatusContainer timelineStatusContainer = new TimelineStatusContainer(timelinePanel, fileInfoPanel);
        getContentPane().add(timelineStatusContainer, BorderLayout.SOUTH);

        this.previewPanel = new PreviewPanel(this);
        PreviewPanelContainer previewPanelContainer = new PreviewPanelContainer(this.previewPanel);
        getContentPane().add(previewPanelContainer, BorderLayout.EAST);

        this.optionsDialog = new OptionsDialog(this);
        this.optionsDialog.AddConfigPanel(new OnionSkinConfigPanel());
        this.pack();
    }

    public void LoadSprite(BBCSprite newSprite) {
        sprite = newSprite;
        if (sprite != null && sprite.GetActiveFrame() != null) {
            if (sprite.GetFilePath() != null) setTitle("BBC Sprite Editor - By Morgan Evans.  Editing File: " + sprite.GetFilePath().getName());
            else setTitle("BBC Sprite Editor - By Morgan Evans.  Editing New File");
            ResizeImagePane();
            colourPickerToolbar.CreatePaletteUsingSprite(sprite);
            timelinePanel.SetSprite(sprite);
            previewPanel.SetFrame(0);
            ((PaintBrushButton) drawingToolbar.GetButton("paintbrush")).Reset();
            RefreshPanels();
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
        if (fileInfoPanel != null) fileInfoPanel.Refresh();
        if (previewPanel != null) previewPanel.Refresh();
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

    public DrawingToolbar GetDrawingToolbar() { return drawingToolbar; }

    public boolean IsActiveDrawingToolbarButton(String buttonKeyValue) {
        return GetDrawingToolbar().IsActiveButton(buttonKeyValue);
    }

    public DrawingToolbarButton GetActiveDrawingToolbarButton() {
        return GetDrawingToolbar().GetActiveButton();
    }

    public EditToolbar GetEditToolbar() { return editToolbar; }

    public OnionSkinManagerToolbar GetOnionSkinManagerToolbar() { return onionSkinManagerToolbar; }

    public MainFrameMenuBar GetMenuBar() { return mainFrameMenuBar; }

    public BBCSprite GetSprite() {
        return sprite;
    }

    public PreviewPanel GetPreviewPanel() { return previewPanel; }

    public ImagePanel GetImagePanel() { return imagePanel; }

    public FileInfoPanel GetFileInfoPanel() { return fileInfoPanel; }

    public OptionsDialog GetOptionsDialog() { return optionsDialog; }

    public ConfigPanel GetConfigPanel(String id) { return optionsDialog.GetConfigPanel(id); }

    public void UpdateTimeline() {
        if (sprite != null) timelinePanel.SetActiveFrame(sprite.GetCurrentFrameIndex());
        timelinePanel.Refresh();
    }

    public void AddKeyPressListener(KeyPressListener keyPressListener) {
        GetImagePanel().AddKeyPressListener(keyPressListener);
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
    private OnionSkinManagerToolbar onionSkinManagerToolbar;
    private PreviewPanel previewPanel;
    private FileInfoPanel fileInfoPanel;
    private OptionsDialog optionsDialog;

    public static Color[] maskColours = new Color[] { new Color(176, 176, 176), new Color( 176, 192, 192) };
}
