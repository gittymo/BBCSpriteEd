package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

import com.plus.mevanspn.BBCSpriteEd.ui.panels.PreviewPanel.PreviewPanel;

import javax.swing.*;
import java.awt.*;

final public class PreviewPanelContainer extends JPanel {
    public PreviewPanelContainer(PreviewPanel previewPanel) {
        super();
        setLayout(new BorderLayout());
        add(previewPanel, BorderLayout.CENTER);
        add(previewPanel.GetToolbar(), BorderLayout.NORTH);
    }
}
