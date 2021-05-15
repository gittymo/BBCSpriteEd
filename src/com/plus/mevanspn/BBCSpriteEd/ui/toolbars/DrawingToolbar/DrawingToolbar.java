package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.MultiFunctionButton.MultiFunctionButton;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.MultiFunctionButton.MultiFunctionButtonState;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

final public class DrawingToolbar extends JToolBar {
    public DrawingToolbar(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        this.buttons = new HashMap<>();
        this.add("pencil", new DrawingToolbarButton("img/pencil.png", "Draw freehand lines (Key: D).", this, 'D'));
        this.add("eraser", new DrawingToolbarButton("img/eraser.png", "Erase pixels in a freehand fashion (Key: E).", this, 'E'));
        this.add("rectangle",
                new MultiFunctionButton(new MultiFunctionButtonState[] {
                        new MultiFunctionButtonState("img/rect.png", "Outlined Rectangle", DRAW_RECT_OPEN),
                        new MultiFunctionButtonState("img/fillrect.png", "Filled Rectangle", DRAW_RECT_FILL)
                }, "Draw outline/filled rectangles. (Key: R/Alt+R)", this, 'R'));
        this.add("line", new DrawingToolbarButton("img/line.png", "Draw straight lines (Key: L).", this, 'L'));
        this.add("floodfill", new DrawingToolbarButton("img/fill.png", "Fill an enclosed space with the chosen colour (Key: F).", this, 'F'));
        this.add("paintbrush", new PaintBrushButton(this));
        this.add("translate", new DrawingToolbarButton("img/trans.png", "Move the contents of the image (Key: T).", this, 'T'));

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
        mainFrame.GetImagePanel().ResetDrawPoints();
        mainFrame.RefreshPanels();
    }

    public DrawingToolbarButton GetActiveButton() {
        return activeButton;
    }

    public boolean IsActiveButton(DrawingToolbarButton drawingToolbarButton) { return drawingToolbarButton == activeButton; }

    public boolean IsActiveButton(String buttonKeyValue) { return IsActiveButton(buttons.get(buttonKeyValue)); }

    public DrawingToolbarButton GetButton(String keyValue) {
        return buttons.get(keyValue);
    }

    MainFrame GetMainFrame() {
        return mainFrame;
    }

    private HashMap<String, DrawingToolbarButton> buttons;
    private DrawingToolbarButton activeButton;
    private final MainFrame mainFrame;

    public static int DRAW_RECT_OPEN = 0;
    public static int DRAW_RECT_FILL = 1;
}
