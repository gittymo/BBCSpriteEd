package com.plus.mevanspn.BBCSpriteEd.ui;

import com.plus.mevanspn.BBCSpriteEd.BBCSprite;
import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

final public class FileMenu extends JMenu {
    public FileMenu(MainFrame parent) {
        super("File");
        this.parent = parent;
        NewFileMenu newFileMenu = new NewFileMenu(this);
        this.add(newFileMenu);
        JMenuItem openFileMenuItem = new JMenuItem("Open...");
        openFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BBCSpriteFilePicker filePicker = new BBCSpriteFilePicker();
                int rv = filePicker.showOpenDialog(parent);
                if (rv == JFileChooser.APPROVE_OPTION) {
                    try {
                        String filePath = filePicker.getSelectedFile().getAbsolutePath();
                        parent.LoadSprite(new BBCSprite(filePath, parent));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        this.add(openFileMenuItem);
        JMenuItem saveAsFileMenuItem = new JMenuItem("Save As...");
        saveAsFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BBCSpriteFilePicker filePicker = new BBCSpriteFilePicker();
                int rv = filePicker.showSaveDialog(parent);
                if (rv == JFileChooser.APPROVE_OPTION) {
                    try {
                        String filePath = filePicker.getSelectedFile().getAbsolutePath();
                        if (!filePath.toLowerCase(Locale.ROOT).endsWith(".bsf")) filePath += ".bsf";
                        parent.GetSprite().WriteToFile( filePath);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        this.add(saveAsFileMenuItem);
        this.add(new JSeparator());
        JMenuItem exitApp = new JMenuItem("Quit");
        exitApp.addActionListener(e -> parent.Quit());
        this.add(exitApp);
    }

    private final MainFrame parent;

    final class NewFileMenu extends JMenu {
        public NewFileMenu(FileMenu parent) {
            super("New");
            this.parent = parent;

            for (int i = 0; i < BBCSprite.DisplayMode.values().length; i++)
                add(new SpriteTypeMenuItem(BBCSprite.DisplayMode.values()[i],this));
        }

        private final FileMenu parent;

        final class SpriteTypeMenuItem extends JMenuItem {
            public SpriteTypeMenuItem( BBCSprite.DisplayMode displayMode, NewFileMenu parent) {
                super("Mode " + displayMode.number + ", " + displayMode.colours.length + " colours. " +
                        displayMode.width + "x" + displayMode.height + " max.");

                addActionListener(e -> {
                    var imageSizeDialog = new ImageSizePicker(displayMode.width, displayMode.height, parent.parent.parent);
                    imageSizeDialog.setVisible(true);
                    if (!imageSizeDialog.cancelled && imageSizeDialog.imageWidth > 0 && imageSizeDialog.imageHeight > 0) {
                        parent.parent.parent.LoadSprite(new BBCSprite(imageSizeDialog.imageWidth, imageSizeDialog.imageHeight, displayMode, parent.parent.parent));
                    } else imageSizeDialog.setVisible(false);
                });
            }

            final class ImageSizePicker extends JDialog {
                public ImageSizePicker(int maxWidth, int maxHeight, MainFrame parentFrame) {
                    super(parentFrame, "Choose sprite size", true);

                    this.imageWidth = this.imageHeight = 0;
                    this.selected = this.cancelled = false;

                    this.imageWidthField = new JTextField();
                    this.imageHeightField = new JTextField();
                    JLabel imageWidthLabel = new JLabel("Width");
                    JLabel imageHeightLabel = new JLabel("Height");
                    JButton buttonOkay = new JButton("Create Sprite");
                    buttonOkay.addActionListener(e -> {
                        selected = true;
                        try {
                            imageWidth = Integer.parseInt(imageWidthField.getText());
                            imageHeight = Integer.parseInt(imageHeightField.getText());
                            if (imageWidth < 1 || imageWidth > maxWidth) imageWidth = 0;
                            if (imageHeight < 1 || imageHeight > maxHeight) imageHeight = 0;
                        } catch (Exception ex) { }
                        cancelled = false;
                        Close();
                    });
                    JButton buttonCancel = new JButton("Cancel");
                    buttonCancel.addActionListener(e -> {
                        selected = false;
                        cancelled = true;
                        Close();
                    });
                    GridLayout gridLayout = new GridLayout(3, 2, 4, 4);
                    setLayout(gridLayout);
                    add(imageWidthLabel);
                    add(imageHeightLabel);
                    add(this.imageWidthField);
                    add(this.imageHeightField);
                    add(buttonOkay);
                    add(buttonCancel);
                    pack();

                    getRootPane().setBorder(new EmptyBorder(4,4,4,4));
                    this.setLocationRelativeTo(parentFrame);
                }

                public void Close() {
                    setVisible(false);
                }

                private int imageWidth, imageHeight;
                private boolean selected, cancelled;
                private final JTextField imageWidthField, imageHeightField;
            }
        }
    }
}
