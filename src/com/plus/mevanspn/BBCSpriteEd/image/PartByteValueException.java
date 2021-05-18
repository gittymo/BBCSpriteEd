package com.plus.mevanspn.BBCSpriteEd.image;

final public class PartByteValueException extends Exception {
    public PartByteValueException(byte value) {
        this.value = value;
    }

    public byte GetValue() {
        return value;
    }

    private byte value;
}
