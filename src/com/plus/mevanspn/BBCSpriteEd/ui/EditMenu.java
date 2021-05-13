package com.plus.mevanspn.BBCSpriteEd.ui;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class EditMenu extends JMenu {
    public EditMenu(MainFrame mainFrame) {
        super("Edit");
        this.mainFrame = mainFrame;
        JMenuItem resizeMenuItem = new JMenuItem("Resize...");
        resizeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ResizeSpriteDialog(GetThis()).setVisible(true);
            }
        });
        add(resizeMenuItem);
    }

    public MainFrame GetMainFrame() {
        return mainFrame;
    }

    public EditMenu GetThis() {
        return this;
    }

    private MainFrame mainFrame;

    final class ResizeSpriteDialog extends JDialog {
        public ResizeSpriteDialog(EditMenu parent) {
            super(parent.GetMainFrame(), "Resize Sprite", true);
            this.parent = parent;
            final int originalWidth = parent.GetMainFrame().GetSprite().GetWidth();
            final int originalHeight = parent.GetMainFrame().GetSprite().GetHeight();
            JLabel newImageWidthLabel = new JLabel("New Width");
            JLabel newImageHeightLabel = new JLabel("New Height");
            JTextField newImageWidth = new JTextField("" + originalWidth);
            JTextField newImageHeight = new JTextField("" + originalHeight);
            JButton okButton = new JButton("Resize");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int newWidth = originalWidth, newHeight = originalHeight;
                    try {
                        newWidth = Integer.parseInt(newImageWidth.getText());
                        newHeight = Integer.parseInt(newImageHeight.getText());
                    } catch (Exception ex) { }
                    if (newWidth < 1 || newWidth > parent.GetMainFrame().GetSprite().GetDisplayMode().width ||
                        newHeight < 1 || newHeight > parent.GetMainFrame().GetSprite().GetDisplayMode().height) {
                        newWidth = originalWidth;
                        newHeight = originalHeight;
                    }
                    if (newWidth != originalWidth || newHeight != originalHeight) {
                        GetMainFrame().GetSprite().RecordHistory();
                        GetMainFrame().GetSprite().Resize(newWidth, newHeight);
                        Close();
                    }
                }
            });
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Close();
                }
            });

            this.setLayout(new GridLayout(3,2));
            this.add(newImageWidthLabel);
            this.add(newImageHeightLabel);
            this.add(newImageWidth);
            this.add(newImageHeight);
            this.add(okButton);
            this.add(cancelButton);
            this.pack();
        }

        void Close() {
            this.setVisible(false);
            this.dispose();
        }

        private EditMenu parent;
    }
}
