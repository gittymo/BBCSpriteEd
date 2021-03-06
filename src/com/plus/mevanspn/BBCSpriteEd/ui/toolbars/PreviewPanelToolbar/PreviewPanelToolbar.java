package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.PreviewPanelToolbar;

import com.plus.mevanspn.BBCSpriteEd.ui.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel.PreviewPanel;

import javax.swing.*;
import java.awt.event.*;

final public class PreviewPanelToolbar extends JToolBar {
    public PreviewPanelToolbar(PreviewPanel previewPanel) {
        super(JToolBar.HORIZONTAL);
        ToolbarButton rwdButton = new ToolbarButton("rwnd.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.ToStart();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        ToolbarButton stopButton = new ToolbarButton("stop.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.Stop();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        ToolbarButton pauseButton = new ToolbarButton("pause.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.Pause();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        ToolbarButton playButton = new ToolbarButton("play.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.Play();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        ToolbarButton fwdButton = new ToolbarButton("ffwd.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.ToEnd();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        add(rwdButton);
        add(stopButton);
        add(pauseButton);
        add(playButton);
        add(fwdButton);

        JComboBox<ZoomComboBoxChoice> zoomOptionsComboBox = new JComboBox<>(new ZoomComboBoxChoice[] {
                new ZoomComboBoxChoice("25%",0.25f),
                new ZoomComboBoxChoice("50%",0.5f),
                new ZoomComboBoxChoice("100%", 1.0f),
                new ZoomComboBoxChoice("200%", 2.0f),
                new ZoomComboBoxChoice("400%", 4.0f),
                new ZoomComboBoxChoice("800%", 8.0f)});
        zoomOptionsComboBox.setSelectedIndex(2);
        zoomOptionsComboBox.addActionListener(e -> {
            ZoomComboBoxChoice zoomComboBoxChoice = (ZoomComboBoxChoice) zoomOptionsComboBox.getSelectedItem();
            if (zoomComboBoxChoice != null) previewPanel.SetZoom(zoomComboBoxChoice.zoom);
        });
        add(zoomOptionsComboBox);

        JSpinner fpsSpinner = new JSpinner(new SpinnerNumberModel(previewPanel.GetFPS(), 1, 50, 1));
        fpsSpinner.addChangeListener(e -> previewPanel.SetFPS((Integer) fpsSpinner.getValue()));
        fpsSpinner.setToolTipText("Change the playback speed (frames per second)");
        add(fpsSpinner);
    }

    final class ZoomComboBoxChoice {
        public ZoomComboBoxChoice(String label, float zoom) {
            this.label = label;
            this.zoom = zoom;
        }

        public String GetLabel() {
            return label;
        }

        @Override
        public String toString() { return GetLabel(); }

        public float GetZoom() {
            return zoom;
        }

        private final String label;
        private float zoom;
    }
}
