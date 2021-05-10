package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

final public class DrawingToolbar extends JToolBar {
    public DrawingToolbar(MainFrame parent) {
        super();

        this.buttons = new HashMap<>();
        this.buttons.put("pencil", new DrawingToolbarButton("/img/pencil.png", "Draw freehand lines.", this));
        this.buttons.put("eraser", new DrawingToolbarButton("/img/eraser.png", "Erase pixels in a freehand fashion.", this));
        this.buttons.put("rectangle", new DrawingToolbarButton("/img/rect.png", "Draw outline/filled rectangles.", this));
        this.buttons.put("line", new DrawingToolbarButton("/img/line.png", "Draw straight lines.", this));
        this.buttons.put("floodfill", new DrawingToolbarButton("/img/fill.png", "Fill an enclosed space with the chosen colour.", this));

        this.buttonsContainer = new JPanel();
        for (Map.Entry<String, DrawingToolbarButton> set : this.buttons.entrySet()) buttonsContainer.add(set.getValue());
        this.add(buttonsContainer);

        SetActiveButton(buttons.get("pencil"));
    }

    public void SetActiveButton(DrawingToolbarButton activeButton) {
        this.activeButton = activeButton;
        for (Map.Entry<String, DrawingToolbarButton> set : this.buttons.entrySet()) {
            set.getValue().setSelected(set.getValue() == activeButton ? true : false);
        }
    }

    public DrawingToolbarButton GetActiveButton() {
        return activeButton;
    }

    public HashMap<String, DrawingToolbarButton> buttons;
    private JPanel buttonsContainer;

    private DrawingToolbarButton activeButton;
}
