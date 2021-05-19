package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

import com.plus.mevanspn.BBCSpriteEd.components.ToolbarButton;
import com.plus.mevanspn.BBCSpriteEd.ui.panels.ImagePanel;

import javax.swing.*;
import java.awt.*;

final public class ImagePanelContainer extends JPanel {
   public ImagePanelContainer(JScrollPane imagePanelScrollPane, ToolbarsContainer toolbarsContainer) {
       super();
       setLayout(new BorderLayout());
       if (imagePanelScrollPane != null && toolbarsContainer != null) {
           add(toolbarsContainer,BorderLayout.NORTH);
           add(imagePanelScrollPane,BorderLayout.CENTER);
       }
   }
}
