package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import java.awt.*;

final public class MainFrame extends JFrame {
    public MainFrame() {
        super("BBC Sprite Editor - By Morgan Evans");
        this.sprite = null;
        this.zoom = 16;
        initComponents();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LoadSprite(new BBCSprite(24, 24, BBCSprite.DisplayMode.ModeOne, this));
    }

    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    private void initComponents() {
        this.borderLayout = new BorderLayout();
        this.setLayout(this.borderLayout);

        this.scrollPane = new JScrollPane();
        this.imagePanel = new ImagePanel(this);
        this.scrollPane.setViewportView(imagePanel);
        this.scrollPane.setPreferredSize(new Dimension(640, 480));
        getContentPane().add(this.scrollPane,BorderLayout.CENTER);

        this.colourPickerToolbar = new ColourPickerToolbar(this);
        getContentPane().add(this.colourPickerToolbar, BorderLayout.WEST);

        this.timelinePanel = new TimelinePanel(this);
        getContentPane().add(this.timelinePanel, BorderLayout.SOUTH);

        this.menubar = new JMenuBar();
        this.menubar.add(new FileMenu(this));
        this.menubar.add(new ZoomMenu(this));
        this.menubar.add(new AnimationMenu(this));
        setJMenuBar(this.menubar);
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
        return colourPickerToolbar.GetActiveColourIndex();
    }

    public BBCSpriteFrame GetActiveFrame() {
        return sprite != null ? sprite.GetActiveFrame() : null;
    }

    public void SetActiveFrameIndex(int index) {
        sprite.SetActiveFrame(sprite.GetFrame(index));
        RefreshImagePane();
        UpdateTimeline();
    }

    public void RefreshImagePane() {
        if (imagePanel != null) imagePanel.repaint();
    }

    public void ResizeImagePane() {
        imagePanel.setPreferredSize(new Dimension(
                (int) (sprite.GetWidth() * zoom * sprite.GetHorizontalPixelRatio()) + 8,
                (int) (sprite.GetHeight() * zoom) + 8));
        scrollPane.revalidate();
        imagePanel.repaint();
    }

    public ImagePanel GetImagePane() {
        return imagePanel;
    }

    public BBCSprite GetSprite() {
        return sprite;
    }

    public void UpdateTimeline() {
        timelinePanel.Refresh();
    }

    public void Quit() {
        System.exit(-1);
    }

    private BBCSprite sprite;
    private ImagePanel imagePanel;
    private JScrollPane scrollPane;
    private ColourPickerToolbar colourPickerToolbar;
    private TimelinePanel timelinePanel;
    private BorderLayout borderLayout;
    private JMenuBar menubar;
    private float zoom;

    public static Color[] maskColours = new Color[] { Color.LIGHT_GRAY, Color.DARK_GRAY };
}
