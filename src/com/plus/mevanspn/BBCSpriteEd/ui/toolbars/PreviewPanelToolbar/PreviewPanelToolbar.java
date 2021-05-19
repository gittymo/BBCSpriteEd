package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.PreviewPanelToolbar;

import com.plus.mevanspn.BBCSpriteEd.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel.PreviewPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

final public class PreviewPanelToolbar extends JToolBar {
    public PreviewPanelToolbar(PreviewPanel previewPanel) {
        super(JToolBar.HORIZONTAL);
        ToolbarButton rwdButton = new ToolbarButton("img/rwnd.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.ToStart();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        ToolbarButton stopButton = new ToolbarButton("img/stop.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.Stop();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        ToolbarButton pauseButton = new ToolbarButton("img/pause.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.Pause();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        ToolbarButton playButton = new ToolbarButton("img/play.png", null) {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.Play();
            }

            @Override
            public void KeyPressed(KeyEvent keyEvent) { }
        };

        ToolbarButton fwdButton = new ToolbarButton("img/ffwd.png", null) {
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
        zoomOptionsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ZoomComboBoxChoice zoomComboBoxChoice = (ZoomComboBoxChoice) zoomOptionsComboBox.getSelectedItem();
                previewPanel.SetZoom(zoomComboBoxChoice.zoom);
            }
        });
        add(zoomOptionsComboBox);

        JSpinner fpsSpinner = new JSpinner(new SpinnerNumberModel(previewPanel.GetFPS(), 1, 50, 1));
        fpsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                previewPanel.SetFPS((Integer) fpsSpinner.getValue());
            }
        });
        fpsSpinner.setToolTipText("Change the playback speed (frames per second)");
        add(fpsSpinner);
    }

    private PreviewPanel previewPanel;

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

        private String label;
        private float zoom;
    }
}
