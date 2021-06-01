package com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;
import com.plus.mevanspn.BBCSpriteEd.ui.toplevel.MainFrame;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.MultiFunctionButton.*;

import javax.swing.*;
import java.util.*;

final public class DrawingToolbar extends JToolBar {
    public DrawingToolbar(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        this.buttons = new HashMap<>();
        this.add("pencil", new DrawingToolbarButton("pencil.png",
                "Draw freehand lines (Key: D).",
                this, new KeyPressEventMatcher('D')));
        this.add("eraser", new DrawingToolbarButton("eraser.png",
                "Erase pixels in a freehand fashion (Key: E).",
                this, new KeyPressEventMatcher('E')));
        this.add("rectangle",
                new MultiFunctionButton(new MultiFunctionButtonState[] {
                        new MultiFunctionButtonState("rect.png", "Outlined Rectangle", DRAW_RECT_OPEN,
                                new KeyPressEventMatcher('R')),
                        new MultiFunctionButtonState("fillrect.png", "Filled Rectangle",
                                DRAW_RECT_FILL, new KeyPressEventMatcher('R', false, true, false))
                }, "Draw outline/filled rectangles. (Key: R/Alt+R)", this));
        this.add("oval",
                new MultiFunctionButton(new MultiFunctionButtonState[] {
                        new MultiFunctionButtonState("oval.png", "Outlined Circle/Oval", DRAW_OVAL_OPEN,
                                new KeyPressEventMatcher('C')),
                        new MultiFunctionButtonState("filloval.png", "Filled Circle/Oval",
                                DRAW_OVAL_FILL, new KeyPressEventMatcher('C', false, true, false))
                }, "Draw outline/filled circles and ovals. (Key: C/Alt+C)", this));
        this.add("line", new DrawingToolbarButton("line.png",
                "Draw straight lines (Key: L).", this, new KeyPressEventMatcher('L')));
        this.add("floodfill", new DrawingToolbarButton("fill.png",
                "Fill an enclosed space with the chosen colour (Key: F).",
                this, new KeyPressEventMatcher('F')));
        this.add("paintbrush", new PaintBrushButton(this));
        this.add("translate", new DrawingToolbarButton("trans.png",
                "Move the contents of the image (Key: T).",
                this, new KeyPressEventMatcher('T')));

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
        mainFrame.GetImagePanel().repaint();
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
    public static int DRAW_OVAL_OPEN = 0;
    public static int DRAW_OVAL_FILL = 1;
}
