package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.MainFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

final public class DrawingToolbar extends JToolBar {
    public DrawingToolbar(MainFrame parent) {
        super();

        this.buttons = new HashMap<>();
        this.add("pencil", new DrawingToolbarButton("img/pencil.png", "Draw freehand lines.", this));
        this.add("eraser", new DrawingToolbarButton("img/eraser.png", "Erase pixels in a freehand fashion.", this));
        this.add("rectangle", new DrawingToolbarButton("img/rect.png", "Draw outline/filled rectangles.", this));
        this.add("line", new DrawingToolbarButton("img/line.png", "Draw straight lines.", this));
        this.add("floodfill", new DrawingToolbarButton("img/fill.png", "Fill an enclosed space with the chosen colour.", this));

        SetActiveButton(buttons.get("pencil"));
    }

    public void add(String keyValue, DrawingToolbarButton button) {
        this.buttons.put(keyValue, button);
        this.add(button);
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
