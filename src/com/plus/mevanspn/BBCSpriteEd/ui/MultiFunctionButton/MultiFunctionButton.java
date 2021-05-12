package com.plus.mevanspn.BBCSpriteEd.ui.MultiFunctionButton;

import com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar.DrawingToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.DrawingToolbar.DrawingToolbarButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collections;
import java.util.LinkedList;


public class MultiFunctionButton extends DrawingToolbarButton implements MouseWheelListener, MouseListener {

    public MultiFunctionButton(String iconFile, String name, String tooltipText, DrawingToolbar parent) {
        super(iconFile, tooltipText, parent);
        this.states = new LinkedList<>();
        this.states.add(new MultiFunctionButtonState(iconFile, name,0));
        this.multiFunctionButtonMenu = new MultiFunctionButtonMenu(this);
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
    }

    public MultiFunctionButton(String[] iconFiles, String[] names, String tooltipText, DrawingToolbar parent) {
        super(iconFiles[0], tooltipText, parent);
        this.states = new LinkedList<>();
        for (int i = 0; i < iconFiles.length; i++) {
            this.states.add(new MultiFunctionButtonState(iconFiles[i], names[i], i));
        }
        this.multiFunctionButtonMenu = new MultiFunctionButtonMenu(this);
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
    }

    public MultiFunctionButton(MultiFunctionButtonState[] states, String tooltipText, DrawingToolbar parent) {
        super(states[0].GetIconFile(), tooltipText, parent);
        this.states = new LinkedList<>();
        Collections.addAll(this.states, states);
        this.multiFunctionButtonMenu = new MultiFunctionButtonMenu(this);
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
    }

    private void setIconToState() {
        this.setIcon(states.get(stateIndex).GetIcon());
        GetParent().SetActiveButton(this);
    }

    public int GetStateValue() {
        return states.get(stateIndex).GetValue();
    }

    public void SetStateValue(int stateValue) {
        if (stateValue >=0 && stateValue < states.size()) {
            stateIndex = stateValue;
            setIconToState();
        }
    }

    LinkedList<MultiFunctionButtonState> GetStates() {
        return states;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0) stateIndex--;
        else stateIndex++;
        if (stateIndex < 0) stateIndex = states.size() - 1;
        if (stateIndex == states.size()) stateIndex = 0;
        setIconToState();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3) {
            multiFunctionButtonMenu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    private int stateIndex = 0;
    private final LinkedList<MultiFunctionButtonState> states;
    private MultiFunctionButtonMenu multiFunctionButtonMenu;
}
