package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.PreviewPanelToolbar;

import com.plus.mevanspn.BBCSpriteEd.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel.PreviewPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
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

        JComboBox<String> zoomOptionsComboBox = new JComboBox<>(new String[] {"25%","50%","100%","200%","400%","800%"});
        add(zoomOptionsComboBox);
    }

    private PreviewPanel previewPanel;
}
