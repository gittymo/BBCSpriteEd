package com.plus.mevanspn.BBCSpriteEd.ui.MultiFunctionButton;

import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.DrawingToolbar;
import com.plus.mevanspn.BBCSpriteEd.ui.toolbars.DrawingToolbar.DrawingToolbarButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collections;
import java.util.LinkedList;


public class MultiFunctionButton extends DrawingToolbarButton implements MouseWheelListener, MouseListener {
    public MultiFunctionButton(MultiFunctionButtonState[] states, String tooltipText, DrawingToolbar parent) {
        super(states[0].GetIconFile(), tooltipText, parent);
        this.states = new LinkedList<>();
        Collections.addAll(this.states, states);
        this.multiFunctionButtonMenu = new MultiFunctionButtonMenu(this);
        this.addMouseWheelListener(this);
        this.addMouseListener(this);
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

    private void setIconToState() {
        this.setIcon(states.get(stateIndex).GetIcon());
        GetParent().SetActiveButton(this);
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
    private final MultiFunctionButtonMenu multiFunctionButtonMenu;
}
