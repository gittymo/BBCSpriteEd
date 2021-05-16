package com.plus.mevanspn.BBCSpriteEd.ui.interfaces;

import java.awt.event.KeyEvent;

final public class KeyPressEventMatcher {
    public KeyPressEventMatcher(char charValue, boolean ctrlDown, boolean altDown, boolean altGraphDown) {
        this.charValue = charValue;
        this.ctrlDown = ctrlDown;
        this.altDown = altDown;
        this.altGraphDown = altGraphDown;
    }

    public KeyPressEventMatcher(char charValue) {
        this.charValue = charValue;
        this.ctrlDown = this.altDown = this.altGraphDown = false;
    }

    public char GetChar() { return charValue; }

    public boolean AltDown() { return altDown; }

    public boolean AltGraphDown() { return altGraphDown; }

    public boolean IsMatch(KeyEvent keyEvent, boolean ignoreCase) {
        boolean match = true;
        char keyEventChar = keyEvent.getKeyChar();
        if (keyEventChar != charValue)
        {
            match = false;
            if (ignoreCase && Character.isAlphabetic(charValue)) {
                if (Character.isUpperCase(charValue) && keyEventChar - 32 == charValue) match = true;

            }
        }
        if (altDown != keyEvent.isAltDown()) match = false;
        if (ctrlDown != keyEvent.isControlDown()) match = false;
        if (altGraphDown != keyEvent.isAltGraphDown()) match = false;
        return match;
    }

    private final char charValue;
    private final boolean ctrlDown, altDown, altGraphDown;
}
