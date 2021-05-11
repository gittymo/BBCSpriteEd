package com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

public class DrawingToolbarMultiButton extends DrawingToolbarButton implements MouseWheelListener {

    public DrawingToolbarMultiButton(String iconFile, String tooltipText, DrawingToolbar parent) {
        super(iconFile, tooltipText, parent);
        this.states = new LinkedList<>();
        this.states.add(new DrawingToolbarMultiButtonState(iconFile, 0));
        this.addMouseWheelListener(this);
    }

    public DrawingToolbarMultiButton(String[] iconFiles, String tooltipText, DrawingToolbar parent) {
        super(iconFiles[0], tooltipText, parent);
        this.states = new LinkedList<>();
        for (int i = 0; i < iconFiles.length; i++) {
            this.states.add(new DrawingToolbarMultiButtonState(iconFiles[i], i));
        }
        this.addMouseWheelListener(this);
    }

    public DrawingToolbarMultiButton(DrawingToolbarMultiButtonState[] states, String tooltipText, DrawingToolbar parent) {
        super(states[0].GetIconFile(), tooltipText, parent);
        this.states = new LinkedList<>();
        for (int i = 0; i < states.length; i++) {
            this.states.add(states[i]);
        }
        this.addMouseWheelListener(this);
    }

    private void setIconToState() {
        this.setIcon(states.get(stateIndex).GetIcon());
    }

    public int GetStateValue() {
        return states.get(stateIndex).GetValue();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0) stateIndex--;
        else stateIndex++;
        if (stateIndex < 0) stateIndex = states.size() - 1;
        if (stateIndex == states.size()) stateIndex = 0;
        setIconToState();
    }

    private int stateIndex = 0;
    private LinkedList<DrawingToolbarMultiButtonState> states;


}
