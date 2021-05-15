package com.plus.mevanspn.BBCSpriteEd.ui.menus.EditMenu.RotateMenu;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class RotateImageDialog extends JDialog {
    public RotateImageDialog(RotateMenu rotateMenu) {
        super(rotateMenu.GetEditMenu().GetMainFrame(), "Rotate Image", true);
        this.setLayout(new GridLayout(2,2));
        degrees = 0;
        cancelled = false;
        JLabel degreesLabel = new JLabel("Degrees");
        JTextField degreesField = new JTextField();
        JButton okayButton = new JButton("Rotate");
        okayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    degrees = Double.parseDouble(degreesField.getText());
                } catch (Exception ex) {
                    degrees = 0;
                } finally {
                    setVisible(false);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelled = true;
                setVisible(false);
            }
        });

        this.getContentPane().add(degreesLabel);
        this.getContentPane().add(degreesField);
        this.getContentPane().add(okayButton);
        this.getContentPane().add(cancelButton);
        this.pack();
    }

    public boolean Cancelled() { return cancelled; }

    public double GetRotationDegrees() { return degrees; }

    private double degrees;
    private boolean cancelled;
}
