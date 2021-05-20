package com.plus.mevanspn.BBCSpriteEd.ui.toplevel;

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
