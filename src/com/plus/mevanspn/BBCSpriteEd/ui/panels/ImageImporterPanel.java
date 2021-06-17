package com.plus.mevanspn.BBCSpriteEd.ui.panels;

import com.plus.mevanspn.BBCSpriteEd.image.BBCImage;
import com.plus.mevanspn.BBCSpriteEd.image.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.image.converters.*;
import com.plus.mevanspn.BBCSpriteEd.ui.components.ToggleButton;
import com.plus.mevanspn.BBCSpriteEd.ui.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;

public final class ImageImporterPanel extends JDialog {
    public ImageImporterPanel(MainFrame mainFrame) {
        super(mainFrame,"Import Image As Sprite...");
        this.mainFrame = mainFrame;
        getContentPane().setLayout(new BorderLayout());
        this.importerControlsPanel = new ImporterControlsPanel(this);
        getContentPane().add(this.importerControlsPanel, BorderLayout.EAST);
        this.importPreviewPanel = new ImportPreviewPanel(this);
        getContentPane().add(this.importPreviewPanel, BorderLayout.CENTER);
        this.sourceImage = null;
        this.pack();
    }

    void LoadImage(String filename) {
        try {
            this.sourceImage = ImageIO.read(new File(filename));
            this.importPreviewPanel.Update();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    void UpdateImage() {
        this.importPreviewPanel.Update();
    }

    BufferedImage GetSourceImage() {
        return sourceImage;
    }

    private ImporterControlsPanel importerControlsPanel;
    private ImportPreviewPanel importPreviewPanel;
    private BufferedImage sourceImage;
    private MainFrame mainFrame;

    class ImporterControlsPanel extends JPanel {
        ImporterControlsPanel(ImageImporterPanel imageImporterPanel) {
            super();
            this.imageImporterPanel = imageImporterPanel;
            this.setLayout(new BorderLayout());
            this.add(new FileLoaderPanel(), BorderLayout.NORTH);
            this.add(new SpriteSourceOptionsPanel(), BorderLayout.CENTER);
            this.add(new DialogConfirmationPanel(), BorderLayout.SOUTH);
            pack();
        }

        private ImageImporterPanel imageImporterPanel;

        class SpriteSourceOptionsPanel extends JPanel {
            SpriteSourceOptionsPanel() {
                super();
                ButtonGroup sourceOptionsButtonGroup = new ButtonGroup();

                JRadioButton wholeImageRadioButton = new JRadioButton("Whole Image");
                sourceOptionsButtonGroup.add(wholeImageRadioButton);

                JRadioButton selectionRadioButton = new JRadioButton("Selection");
                sourceOptionsButtonGroup.add(selectionRadioButton);

                JRadioButton tilesRadioButton = new JRadioButton("Tiles");
                sourceOptionsButtonGroup.add(tilesRadioButton);

                add(wholeImageRadioButton);
                add(selectionRadioButton);
                add(tilesRadioButton);

                pack();
            }
        }

        class FileLoaderPanel extends JPanel {
            FileLoaderPanel() {
                JButton imageFileChooserButton = new JButton("Open File...");
                imageFileChooserButton.addActionListener(e -> {
                    JFileChooser imageFileChooser = new JFileChooser();
                    imageFileChooser.setFileFilter(new FileNameExtensionFilter("Image file types","JPG","JPEG","PNG","BMP","PBM"));
                    int res = imageFileChooser.showOpenDialog(imageImporterPanel);
                    if (res == JFileChooser.APPROVE_OPTION) imageImporterPanel.LoadImage(imageFileChooser.getSelectedFile().getAbsolutePath());
                    imageFileChooser.setVisible(false);
                });
                add(imageFileChooserButton);
                pack();
            }
        }

        class DialogConfirmationPanel extends JPanel {
            DialogConfirmationPanel() {
                JButton okButton = new JButton("Import");
                okButton.addActionListener(e -> {
                    imageImporterPanel.setVisible(false);
                    BufferedImage convertedImage = importPreviewPanel.importPreviewImagePanel.GetConvertedSprite();
                    BBCSprite convertedSprite = new BBCSprite(convertedImage.getWidth(), convertedImage.getHeight(),
                            importPreviewPanel.importPreviewImagePanelControls.GetDisplayMode(), mainFrame);
                    BBCImage activeSpriteImage = convertedSprite.GetActiveFrame().GetRenderedImage();
                    byte[] convertedImageRasterData = new byte[convertedImage.getWidth() * convertedImage.getHeight()];
                    convertedImage.getRaster().getDataElements(0, 0, convertedImage.getWidth(),
                            convertedImage.getHeight(), convertedImageRasterData);
                    activeSpriteImage.getRaster().setDataElements(0, 0, convertedImage.getWidth(),
                            convertedImage.getHeight(), convertedImageRasterData);
                    mainFrame.LoadSprite(convertedSprite);
                });
                add(okButton);

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(e -> {
                    imageImporterPanel.setVisible(false);
                });
                add(cancelButton);
                pack();
            }
        }
    }

    class ImportPreviewPanel extends JPanel {
        ImportPreviewPanel(ImageImporterPanel imageImporterPanel) {
            super();
            this.setBorder(new EmptyBorder(4,4,4,4));
            this.imageImporterPanel = imageImporterPanel;
            setLayout(new BorderLayout());
            this.importPreviewImagePanel = new ImportPreviewImagePanel(this);
            this.importPreviewPanelScrollPane = new JScrollPane(this.importPreviewImagePanel);
            add(importPreviewPanelScrollPane, BorderLayout.CENTER);
            this.importPreviewImagePanelControls = new ImportPreviewImagePanelControls(this);
            add(importPreviewImagePanelControls, BorderLayout.SOUTH);
            pack();
        }

        public void Update() {
            this.revalidate();
            this.importPreviewImagePanel.revalidate();
            this.importPreviewPanelScrollPane.revalidate();
            this.importPreviewImagePanel.repaint();
        }

        public BufferedImage GetImage() {
            return imageImporterPanel.GetSourceImage();
        }

        public ImportPreviewImagePanelControls GetControls() {
            return importPreviewImagePanelControls;
        }

        private JScrollPane importPreviewPanelScrollPane;
        private ImportPreviewImagePanel importPreviewImagePanel;
        private ImageImporterPanel imageImporterPanel;
        private ImportPreviewImagePanelControls importPreviewImagePanelControls;

        class ImportPreviewImagePanel extends JPanel {
            ImportPreviewImagePanel(ImportPreviewPanel importPreviewPanel) {
                super();
                this.importPreviewPanel = importPreviewPanel;
            }

            BufferedImage GetConvertedSprite() {
                final BBCSprite.DisplayMode dm = importPreviewImagePanelControls.GetDisplayMode();
                final IImageConverter imc = importPreviewImagePanelControls.GetConversionMethod();
                return imc.Convert(sourceImage, dm);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                final BufferedImage sourceImage = imageImporterPanel.GetSourceImage();
                if (sourceImage != null) {
                    final ImportPreviewImagePanelControls importPreviewImagePanelControls = importPreviewPanel.GetControls();
                    final float zoom = importPreviewImagePanelControls.GetZoom();
                    final BBCSprite.DisplayMode dm = importPreviewImagePanelControls.GetDisplayMode();
                    final BufferedImage resultingImage = GetConvertedSprite();
                    final int xoffset = (int) (getWidth() - (resultingImage.getWidth() * (zoom * dm.pixelRatio))) / 2;
                    final int yoffset = (int) (getHeight() - (resultingImage.getHeight() * zoom)) / 2;
                    g.drawImage(resultingImage, xoffset, yoffset, (int) (resultingImage.getWidth() * (zoom * dm.pixelRatio)),
                            (int) (resultingImage.getHeight() * zoom), null);
                    if (importPreviewImagePanelControls.ShowGrid() && zoom >= 4) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                1.0f, new float[] { 1.0f, 1.0f}, 1.0f));
                        g2.setColor(new Color(0, 0, 128, 255));
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.33f));
                        for (float x = zoom * dm.pixelRatio; x < resultingImage.getWidth() * zoom * dm.pixelRatio; x += zoom * dm.pixelRatio) {
                            g2.drawLine(xoffset + (int) x, yoffset, xoffset + (int) x, yoffset + (int) (resultingImage.getHeight() * zoom));
                        }
                        for (float y = zoom; y < resultingImage.getHeight() * zoom; y += zoom) {
                            g2.drawLine(xoffset, yoffset + (int) y, xoffset + (int) (resultingImage.getWidth() * zoom * dm.pixelRatio),
                                    yoffset + (int) y);
                        }
                    }
                    g.setColor(Color.BLACK);
                    g.drawRect(xoffset, yoffset, (int) (resultingImage.getWidth() * zoom * dm.pixelRatio), (int) (resultingImage.getHeight() * zoom));
                }
            }

            @Override
            public Dimension getPreferredSize() {
                final BufferedImage sourceImage = imageImporterPanel.GetSourceImage();
                final float zoom = importPreviewImagePanelControls.GetZoom();
                final BBCSprite.DisplayMode dm = importPreviewImagePanelControls.GetDisplayMode();
                return sourceImage != null ?
                        new Dimension((int) (sourceImage.getWidth() * zoom * dm.pixelRatio), (int) (sourceImage.getHeight() * zoom)) :
                        new Dimension(320,256);
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }

            private ImportPreviewPanel importPreviewPanel;
        }

        class ImportPreviewImagePanelControls extends JPanel {
            ImportPreviewImagePanelControls(ImportPreviewPanel importPreviewPanel) {
                super();
                this.importPreviewPanel = importPreviewPanel;
                this.zoom = 1;
                this.showGrid = true;
                this.conversionMethodsJComboBox = new JComboBox<>(new IImageConverter[] {
                        new NoDitherImageConverter(),
                        new OrderedDitherImageConverter(),
                        new RandomDitherImageConverter(),
                        new MyDitherImageConverter(),
                        new FSDiffusionImageConverter()
                });
                this.conversionMethodsJComboBox.addActionListener(e -> {
                    importPreviewPanel.Update();
                });
                this.add(this.conversionMethodsJComboBox);

                this.displayModeJComboBox = new JComboBox<>(BBCSprite.DisplayMode.values());
                this.displayModeJComboBox.addActionListener(e -> {
                    importPreviewPanel.Update();
                });
                this.add(this.displayModeJComboBox);

                ToolbarButton zoomInButton = new ToolbarButton("zoomin.png",new KeyPressEventMatcher('+', true, false, false)) {
                    @Override
                    public void KeyPressed(KeyEvent keyEvent) {
                        if (zoom < 16) zoom = zoom * 2;
                        importPreviewPanel.Update();
                    }

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (zoom < 16) zoom = zoom * 2;
                        importPreviewPanel.Update();
                    }
                };
                this.add(zoomInButton);

                ToolbarButton zoomResetButton = new ToolbarButton("zoomreset.png",new KeyPressEventMatcher('.', true, false, false)) {
                    @Override
                    public void KeyPressed(KeyEvent keyEvent) {
                        if (zoom != 1) {
                            zoom = 1;
                            importPreviewPanel.Update();
                        }
                    }

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (zoom != 1) {
                            zoom = 1;
                            importPreviewPanel.Update();
                        }
                    }
                };
                this.add(zoomResetButton);

                ToolbarButton zoomOutButton = new ToolbarButton("zoomout.png",new KeyPressEventMatcher('-', true, false, false)) {
                    @Override
                    public void KeyPressed(KeyEvent keyEvent) {
                        if (zoom != 1) {
                            if (zoom < 1) zoom = zoom * 2;
                            else zoom = zoom / 2;
                        }
                        importPreviewPanel.Update();
                    }

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (zoom != 1) {
                            if (zoom < 1) zoom = zoom * 2;
                            else zoom = zoom / 2;
                        }
                        importPreviewPanel.Update();
                    }
                };
                this.add(zoomOutButton);

                ToggleButton gridToggleButton = new ToggleButton("showgrid.png","hidegrid.png", new KeyPressEventMatcher('g', false, false, false));
                gridToggleButton.addActionListener(e -> {
                    showGrid = !showGrid;
                    importPreviewPanel.Update();
                });
                this.add(gridToggleButton);

                pack();
            }

            public float GetZoom() { return zoom; }

            public boolean ShowGrid() { return showGrid; }

            public BBCSprite.DisplayMode GetDisplayMode() {
                return (BBCSprite.DisplayMode) displayModeJComboBox.getSelectedItem();
            }

            public IImageConverter GetConversionMethod() {
                return (IImageConverter) conversionMethodsJComboBox.getSelectedItem();
            }

            private JComboBox<IImageConverter> conversionMethodsJComboBox;
            private JComboBox<BBCSprite.DisplayMode> displayModeJComboBox;
            private float zoom;
            private boolean showGrid;
            private ImportPreviewPanel importPreviewPanel;
        }
    }
}
