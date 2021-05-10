package com.plus.mevanspn.BBCSpriteEd;

final public class InvalidSpriteFileException extends Exception {
    public InvalidSpriteFileException(String filename) {
        super("The file '" + filename + "' is not a valid BBC Sprite file.");
    }
}
