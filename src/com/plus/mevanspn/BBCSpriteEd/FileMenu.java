package com.plus.mevanspn.BBCSpriteEd;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class FileMenu extends JMenu {
    public FileMenu(MainFrame parent) {
        super("File");
        this.parent = parent;
        this.newFileMenu = new NewFileMenu(this);
        this.add(this.newFileMenu);
        this.add(new JSeparator());
        JMenuItem exitApp = new JMenuItem("Quit");
        exitApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.Quit();
            }
        });
        this.add(exitApp);
    }

    private MainFrame parent;
    private NewFileMenu newFileMenu;

    final class NewFileMenu extends JMenu {
        public NewFileMenu(FileMenu parent) {
            super("New");
            this.parent = parent;

            for (int i = 0; i < BBCSprite.DisplayMode.values().length; i++)
                add(new SpriteTypeMenuItem(BBCSprite.DisplayMode.values()[i],this));
        }

        private FileMenu parent;
        private SpriteTypeMenuItem[] spriteTypeMenuItems;

        final class SpriteTypeMenuItem extends JMenuItem {
            public SpriteTypeMenuItem( BBCSprite.DisplayMode displayMode, NewFileMenu parent) {
                super("Mode " + displayMode.number + ", " + displayMode.colours.length + " colours. " +
                        displayMode.width + "x" + displayMode.height + " max.");
                        addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        var imageSizeDialog = new ImageSizePicker(displayMode.width, displayMode.height, parent.parent.parent);
                        imageSizeDialog.setVisible(true);
                        if (!imageSizeDialog.cancelled && imageSizeDialog.imageWidth > 0 && imageSizeDialog.imageHeight > 0) {
                            parent.parent.parent.LoadSprite(new BBCSprite(imageSizeDialog.imageWidth, imageSizeDialog.imageHeight, displayMode, parent.parent.parent));
                        } else imageSizeDialog.setVisible(false);
                    }
                });
                this.parent = parent;
                this.displayMode = displayMode;
            }

            private BBCSprite.DisplayMode displayMode;
            private NewFileMenu parent;

            final class ImageSizePicker extends JDialog {
                public ImageSizePicker(int maxWidth, int maxHeight, MainFrame parentFrame) {
                    super(parentFrame, "Choose sprite size", true);

                    this.maxWidth = maxWidth;
                    this.maxHeight = maxHeight;
                    this.imageWidth = this.imageHeight = 0;
                    this.selected = this.cancelled = false;

                    this.imageWidthField = new JTextField();
                    this.imageHeightField = new JTextField();
                    this.imageWidthLabel = new JLabel("Width");
                    this.imageHeightLabel = new JLabel("Height");
                    this.buttonOkay = new JButton("Create Sprite");
                    this.buttonOkay.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selected = true;
                            try {
                                imageWidth = Integer.parseInt(imageWidthField.getText());
                                imageHeight = Integer.parseInt(imageHeightField.getText());
                                if (imageWidth < 1 || imageWidth > maxWidth) imageWidth = 0;
                                if (imageHeight < 1 || imageHeight > maxHeight) imageHeight = 0;
                            } catch (Exception ex) { }
                            cancelled = false;
                            Close();
                        }
                    });
                    this.buttonCancel = new JButton("Cancel");
                    this.buttonCancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selected = false;
                            cancelled = true;
                            Close();
                        }
                    });
                    GridLayout gridLayout = new GridLayout(3, 2, 4, 4);
                    setLayout(gridLayout);
                    add(this.imageWidthLabel);
                    add(this.imageHeightLabel);
                    add(this.imageWidthField);
                    add(this.imageHeightField);
                    add(this.buttonOkay);
                    add(this.buttonCancel);
                    pack();

                    getRootPane().setBorder(new EmptyBorder(4,4,4,4));
                    this.setLocationRelativeTo(parentFrame);
                }

                public int GetImageWidth() {
                    try {
                        if (!cancelled && selected) return Integer.parseInt(imageWidthField.getText());
                    } catch (Exception ex) { return 0; }
                    return 0;
                }

                public int GetImageHeight() {
                    try {
                        if (!cancelled && selected) return Integer.parseInt(imageHeightField.getText());
                    } catch (Exception ex) { return 0; }
                    return 0;
                }

                public void Close() {
                    setVisible(false);
                }

                private int maxWidth, maxHeight, imageWidth, imageHeight;
                private boolean selected, cancelled;
                private JTextField imageWidthField, imageHeightField;
                private JLabel imageWidthLabel, imageHeightLabel;
                private JButton buttonOkay, buttonCancel;
            }
        }
    }
}
