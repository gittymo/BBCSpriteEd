package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

final public class DrawingToolbar extends JToolBar {
    public DrawingToolbar() {
        super();

        this.buttons = new HashMap<>();
        this.add("pencil", new DrawingToolbarButton("img/pencil.png", "Draw freehand lines.", this));
        this.add("eraser", new DrawingToolbarButton("img/eraser.png", "Erase pixels in a freehand fashion.", this));
        this.add("rectangle",
                new DrawingToolbarMultiButton(new DrawingToolbarMultiButtonState[] {
                        new DrawingToolbarMultiButtonState("img/rect.png", DRAW_RECT_OPEN),
                        new DrawingToolbarMultiButtonState("img/fillrect.png", DRAW_RECT_FILL)
                }, "Draw outline/filled rectangles.", this));
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
            set.getValue().setSelected(set.getValue() == activeButton);
        }
    }

    public DrawingToolbarButton GetActiveButton() {
        return activeButton;
    }

    public DrawingToolbarButton GetButton(String keyValue) {
        return buttons.get(keyValue);
    }

    public HashMap<String, DrawingToolbarButton> buttons;
    private DrawingToolbarButton activeButton;

    public static int DRAW_RECT_OPEN = 0;
    public static int DRAW_RECT_FILL = 1;
}
