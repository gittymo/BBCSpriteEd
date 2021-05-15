package com.plus.mevanspn.BBCSpriteEd.ui.interfaces;

import java.awt.event.KeyEvent;

public interface KeyPressBroadcaster {
    public void AddKeyPressListener(KeyPressListener keyPressListener);
    public void RemoveKeyPressListener(KeyPressListener keyPressListener);
    public void BroadcastKeyPress(KeyEvent keyEvent);
}
