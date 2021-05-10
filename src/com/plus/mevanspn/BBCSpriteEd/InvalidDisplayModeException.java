package com.plus.mevanspn.BBCSpriteEd;

final public class InvalidDisplayModeException extends Exception {
    public InvalidDisplayModeException(int modeNumber) {
        super("The mode number '" + modeNumber + "' is invalid.");
    }
}
