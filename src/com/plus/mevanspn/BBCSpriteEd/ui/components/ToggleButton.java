package com.plus.mevanspn.BBCSpriteEd.ui.components;

import com.plus.mevanspn.BBCSpriteEd.ui.interfaces.KeyPressEventMatcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * ToggleButton objects are essentially ToolbarButtons that have two states which can be flipped between.  Each state
 * can have its own icon so that the state can be visually represented.
 */
public class ToggleButton extends ToolbarButton implements ActionListener {
    /**
     * Creates an instance of a ToggleButton.  The ToggleButton will use the icons whose images are loaded from the
     * provided filenames.  An optional KeyPressEventMatcher can be provided to map key presses to the button.
     * @param onIconFile String containing the filename of the on/positive image for the button.
     * @param offIconFile String containing the filename of the off/negative image for the button.
     * @param keyPressEventMatcher Reference to KeyPressEventMatcher object that will handle key mappings for this button.
     */
    public ToggleButton(String onIconFile, String offIconFile, KeyPressEventMatcher keyPressEventMatcher) {
        super(keyPressEventMatcher);
        this.keyPressEventMatcher = keyPressEventMatcher;
        this.onImageIcon = loadIcon(onIconFile);
        this.offImageIcon = loadIcon(offIconFile);
        SetState(true);
    }

    /**
     * Sets the on/off (positive/negative) state of the button.  The button's image will be immediately updated to
     * reflect the change.
     * @param state State to set the button to (true = on, false = off)
     */
    public void SetState(boolean state) {
        this.state = state;
        if (state) {
            if (onImageIcon != null) {
                setIcon(new ImageIcon(onImageIcon));
                this.iconImage = onImageIcon;
            }
        }
        else {
            if (offImageIcon != null) {
                setIcon(new ImageIcon(offImageIcon));
                this.iconImage = offImageIcon;
            }
        }
        repaint();
    }

    /**
     * Returns the state of the ToggleButton.
     * @return Current state of the ToggleButton (true = on, false = off)
     */
    public boolean GetState() {
        return state;
    }

    /**
     * Toggles the state of the button to be the opposite of its current value.
     */
    public void Toggle() {
        SetState(!this.state);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toggle();
    }

    /**
     * Base class key press event handler.  Usually this method will be overridden by a sub-class to suit the purpose
     * of the button.  The base method will simply toggle the state of the button when the expected keys are pressed.
     * @param keyEvent Reference to KeyEvent object.  This is a standard java.awt.event object.
     */
    @Override
    public void KeyPressed(KeyEvent keyEvent) {
        if (keyPressEventMatcher != null && keyPressEventMatcher.IsMatch(keyEvent,true)) Toggle();
    }

    private final BufferedImage onImageIcon;
    private final BufferedImage offImageIcon;
    private boolean state;
    private final KeyPressEventMatcher keyPressEventMatcher;
}